package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;
import org.testng.annotations.Test;

public class Dummyclass extends BaseClass {


    @Test
    public void test() {
        String title = getDriver().getTitle();
        assert title.contains("OrangeHRM")
                : "Test Failed - Title does not contain OrangeHRM. Actual title: " + title;
        System.out.println("Test Passed");
    }
}

