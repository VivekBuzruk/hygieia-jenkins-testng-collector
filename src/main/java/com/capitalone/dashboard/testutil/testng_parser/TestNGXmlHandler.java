/* 
 * The MIT License
 * 
 * Copyright (c) 2010 Bruno P. Kinoshita <http://www.kinoshita.eti.br>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.capitalone.dashboard.testutil.testng_parser;
/** Modified by Vivek S. Buzruk **/

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The TestNG XML Handler.
 * @since 0.1
 */
public class TestNGXmlHandler extends DefaultHandler implements Serializable {

	private static final String TestNG_ELEM_SUITE = "suite";
	private static final String TestNG_ELEM_TEST_METHOD = "test-method";
	private static final String TestNG_ELEM_CLASS = "class";
	private static final String TestNG_ELEM_TEST = "test";
	private static final String TestNG_ELEM_GROUPS = "groups";
	private static final String TestNG_ELEM_GROUP = "group";
	private static final String TestNG_RESULT_STATUS = "status";
	private static final String TestNG_TM_SIGNATURE = "signature";
	private static final String TestNG_TM_IS_CONFIG = "is-config";
	private static final String TestNG_ELEM_ATTR_NAME = "name";
	private static final String TestNG_ELEM_ATTR_STARTED_AT = "started-at";
	private static final String TestNG_ELEM_ATTR_FINISHED_AT = "finished-at";
	private static final String TestNG_ELEM_ATTR_DURATION_MS = "duration-ms";
	private static final String TestNG_ELEM_ATTR_DATA_PROVIDER = "data-provider";

	private static final long serialVersionUID = -7393574325643071292L;

	private TestNG_Suite suite;
	private TestNG_Test test;
	private TestNG_Class clazz;
	private TestNG_TestMethod testMethod;
	private final List<TestNG_Suite> suitesList;
	/**
	 * Default constructor.
	 */
	public TestNGXmlHandler() {
		super();
		suitesList = new ArrayList<TestNG_Suite>();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (TestNG_ELEM_SUITE.equals(qName)) {
			suite = new TestNG_Suite();

			suite.setDurationMs(attributes.getValue(TestNG_ELEM_ATTR_DURATION_MS));
			suite.setFinishedAt(attributes.getValue(TestNG_ELEM_ATTR_FINISHED_AT));
			suite.setStartedAt(attributes.getValue(TestNG_ELEM_ATTR_STARTED_AT));
			suite.setName(attributes.getValue(TestNG_ELEM_ATTR_NAME));
		} else if (TestNG_ELEM_TEST.equals(qName)) {
			test = new TestNG_Test();

			test.setDurationMs(attributes.getValue(TestNG_ELEM_ATTR_DURATION_MS));
			test.setFinishedAt(attributes.getValue(TestNG_ELEM_ATTR_FINISHED_AT));
			test.setStartedAt(attributes.getValue(TestNG_ELEM_ATTR_STARTED_AT));
			test.setName(attributes.getValue(TestNG_ELEM_ATTR_NAME));
		} else if (TestNG_ELEM_CLASS.equals(qName)) {
			clazz = new TestNG_Class();

			clazz.setName(attributes.getValue(TestNG_ELEM_ATTR_NAME));
		} else if (TestNG_ELEM_TEST_METHOD.equals(qName)) {
			testMethod = new TestNG_TestMethod();

			testMethod.setDurationMs(attributes.getValue(TestNG_ELEM_ATTR_DURATION_MS));
			testMethod.setFinishedAt(attributes.getValue(TestNG_ELEM_ATTR_FINISHED_AT));
			testMethod.setStartedAt(attributes.getValue(TestNG_ELEM_ATTR_STARTED_AT));
			testMethod.setName(attributes.getValue(TestNG_ELEM_ATTR_NAME));
			testMethod.setIsConfig(attributes.getValue(TestNG_TM_IS_CONFIG));
			testMethod.setSignature(attributes.getValue(TestNG_TM_SIGNATURE));
			testMethod.setStatus(attributes.getValue(TestNG_RESULT_STATUS));
			testMethod.setDataProvider(attributes.getValue(TestNG_ELEM_ATTR_DATA_PROVIDER));
		} else if (TestNG_ELEM_GROUPS.equals(qName)) {
			// Ignored
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (TestNG_ELEM_TEST.equals(qName)) {
			suite.addTest(test);
		} else if (TestNG_ELEM_CLASS.equals(qName)) {
			test.addClass(clazz);
		} else if (TestNG_ELEM_TEST_METHOD.equals(qName)) {
			clazz.addTestMethod(testMethod);
		} else if (TestNG_ELEM_SUITE.equals(qName)){
			this.suitesList.add(this.suite);
		} else if (TestNG_ELEM_GROUPS.equals(qName)) {
			// Ignored			
		}
	}
	/**
	 * Retrieves the parsed Suites.
	 * @return a List of parsed Suites.
	 */
	public List<TestNG_Suite> getSuite() {
		return suitesList;
	}
}
