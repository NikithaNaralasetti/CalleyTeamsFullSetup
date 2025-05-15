package pompages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.calleyteams.BaseClass;

import java.time.Duration;

public class LoginPage extends BaseClass {

    private static final String LOGIN_URL = "https://app.getcalley.com/Login.aspx";
    private WebDriverWait wait;
    private JavascriptExecutor jsExecutor;
    private Actions actions;

    @FindBy(id = "txtEmailId")
    private WebElement emailInput;

    @FindBy(id = "txtPassword")
    private WebElement passwordInput;

    @FindBy(xpath = "//input[@type='submit' and @value='Log in']")
    private WebElement loginButton;

    @FindBy(xpath = "//span[contains(text(),'Dashboard')]")
    private WebElement dashboardHeader;

    public LoginPage() {
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        jsExecutor = (JavascriptExecutor) driver;
        actions = new Actions(driver);
    }

    public void navigateToLoginPage() {
        driver.get(LOGIN_URL);
        System.out.println("Navigated to Login Page");
    }

    public void enterEmail(String email) {
        try {
            wait.until(ExpectedConditions.visibilityOf(emailInput));
            emailInput.clear();
            emailInput.sendKeys(email);
            System.out.println("Entered email: " + email);
        } catch (Exception e) {
            System.out.println("Error entering email: " + e.getMessage());
        }
    }

    public void enterPassword(String password) {
        try {
            wait.until(ExpectedConditions.visibilityOf(passwordInput));
            passwordInput.clear();
            passwordInput.sendKeys(password);
            System.out.println("Entered password");
        } catch (Exception e) {
            System.out.println("Error entering password: " + e.getMessage());
        }
    }

    public void clickLogin() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(loginButton));
            jsExecutor.executeScript("arguments[0].scrollIntoView(true);", loginButton);

            try {
                loginButton.click();
                System.out.println("Clicked login using standard click.");
            } catch (Exception e1) {
                System.out.println("Standard click failed. Trying Actions class. " + e1.getMessage());
                try {
                    actions.moveToElement(loginButton).click().perform();
                    System.out.println("Clicked login using Actions class.");
                } catch (Exception e2) {
                    System.out.println("Actions failed. Trying JS click. " + e2.getMessage());
                    jsExecutor.executeScript("arguments[0].click();", loginButton);
                    System.out.println("Clicked login using JS click.");
                }
            }

        } catch (Exception e) {
            System.out.println("Login button click failed: " + e.getMessage());
        }
    }

    public boolean isLoginSuccessful() {
        try {
            wait.until(ExpectedConditions.visibilityOf(dashboardHeader));
            boolean isDisplayed = dashboardHeader.isDisplayed();
            System.out.println("Login success: " + isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            System.out.println("Login failed or dashboard not visible: " + e.getMessage());
            return false;
        }
    }

    public void loginUser(String email, String password) {
        navigateToLoginPage();
        enterEmail(email);
        enterPassword(password);
        clickLogin();
    }
}
