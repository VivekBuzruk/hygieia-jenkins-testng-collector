package com.capitalone.dashboard.collector;

import com.capitalone.dashboard.model.TestCase;
import com.capitalone.dashboard.model.TestCaseStatus;
import com.capitalone.dashboard.model.TestCaseStep;
import com.capitalone.dashboard.model.TestSuite;
import com.capitalone.dashboard.model.TestSuiteType;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.capitalone.dashboard.testutil.testng_parser.*;

//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Transforms a Selenium XML Value into a TestResult
 */
@Component
public class TestNGxmlToTestResultTransformer implements Transformer<String, List<TestSuite>> {
	private static final Log LOG = LogFactory.getLog(TestNGxmlToTestResultTransformer.class);

	/**
	 * The TestNG parser.
	 */
	private TestNGParser parser;

	/**
	 * Initializes the TestNG parser.
	 */
	public void setUp() {
		this.parser = new TestNGParser();
	}

	@Override
	public List<TestSuite> transformer(String xml) {
		if (StringUtils.isEmpty(xml)) {
			throw new IllegalArgumentException("XML must not be empty");
		}

		setUp();
		List<TestSuite> suites = new ArrayList<>();
		// TestNG_Suite testNG_suite = null;
		List<TestNG_Suite> testNG_suites = null;
//        try {
//            // Parse features
//            for (TestSuite featureObj :  parser.parse(xml, "xmlFile")) {
//                suites.add(parseFeatureAsTestSuite(feature));
//            }
//        } catch (ParseException e) {
//            LOG.error(e);
//        }

		try {
			testNG_suites = this.parser.parse(xml, "xmlFile");
			for (TestNG_Suite testNG_suite : testNG_suites) {
				suites.add(testNGSuitetoTestSuite(testNG_suite));
			}

		} catch (TestNG_ParserException e) {
			LOG.error("Failed to parse testng file '" + "xmlFile" + "': " + e.getMessage()); // fill xmlFile
		}

		return suites;
	}

	private TestSuite testNGSuitetoTestSuite(TestNG_Suite testNG_suite) {
		TestSuite suite = new TestSuite();

		suite.setId(testNG_suite.getName());
		suite.setType(TestSuiteType.Functional);
		suite.setDescription(
				testNG_suite.getName() + " finished in --> " + testNG_suite.getDurationMs() + "Ms, Started at -> "
						+ testNG_suite.getStartedAt() + ", Finished at -> " + testNG_suite.getFinishedAt());
		long duration = 0;

		int testCaseTotalCount = testNG_suite.getTests().size();
		int tsSkippedCount = 0, tsSuccessCount = 0, tsFailCount = 0, tsUnknownCount = 0;

		for (TestNG_Test testNG_tc : testNG_suite.getTests()) {
			duration += Integer.parseInt(testNG_tc.getDurationMs());

			TestCase testCase = testNGTCtoTestCase(testNG_tc);
			switch (testCase.getStatus()) {
			case Success:
				tsSuccessCount++;
				break;
			case Failure:
				tsFailCount++;
				break;
			case Skipped:
				tsSkippedCount++;
				break;
			default:
				tsUnknownCount++;
				break;
			}
			suite.getTestCases().add(testCase);

		}
		suite.setSuccessTestCaseCount(tsSuccessCount);
		suite.setFailedTestCaseCount(tsFailCount);
		suite.setSkippedTestCaseCount(tsSkippedCount);
		suite.setTotalTestCaseCount(testCaseTotalCount);
		suite.setUnknownStatusCount(tsUnknownCount);
		suite.setDuration(duration);

		return suite;
	}

	private TestCase testNGTCtoTestCase(TestNG_Test testNG_tc) {
		TestCase testCase = new TestCase();
		testCase.setId(testNG_tc.getName());
		// testCase.setTags("Functional");
		testCase.setDescription(testNG_tc.getName() + " finished in --> " + testNG_tc.getDurationMs()
				+ "Ms, Started at -> " + testNG_tc.getStartedAt() + ", Finished at -> " + testNG_tc.getFinishedAt());

		// Parse each step as a TestCase
		// int testStepSuccessCount = 0, testStepFailCount = 0, testStepSkippedCount =
		// 0, testStepUnknownCount = 0, hookCounter = 0;
		long testDuration = 0;

		int tStepTotalCount = 0;
		int tStepSkippedCount = 0, tStepSuccessCount = 0, tStepFailCount = 0, tStepUnknownCount = 0;

		for (TestNG_Class testNG_cls : testNG_tc.getClasses()) {
			tStepTotalCount += testNG_cls.getTestMethods().size();
			for (TestNG_TestMethod testNG_tm : testNG_cls.getTestMethods()) {
				TestCaseStep testCaseStep = testNGTMtoTCstep(testNG_tm);
				testDuration += testCaseStep.getDuration();
				switch (TestNG_Statuses.valueOf(testNG_tm.getStatus())) {
				case PASS:
					tStepSuccessCount++;
					break;
				case FAIL:
					tStepFailCount++;
					break;
				case SKIP:
					tStepSkippedCount++;
					break;
				default:
					tStepUnknownCount++;
					break;
				}

				testCase.getTestSteps().add(testCaseStep);
			}
		}
		// Set Duration
		testCase.setDuration(testDuration);
		testCase.setSuccessTestStepCount(tStepSuccessCount);
		testCase.setSkippedTestStepCount(tStepSkippedCount);
		testCase.setFailedTestStepCount(tStepFailCount);
		testCase.setUnknownStatusCount(tStepUnknownCount);
		testCase.setTotalTestStepCount(testCase.getTestSteps().size());
		// Set Status
		if (tStepFailCount > 0) {
			testCase.setStatus(TestCaseStatus.Failure);
		} else if (tStepSkippedCount > 0) {
			testCase.setStatus(TestCaseStatus.Skipped);
		} else if (tStepSuccessCount > 0) {
			testCase.setStatus(TestCaseStatus.Success);
		} else {
			testCase.setStatus(TestCaseStatus.Unknown);
		}

//     for (Object tag : getJsonArray(scenarioElement, "tags")) {
//         testCase.getTags().add(getString((JSONObject) tag, "name"));
//     }

		return testCase;
	}

	private TestCaseStep testNGTMtoTCstep(TestNG_TestMethod testNG_tm) {
		TestCaseStep step = new TestCaseStep();

		step.setId(testNG_tm.getName());
		step.setDuration(Long.parseLong(testNG_tm.getDurationMs()));
		step.setDescription(testNG_tm.getSignature() + ", Started at => " + testNG_tm.getStartedAt()
				+ ", Finished at => " + testNG_tm.getFinishedAt());

		step.setStatus(parseStatus(testNG_tm.getStatus()));
		return step;
	}

	private TestCaseStatus parseStatus(String testNG_tmStatus) {
		switch (TestNG_Statuses.valueOf(testNG_tmStatus)) {
		case PASS:
			return TestCaseStatus.Success;
		case FAIL:
			return TestCaseStatus.Failure;
		case SKIP:
			return TestCaseStatus.Skipped;
		default:
			return TestCaseStatus.Unknown;
		}
	}
}
