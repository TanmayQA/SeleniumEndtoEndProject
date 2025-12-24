package com.orangehrm.base;

import com.orangehrm.actiondriver.Actiondriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class BaseClass {

    protected static Properties prop;
    //protected static WebDriver driver;
    //private static Actiondriver actiondriver;
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<Actiondriver> actiondriver = new ThreadLocal<>();
    public static final Logger logger = LoggerManager.getLogger(BaseClass.class);
    protected ThreadLocal<SoftAssert> softAssertThreadLocal = ThreadLocal.withInitial(SoftAssert::new);

    public SoftAssert getSoftassert() {
        return softAssertThreadLocal.get();
    }
    @BeforeSuite
    public void loadconfig() throws IOException {
        // load the configuration file
        prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/config.properties");
        prop.load(fis);
        logger.info("config.properties file load");
        // Start the Extent Report
        // ExtentManager.getReporter(); -- This has been implemented in TestListners class
    }

    @BeforeMethod
    public void setup() throws IOException {
        // START EXTENT TEST for this class/method
        // ExtentManager.startTest(this.getClass().getSimpleName());
        System.out.println("Setting up the webdriver for : " + this.getClass().getSimpleName());
        launchBrowser();
        configureBrowser();
        logger.info("WebDriver Initilized and Browser maximized");
        logger.trace("This is the fatal message");
        logger.error("This is the error message");
        logger.debug("This is the debug message");
        logger.fatal("This is the fatal message");
        logger.warn("This is the warn message");
        // Initialize the action driver only once -- singleton pattern

//        if (actiondriver == null) {
//            actiondriver = new Actiondriver(driver);
//            System.out.println("Action driver instance is created. " + Thread.currentThread().getId());
//        }

        //Initialize the Action driver for the current thread
        actiondriver.set(new Actiondriver(getDriver()));
        logger.info("Actiondriver initilized for thread" + Thread.currentThread());

    }

    // Initialize the Webdriver based on browswer define in config files
    private void launchBrowser() {
        String browser = prop.getProperty("browser");

        if (browser.equalsIgnoreCase("chrome")) {

            // Create ChromeOptions

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");// run chrome in headless mode
            options.addArguments("--disable-gpu");//Disable GPU for headless mode
            //options.addArguments("--window-size=1900,1080"); // set window size
            options.addArguments("--disable-notifications"); // Disable browser notifications
            options.addArguments("no-sandbox");// Required for some CI environment
            options.addArguments("--disable-dev-shm-usage"); // Resolve issues is resources

            //  driver = new ChromeDriver();
            driver.set(new ChromeDriver(options));
            ExtentManager.registerDriver(getDriver());
            logger.info("Chromedriver Intiliazed");
        } else if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");// run chrome in headless mode
            options.addArguments("--disable-gpu");//Disable GPU for headless mode
            //options.addArguments("--window-size=1900,1080"); // set window size
            options.addArguments("--disable-notifications"); // Disable browser notifications
            options.addArguments("no-sandbox");// Required for some CI environment, needed for CI/CD environment
            options.addArguments("--disable-dev-shm-usage"); //prevent crashes in low- res
            // driver = new FirefoxDriver();
            driver.set(new FirefoxDriver(options));
            ExtentManager.registerDriver(getDriver());
            logger.info("Firefox Intiliazed");

        } else if (browser.equalsIgnoreCase("edge")) {
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--headless");// run chrome in headless mode
            options.addArguments("--disable-gpu");//Disable GPU for headless mode
            //options.addArguments("--window-size=1900,1080"); // set window size
            options.addArguments("--disable-notifications"); // Disable browser notifications
            options.addArguments("no-sandbox");// Required for some CI environment, needed for CI/CD environment
            options.addArguments("--disable-dev-shm-usage"); //prevent crashes in low- res
            // driver = new EdgeDriver();
            driver.set(new EdgeDriver(options));
            ExtentManager.registerDriver(getDriver());
            logger.info("Edgedriver Intiliazed");
        } else {
            throw new IllegalArgumentException("Browser not supported" + browser);
        }
    }

    //Config Browser settings :-
    private void configureBrowser() {
        // Implicit wait

        int implicitwait = Integer.parseInt(prop.getProperty("implicitWait"));

        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitwait));

        // maximize the browser

        getDriver().manage().window().maximize();

        // Navigate to url

        try {
            getDriver().get(prop.getProperty("url"));
        } catch (Exception e) {
            System.out.println("failed to navigate to the url :" + e.getMessage());
        }
    }

    // Webdriver getter method
    public static WebDriver getDriver() {
        if (driver.get() == null) {
            System.out.println("Webdriver is not initialized");
            throw new IllegalArgumentException("Webdriver is not initialized");
        }
        return driver.get();
    }

    //  getter method for ActionDriver
    public static Actiondriver getActionDriver() {
        if (actiondriver.get() == null) {
            System.out.println("Webdriver is not initialized");
            throw new IllegalArgumentException("Webdriver is not initialized");
        }
        return actiondriver.get();
    }

    // Driver setter method
    public void setDriver(ThreadLocal<WebDriver> driver) {
        this.driver = driver;
    }

    // Getter method for prop
    public static Properties getProp() {
        return prop;
    }

    @AfterMethod
    public void teadDown() {
        if (driver != null) {
            try {
                getDriver().quit();
            } catch (Exception e) {
                System.out.println("unable to quit the url :" + e.getMessage());
            }
        }
        logger.info("Webdriver instance is closed.");
        driver.remove();
        actiondriver.remove();
        // ExtentManager.endTest();
//        driver = null;
//        actiondriver = null;

    }
    // This has been implemented in TestListners class
//    @AfterSuite
////    public void closeReport() {
////        ExtentManager.flushReport();
////    }


}
