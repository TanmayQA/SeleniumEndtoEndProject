package com.orangehrm.listeners;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;
import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TestListeners implements ITestListener, IAnnotationTransformer {

    // retry logic
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {

        annotation.setRetryAnalyzer(RetryAnalyzer.class);

    }

    // Triggere when a test starts
    @Override
    public void onTestStart(ITestResult result) {

        String testname = result.getMethod().getMethodName();
        // Start logging in Extent Reports
        ExtentManager.startTest(testname);
        ExtentManager.logStep("Test Started: " + testname);
    }

    // Triggered when a test succeeds
    @Override
    public void onTestSuccess(ITestResult result) {
        String testname = result.getMethod().getMethodName();
        if (!(result.getTestClass().getName().toLowerCase().contains("api"))) {
            ExtentManager.loginStepwithScreenshot(BaseClass.getDriver(), "Test Passed successfully", "Test End: " + testname + "Test Passed");
        } else {
            ExtentManager.loginStepforApi("Test End: " + testname + "Test Passed");

        }
    }

    // Triggered when a test fail
    @Override
    public void onTestFailure(ITestResult result) {
        String testname = result.getMethod().getMethodName();
        String failuremsg = result.getThrowable().getMessage();
        ExtentManager.logStep(failuremsg);
        if (!(result.getTestClass().getName().toLowerCase().contains("api"))) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Test Failed!", "Test End: " + testname + " Test Failed");
        } else {
            ExtentManager.logFailureApi("Test End: " + testname + "Test Passed");
        }
    }

    // Triggered when test skip
    @Override
    public void onTestSkipped(ITestResult result) {
        String testname = result.getMethod().getMethodName();
        ExtentManager.logSkip("testSkip: " + testname);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
    }

    // Trigger when a suite starts
    @Override
    public void onStart(ITestContext context) {

        // Initialize the extent Reports
        ExtentManager.getReporter();
    }

    // Triggered when the suite ends
    @Override
    public void onFinish(ITestContext context) {

        // Flush the Extent Reports.
        ExtentManager.flushReport();
    }
}
