package com.restAssured.steps;

import com.restAssured.service.UserService;
import com.restAssured.model.User;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.util.Map;

public class UserServiceStep {
    private User user, actualUser;
    private Response response;
    private SoftAssert softAssert;

    @Given("^Setting up user Rest API$")
    public void settingUpUserRestAPI() throws Throwable {
    }

    @When("^I send a request to create user:$")
    public void iSendARequestToCreateUser(DataTable table) throws Throwable {
        Map<String, String> hashMap = table.asMap(String.class, String.class);
        user = new User(hashMap.get("first_name"), hashMap.get("last_name"), hashMap.get("gender"), hashMap.get("email"), hashMap.get("status"));

        response = new UserService().createUser(user.getJSON());
        Assert.assertEquals(response.getStatusCode(), 302);
    }

    @When("^I send a request to get user$")
    public void iSendARequestToGetUser() throws Throwable {
        response = new UserService().getUser(user.getId());
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @When("^I send a request to update user:$")
    public void iSendARequestToUpdateUser(DataTable table) throws Throwable {
        // Update last name and gender
        Map<String, String> hashMap = table.asMap(String.class, String.class);
        user.setLastName(hashMap.get("last_name"));
        user.setGender(hashMap.get("gender"));

        response = new UserService().updateUser(user.getId(), user.getJSON());
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @When("^I send a request to patch user:$")
    public void iSendARequestToPatchUser(DataTable table) throws Throwable {
        // Update last name and gender
        Map<String, String> hashMap = table.asMap(String.class, String.class);
        user.setLastName(hashMap.get("last_name"));
        user.setGender(hashMap.get("gender"));

        response = new UserService().updateUserPatch(user.getId(), user.getJSON());
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @When("^I send a request to delete user$")
    public void iSendARequestToDelete() throws Throwable {
        response = new UserService().deleteUser(user.getId());
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Then("^I should validate the created user$")
    public void iShouldValidateTheCreatedUser() throws Throwable {
        Map<String, String> hashMap = response.jsonPath().getMap("result");
        actualUser = new User().get(hashMap);

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
    public void iShouldValidateTheGetUser() throws Throwable {
        Map<String, String> hashMap = response.jsonPath().getMap("result");
        actualUser = new User().get(hashMap);

        // Asserts
        Assert.assertEquals(actualUser.getFirstName(), user.getFirstName());
    }

    @Then("^I should validate the (updated|patched) user$")
    public void iShouldValidateTheUserAction(String action) throws Throwable {
        Map<String, String> hashMap = response.jsonPath().getMap("result");
        actualUser = new User().get(hashMap);

        softAssert = new SoftAssert();
        softAssert.assertEquals(actualUser.getLastName(), user.getLastName());
        softAssert.assertEquals(actualUser.getGender(), user.getGender());
        softAssert.assertAll();
    }

    @Then("^I should validate the deleted user$")
    public void iShouldValidateTheDeletedUser() throws Throwable {
        Assert.assertTrue(response.jsonPath().getMap("result") == null);
    }
}