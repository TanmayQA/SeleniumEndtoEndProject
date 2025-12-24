package com.orangehrm.utilities;

import org.testng.annotations.DataProvider;

import java.util.List;

public class DataProviders {
    private static final String File_Path = System.getProperty("user.dir") + "/src/test/resources/testdata/LoginData.xlsx";

    @DataProvider(name = "ValidLogin")
    public static Object[][] validLoginData() {
        return getSheetData("ValidLogin");
    }

    @DataProvider(name = "Invalidlogin")
    public static Object[][] InvalidLoginData() {
        return getSheetData("Invalidlogin");
    }

    @DataProvider(name = "employeeverification")
    public static Object[][] employeeverification() {
        return getSheetData("EmployeeData");
    }

    private static Object[][] getSheetData(String sheetname) {
        List<String[]> sheetdata = ExcelReaderUtility.getSheetData(File_Path, sheetname);

        Object[][] data = new Object[sheetdata.size()][sheetdata.get(0).length];

        for (int i = 0; i < sheetdata.size(); i++) {
            data[i] = sheetdata.get(i);
        }

        return data;


    }
}
