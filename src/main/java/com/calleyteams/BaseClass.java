package com.calleyteams;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass {

    protected static WebDriver driver;
    protected static WebDriverWait wait;
    protected static Properties properties;

    /**
     * Initializes the WebDriver, WebDriverWait, and loads properties from file.
     */
    public static void initialization() {
        try {
            loadProperties();

            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            // Implicit wait is generally not recommended with explicit waits but kept if needed.
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        } catch (Exception e) {
            System.err.println("Error initializing WebDriver: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads the test configuration from a properties file.
     */
    private static void loadProperties() {
        try (FileInputStream input = new FileInputStream("src/main/resources/data.properties")) {
            properties = new Properties();
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Error loading properties file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Waits until the element is clickable.
     * @param element WebElement to wait for
     */
    protected static void waitForElementToBeClickable(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            System.err.println("Element not clickable: " + e.getMessage());
        }
    }

    /**
     * Waits until the element is visible.
     * @param element WebElement to wait for
     */
    protected static void waitForElementToBeVisible(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            System.err.println("Element not visible: " + e.getMessage());
        }
    }

    /**
     * Scrolls the page to bring the element into view.
     * @param element WebElement to scroll to
     */
    protected static void scrollToElement(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        } catch (Exception e) {
            System.err.println("Error scrolling to element: " + e.getMessage());
        }
    }

    /**
     * Performs click using JavaScript when normal click fails.
     * @param element WebElement to click
     */
    protected static void clickUsingJS(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            System.err.println("Error clicking element with JS: " + e.getMessage());
        }
    }

    /**
     * Quits the WebDriver session gracefully.
     */
    public static void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.err.println("Error during driver quit: " + e.getMessage());
            }
        }
    }
}
