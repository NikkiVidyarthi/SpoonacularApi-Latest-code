package tests;

import groovy.xml.XmlParser;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;


public class MealPostRequest extends TestBase {


 public String excelFilePath;
 public Object driver;
 public String[] nutrientValues;
 public XmlParser properties;

 @Test
 public void mealPlan() throws IOException {
  Properties properties = loadProperties("config.properties");
  String apiKey = properties.getProperty("api.key");
  String apiUrl = properties.getProperty("api.url");
  String browserPath = properties.getProperty("Browser");

// Set the Chrome driver path
  System.setProperty("webdriver.chrome.driver", browserPath);

  excelFilePath = properties.getProperty("AbsolutePath") + "/ReadFromExcel.xlsx";
  FileInputStream inputStream = new FileInputStream(excelFilePath);

  Workbook workbook = new XSSFWorkbook(inputStream);
  Sheet firstSheet = workbook.getSheetAt(0);
  Iterator<Row> rowIterator = firstSheet.iterator();





  while (rowIterator.hasNext()) {
   Row row = rowIterator.next();
   Iterator<Cell> cellIterator = row.cellIterator();

   while (cellIterator.hasNext()) {
    Cell cell = cellIterator.next();
    System.out.print(cell.toString() + " - ");
   }
   System.out.println();
  }

  workbook.close();
  inputStream.close();
 }



 @Test
 public void testNutritionalValueCalculation() throws InterruptedException, FileNotFoundException, SAXNotSupportedException, SAXNotRecognizedException {
  System.setProperty("webdriver.chrome.driver", "browserPath");
  excelFilePath = properties.getProperty("AbsolutePath") + "/ReadFromExcel.xlsx";
  FileInputStream inputStream = new FileInputStream(excelFilePath);



  String script = "document.getElementById('generateButton').click();";
  driver.toString();

// Rest of your code

  double totalProtein = calculateTotalNutrition("protein");
  double totalCarbohydrates = calculateTotalNutrition("carbohydrates");
  double totalFat = calculateTotalNutrition("fat");

  // Define the daily nutritional limits
  double maxDailyProtein = 100.0;
  double maxDailyCarbohydrates = 150.0;
  double maxDailyFat = 50.0;

  driver.wait();

  // Assert that calculated nutritional values do not exceed limits
  Assert.assertTrue(totalProtein <= maxDailyProtein, "Total protein exceeds limit");
  Assert.assertTrue(totalCarbohydrates <= maxDailyCarbohydrates, "Total carbohydrates exceed limit");
  Assert.assertTrue(totalFat <= maxDailyFat, "Total fat exceeds limit");
 }



 // Utility method to calculate total nutritional values
 public double calculateTotalNutrition(String nutrient) {
  double totalNutrition = 0.0;

  // Execute  to get the nutrient values
  String script = "return Array.from(document.querySelectorAll('.meal-container ."+nutrient+"')).map(e => e.textContent);";

  for (String nutrientValueText : nutrientValues) {
   double nutrientValue = parseNutritionValue(nutrientValueText);
   totalNutrition += nutrientValue;
  }

  return totalNutrition;
 }
 public List<HashMap<String, Object>> calculateNutrition(List<HashMap<String, Object>> mealItems) {
  for (HashMap<String, Object> mealItem : mealItems) {
   double protein = parseNutritionValue(mealItem.getOrDefault("Protein", "").toString());
   double carbohydrates = parseNutritionValue(mealItem.getOrDefault("Carbohydrates", "").toString());
   double fat = parseNutritionValue(mealItem.getOrDefault("Fat", "").toString());
   double totalNutrition = protein + carbohydrates + fat;
   mealItem.put("Total Nutrition", totalNutrition);
  }
  return mealItems;
 }

 public double parseNutritionValue(String value) {
  String numericValue = value.replace("gms", "").trim();
  return Double.parseDouble(numericValue);
 }


 public void PostRequest(String apiUrl, List<HashMap<String, Object>> json, String apiKey) {
  for (HashMap<String, Object> mealPlan : json) {
   String requestObject = GsontoJSON.convettoJSN(mealPlan);
   Response response = postRequest(apiUrl, requestObject);
   System.out.println("Response Body: " + response.getBody().asString());
   assertEquals(response.statusCode(), 200, "POST request should succeed.");

  }
 }

 public static Response postRequest(String url, String requestBody) {
  Response response = given()
          .queryParam("apiKey", "apiKey")
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
    throw new IOException("The " + fileName + " file  not exist .");
   }
   properties.load(inputStream);
  }
  return properties;
 }
}
