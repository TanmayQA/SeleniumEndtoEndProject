package com.orangehrm.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExtentManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static Map<Long, WebDriver> driverMap = new HashMap<>();

    // Initialize the extent Report

    public static ExtentReports getReporter() {

        if (extent == null) {
            String reportPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReport/ExtentReport.html";
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setReportName("Automation Test Report");
            spark.config().setDocumentTitle("OrangeHRM Report");
            spark.config().setTheme(Theme.DARK);
            extent = new ExtentReports();
            extent.attachReporter(spark);
            // add system information

            extent.setSystemInfo("Operating System", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("user Name", System.getProperty("user.name"));
        }
        return extent;
    }

    // start the test

    public static ExtentTest startTest(String testname) {
        ExtentTest extentTest = getReporter().createTest(testname);
        test.set(extentTest);
        return extentTest;
    }

    // end test

    public static void endTest() {
        test.remove();
    }

    // get current thread's test

    public static ExtentTest getTest() {
        return test.get();
    }

    // method to get the name of the current test
    public static String getTestname() {
        ExtentTest currentTest = getTest();
        if (currentTest != null) {
            return currentTest.getModel().getName();
        }

        return "No test is currently active for this thread";
    }

    // Log the step
    public static void logStep(String logmsg) {
        ExtentTest currentTest = getTest();
        if (currentTest != null) {
            currentTest.info(logmsg);
        }
    }


    // log step with screenshot
    public static void loginStepwithScreenshot(WebDriver driver, String logmsg, String screenShotMessage) {
        ExtentTest currentTest = getTest();
        if (currentTest != null) {
            currentTest.pass(logmsg);
            attachScreenshot(driver, logmsg);
        }

    }

    // log step with screenshot for API
    public static void loginStepforApi(String logmsg) {

        getTest().pass(logmsg);

    }

    // Log a failure
    public static void logFailure(WebDriver driver, String logmsg, String screenShotMessage) {
        ExtentTest currentTest = getTest();
        if (currentTest != null) {
            currentTest.fail(logmsg);
            attachScreenshot(driver, logmsg);
        }
    }

    // Log a failure for api
    public static void logFailureApi(String logmsg) {
        String colormessage = String.format("<span style='color:red;'>%s</span>", logmsg);
        getTest().fail(colormessage);

    }


    // Log a skip
    public static void logSkip(String logmsg) {
        ExtentTest currentTest = getTest();
        if (currentTest != null) {
            currentTest.skip(logmsg);
        }
    }

    // Take a screenshot with date and time in the file

    public static String takeScreenshot(WebDriver driver, String screenShotName) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        // format dae and time for the file name
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        // save the file
        String despath = System.getProperty("user.dir") + "/src/test/resources/screenshots/" + screenShotName + "_" + timeStamp + ".png";
        try {
            File finalPath = new File(despath);
            FileUtils.copyFile(src, finalPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // convert screenshot to Base64 for embedding in the report

        String base64Format = convertToBase64(src);
        return base64Format;
    }
    // convert string format to base64 format

    public static String convertToBase64(File screenshotFile) {
        String base64format = "";
        // Read the file content into a byte array
        byte[] filecontent = null;
        try {
            filecontent = FileUtils.readFileToByteArray(screenshotFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // convert the byte array to Base64 String
        return base64format = Base64.getEncoder().encodeToString(filecontent);
    }

    // Attach screenshot to report using Base64

    public static void attachScreenshot(WebDriver driver, String message) {
        try {
            String screenShotBase64 = takeScreenshot(driver, getTestname());
            getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotBase64).build());
        } catch (Exception e) {
            ExtentTest currentTest = getTest();
            if (currentTest != null) {
                currentTest.fail("Failed to attach screenshot: " + message);
            }
        }
    }

    // Register WebDriver for current Thread

    public static void registerDriver(WebDriver driver) {
        driverMap.put(Thread.currentThread().getId(), driver);
    }


    public static void flushReport() {
        getReporter().flush();
    }

}
