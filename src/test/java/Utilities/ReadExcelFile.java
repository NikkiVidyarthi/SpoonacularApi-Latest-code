package Utilities;



import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

public class ReadExcelFile {
    public static Object[][] readExcel(String filePath, String fileName, String sheetName) throws Exception {


        File file = new File(filePath + "//" + fileName);

        FileInputStream inputStream = new FileInputStream(file);
        System.out.println(file.getAbsolutePath());
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        System.out.println("Workbook Sheet ---- " + sheetName);
        XSSFSheet workbookSheet = workbook.getSheet(sheetName);

        int rowCount = workbookSheet.getLastRowNum() - workbookSheet.getFirstRowNum();
        System.out.println("rowCount -- " + rowCount);

        Object[][] projectDetails = new Object[rowCount][1];
        DataFormatter formatter = new DataFormatter();

        for (int i = 1; i <= rowCount; i++) {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            Row row = workbookSheet.getRow(i);

            for (int j = 0; j < row.getLastCellNum(); j++) {

                hashMap.put(formatter.formatCellValue(workbookSheet.getRow(0).getCell(j)), formatter.formatCellValue(row.getCell(j)));

            }
            projectDetails[i - 1][0] = hashMap;
        }
        workbook.close();
        inputStream.close();

        return projectDetails;
    }
}


