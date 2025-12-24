package com.orangehrm.actiondriver;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class Actiondriver {

    private WebDriver driver;
    private WebDriverWait wait;
    public static final Logger logger = BaseClass.logger;

    public Actiondriver(WebDriver driver) {
        this.driver = driver;

        int waitTime = Integer.parseInt(BaseClass.getProp().getProperty("explicitswait"));

        this.wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime));
        logger.info("Webdriver instance is created");
    }


    // Method to Click an element
    public void click(By by) {
        String elementdecription = getElementDescription(by);
        try {
            applyBorder(by, "green");
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
            ExtentManager.logStep("clicked an element" + elementdecription);
            logger.info("Clicked an element --> " + elementdecription);
        } catch (Exception e) {
            applyBorder(by, "red");
            ExtentManager.logFailure(BaseClass.getDriver(), "unable to click", elementdecription);
            logger.info("Unable to click element : " + e.getMessage());
        }
    }

    // Method to Enter text into an input field

    public void enterText(By by, String value) {
        try {
            applyBorder(by, "green");
            waitForElementToBeVisible(by);
            WebElement element = driver.findElement(by);
            element.clear();
            element.sendKeys(value);
            logger.info("Value entered : " + getElementDescription(by) + " " + value);
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.info("Unable to enter text  : " + e.getMessage());
        }
    }

    // Method to get text from an input field

    public String getText(By by) {
        try {
            applyBorder(by, "green");
            waitForElementToBeVisible(by);
            return driver.findElement(by).getText();
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.info("Unable to get text  : " + e.getMessage());
            return "";
        }
    }

    public void waitForElement(By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            logger.info("Element is visible : " + getElementDescription(locator));
        } catch (TimeoutException e) {
            logger.error("Element not visible after waiting : " + getElementDescription(locator));
            throw e;
        }
    }

    // Method to compare two text
//    public boolean compareText(By by, String expectedText) {
//
//        try {
//            waitForElementToBeVisible(by);
//            String actualtext = driver.findElement(by).getText();
//            if (expectedText.equalsIgnoreCase(actualtext)) {
//                applyBorder(by,"green");
//                logger.info("text are matching ");
//                ExtentManager.loginStepwithScreenshot(BaseClass.getDriver(), "Compare text", "Text verified successfully !" + actualtext + " equals " + expectedText);
//                return true;
//
//            } else {
//                applyBorder(by,"red");
//                logger.info("text are not matching ");
//                ExtentManager.logFailure(BaseClass.getDriver(), "Text Comparison failed", "Text verified successfully !" + actualtext + " not bequals " + expectedText);
//                return false;
//            }
//        } catch (Exception e) {
//            logger.info("unable to compare text " + e.getMessage());
//
//        }
//        return false;
//    }


    // Method to compare two text
    public boolean compareText(By by, String expectedText) {
        try {
            waitForElementToBeVisible(by);

            String actualText = driver.findElement(by).getText().trim();
            String expectedClean = expectedText.trim();

            logger.info("Comparing → Expected: '" + expectedClean + "' | Actual: '" + actualText + "'");

            if (expectedClean.equalsIgnoreCase(actualText)) {

                applyBorder(by, "green");
                logger.info("Text matches!");

                ExtentManager.loginStepwithScreenshot(BaseClass.getDriver(),
                        "Compare text",
                        "Text verified successfully → " + actualText);

                return true;

            } else {

                applyBorder(by, "red");
                logger.info("Text does NOT match!");

                ExtentManager.logFailure(BaseClass.getDriver(),
                        "Text Comparison failed",
                        "Expected: " + expectedClean + " | Actual: " + actualText);

                return false;
            }

        } catch (Exception e) {
            logger.info("Unable to compare text → " + e.getMessage());
            return false;
        }
    }


    // wait for element to be clickable

    private void waitForElementToBeClickable(By by) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            logger.info("element is not clickable : " + e.getMessage());
        }
    }

    // Wait for the page to load
    public void waitForPageLoad(int timeOutInSec) {
        try {
            wait.withTimeout(Duration.ofSeconds(timeOutInSec))
                    .until(webDriver ->
                            ((JavascriptExecutor) webDriver)
                                    .executeScript("return document.readyState")
                                    .equals("complete"));
        } catch (Exception e) {
            logger.info("page did not load within : " + timeOutInSec + e.getMessage());
        }
    }


    //Scroll to an element
    public void scrolltoElement(By by) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement element = driver.findElement(by);
            js.executeScript("arguments[0].scrollIntoView(true)", element);
        } catch (Exception e) {
            logger.info("unable locate the element : " + e.getMessage());
        }
    }
    // wait for element to be visible

    private void waitForElementToBeVisible(By by) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            logger.info("element is not visible : " + e.getMessage());
        }
    }

    // Method to check if an element is displayed
    public boolean isDisplayed(By by) {
        try {
            waitForElementToBeVisible(by);
            applyBorder(by, "green");
            logger.info("Element is displayed : " + getElementDescription(by));
            return driver.findElement(by).isDisplayed();

        } catch (Exception e) {
            applyBorder(by, "green");
            logger.info("element is not displayed");
            return false;
        }
    }
    // Method to get the description of an element using By locator

    public String getElementDescription(By locator) {
        // check for null driver or locator to avoid NullPointer Exception

        if (driver == null) {
            return "driver is null";
        }
        if (locator == null) {
            return "locator is null";
        }
        // find the element using the locator
        WebElement element = driver.findElement(locator);

        // Get Element Attribute

        String name = element.getDomAttribute("name");
        String id = element.getDomAttribute("id");
        String text = element.getText();
        String className = element.getDomAttribute("class");
        String placeHolder = element.getDomAttribute("placeholder");

        // Return the description based on element attributes
        try {
            if (isNotEmpty(name)) {
                return "Element with name: " + name;
            } else if (isNotEmpty(id)) {
                return "Element with id: " + id;
            } else if (isNotEmpty(text)) {
                return "Element with text: " + truncate(text, 50);
            } else if (isNotEmpty(className)) {
                return "Element with classname: " + className;
            } else if (isNotEmpty(placeHolder)) {
                return "Element with placeholder: " + placeHolder;
            }
        } catch (Exception e) {

            logger.error("unable to describe the element " + e.getMessage());
        }
        return "unable to describe the element";

    }

    // utility method to check a string is not NULL or empty

    private boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    // utility  method to truncate long string
    private String truncate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength) + "...";
    }

    // Utility Method to Border an element

    public void applyBorder(By by, String color) {
        // locate the element
        try {
            WebElement element = driver.findElement(by);

            // Apply the border
            String script = "arguments[0].style.border='3px solid " + color + "'";
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(script, element);
            logger.info("Applied the border with color " + color + "to element " + getElementDescription(by));

        } catch (Exception e) {
            logger.info("fail to apply color to element " + getElementDescription(by), e);
        }
    }

    // ===================== Select Methods =====================

    // Method to select a dropdown by visible text
    public void selectByVisibleText(By by, String value) {
        try {
            WebElement element = driver.findElement(by);
            new Select(element).selectByVisibleText(value);
            applyBorder(by, "green");
            logger.info("Selected dropdown value: " + value);
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to select dropdown value: " + value, e);
        }
    }

    // Method to select a dropdown by value
    public void selectByValue(By by, String value) {
        try {
            WebElement element = driver.findElement(by);
            new Select(element).selectByValue(value);
            applyBorder(by, "green");
            logger.info("Selected dropdown value by actual value: " + value);
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to select dropdown by value: " + value, e);
        }
    }

    // Method to select a dropdown by index
    public void selectByIndex(By by, int index) {
        try {
            WebElement element = driver.findElement(by);
            new Select(element).selectByIndex(index);
            applyBorder(by, "green");
            logger.info("Selected dropdown value by index: " + index);
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to select dropdown by index: " + index, e);
        }
    }

    // Method to get all options from a dropdown
    public List<String> getDropdownOptions(By by) {
        List<String> optionsList = new ArrayList<>();
        try {
            WebElement dropdownElement = driver.findElement(by);
            Select select = new Select(dropdownElement);
            for (WebElement option : select.getOptions()) {
                optionsList.add(option.getText());
            }
            applyBorder(by, "green");
            logger.info("Retrieved dropdown options for " + getElementDescription(by));
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to get dropdown options: " + e.getMessage());
        }
        return optionsList;
    }


    // ===================== JavaScript Utility Methods =====================

    // Method to click using JavaScript
    public void clickUsingJS(By by) {
        try {
            WebElement element = driver.findElement(by);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            applyBorder(by, "green");
            logger.info("Clicked element using JavaScript: " + getElementDescription(by));
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to click using JavaScript", e);
        }
    }

    // Method to scroll to the bottom of the page
    public void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        logger.info("Scrolled to the bottom of the page.");
    }

    // Method to highlight an element using JavaScript
    public void highlightElementJS(By by) {
        try {
            WebElement element = driver.findElement(by);
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid yellow'", element);
            logger.info("Highlighted element using JavaScript: " + getElementDescription(by));
        } catch (Exception e) {
            logger.error("Unable to highlight element using JavaScript", e);
        }
    }

    // ===================== Window and Frame Handling =====================

    // Method to switch between browser windows
    public void switchToWindow(String windowTitle) {
        try {
            Set<String> windows = driver.getWindowHandles();
            for (String window : windows) {
                driver.switchTo().window(window);
                if (driver.getTitle().equals(windowTitle)) {
                    logger.info("Switched to window: " + windowTitle);
                    return;
                }
            }
            logger.warn("Window with title " + windowTitle + " not found.");
        } catch (Exception e) {
            logger.error("Unable to switch window", e);
        }
    }

    // Method to switch to an iframe
    public void switchToFrame(By by) {
        try {
            driver.switchTo().frame(driver.findElement(by));
            logger.info("Switched to iframe: " + getElementDescription(by));
        } catch (Exception e) {
            logger.error("Unable to switch to iframe", e);
        }
    }

    // Method to switch back to the default content
    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
        logger.info("Switched back to default content.");
    }

    // ===================== Alert Handling =====================

    // Method to accept an alert popup
    public void acceptAlert() {
        try {
            driver.switchTo().alert().accept();
            logger.info("Alert accepted.");
        } catch (Exception e) {
            logger.error("No alert found to accept", e);
        }
    }

    // Method to dismiss an alert popup
    public void dismissAlert() {
        try {
            driver.switchTo().alert().dismiss();
            logger.info("Alert dismissed.");
        } catch (Exception e) {
            logger.error("No alert found to dismiss", e);
        }
    }

    // Method to get alert text
    public String getAlertText() {
        try {
            return driver.switchTo().alert().getText();
        } catch (Exception e) {
            logger.error("No alert text found", e);
            return "";
        }
    }

    // ===================== Browser Actions =====================

    public void refreshPage() {
        try {
            driver.navigate().refresh();
            ExtentManager.logStep("Page refreshed successfully.");
            logger.info("Page refreshed successfully.");
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to refresh page", "refresh_page_failed");
            logger.error("Unable to refresh page: " + e.getMessage());
        }
    }

    public String getCurrentURL() {
        try {
            String url = driver.getCurrentUrl();
            ExtentManager.logStep("Current URL fetched: " + url);
            logger.info("Current URL fetched: " + url);
            return url;
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to fetch current URL", "get_current_url_failed");
            logger.error("Unable to fetch current URL: " + e.getMessage());
            return null;
        }
    }

    public void maximizeWindow() {
        try {
            driver.manage().window().maximize();
            ExtentManager.logStep("Browser window maximized.");
            logger.info("Browser window maximized.");
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to maximize window", "maximize_window_failed");
            logger.error("Unable to maximize window: " + e.getMessage());
        }
    }

    // ===================== Advanced WebElement Actions =====================

    public void moveToElement(By by) {
        String elementDescription = getElementDescription(by);
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(driver.findElement(by)).perform();
            ExtentManager.logStep("Moved to element: " + elementDescription);
            logger.info("Moved to element --> " + elementDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to move to element", elementDescription + "_move_failed");
            logger.error("Unable to move to element: " + e.getMessage());
        }
    }

    public void dragAndDrop(By source, By target) {
        String sourceDescription = getElementDescription(source);
        String targetDescription = getElementDescription(target);
        try {
            Actions actions = new Actions(driver);
            actions.dragAndDrop(driver.findElement(source), driver.findElement(target)).perform();
            ExtentManager.logStep("Dragged element: " + sourceDescription + " and dropped on " + targetDescription);
            logger.info("Dragged element: " + sourceDescription + " and dropped on " + targetDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to drag and drop", sourceDescription + "_drag_failed");
            logger.error("Unable to drag and drop: " + e.getMessage());
        }
    }

    public void doubleClick(By by) {
        String elementDescription = getElementDescription(by);
        try {
            Actions actions = new Actions(driver);
            actions.doubleClick(driver.findElement(by)).perform();
            ExtentManager.logStep("Double-clicked on element: " + elementDescription);
            logger.info("Double-clicked on element --> " + elementDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to double-click element", elementDescription + "_doubleclick_failed");
            logger.error("Unable to double-click element: " + e.getMessage());
        }
    }

    public void rightClick(By by) {
        String elementDescription = getElementDescription(by);
        try {
            Actions actions = new Actions(driver);
            actions.contextClick(driver.findElement(by)).perform();
            ExtentManager.logStep("Right-clicked on element: " + elementDescription);
            logger.info("Right-clicked on element --> " + elementDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to right-click element", elementDescription + "_rightclick_failed");
            logger.error("Unable to right-click element: " + e.getMessage());
        }
    }

    public void sendKeysWithActions(By by, String value) {
        String elementDescription = getElementDescription(by);
        try {
            Actions actions = new Actions(driver);
            actions.sendKeys(driver.findElement(by), value).perform();
            ExtentManager.logStep("Sent keys to element: " + elementDescription + " | Value: " + value);
            logger.info("Sent keys to element --> " + elementDescription + " | Value: " + value);
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to send keys", elementDescription + "_sendkeys_failed");
            logger.error("Unable to send keys to element: " + e.getMessage());
        }
    }

    public void clearText(By by) {
        String elementDescription = getElementDescription(by);
        try {
            driver.findElement(by).clear();
            ExtentManager.logStep("Cleared text in element: " + elementDescription);
            logger.info("Cleared text in element --> " + elementDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to clear text", elementDescription + "_clear_failed");
            logger.error("Unable to clear text in element: " + e.getMessage());
        }
    }

    // Method to upload a file
    public void uploadFile(By by, String filePath) {
        try {
            driver.findElement(by).sendKeys(filePath);
            applyBorder(by, "green");
            logger.info("Uploaded file: " + filePath);
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to upload file: " + e.getMessage());
        }
    }


}
