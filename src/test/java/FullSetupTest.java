import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pompages.*;
import com.calleyteams.BaseClass;

public class FullSetupTest extends BaseClass {

    private RegistrationPage registrationPage;
    private LoginPage loginPage;
    private AgentPage agentPage;
    private CSVUploadPage csvUploadPage;
    private DashboardPage dashboardPage;

    @BeforeMethod
    public void setUp() {
        initialization();
        registrationPage = new RegistrationPage();
        loginPage = new LoginPage();
        agentPage = new AgentPage(driver);
        csvUploadPage = new CSVUploadPage(driver);
        dashboardPage = new DashboardPage();
    }

    @Test
    public void testFullCalleyTeamsSetup() throws InterruptedException {
        String fullName = getSafeProperty("registration.txtName", "Test User");
        String email = getSafeProperty("registration.email", "test" + System.currentTimeMillis() + "@example.com");
        String phone = getSafeProperty("registration.phone", "9876543210");
        String password = getSafeProperty("registration.password", "Test@123");

        System.out.println("Registering user...");
        registrationPage.registerUser(fullName, email, phone, password);
        Assert.assertTrue(registrationPage.isRegistrationSuccessful(), "User registration failed!");

        System.out.println("Logging in user...");
        loginPage.loginUser(email, password);
        Assert.assertTrue(loginPage.isLoginSuccessful(), "User login failed!");

        String agentName = getSafeProperty("agent.name", "Agent A");
        String agentEmail = getSafeProperty("agent.email", "agent" + System.currentTimeMillis() + "@example.com");
        String agentPhone = getSafeProperty("agent.phone", "9876501234");
        String agentPassword = getSafeProperty("agent.password", "Agent@123");

        System.out.println("Adding agent...");
        agentPage.addAgent(agentName, agentEmail, agentPhone, agentPassword);

        String listName = getSafeProperty("csv.listName", "Lead List " + System.currentTimeMillis());
        String agentForUpload = getSafeProperty("csv.agent", agentName); // Must match dropdown label in UI
        String filePath = getSafeProperty("csv.filePath", "D:/sample.csv");

        System.out.println("Uploading CSV...");
        csvUploadPage.uploadCSV(listName, agentForUpload, filePath);
        Assert.assertTrue(csvUploadPage.isCSVImportedSuccessfully(), "CSV upload failed!");
    }

    @AfterMethod
    public void tearDownTest() {
        tearDown();
    }

    private String getSafeProperty(String key, String defaultValue) {
        String value = properties.getProperty(key);
        return (value != null && !value.trim().isEmpty()) ? value : defaultValue;
    }
}
