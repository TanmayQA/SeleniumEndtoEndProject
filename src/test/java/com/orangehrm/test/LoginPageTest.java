package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.xml.crypto.Data;

public class LoginPageTest extends BaseClass {
    private static final Logger log = LoggerFactory.getLogger(LoginPageTest.class);
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void LoginPageTest() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());

    }

    @Test(dataProvider = "ValidLogin",dataProviderClass = DataProviders.class)
    public void verifyValidLoginTest(String username, String password) {
       // ExtentManager.startTest("valid login test"); --- Implemented in TestListners class
        //ExtentManager.logStep("Navigating to login page entering username and password");
        loginPage.login(username, password);
        Assert.assertTrue(homePage.isAdminTabVisiable(), "Admin tab should be visible after successful login");
        homePage.logout();
    }

    @Test(dataProvider = "Invalidlogin",dataProviderClass = DataProviders.class)
    public void invalidlogintest(String username, String password) {
        loginPage.login(username, password);
        String expectederrormessage = "Invalid credentials";
        Assert.assertTrue(loginPage.verifyErrormessage(expectederrormessage), "Test Failed: Invalid Error message");
    }


}
