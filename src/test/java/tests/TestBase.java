package tests;
import org.testng.annotations.BeforeSuite;
import static io.restassured.RestAssured.baseURI;
public class TestBase {
    @BeforeSuite
    public static void setup() { baseURI ="https://api.spoonacular.com/users/connect";}
}
