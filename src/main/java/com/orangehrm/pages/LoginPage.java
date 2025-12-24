package com.orangehrm.pages;

import com.orangehrm.actiondriver.Actiondriver;
import com.orangehrm.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private Actiondriver actiondriver;

    // Define locators using By class

    private By usernamefield = By.name("username");
    private By passwordfield = By.xpath("//input[@type ='password']");
    private By loginbtn = By.xpath("//button[@type ='submit']");
    private By loginerrormessage = By.xpath("//p[text()='Invalid credentials']");

    // initialize the Actiondriver object by passing webdriver instance
    public LoginPage(WebDriver driver) {
        this.actiondriver = BaseClass.getActionDriver();
    }

    // Method to perform login
    public void login(String username, String password) {
        actiondriver.enterText(usernamefield, username);
        actiondriver.enterText(passwordfield, password);
        actiondriver.click(loginbtn);

    }

    //method to check if error message is displayed
    public boolean isErrorMessageisDisplayed() {
        return actiondriver.isDisplayed(loginerrormessage);
    }

    //method to get the text from error message
    public String geterrormessageText() {
        return actiondriver.getText(loginerrormessage);
    }

    //Verify if error is correct or not
    public boolean verifyErrormessage(String expectedError) {
        return actiondriver.compareText(loginerrormessage, expectedError);
    }
}
