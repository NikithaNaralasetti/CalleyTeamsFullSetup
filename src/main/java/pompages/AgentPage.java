package pompages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class AgentPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    // Constructor
    public AgentPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.js = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    // Elements
    @FindBy(xpath = "//span[normalize-space()='Team']")
    private WebElement teamMenuLink;

    @FindBy(xpath = "//span[normalize-space()='Agents']")
    private WebElement agentsMenuLink;

    @FindBy(name = "ctl00$ContentPlaceHolder1$txt_name")
    private WebElement agentNameInput;

    @FindBy(name = "ctl00$ContentPlaceHolder1$txt_email")
    private WebElement agentEmailInput;

    @FindBy(name = "ctl00$ContentPlaceHolder1$txt_mobile")
    private WebElement agentPhoneInput;

    @FindBy(name = "ctl00$ContentPlaceHolder1$txt_pass")
    private WebElement agentPasswordInput;

    @FindBy(id = "ContentPlaceHolder1_btn_submit") // Fixed the incorrect locator
    private WebElement saveAgentButton;


    // Utility methods
    private void waitForElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    private void waitForElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    private void clickUsingJS(WebElement element) {
        js.executeScript("arguments[0].click();", element);
    }

    // Navigation
    public void navigateToAgentPage() {
        try {
            Actions actions = new Actions(driver);

            waitForElementToBeVisible(teamMenuLink);
            actions.moveToElement(teamMenuLink).perform(); // Hover to reveal submenu

            waitForElementToBeClickable(agentsMenuLink);
            agentsMenuLink.click(); // Click 'Agents'

            System.out.println("Navigated to Agent Page");
        } catch (Exception e) {
            System.err.println("Error navigating to Agent page: " + e.getMessage());
        }
    }

    // Agent Entry Methods
    public void enterAgentName(String name) {
        try {
            waitForElementToBeVisible(agentNameInput);
            agentNameInput.clear();
            agentNameInput.sendKeys(name);
            System.out.println("Entered agent name: " + name);
        } catch (Exception e) {
            System.err.println("Error entering agent name: " + e.getMessage());
        }
    }

    public void enterAgentEmail(String email) {
        try {
            waitForElementToBeVisible(agentEmailInput);
            agentEmailInput.clear();
            agentEmailInput.sendKeys(email);
            System.out.println("Entered agent email: " + email);
        } catch (Exception e) {
            System.err.println("Error entering agent email: " + e.getMessage());
        }
    }

    public void enterAgentPhone(String phone) {
        try {
            waitForElementToBeVisible(agentPhoneInput);
            agentPhoneInput.clear();
            agentPhoneInput.sendKeys(phone);
            System.out.println("Entered agent phone: " + phone);
        } catch (Exception e) {
            System.err.println("Error entering agent phone: " + e.getMessage());
        }
    }

    public void enterAgentPassword(String password) {
        try {
            waitForElementToBeVisible(agentPasswordInput);
            agentPasswordInput.clear();
            agentPasswordInput.sendKeys(password);
            System.out.println("Entered agent password");
        } catch (Exception e) {
            System.err.println("Error entering agent password: " + e.getMessage());
        }
    }

    public void clickSaveAgent() {
        try {
            waitForElementToBeClickable(saveAgentButton);
            saveAgentButton.click();
            System.out.println("Clicked Save Agent");
        } catch (Exception e) {
            System.err.println("Error clicking Save Agent button: " + e.getMessage());
        }
    }



    public void addAgent(String name, String email, String phone, String password) {
        navigateToAgentPage();
        enterAgentName(name);
        enterAgentEmail(email);
        enterAgentPhone(phone);
        enterAgentPassword(password);
        clickSaveAgent();
    }
}
