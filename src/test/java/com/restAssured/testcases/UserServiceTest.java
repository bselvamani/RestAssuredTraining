package com.restAssured.testcases;

import java.util.List;
import java.util.Map;

import com.restAssured.service.UserService;
import com.restAssured.model.User;
import org.testng.Assert;
import org.testng.annotations.*;

import io.restassured.response.Response;
import org.testng.asserts.SoftAssert;

public class UserServiceTest {
    private User user, actualUser;
    private SoftAssert softAssert;

    @Test
    public void test_get_all_user() {
        Response response = new UserService().getAllUser();
        Assert.assertEquals(response.getStatusCode(), 200);

        List<Map<String, String>> hashMapList = response.jsonPath().getList("result");
        Assert.assertTrue(hashMapList.size() >= 1);
    }

    @Test
    public void test_get_all_user_on_page() {
        Response response = new UserService().getAllUserOnPage("2");
        Assert.assertEquals(response.getStatusCode(), 200);

        List<Map<String, String>> hashMapList = response.jsonPath().getList("result");
        Assert.assertTrue(hashMapList.size() >= 1);
    }

    @Test(groups = "user", dataProvider = "user-data")
    public void test_create_user(String first_name, String last_name, String gender, String email, String status) {
        Response response = new UserService().createUser(new User(first_name, last_name, gender, email, status).getJSON());
        //Assert.assertEquals(response.getStatusCode(), 302);

        Map<String, String> hashMap = response.jsonPath().getMap("result");
        user = new User().get(hashMap);

        // Asserts
        softAssert = new SoftAssert();
        softAssert.assertEquals(user.getFirstName(), first_name);
        softAssert.assertEquals(user.getLastName(), last_name);
        softAssert.assertEquals(user.getGender(), gender);
        softAssert.assertEquals(user.getEmail(), email);
        softAssert.assertEquals(user.getStatus(), status);
        softAssert.assertAll();
    }

    @Test(dependsOnGroups = "user")
    public void test_get_user() {
        Response response = new UserService().getUser(user.getId());
        Assert.assertEquals(response.getStatusCode(), 200);

        Map<String, String> hashMap = response.jsonPath().getMap("result");
        actualUser = new User().get(hashMap);

        // Asserts
        Assert.assertEquals(actualUser.getFirstName(), user.getFirstName());
    }

    @Test(dependsOnGroups = "user")
    public void test_get_all_user_by_name() {
        Response response = new UserService().getAllUserWithName(user.getFirstName());
        Assert.assertEquals(response.getStatusCode(), 200);

        List<Map<String, String>> hashMapList = response.jsonPath().getList("result");
        Assert.assertTrue(hashMapList.size() >= 1);
    }

    @Test(dependsOnGroups = "user")
    public void test_update_user() {
        // Update last name and gender
        user.setLastName("Updated");
        user.setGender("female");

        Response response = new UserService().updateUser(user.getId(), user.getJSON());
        Assert.assertEquals(response.getStatusCode(), 200);

        Map<String, String> hashMap = response.jsonPath().getMap("result");
        actualUser = user.get(hashMap);

        // Asserts
        softAssert = new SoftAssert();
        softAssert.assertEquals(actualUser.getLastName(), user.getLastName());
        softAssert.assertEquals(actualUser.getGender(), user.getGender());
        softAssert.assertAll();
    }

    @Test(dependsOnGroups = "user")
    public void test_update_user_patch() {
        // Update last name and gender
        user.setLastName("Patched");
        user.setGender("male");

        Response response = new UserService().updateUserPatch(user.getId(), user.getJSON());
        Assert.assertEquals(response.getStatusCode(), 200);

        Map<String, String> hashMap = response.jsonPath().getMap("result");
        actualUser = user.get(hashMap);

        // Asserts
        softAssert = new SoftAssert();
        softAssert.assertEquals(actualUser.getLastName(), user.getLastName());
        softAssert.assertEquals(actualUser.getGender(), user.getGender());
        softAssert.assertAll();
    }

    @AfterClass(alwaysRun = true)
    public void test_delete_user() {
        if (user == null) return;

        Response response = new UserService().deleteUser(user.getId());
        Assert.assertEquals(response.getStatusCode(), 200);

        Assert.assertTrue(response.jsonPath().getMap("result") == null);
    }

    @DataProvider(name = "user-data")
    public Object[][] createUserData() {
        return new Object[][]{
                {"Bright", "Lastname", "male", "xyz111abc@gmail.com", "active"}};
    }
}