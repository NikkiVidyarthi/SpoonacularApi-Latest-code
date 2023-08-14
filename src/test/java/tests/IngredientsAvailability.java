package tests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class IngredientsAvailability extends TestBase {

    @Test
    public void verifyIngredientsAvailability() throws IOException {
        Properties properties = loadProperties("config.properties");
        String apiKey = properties.getProperty("api.key");
        String apiUrl = properties.getProperty("api.url");
        String browserPath = properties.getProperty("Browser");

// Set the Chrome driver path
        System.setProperty("webdriver.chrome.driver", browserPath);

// Load ingredient availability data
        List<HashMap<String, Object>> ingredientAvailabilityData = loadIngredientAvailabilityData();

// Load recipes data
        List<HashMap<String, Object>> recipes = loadRecipes();

        for (HashMap<String, Object> recipe : recipes) {
            if (checkIngredientsAvailability(recipe, ingredientAvailabilityData)) {
                System.out.println("Ingredients available for recipe: " + recipe.get("Title"));
            } else {
                System.out.println("Some ingredients missing for recipe: " + recipe.get("Title"));
// Implement logic to suggest alternatives or go shopping within budget
                suggestAlternativeOrShopping(recipe, apiKey, apiUrl);
            }
        }
    }

    public boolean checkIngredientsAvailability(
            HashMap<String, Object> recipe,
            List<HashMap<String, Object>> ingredientAvailabilityData) {
        List<String> recipeIngredientIDs = (List<String>) recipe.get("IngredientIDs");

        for (String ingredientID : recipeIngredientIDs) {
            boolean ingredientAvailable = false;
            for (HashMap<String, Object> ingredientData : ingredientAvailabilityData) {
                if (ingredientData.get("ID").equals(ingredientID)) {
                    int availableGrams = (int) ingredientData.get("GRAMS");
                    if (availableGrams > 0) {
                        ingredientAvailable = true;
                        break;
                    }
                }
            }
            if (!ingredientAvailable) {
                return false;
            }
        }
        return true;
    }

    public void suggestAlternativeOrShopping(HashMap<String, Object> recipe, String apiKey, String apiUrl) {
// Implement logic to suggest alternative dishes or decide whether to go shopping
// based on available budget and alternative options
// This logic will vary based on your requirements
        System.out.println("Suggesting alternatives or shopping for recipe: " + recipe.get("Title"));
// Example: Send a POST request to suggest alternatives or perform shopping
        String requestObject =  GsontoJSON.convettoJSN(recipe);
        Response response = postRequest(apiUrl, requestObject, apiKey);
        System.out.println("Response Body: " + response.getBody().asString());
        assertEquals(response.statusCode(), 200, "POST request should succeed.");
    }

    private List<HashMap<String, Object>> loadIngredientAvailabilityData() {
        List<HashMap<String, Object>> ingredientAvailabilityData = new ArrayList<>();

        HashMap<String, Object> ingredient1 = new HashMap<>();
        ingredient1.put("ID", "11135");
        ingredient1.put("GRAMS", 150);
        ingredientAvailabilityData.add(ingredient1);

        HashMap<String, Object> ingredient2 = new HashMap<>();
        ingredient2.put("ID", "20040");
        ingredient2.put("GRAMS", 100);
        ingredientAvailabilityData.add(ingredient2);

// Add more ingredients as needed

        return ingredientAvailabilityData;
    }
    public List<HashMap<String, Object>> loadRecipes() {
        List<HashMap<String, Object>> Recipes = new ArrayList<>();
        HashMap<String, Object> recipe1 = new HashMap<>();
        recipe1.put("ID", "428278");
        recipe1.put("Title", "Chicken");
        recipe1.put("IngredientIDs", Arrays.asList("11135", "20040", "716426"));
        Recipes.add(recipe1);

        HashMap<String, Object> recipe2 = new HashMap<>();
        recipe2.put("ID", "20421");
        recipe2.put("Title", "cooked pasta");
        recipe2.put("IngredientIDs", Arrays.asList("10016069", "5114", "11564"));
        Recipes.add(recipe2);

// Add more recipes as needed

        return Recipes;

    }

    private Properties loadProperties(String fileName) throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IOException("The " + fileName + " file does not exist in the specified location.");
            }
            properties.load(inputStream);
        }
        return properties;
    }

    private Response postRequest(String url, String requestBody, String apiKey) {
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
}

