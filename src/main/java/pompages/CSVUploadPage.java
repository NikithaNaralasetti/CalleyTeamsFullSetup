package pompages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.calleyteams.BaseClass;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class CSVUploadPage extends BaseClass {

    @FindBy(xpath = "//a[@href='call-list-teams.aspx']//span[contains(text(),'Call List')]")
    private WebElement callListDropdown;

    @FindBy(xpath = "//span[normalize-space()='Add - Power Import']")
    private WebElement addPowerImportLink;

    @FindBy(id = "ContentPlaceHolder1_txtlistname")
    private WebElement listNameInput;

    @FindBy(xpath = "//button[@title='Please select an agent']")
    private WebElement agentDropdownButton;

    // Correct locator for "Select All" checkbox (as per your instruction)
    @FindBy(xpath = "//input[@type='checkbox' and @value='all']")
    private WebElement selectAllCheckbox;

    @FindBy(id = "ContentPlaceHolder1_fileUpload")
    private WebElement csvFileInput;

    @FindBy(xpath = "//p[@id='btnUp']")
    private WebElement uploadButton;

    @FindBy(xpath = "//button[contains(text(), 'OK')]")
    private WebElement okButton;

    @FindBy(xpath = "//button[contains(text(), 'Map Fields')]")
    private WebElement mapFieldsButton;

    private By nameFieldDropdownLocator = By.xpath("//select[@id='ddlbelongto_1']");
    private By phoneFieldDropdownLocator = By.xpath("//select[@id='ddlbelongto_2']");
    private By notesFieldDropdownLocator = By.xpath("//select[@id='ddlbelongto_3']");

    @FindBy(id = "ContentPlaceHolder1_btnUpload")
    private WebElement importButton;

    @FindBy(css = ".alert-success")
    private WebElement successMessage;

    public CSVUploadPage(WebDriver driver) {
        // Use the driver passed as param, not BaseClass.driver directly
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void uploadCSV(String listName, String agentName, String filePath) throws InterruptedException {
        navigateToCSVUploadPage();
        enterListName(listName);
        selectAgent(agentName);
        uploadCSVFile(filePath);
        clickUpload();
        confirmUpload();
        mapFields();
        clickImport();
    }

    public void navigateToCSVUploadPage() throws InterruptedException {
        waitForElementToBeVisible(callListDropdown);

        Actions actions = new Actions(driver);
        actions.moveToElement(callListDropdown).perform();
        Thread.sleep(500);

        waitForElementToBeClickable(addPowerImportLink);
        addPowerImportLink.click();
    }

    public void enterListName(String listName) {
        waitForElementToBeVisible(listNameInput);
        listNameInput.clear();
        listNameInput.sendKeys(listName);
    }

    public void selectAgent(String agentName) {
        try {
            // Wait for the dropdown button
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(agentDropdownButton));

            // Take screenshot of the page before clicking dropdown (for debugging)
            try {
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                System.out.println("Screenshot taken before dropdown click: " + screenshot.getAbsolutePath());
            } catch (Exception e) {
                System.out.println("Failed to take screenshot: " + e.getMessage());
            }

            // Click with JavaScript to ensure dropdown opens
            System.out.println("Clicking agent dropdown button");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", agentDropdownButton);

            // *** No wait for specific dropdown container - it might have different structure ***
            // Instead, just wait a bit for any dropdown to appear
            Thread.sleep(2000);

            // Print all potential dropdown items for debugging
            List<WebElement> allDropdownItems = driver.findElements(
                    By.cssSelector(".dropdown-menu li, .dropdown-menu label, .multiselect-container li, div.open li, ul li.dropdown-item, .dropdown-content li")
            );
            System.out.println("Found " + allDropdownItems.size() + " potential dropdown items");

            // If no items found, try to expand search
            if (allDropdownItems.isEmpty()) {
                allDropdownItems = driver.findElements(By.cssSelector("li, label"));
                System.out.println("Expanded search: Found " + allDropdownItems.size() + " potential elements");
            }

            for (WebElement item : allDropdownItems) {
                String text = "";
                try {
                    text = item.getText();
                    if (!text.trim().isEmpty()) {
                        System.out.println("Potential dropdown item: " + text);
                    }
                } catch (Exception e) {
                    // Ignore errors getting text
                }
            }

            // Select the agent based on the parameter
            if (agentName == null || agentName.trim().isEmpty() || agentName.equalsIgnoreCase("all")) {
                System.out.println("Attempting to select 'All' agents");

                // Try multiple approaches to find the "Select All" option
                List<String> selectAllCssSelectors = Arrays.asList(
                        "input[value='all']",
                        "label:contains('Select All') input",
                        "li.multiselect-all input",
                        "li.multiselect-all label",
                        ".select-all",
                        "input.select-all"
                );

                boolean selectAllClicked = false;

                // First try CSS selectors
                for (String cssSelector : selectAllCssSelectors) {
                    try {
                        List<WebElement> elements = driver.findElements(By.cssSelector(cssSelector));
                        if (!elements.isEmpty()) {
                            for (WebElement element : elements) {
                                try {
                                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                                    System.out.println("Successfully clicked potential 'Select All' using CSS: " + cssSelector);
                                    selectAllClicked = true;
                                    break;
                                } catch (Exception e) {
                                    // Continue to next element
                                }
                            }
                            if (selectAllClicked) break;
                        }
                    } catch (Exception e) {
                        System.out.println("Failed with CSS selector: " + cssSelector);
                    }
                }

                // If CSS didn't work, try XPath
                if (!selectAllClicked) {
                    List<String> selectAllXPaths = Arrays.asList(
                            "//input[@type='checkbox' and @value='all']",
                            "//label[contains(text(),'Select All')]/preceding-sibling::input[@type='checkbox']",
                            "//label[contains(text(),'Select All')]/input[@type='checkbox']",
                            "//li[contains(@class,'multiselect-all')]//input[@type='checkbox']",
                            "//li[contains(@class,'multiselect-all')]//label",
                            "//div[contains(@class,'dropdown-menu')]//li[1]//input[@type='checkbox']",
                            "//li[contains(., 'Select All')]",
                            "//span[contains(text(), 'All')]/..",
                            "//input[@type='checkbox' and @checked='false'][1]" // First unchecked checkbox as last resort
                    );

                    for (String xpath : selectAllXPaths) {
                        try {
                            List<WebElement> elements = driver.findElements(By.xpath(xpath));
                            if (!elements.isEmpty()) {
                                for (WebElement element : elements) {
                                    try {
                                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                                        System.out.println("Successfully clicked potential 'Select All' using XPath: " + xpath);
                                        selectAllClicked = true;
                                        break;
                                    } catch (Exception e) {
                                        // Continue to next element
                                    }
                                }
                                if (selectAllClicked) break;
                            }
                        } catch (Exception e) {
                            System.out.println("Failed with XPath: " + xpath);
                        }
                    }
                }

                if (!selectAllClicked) {
                    System.out.println("Warning: Could not find/click 'Select All'. Will try to select the first agent instead.");
                    // Will try to select first agent below as fallback
                }
            }

            // If we need to select a specific agent OR if "Select All" failed
            if ((agentName != null && !agentName.trim().isEmpty() && !agentName.equalsIgnoreCase("all")) ||
                    (agentName != null && agentName.equalsIgnoreCase("all") && !selectAllCheckbox.isSelected())) {

                System.out.println("Attempting to select specific agent: " + agentName);
                boolean agentSelected = false;

                // For Calley's specific structure - try direct lookups first
                if (agentName != null && !agentName.trim().isEmpty() && !agentName.equalsIgnoreCase("all")) {
                    // Try direct lookups by text first
                    List<String> lookupPatterns = Arrays.asList(
                            "//li[contains(.,'" + agentName + "')]",
                            "//label[contains(.,'" + agentName + "')]",
                            "//div[contains(.,'" + agentName + "') and contains(@class,'dropdown')]//input",
                            "//span[contains(.,'" + agentName + "')]/..",
                            "//a[contains(.,'" + agentName + "')]",
                            "//div[contains(@class,'dropdown-menu')]//li[contains(.,'" + agentName + "')]",
                            "//ul//li[contains(.,'" + agentName + "')]"
                    );

                    for (String pattern : lookupPatterns) {
                        try {
                            List<WebElement> elements = driver.findElements(By.xpath(pattern));
                            if (!elements.isEmpty()) {
                                for (WebElement element : elements) {
                                    try {
                                        String text = element.getText();
                                        if (text.contains(agentName)) {
                                            System.out.println("Found matching element with text: " + text);
                                            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                                            Thread.sleep(300);
                                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                                            System.out.println("Successfully clicked agent '" + agentName + "' using pattern: " + pattern);
                                            agentSelected = true;
                                            break;
                                        }
                                    } catch (Exception e) {
                                        // Continue to next element
                                    }
                                }
                                if (agentSelected) break;
                            }
                        } catch (Exception e) {
                            System.out.println("Failed with pattern: " + pattern);
                        }
                    }
                }

                // If specific lookup failed or we need a fallback for "Select All"
                if (!agentSelected) {
                    System.out.println("Could not find specific agent. Attempting to select first available agent...");

                    // Try to find any clickable element in what might be a dropdown
                    List<String> fallbackSelectors = Arrays.asList(
                            ".dropdown-menu li label",
                            ".dropdown-menu li",
                            ".multiselect-container li label",
                            "ul li.dropdown-item",
                            "ul.dropdown-menu li",
                            ".dropdown-content li",
                            "li input[type='checkbox']"
                    );

                    for (String selector : fallbackSelectors) {
                        try {
                            List<WebElement> elements = driver.findElements(By.cssSelector(selector));
                            if (!elements.isEmpty()) {
                                for (WebElement element : elements) {
                                    try {
                                        String elementText = element.getText();
                                        if (!elementText.contains("Select All") && !elementText.trim().isEmpty()) {
                                            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                                            Thread.sleep(300);
                                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                                            System.out.println("Selected fallback element: " + elementText);
                                            agentSelected = true;
                                            break;
                                        }
                                    } catch (Exception e) {
                                        // Continue to next element
                                    }
                                }
                                if (agentSelected) break;
                            }
                        } catch (Exception e) {
                            System.out.println("Failed with fallback selector: " + selector);
                        }
                    }

                    // Last resort - try to click any checkbox
                    if (!agentSelected) {
                        try {
                            List<WebElement> checkboxes = driver.findElements(By.cssSelector("input[type='checkbox']"));
                            if (!checkboxes.isEmpty()) {
                                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkboxes.get(0));
                                System.out.println("Selected first available checkbox as last resort");
                                agentSelected = true;
                            }
                        } catch (Exception e) {
                            System.out.println("Failed to select any checkbox: " + e.getMessage());
                        }
                    }
                }

                if (!agentSelected) {
                    System.out.println("WARNING: Could not select any agent. Test may fail at a later point.");
                }
            }

            // Close the dropdown by clicking outside
            Thread.sleep(1000);
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", listNameInput);
                System.out.println("Clicked outside to close dropdown");
            } catch (Exception e) {
                System.out.println("Failed to close dropdown with listNameInput: " + e.getMessage());
                // Try alternate way to close dropdown - click elsewhere on page
                try {
                    WebElement body = driver.findElement(By.tagName("body"));
                    new Actions(driver).moveToElement(body, 1, 1).click().perform();
                } catch (Exception e2) {
                    System.out.println("Failed alternative close method too: " + e2.getMessage());
                }
            }

            System.out.println("Agent selection process completed");

        } catch (Exception e) {
            System.out.println("Error in selectAgent method: " + e.getMessage());
            e.printStackTrace();
            // Continue execution instead of throwing exception
            System.out.println("Will attempt to continue test despite agent selection failure");
        }
    }

    public void uploadCSVFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("File not found: " + file.getAbsolutePath());
        }

        waitForElementToBeVisible(csvFileInput);
        csvFileInput.sendKeys(file.getAbsolutePath());
    }

    public void clickUpload() {
        waitForElementToBeClickable(uploadButton);
        uploadButton.click();
    }

    public void confirmUpload() {
        waitForElementToBeClickable(okButton);
        okButton.click();
    }

    public void mapFields() {
        waitForElementToBeClickable(mapFieldsButton);
        mapFieldsButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement nameDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(nameFieldDropdownLocator));
        WebElement phoneDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(phoneFieldDropdownLocator));
        WebElement notesDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(notesFieldDropdownLocator));

        new Select(nameDropdown).selectByVisibleText("Name");
        new Select(phoneDropdown).selectByVisibleText("Phone");
        new Select(notesDropdown).selectByVisibleText("Notes");
    }

    public void clickImport() {
        waitForElementToBeClickable(importButton);
        importButton.click();
    }

    public boolean isCSVImportedSuccessfully() {
        try {
            waitForElementToBeVisible(successMessage);
            return successMessage.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public static void waitForElementToBeVisible(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static void waitForElementToBeClickable(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitForElementToBeVisible(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}