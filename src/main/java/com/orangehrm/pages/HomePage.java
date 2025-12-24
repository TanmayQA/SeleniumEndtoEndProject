package com.orangehrm.pages;

import com.orangehrm.actiondriver.Actiondriver;
import com.orangehrm.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private Actiondriver actiondriver;

    // Define locators using By class

    private By adminTab = By.xpath("//span[text()='Admin']");
    private By useridbtn = By.xpath("//span[@class='oxd-userdropdown-tab']//p");
    private By logoutbtn = By.xpath("//a[text()='Logout']");
    private By orangeHRMLogo = By.xpath("//div[@class='oxd-brand-banner']//img");
    private By pimtab = By.xpath("//span[text()='PIM']");
    private By searchbtn = By.xpath("//button[text()=' Search ']");
    private By empsearch = By.xpath("//label[text()='Employee Name']/parent::div/following-sibling::div/div/div/input");



    // initialize the Actiondriver object by passing webdriver instance
    public HomePage(WebDriver driver) {
        this.actiondriver = BaseClass.getActionDriver();
    }

    // Method to verify if admin tab is visible
    public boolean isAdminTabVisiable() {
        return actiondriver.isDisplayed(adminTab);
    }


    // Method to Navigate the PIM Tab

    public void clickOnPimTab() {
        actiondriver.click(pimtab);
    }

    // Employee Search
    public void employeesearch(String employeeName) {
        actiondriver.enterText(empsearch, employeeName);
        actiondriver.click(searchbtn);

        actiondriver.waitForElement(employeeRowByFirstName(employeeName));
        actiondriver.scrolltoElement(employeeRowByFirstName(employeeName));
    }


    // ===== ROW LOCATOR (ANCHOR) =====
    private By employeeRowByFirstName(String employeeName) {
        return By.xpath(
                "//div[contains(@class,'oxd-table-row--with-border')" +
                        " and .//div[contains(normalize-space(),'" + employeeName + "')]]"
        );
    }



    // ===== COLUMN LOCATORS =====
    // Column 3 → First (& Middle) Name
    private By employeefirstandmiddlename(String employeeName) {
        return By.xpath(
                "//div[contains(@class,'oxd-table-row--with-border')" +
                        " and .//div[contains(normalize-space(),'" + employeeName + "')]]" +
                        "/div[3]"
        );
    }




    // Column 4 → Last Name
    private By employeelastname(String employeeName) {
        return By.xpath(
                "//div[contains(@class,'oxd-table-row--with-border')" +
                        " and .//div[contains(normalize-space(),'" + employeeName + "')]]" +
                        "/div[4]"
        );
    }



    // ===== VERIFICATION METHODS =====
    public boolean verifyEmployeefirstnameAndMiddleName(
            String employeeName,
            String expectedFirstAndMiddleName) {

        return actiondriver.compareText(
                employeefirstandmiddlename(employeeName),
                expectedFirstAndMiddleName
        );
    }

    public boolean verifyEmployeelastName(
            String employeeName,
            String expectedLastName) {

        return actiondriver.compareText(
                employeelastname(employeeName),
                expectedLastName
        );
    }



    // Method to verify the HRM logo
    public boolean verifyOrangeHrmLogo() {
        return actiondriver.isDisplayed(orangeHRMLogo);
    }

    //Method to perform logout operation
    public void logout() {
        actiondriver.click(useridbtn);
        actiondriver.click(logoutbtn);
    }

}
