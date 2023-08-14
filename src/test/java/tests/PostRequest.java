package tests;


import datamodel.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;


public class PostRequest  extends TestBase {
     public Response response;

    @Test
    public void newLogin() throws IOException {
        // Load the properties from config.properties file
        Properties properties = loadProperties("config.properties");
        String apiKey = properties.getProperty("api.key");

        User user1 = new User();
        user1.setUsername("api_123_user");
        user1.setFirstName("yash");
        user1.setLastName("patel");
        user1.setEmail("yash@gmail.com");


// Convert the LoginUser object to JSON string
        String requestObject = GsontoJSON.convettoJSN(user1);
        response = performPostRequest("https://api.spoonacular.com/users/connect", requestObject, apiKey);
        System.out.println("json-body post request is " + requestObject);

// Assert the response status code is 200
        assertEquals(response.statusCode(), 200);

// Log the response body
        response.then().log().body();
    }

    public Response performPostRequest(String url, String requestBody, String apiKey) {
        Response response = given()
                .queryParam("apiKey", apiKey)
                .contentType(ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post(url)
                .then()
                .extract().response();

        return response;
    }

    public Properties loadProperties(String fileName) throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IOException("Could not find the properties file: " + fileName);
            }
            properties.load(inputStream);
        }
        return properties;
    }
}


