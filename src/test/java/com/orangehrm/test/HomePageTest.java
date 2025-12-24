package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import org.openqa.selenium.bidi.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomePageTest extends BaseClass {
    private static final Logger log = LoggerFactory.getLogger(HomePageTest.class);
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void HomePageTest()
    {
       loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test(dataProvider = "ValidLogin",dataProviderClass = DataProviders.class)
    public void verifyOrangeHRMLogo(String username,String password)
    {
        loginPage.login(username, password);
        Assert.assertTrue(homePage.verifyOrangeHrmLogo(),"Logo is not visible");

    }


}
