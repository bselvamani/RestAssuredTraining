package com.restAssured.steps;

import com.restAssured.service.UserService;
import com.restAssured.model.User;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.asserts.SoftAssert;

import java.util.Map;
import java.util.UUID;

public class UserServiceStep {
    private User user, actualUser;
    private Response response;
    private SoftAssert softAssert;
    private final UserService userService = new UserService();

    @Given("^Setting up user Rest API$")
    public void settingUpUserRestAPI() {
    }

    @When("^I send a request to create user:$")
    public void iSendARequestToCreateUser(DataTable table) {
        ensureAuthAvailable();
        Map<String, String> hashMap = table.asMap(String.class, String.class);
        user = new User(
            hashMap.get("first_name"),
            hashMap.get("last_name"),
            hashMap.get("gender"),
            uniqueEmail(hashMap.get("email")),
            hashMap.get("status")
        );

        response = userService.createUser(user.getJSON());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(isApiSuccess(response), "Expected API success code for create call");
    }

    @When("^I send a request to get user$")
    public void iSendARequestToGetUser() {
        response = userService.getUser(user.getId());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("code"), 200);
    }

    @When("^I send a request to update user:$")
    public void iSendARequestToUpdateUser(DataTable table) {
        ensureAuthAvailable();
        // Update last name and gender
        Map<String, String> hashMap = table.asMap(String.class, String.class);
        user.setLastName(hashMap.get("last_name"));
        user.setGender(hashMap.get("gender"));

        response = userService.updateUser(user.getId(), user.getJSON());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(isApiSuccess(response), "Expected API success code for update call");
    }

    @When("^I send a request to patch user:$")
    public void iSendARequestToPatchUser(DataTable table) {
        ensureAuthAvailable();
        // Update last name and gender
        Map<String, String> hashMap = table.asMap(String.class, String.class);
        user.setLastName(hashMap.get("last_name"));
        user.setGender(hashMap.get("gender"));

        response = userService.updateUserPatch(user.getId(), user.getJSON());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(isApiSuccess(response), "Expected API success code for patch call");
    }

    @When("^I send a request to delete user$")
    public void iSendARequestToDelete() {
        ensureAuthAvailable();
        response = userService.deleteUser(user.getId());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(isApiSuccess(response), "Expected API success code for delete call");
    }

    @Then("^I should validate the created user$")
    public void iShouldValidateTheCreatedUser() {
        Map<String, Object> hashMap = response.jsonPath().getMap("data");
        actualUser = new User().fromMap(hashMap);

        user.setId(actualUser.getId());
        // Asserts
        softAssert = new SoftAssert();
        softAssert.assertEquals(actualUser.getFirstName(), user.getFirstName());
        softAssert.assertEquals(actualUser.getLastName(), user.getLastName());
        softAssert.assertEquals(actualUser.getGender(), user.getGender());
        softAssert.assertEquals(actualUser.getEmail(), user.getEmail());
        softAssert.assertEquals(actualUser.getStatus(), user.getStatus());
        softAssert.assertAll();
    }

    @Then("^I should validate the get user$")
    public void iShouldValidateTheGetUser() {
        Map<String, Object> hashMap = response.jsonPath().getMap("data");
        actualUser = new User().fromMap(hashMap);

        // Asserts
        Assert.assertEquals(actualUser.getFirstName(), user.getFirstName());
    }

    @Then("^I should validate the (updated|patched) user$")
    public void iShouldValidateTheUserAction(String action) {
        Map<String, Object> hashMap = response.jsonPath().getMap("data");
        actualUser = new User().fromMap(hashMap);

        softAssert = new SoftAssert();
        softAssert.assertEquals(actualUser.getLastName(), user.getLastName());
        softAssert.assertEquals(actualUser.getGender(), user.getGender());
        softAssert.assertAll();
    }

    @Then("^I should validate the deleted user$")
    public void iShouldValidateTheDeletedUser() {
        Assert.assertNull(response.jsonPath().get("data"));
    }

    private boolean isApiSuccess(Response response) {
        int code = response.jsonPath().getInt("code");
        return code >= 200 && code < 300;
    }

    private String uniqueEmail(String email) {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        int atIndex = email.indexOf('@');
        if (atIndex <= 0) {
            return "restassured." + suffix + "@example.com";
        }
        return email.substring(0, atIndex) + "+" + suffix + email.substring(atIndex);
    }

    private void ensureAuthAvailable() {
        if (!userService.isAuthConfigured()) {
            throw new SkipException("Write API steps skipped: GoRest token is not configured.");
        }
    }
}