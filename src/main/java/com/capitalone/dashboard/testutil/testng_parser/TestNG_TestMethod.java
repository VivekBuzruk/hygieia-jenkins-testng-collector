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

/**
 * Represents the &lt;test-method&gt; tag. This tag is child of the &lt;class&gt; tag.
 *
 * @since 0.1
 */
public class TestNG_TestMethod implements Serializable {

	private static final long serialVersionUID = 196237635867018108L;
	/**
	 * The status attribute.
	 */
	private String status;
	/**
	 * The signature attribute.
	 */
	private String signature;
	/**
	 * The name attribute.
	 */
	private String name;
	/**
	 * The is-config attribute.
	 */
	private String isConfig;
	/**
	 * The duration attribute.
	 */
	private String durationMs;
	/**
	 * The started-at attribute.
	 */
	private String startedAt;
	/**
	 * The finished-at attribute.
	 */
	private String finishedAt;
	/**
	 * The data-provider.
	 */
	private String dataProvider;
	/**
	 * Default constructor.
	 */
	public TestNG_TestMethod() {
		super();
	}
	/**
	 * Retrieves the status.
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * Sets the status.
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * Retrieves the signature.
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}
	/**
	 * Sets the signature.
	 * @param signature the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}
	/**
	 * Retrieves the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Retrieves the isConfig.
	 * @return the isConfig
	 */
	public String getIsConfig() {
		return isConfig;
	}
	/**
	 * Sets the isConfig.
	 * @param isConfig the isConfig to set
	 */
	public void setIsConfig(String isConfig) {
		this.isConfig = isConfig;
	}
	/**
	 * Retrieves the duration in ms.
	 * @return the duration in ms.
	 */
	public String getDurationMs() {
		return durationMs;
	}
	/**
	 * Sets the duration in ms.
	 * @param durationMs the duration in ms to set.
	 */
	public void setDurationMs(String durationMs) {
		this.durationMs = durationMs;
	}
	/**
	 * Retrieves the startedAt.
	 * @return the startedAt
	 */
	public String getStartedAt() {
		return startedAt;
	}
	/**
	 * Sets the startedAt.
	 * @param startedAt the startedAt to set
	 */
	public void setStartedAt(String startedAt) {
		this.startedAt = startedAt;
	}
	/**
	 * Retrieves the finishedAt.
	 * @return the finishedAt
	 */
	public String getFinishedAt() {
		return finishedAt;
	}
	/**
	 * Sets the finishedAt.
	 * @param finishedAt the finishedAt to set
	 */
	public void setFinishedAt(String finishedAt) {
		this.finishedAt = finishedAt;
	}
	/**
	 * @return the dataProvider
	 */
	public String getDataProvider() {
		return dataProvider == null ? "" : dataProvider;
	}
	/**
	 * @param dataProvider the dataProvider to set
	 */
	public void setDataProvider(String dataProvider) {
		this.dataProvider = dataProvider;
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
	    if (obj != null && obj instanceof TestNG_TestMethod) {
	        TestNG_TestMethod other = (TestNG_TestMethod) obj;
	        return 
	                this.signature != null
	                && this.signature.equals(other.signature)
	                && this.name != null
	                && this.name.equals(other.name)
	                && this.dataProvider != null
	                && this.dataProvider.equals(other.dataProvider); 
	    }
	    return false;
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
	    int hash = getClass().getCanonicalName().hashCode();
	    hash <<= 2;
	    if (this.signature != null) {
	        hash ^= this.signature.hashCode();
	    }
	    if (this.name != null) {
	        hash ^= this.name.hashCode();
	    }
	    if (this.dataProvider != null) {
	        hash ^= this.dataProvider.hashCode();
	    }
	    return hash;
	}
}
