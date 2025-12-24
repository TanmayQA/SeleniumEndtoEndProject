package com.orangehrm.utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReaderUtility {

    public static List<String[]> getSheetData(String filepath, String sheetName) {
        List<String[]> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filepath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet " + sheetName + " doesn't exists");
            }
            // Iterate through rows
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                // Read all the cells in the row
                List<String> rowdata = new ArrayList<>();
                for (Cell cell : row) {
                    rowdata.add(getCellValue(cell));
                }
                // convert rowdata to String array
                data.add(rowdata.toArray(new String[0]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf((int) cell.getNumericCellValue());

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";

        }
    }
}
