package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.testng.Assert.assertEquals;


public class Shoppingcarttest extends TestBase {

    @Test
    public void Shopping() throws Exception {
        Properties properties = loadProperties("config.properties");
        String apiKey = properties.getProperty("api.key");
        String apiUrl = properties.getProperty("api.url");
        String browserPath = properties.getProperty("Browser");

// Set the Chrome driver path
        System.setProperty("webdriver.chrome.driver", browserPath);

        String excelFilePath = properties.getProperty("AbsolutePath") +"/shopping.xlsx";
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
    public void PostRequests(String apiUrl, List<HashMap<String, Object>> json, String apiKey) {
        for (HashMap<String, Object> Shopping : json) {
            String requestObject = GsontoJSON.convettoJSN(Shopping);
            Response response = postRequest(apiUrl, requestObject, apiKey);
            System.out.println("Response Body: " + response.getBody().asString());
            assertEquals(response.statusCode(), 200, "POST request should succeed.");
        }
    }

    public static Response postRequest(String url, String requestBody, String apiKey) {
        Response response = RestAssured.given()
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
                throw new FileNotFoundException("The " + fileName + " file does not exist .");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}