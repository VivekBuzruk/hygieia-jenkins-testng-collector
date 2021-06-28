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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * TestNG parser.
 * @since 0.1
 */
public class TestNGParser implements Serializable {

	private static final long serialVersionUID = -6714585408222816355L;

	private static final String APACHE_EXT_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
	
	private static final Logger LOGGER = Logger.getLogger(TestNGParser.class.getName());

	public List<TestNG_Suite> parse(String xmlString, String sourcePath)  throws TestNG_ParserException {
		InputStream is = null;
		List<TestNG_Suite> mySuiteList = null;

		// or, from a URL, then retrieve an InputStream from a URL
		try {
			is =  new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8)); 
			mySuiteList = parse(is, sourcePath);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ioe) {
					// OK, Do nothing
				}
			}
		}
		return mySuiteList;
	}
	
	public List<TestNG_Suite> parse(URL testng_results_url)  throws TestNG_ParserException {
		InputStream is = null;
		List<TestNG_Suite> mySuiteList = null;

		// or, from a URL, then retrieve an InputStream from a URL
		try {
			is = testng_results_url.openStream();	
			mySuiteList = parse(is, testng_results_url.getPath());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ioe) {
					// OK, Do nothing
				}
			}
		}
		return mySuiteList;
	}
	/**
	 * Parses the content of an input stream and returns a Suite.
	 * 
	 * @param file the input file
	 * @return List of Resulting object.
	 * @throws TestNG_ParserException in case of any exception found by the parser
	 */
	public List<TestNG_Suite> parse(File file) throws TestNG_ParserException {
		InputStream is = null;
		List<TestNG_Suite> mySuiteList = null;
		
		// if we were getting data from a file, we might use:
		try {
			is = new FileInputStream(file);
			mySuiteList = parse(is, file.getAbsolutePath());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ioe) {
					// OK, Do nothing
				}
			}
		}
		return mySuiteList;
	}
		
	public List<TestNG_Suite> parse(InputStream xmlInputStream, String myPath) throws TestNG_ParserException {

		// FileInputStream fileInputStream = null;
		final TestNGXmlHandler handler = new TestNGXmlHandler();

		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		try {
			factory.setFeature(APACHE_EXT_DTD, false);
		} catch (ParserConfigurationException e) {
		    LOGGER.log(Level.SEVERE, e.getMessage(), e);
		} catch (SAXNotRecognizedException e) {
		    LOGGER.log(Level.SEVERE, e.getMessage(), e);
		} catch (SAXNotSupportedException e) {
		    LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}

		SAXParser parser = null;
		final List<TestNG_Suite> suites;

		try {
			// fileInputStream = new FileInputStream(file); // use xmlInputStream
			parser = factory.newSAXParser();
			parser.parse(xmlInputStream, handler);

			suites = handler.getSuite();
			// Setting file for all the suites
			for (TestNG_Suite suite : suites) {
				suite.setFile(myPath);
			}

		} catch (ParserConfigurationException e) {
			throw new TestNG_ParserException(e);
		} catch (SAXException e) {
			throw new TestNG_ParserException(e);
		} catch (IOException e) {
			throw new TestNG_ParserException(e);
		} 
		return suites;
	}

}
