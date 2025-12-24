package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;
import org.testng.annotations.Test;

public class Dummyclass extends BaseClass {

@Test
    public void test()
    {
        //Test
        String title = getDriver().getTitle();
        assert title.equals("OrangeHRM"):"Test Failed - Title is not matching";
        System.out.println("Test Passed");
    }
}
