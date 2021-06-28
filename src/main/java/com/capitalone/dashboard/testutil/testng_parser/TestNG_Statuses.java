package com.capitalone.dashboard.testutil.testng_parser;
/** Modified by Vivek S. Buzruk **/

/* package */ public enum TestNG_Statuses {

    PASS("pass"), FAIL("fail"), SKIP("skip");
    
    private String value;
    
    TestNG_Statuses(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return getValue();
    }
    
    static TestNG_Statuses get(String value) {
        if ("pass".equalsIgnoreCase(value)) {
            return PASS;
        } else if("fail".equalsIgnoreCase(value)) {
            return FAIL;
        } else if("skip".equalsIgnoreCase(value)) {
            return SKIP;
        }
        return null;
    }
    
}
