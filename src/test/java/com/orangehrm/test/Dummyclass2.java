package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import org.testng.annotations.Test;

public class Dummyclass2 extends BaseClass {

@Test
    public void test()
    {
        //check-in 2
        String title = getDriver().getTitle();
        assert title.contains("OrangeHRM"):"Test Failed - Title is not matching";
        System.out.println("Test Passed");
    }
}
