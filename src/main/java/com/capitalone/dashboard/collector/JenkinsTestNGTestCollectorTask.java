package com.capitalone.dashboard.collector;

import com.capitalone.dashboard.model.Build;
import com.capitalone.dashboard.model.CollectorItem;
import com.capitalone.dashboard.model.CollectorType;
import com.capitalone.dashboard.model.JenkinsTestNGTestCollector;
import com.capitalone.dashboard.model.JenkinsJob;
import com.capitalone.dashboard.model.TestResult;
import com.capitalone.dashboard.repository.BaseCollectorRepository;
import com.capitalone.dashboard.repository.ComponentRepository;
import com.capitalone.dashboard.repository.JenkinsTestNGTestCollectorRepository;
import com.capitalone.dashboard.repository.JenkinsTestNGTestJobRepository;
import com.capitalone.dashboard.repository.TestResultRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("PMD.UnnecessaryFullyQualifiedName")
// Will need to rename com.capitalone.dashboard.Component as it conflicts with Spring.
@Component
public class JenkinsTestNGTestCollectorTask extends
        CollectorTask<JenkinsTestNGTestCollector> {

    private final JenkinsTestNGTestCollectorRepository jenkinsTestNGTestCollectorRepository;
    private final JenkinsTestNGTestJobRepository jenkinsTestNGTestJobRepository;
    
    private final TestResultRepository testResultRepository;
    private final JenkinsClient jenkinsClient;
    private final JenkinsTestNGSettings jenkinsTestNGTestSettings;
    private final ComponentRepository dbComponentRepository;

    @Autowired
    public JenkinsTestNGTestCollectorTask(
            TaskScheduler taskScheduler,
            JenkinsTestNGTestCollectorRepository jenkinsTestNGTestCollectorRepository,
            JenkinsTestNGTestJobRepository jenkinsTestNGTestJobRepository,
            TestResultRepository testResultRepository,
            JenkinsClient jenkinsTestNGTestClient,
            JenkinsTestNGSettings jenkinsTestNGTestSettings,
            ComponentRepository dbComponentRepository) {
        super(taskScheduler, "JenkinsTestNGTest");
        this.jenkinsTestNGTestCollectorRepository = jenkinsTestNGTestCollectorRepository;
        this.jenkinsTestNGTestJobRepository = jenkinsTestNGTestJobRepository;
        this.testResultRepository = testResultRepository;  //new TestResultRepository();
        this.jenkinsClient = jenkinsTestNGTestClient;
        this.jenkinsTestNGTestSettings = jenkinsTestNGTestSettings;
        this.dbComponentRepository = dbComponentRepository;
    }

    @Override
    public JenkinsTestNGTestCollector getCollector() {
        return JenkinsTestNGTestCollector
                .prototype(jenkinsTestNGTestSettings.getServers());
    }

    @Override
    public BaseCollectorRepository<JenkinsTestNGTestCollector> getCollectorRepository() {
        return jenkinsTestNGTestCollectorRepository;
    }

    @Override
    public String getCron() {
        return jenkinsTestNGTestSettings.getCron();
    }

    @Override
    public void collect(JenkinsTestNGTestCollector collector) {

        long start = System.currentTimeMillis();

        clean(collector);

        for (String instanceUrl : collector.getBuildServers()) {
            logBanner(instanceUrl);

            Map<JenkinsJob, Set<Build>> buildsByJob = jenkinsClient
                    .getInstanceJobs(instanceUrl);
            log("Fetched jobs", start);

            addNewJobs(buildsByJob.keySet(), collector);

            List<JenkinsJob> enabledJobs = enabledJobs(collector, instanceUrl);
            if ( ! enabledJobs.isEmpty())
            {
                addNewTestSuites(enabledJobs);
            }
            else
            {
            	log("WARNING: No Enabled Jobs found with artifacts pattern: " + jenkinsTestNGTestSettings.getTestNGXmlRegEx());
            }
            log("Finished", start);
        }
    }

    /**
     * Clean up unused hudson/jenkins collector items
     *
     * @param collector the collector
     */

    private void clean(JenkinsTestNGTestCollector collector) {

        // First delete jobs that will be no longer collected because servers have moved etc.
        deleteUnwantedJobs(collector);

        Set<ObjectId> uniqueIDs = new HashSet<>();
        for (com.capitalone.dashboard.model.Component comp : dbComponentRepository
                .findAll()) {
            if (comp.getCollectorItems() == null
                    || comp.getCollectorItems().isEmpty()) continue;
            List<CollectorItem> itemList = comp.getCollectorItems().get(
                    CollectorType.Test);
            if (itemList == null) continue;
            for (CollectorItem ci : itemList) {
                if (ci != null
                        && ci.getCollectorId().equals(collector.getId())) {
                    uniqueIDs.add(ci.getId());
                }

            }
        }

        List<JenkinsJob> jobList = new ArrayList<>();
        Set<ObjectId> udId = new HashSet<>();
        udId.add(collector.getId());
        for (JenkinsJob job : jenkinsTestNGTestJobRepository
                .findByCollectorIdIn(udId)) {
            if (job != null) {
                job.setEnabled(uniqueIDs.contains(job.getId()));
                jobList.add(job);
            }
        }
        jenkinsTestNGTestJobRepository.save(jobList);
    }

    private void deleteUnwantedJobs(JenkinsTestNGTestCollector collector) {

        List<JenkinsJob> deleteJobList = new ArrayList<>();
        Set<ObjectId> udId = new HashSet<>();
        udId.add(collector.getId());
        for (JenkinsJob job : jenkinsTestNGTestJobRepository.findByCollectorIdIn(udId)) {
            if (!collector.getBuildServers().contains(job.getInstanceUrl()) ||
                    (!job.getCollectorId().equals(collector.getId()))) {
                deleteJobList.add(job);
            }
        }

        jenkinsTestNGTestJobRepository.delete(deleteJobList);

    }
    // Jenkins Helper methods

    private List<JenkinsJob> enabledJobs(
            JenkinsTestNGTestCollector collector, String instanceUrl) {
        return jenkinsTestNGTestJobRepository.findEnabledJenkinsJobs(
                collector.getId(), instanceUrl);
    }

    /**
     * Adds new {@link JenkinsJob}s to the database as disabled jobs.
     *
     * @param jobs      list of {@link JenkinsJob}s
     * @param collector the {@link JenkinsTestNGTestCollector}
     */
    private void addNewJobs(Set<JenkinsJob> jobs,
                            JenkinsTestNGTestCollector collector) {
        long start = System.currentTimeMillis();
        int count = 0;

        for (JenkinsJob job : jobs) {
            if (jenkinsClient.buildHasTestNGResults(job.getJobUrl())
                    && isNewJob(collector, job)) {
                job.setCollectorId(collector.getId());
                job.setEnabled(false); // Do not enable for collection. Will be
                // enabled when added to dashboard
                job.setDescription(job.getJobName());
                jenkinsTestNGTestJobRepository.save(job);
                count++;
            }
        }
        log("New jobs", start, count);
    }

    private void addNewTestSuites(List<JenkinsJob> enabledJobs) {
        long start = System.currentTimeMillis();
        int count = 0;
        for (JenkinsJob job : enabledJobs) {
            Build buildSummary = jenkinsClient.getLastSuccessfulBuild(job.getJobUrl());
			if (isNewTestNGResult(job, buildSummary)) {
                job.setLastUpdated(System.currentTimeMillis());
                jenkinsTestNGTestJobRepository.save(job);
                // Obtain the Test Result
                TestResult result = jenkinsClient.getTestNGTestResult(job.getJobUrl());
                if (result != null) {
                    result.setCollectorItemId(job.getId());
                    result.setTimestamp(System.currentTimeMillis());
                    testResultRepository.save(result);
                    count++;
                }
            }
        }
        log("New test suites", start, count);
    }

    private boolean isNewJob(JenkinsTestNGTestCollector collector,
                             JenkinsJob job) {
        return jenkinsTestNGTestJobRepository.findJenkinsJob(
                collector.getId(), job.getInstanceUrl(), job.getJobName()) == null;
    }

    private boolean isNewTestNGResult(JenkinsJob job, Build build) {
        return testResultRepository.findByCollectorItemIdAndExecutionId(
                job.getId(), build.getNumber()) == null;
    }

    @SuppressWarnings("unused")
	private Set<Build> nullSafe(Set<Build> builds) {
        return builds == null ? new HashSet<Build>() : builds;
    }
}
