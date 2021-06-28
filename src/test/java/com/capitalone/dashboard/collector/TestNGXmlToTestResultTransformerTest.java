package com.capitalone.dashboard.collector;

import com.capitalone.dashboard.model.TestCase;
import com.capitalone.dashboard.model.TestCaseStatus;
import com.capitalone.dashboard.model.TestSuite;
import com.capitalone.dashboard.model.TestSuiteType;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class TestNGXmlToTestResultTransformerTest {
	TestNGxmlToTestResultTransformer transformer = new TestNGxmlToTestResultTransformer();

    @Test
    public void testTransform() throws Exception {
        String xml = getXML("testng-results.xml");

        Iterable<TestSuite> suites = transformer.transformer(xml);
//        assertThat(suites, notNullValue());
//
//        Iterator<TestSuite> suiteIt = suites.iterator();
//        Iterator<TestCase> testCaseIt;
//        TestSuite suite;
//
//        suite = suiteIt.next();
//        testCaseIt = suite.getTestCases().iterator();
//        assertSuite(suite, "Feature:eCUKE Feature", 4, 0, 0, 4, 15019839l);
//
//        assertTestCase(testCaseIt.next(), "ecuke-feature;i-say-hi", "Scenario:I say hi", 4001555l, TestCaseStatus.Success);
//        assertThat(testCaseIt.hasNext(), is(true));
//
//        assertTestCase(testCaseIt.next(), "ecuke-feature;you-say-hi", "Scenario:You say hi", 1001212l, TestCaseStatus.Success);
//        assertThat(testCaseIt.hasNext(), is(true));
//
//        assertTestCase(testCaseIt.next(), "ecuke-feature;eating-cucumbers", "Scenario Outline:Eating Cucumbers", 2013197l, TestCaseStatus.Success);
//        assertThat(testCaseIt.hasNext(), is(true));
//
//        assertTestCase(testCaseIt.next(), "ecuke-feature;eating-cucumbers", "Scenario Outline:Eating Cucumbers", 8003875l, TestCaseStatus.Success);
//        assertThat(testCaseIt.hasNext(), is(false));

    }

//    private void assertSuite(TestSuite suite, String desc, int success, int fail, int skip, int total, long duration) {
//        assertThat(suite.getType(), is(TestSuiteType.Functional));
//        assertThat(suite.getDescription(), is(desc));
//        assertThat(suite.getFailedTestCaseCount(), is(fail));
//        assertThat(suite.getSuccessTestCaseCount(), is(success));
//        assertThat(suite.getSkippedTestCaseCount(), is(skip));
//        assertThat(suite.getTotalTestCaseCount(), is(total));
//        assertThat(suite.getDuration(), is(duration));
//        assertThat(suite.getStartTime(), is(0l));
//        assertThat(suite.getEndTime(), is(0l));
//    }
//
//    private void assertTestCase(TestCase tc, String id, String name, long duration, TestCaseStatus status) {
//        assertThat(tc.getId(), is(id));
//        assertThat(tc.getDescription(), is(name));
//        assertThat(tc.getDuration(), is(duration));
//        assertThat(tc.getStatus(), is(status));
//    }

    private String getXML(String fileName) throws IOException {
        InputStream inputStream = TestNGXmlToTestResultTransformerTest.class.getResourceAsStream(fileName);
        return IOUtils.toString(inputStream);
    }
}
