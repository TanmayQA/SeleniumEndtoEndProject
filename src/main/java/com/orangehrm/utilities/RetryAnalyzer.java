package com.orangehrm.utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int retrycount = 0; // no of retires
    private static final int maxRetryCount = 2; // maximum no of retries

    @Override
    public boolean retry(ITestResult iTestResult) {

        if (retrycount < maxRetryCount) {
            retrycount++;
            return true;
        }
        return false;
    }
}
