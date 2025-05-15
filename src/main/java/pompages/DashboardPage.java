package pompages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.calleyteams.BaseClass;

public class DashboardPage extends BaseClass {

    // Page Elements with actual locators
    @FindBy(xpath = "//span[contains(text(), 'Dashboard')]")
    private WebElement dashboardTitle;

    @FindBy(xpath = "//a[contains(text(), 'Logout') or contains(@href, 'Logout')]")
    private WebElement logoutLink;

    @FindBy(xpath = "//a[contains(text(), 'Agents') or contains(@href, 'Agents')]")
    private WebElement agentsLink;

    @FindBy(xpath = "//a[contains(text(), 'Call List') or contains(@href, 'CallList')]")
    private WebElement callListLink;

    // Constructor
    public DashboardPage() {
        PageFactory.initElements(driver, this);
    }

    // Page Methods
    public boolean isDashboardLoaded() {
        try {
            waitForElementToBeVisible(dashboardTitle);
            boolean isLoaded = dashboardTitle.isDisplayed();
            System.out.println("Dashboard loaded status: " + isLoaded);
            return isLoaded;
        } catch (Exception e) {
            System.out.println("Error checking if dashboard is loaded: " + e.getMessage());
            return false;
        }
    }

    public void navigateToAgents() {
        try {
            waitForElementToBeClickable(agentsLink);
            // Sometimes normal click doesn't work, so we use JS click as a backup
            try {
                agentsLink.click();
            } catch (Exception e) {
                clickUsingJS(agentsLink);
            }
            System.out.println("Navigated to Agents page");
        } catch (Exception e) {
            System.out.println("Error navigating to Agents page: " + e.getMessage());
        }
    }

    public void navigateToCallList() {
        try {
            waitForElementToBeClickable(callListLink);
            // Sometimes normal click doesn't work, so we use JS click as a backup
            try {
                callListLink.click();
            } catch (Exception e) {
                clickUsingJS(callListLink);
            }
            System.out.println("Navigated to Call List page");
        } catch (Exception e) {
            System.out.println("Error navigating to Call List page: " + e.getMessage());
        }
    }

    public void logout() {
        try {
            waitForElementToBeClickable(logoutLink);
            // Sometimes normal click doesn't work, so we use JS click as a backup
            try {
                logoutLink.click();
            } catch (Exception e) {
                clickUsingJS(logoutLink);
            }
            System.out.println("Logged out successfully");
        } catch (Exception e) {
            System.out.println("Error logging out: " + e.getMessage());
        }
    }
}