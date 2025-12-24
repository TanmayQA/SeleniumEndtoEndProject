package com.orangehrm.utilities;

import com.orangehrm.base.BaseClass;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DBConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/OrangeHRM";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";
    private static final Logger logger = BaseClass.logger;

    public static Connection getDBConnection() {

        try {

            logger.info("starting DB Connection...");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            logger.info("DB Connection Successful");
            return conn;
        } catch (SQLException e) {
            logger.error("Error while establizing the DB connection");
            e.printStackTrace();
            return null;
        }


    }

    // Get the employeeDetails from DB

    public static Map<String, String> getEmployeeDetails(String employee_id) {

        String query = "SELECT emp_firstname, emp_middle_name,emp_lastname FROM `hs_hr_employee` WHERE employee_id=" + employee_id;
        Map<String, String> employeedetails = new HashMap<>();
        try (Connection conn = getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {


            logger.info(" Executing query :- " + query);
            if (rs.next()) {
                String firstname = rs.getString("emp_firstname");
                String middlename = rs.getString("emp_middle_name");
                String lastname = rs.getString("emp_lastname");

                // Store in a map

                employeedetails.put("firstname", firstname);
                employeedetails.put("middlename", middlename!=null?middlename:"");
                employeedetails.put("lastname", lastname);
                logger.info("Query executed successfully");
                logger.info("Employee data fetched");
                logger.info(firstname+","+middlename+","+lastname);

            } else {
                logger.error("Employee not found");
            }

        } catch (Exception e) {
            logger.error("Error while executing query");
            e.printStackTrace();
        }
        return employeedetails;
    }
}
