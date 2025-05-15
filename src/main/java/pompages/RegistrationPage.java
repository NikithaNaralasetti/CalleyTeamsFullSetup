package pompages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.calleyteams.BaseClass;

public class RegistrationPage extends BaseClass {

    private static final String REGISTRATION_URL = "https://app.getcalley.com/registration.aspx";

    @FindBy(xpath = "//input[@id='txtName']")
    private WebElement fullNameInput;

    @FindBy(xpath = "//input[@id='txtEmail']")
    private WebElement emailInput;

    @FindBy(xpath = "//input[@id='txt_mobile']")
    private WebElement phoneInput;

    @FindBy(xpath = "//input[@id='txtPassword']")
    private WebElement passwordInput;

    @FindBy(xpath = "//input[@id='checkbox-signup']")
    private WebElement termsCheckbox;

    @FindBy(xpath = "//input[@id='btnSignUp']")
    private WebElement registerButton;

    // Constructor
    public RegistrationPage() {
        PageFactory.initElements(driver, this);
    }

    public void navigateToRegistrationPage() {
        driver.get(REGISTRATION_URL);
        System.out.println("Navigated to Registration Page");
    }

    public void enterFullName(String fullName) {
        waitForElementToBeVisible(fullNameInput);
        fullNameInput.clear();
        fullNameInput.sendKeys(fullName);
        System.out.println("Entered full name");
    }

    public void enterEmail(String email) {
        waitForElementToBeVisible(emailInput);
        emailInput.clear();
        emailInput.sendKeys(email);
        System.out.println("Entered email");
    }

    public void enterPhone(String phone) {
        waitForElementToBeVisible(phoneInput);
        phoneInput.clear();
        phoneInput.sendKeys(phone);
        System.out.println("Entered phone");
    }

    public void enterPassword(String password) {
        waitForElementToBeVisible(passwordInput);
        passwordInput.clear();
        passwordInput.sendKeys(password);
        System.out.println("Entered password");
    }

    public void acceptTermsAndConditions() {
        try {
            waitForElementToBeClickable(termsCheckbox);
            if (!termsCheckbox.isSelected()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", termsCheckbox);
            }
            System.out.println("Accepted terms");
        } catch (Exception e) {
            System.out.println("Error accepting terms: " + e.getMessage());
        }
    }

    public void solveCaptchaManually() {
        try {
            System.out.println("Please solve the CAPTCHA manually within 20 seconds...");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("CAPTCHA wait interrupted");
        }
    }

    public void clickRegister() {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", registerButton);
            waitForElementToBeClickable(registerButton);
            try {
                registerButton.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", registerButton);
            }
            System.out.println("Clicked register");
        } catch (Exception e) {
            System.out.println("Error clicking register: " + e.getMessage());
        }
    }

    public boolean isRegistrationSuccessful() {
        try {
            Thread.sleep(5000);
            String pageSource = driver.getPageSource().toLowerCase();
            String currentUrl = driver.getCurrentUrl();

            System.out.println("Current URL: " + currentUrl);
            return pageSource.contains("thank you") ||
                    pageSource.contains("success") ||
                    currentUrl.contains("success");
        } catch (Exception e) {
            System.out.println("Error checking registration result: " + e.getMessage());
            return false;
        }
    }

    public void registerUser(String fullName, String email, String phone, String password) {
        navigateToRegistrationPage();
        enterFullName(fullName);
        enterEmail(email);
        enterPhone(phone);
        enterPassword(password);
        acceptTermsAndConditions();
        solveCaptchaManually();
        clickRegister();
    }
}
