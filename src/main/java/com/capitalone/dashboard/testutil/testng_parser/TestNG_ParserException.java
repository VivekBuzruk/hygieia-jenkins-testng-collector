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

/**
 * TestNG parser exception.
 * @since 0.1
 */
public class TestNG_ParserException extends RuntimeException {

	private static final long serialVersionUID = 1360623334007156711L;
	/**
	 * Default no args constructor.
	 */
	public TestNG_ParserException() {
		super();
	}
	/**
	 * @param message exception message
	 */
	public TestNG_ParserException(String message) {
		super(message);
	}
	/**
	 * @param cause exception cause
	 */
	public TestNG_ParserException(Throwable cause) {
		super(cause);
	}
	/**
	 * @param message exception message
	 * @param cause exception cause
	 */
	public TestNG_ParserException(String message, Throwable cause) {
		super(message, cause);
	}
}
