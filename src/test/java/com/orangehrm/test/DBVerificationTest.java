package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DBConnection;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;
import com.sun.source.tree.AssertTree;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Map;

public class DBVerificationTest extends BaseClass {
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void LoginPageTest() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());

    }

    @Test(dataProvider = "employeeverification", dataProviderClass = DataProviders.class)
    public void verifyEmployeeNameFromDB(String empId, String empName) {

        SoftAssert softAssert = getSoftAssert();

        ExtentManager.logStep("Login with admin credentials");
        loginPage.login(prop.getProperty("username"), prop.getProperty("password"));

        ExtentManager.logStep("Click on PIM tab");
        homePage.clickOnPimTab();

        ExtentManager.logStep("Search employee");
        homePage.employeesearch(empName);

        ExtentManager.logStep("Fetch employee details from DB");
        Map<String, String> employeedetails = DBConnection.getEmployeeDetails(empId);

        String empfirstname = employeedetails.get("firstname").trim();
        String empmiddlename = employeedetails.get("middlename");
        String emplastname = employeedetails.get("lastname").trim();

        // Build First + Middle Name
        String emplfirstAndmiddlename;
        if (empmiddlename == null || empmiddlename.trim().isEmpty()) {
            emplfirstAndmiddlename = empfirstname;
        } else {
            emplfirstAndmiddlename = empfirstname + " " + empmiddlename.trim();
        }

        ExtentManager.logStep("Verify first & middle name");
        softAssert.assertTrue(
                homePage.verifyEmployeefirstnameAndMiddleName(empName, emplfirstAndmiddlename),
                "First and middle name are not matching"
        );

        ExtentManager.logStep("Verify last name");
        softAssert.assertTrue(
                homePage.verifyEmployeelastName(empName, emplastname),
                "Last name is not matching"
        );

        softAssert.assertAll();
    }


}



