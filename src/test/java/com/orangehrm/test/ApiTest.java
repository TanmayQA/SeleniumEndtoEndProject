package com.orangehrm.test;

import com.google.protobuf.Api;
import com.orangehrm.utilities.APIUtility;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;
import io.restassured.response.Response;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ApiTest {

    private static final Logger log = LoggerFactory.getLogger(ApiTest.class);

    //  @Test(retryAnalyzer = RetryAnalyzer.class)


    @Test
    public void verifyGetUserApi() {
        // step1 - define api endpoints

        String endpoints = "https://jsonplaceholder.typicode.com/users/1";

        ExtentManager.logStep("API Endpoint: " + endpoints);

        //Step2 : Send get request

        ExtentManager.logStep("Sending Get Request to the api");
        Response response = APIUtility.SendGetRequest(endpoints);

        //Step3 : Validate status code
        ExtentManager.logStep("validating the API Response status code");
        boolean isStatusCode = APIUtility.validateStatuscode(response, 200);

        Assert.assertTrue(isStatusCode, "Status code is not as expected");

        if (isStatusCode) {
            ExtentManager.loginStepforApi("Status code Validation is passed");

        } else {
            ExtentManager.logFailureApi("Status code validation is failed");
        }

        // Step 4 username

        ExtentManager.logStep("Validating the response body for username");

        String username = APIUtility.extractJsonResponsevalue(response, "username");

        boolean isUsernameValid = "Bret".equals(username);

        Assert.assertTrue(isUsernameValid, "Username is not valid");

        if (isUsernameValid) {
            ExtentManager.loginStepforApi("Username validation is passed");
        } else {
            ExtentManager.logFailureApi("username Validation is failed.");
        }

        // Step 5 - email
        ExtentManager.logStep("Validating the response body for username");

        String email = APIUtility.extractJsonResponsevalue(response, "email");

        boolean isemailValid = "Sincere@april.biz".equals(email);

        Assert.assertTrue(isemailValid, "email is not valid");

        if (isemailValid) {
            ExtentManager.loginStepforApi("email validation is passed");
        } else {
            ExtentManager.logFailureApi("email Validation is failed.");
        }

    }
}
