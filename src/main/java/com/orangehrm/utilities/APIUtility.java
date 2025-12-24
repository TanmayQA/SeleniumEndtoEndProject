package com.orangehrm.utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class APIUtility {

    // Method to send the Get Request

    public static Response SendGetRequest(String endpoint) {
        return RestAssured.get(endpoint);
    }

    // Method to Send the Post Request

    public static void sendPostRequest(String endpoint, String payload) {
        RestAssured.given().header("content-Type", "application/json")
                .body(payload)
                .post();
    }

    // method to validate the response status

    public static boolean validateStatuscode(Response response, int statuscode) {
        return response.getStatusCode() == statuscode;
    }

    // Method to extract value from json response

    public static String extractJsonResponsevalue(Response response, String value) {
        return response.jsonPath().getString(value);
    }
}
