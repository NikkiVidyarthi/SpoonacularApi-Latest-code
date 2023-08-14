package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import static org.testng.Assert.assertEquals;


public class WeekendWineCheck {



    @Test
    public void checkWineForWeekendMeals() throws IOException {
        Properties properties = loadProperties("config.properties");
        String apiKey = properties.getProperty("api.key");
        String apiUrl = properties.getProperty("api.url");
        String browserPath = properties.getProperty("Browser");


        // Set the Chrome driver path
        System.setProperty("webdriver.chrome.driver", browserPath);
        String excelFilePath = properties.getProperty("AbsolutePath") +"/ReadFromExcel.xlsx";
        FileInputStream inputStream = new FileInputStream(excelFilePath);


        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = firstSheet.iterator();

        // Loop through rows
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // Get day value as string or numeric
            String day;
            Cell dayCell = row.getCell(0);

            // Add a null check for dayCell
            if (dayCell == null) {
                continue;
            }

            if (dayCell.getCellType() == CellType.STRING) {
                day = dayCell.getStringCellValue();
            } else if (dayCell.getCellType() == CellType.NUMERIC) {
                day = String.valueOf((int) dayCell.getNumericCellValue());
            } else {
                continue;
            }

            // Skip the check for weekdays (Monday to Friday)
            if (day.equalsIgnoreCase("Saturday") || day.equalsIgnoreCase("Sunday")) {
                // Get ID3 value as string or numeric
                String id3;
                Cell id3Cell = row.getCell(6);

                // Similar null check for id3Cell
                if (id3Cell == null) {
                    continue;
                }

                if (id3Cell.getCellType() == CellType.STRING) {
                    id3 = id3Cell.getStringCellValue();
                } else if (id3Cell.getCellType() == CellType.NUMERIC) {
                    id3 = String.valueOf((int) id3Cell.getNumericCellValue());
                } else {
                    continue;
                }


                // Perform wine availability check based on ID3 value
                boolean wineAvailable = isWineAvailable(apiUrl, id3);

                if (wineAvailable) {
                    System.out.println("Wine cannot be used for the " + day + " meal.");
                } else {
                    System.out.println("Wine used for the " + day + " meal.");
                }
            }
        }


        workbook.close();
        inputStream.close();
    }


    public void PostRequests(String apiUrl, List<HashMap<String, Object>> json, String apiKey) {
        for (HashMap<String, Object> checkWineForWeekendMeals : json) {
            String requestObject = GsontoJSON.convettoJSN(checkWineForWeekendMeals);
            Response response = isWineAvailable(apiUrl, requestObject, apiKey);
            System.out.println("Response Body: " + response.getBody().asString());
            assertEquals(response.statusCode(), 200, "POST request should succeed.");
        }
    }

    public static Response isWineAvailable(String url, String requestBody, String apiKey) {
        Response response = RestAssured.given()
                .queryParam("ingredientId")
                .contentType(ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .get(url)
                .then()
                .extract().response();
        return response;
    }

    public boolean isWineAvailable(String apiUrl, String ingredientId) {
        return performApiCall(apiUrl, ingredientId);
    }
    public boolean performApiCall(String apiUrl, String ingredientId) {
        return false;
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
