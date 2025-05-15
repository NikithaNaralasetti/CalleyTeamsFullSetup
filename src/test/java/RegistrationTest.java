import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pompages.RegistrationPage;
import com.calleyteams.BaseClass;

public class RegistrationTest extends BaseClass {

    private RegistrationPage registrationPage;

    @BeforeMethod
    public void setUp() {
        initialization();
        registrationPage = new RegistrationPage();
    }

    @Test
    public void testUserRegistration() {
        String fullName = "Nikitha";
        String email = "nikithanarala" + System.currentTimeMillis() + "@example.com"; // unique email
        String phone = "9876543210";
        String password = "Test@123";

        registrationPage.registerUser(fullName, email, phone, password);

        Assert.assertTrue(registrationPage.isRegistrationSuccessful(), "User registration failed!");
    }

    @AfterMethod
    public static void tearDown() {
        BaseClass.tearDown();
    }
}
