package com.restAssured.testcases;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.restAssured.service.UserService;
import com.restAssured.model.User;
import org.testng.Assert;
import org.testng.annotations.*;

import io.restassured.response.Response;
import org.testng.asserts.SoftAssert;
import org.testng.SkipException;

public class UserServiceTest {
    private User user, actualUser;
    private SoftAssert softAssert;
    private final UserService userService = new UserService();

    @Test
    public void testGetAllUser() {
        Response response = userService.getAllUser();
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("code"), 200);

        List<Map<String, Object>> hashMapList = response.jsonPath().getList("data");
        Assert.assertTrue(hashMapList.size() >= 1);
    }

    @Test
    public void testGetAllUserOnPage() {
        Response response = userService.getAllUserOnPage("2");
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("code"), 200);

        List<Map<String, Object>> hashMapList = response.jsonPath().getList("data");
        Assert.assertTrue(hashMapList.size() >= 1);
    }

    @Test(groups = "user", dataProvider = "user-data")
    public void testCreateUser(String first_name, String last_name, String gender, String email, String status) {
        ensureAuthAvailable();
        Response response = userService.createUser(new User(first_name, last_name, gender, email, status).getJSON());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(isApiSuccess(response), "Expected API success code for create call");

        Map<String, Object> hashMap = response.jsonPath().getMap("data");
        user = new User().fromMap(hashMap);

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
    public void testGetUser() {
        Response response = userService.getUser(user.getId());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("code"), 200);

        Map<String, Object> hashMap = response.jsonPath().getMap("data");
        actualUser = new User().fromMap(hashMap);

        // Asserts
        Assert.assertEquals(actualUser.getFirstName(), user.getFirstName());
    }

    @Test(dependsOnGroups = "user")
    public void testGetAllUserByName() {
        Response response = userService.getAllUserWithName(user.getFirstName());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("code"), 200);

        List<Map<String, Object>> hashMapList = response.jsonPath().getList("data");
        Assert.assertTrue(hashMapList.size() >= 1);
    }

    @Test(dependsOnGroups = "user")
    public void testUpdateUser() {
        ensureAuthAvailable();
        // Update last name and gender
        user.setLastName("Updated");
        user.setGender("female");

        Response response = userService.updateUser(user.getId(), user.getJSON());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(isApiSuccess(response), "Expected API success code for update call");

        Map<String, Object> hashMap = response.jsonPath().getMap("data");
        actualUser = new User().fromMap(hashMap);

        // Asserts
        softAssert = new SoftAssert();
        softAssert.assertEquals(actualUser.getLastName(), user.getLastName());
        softAssert.assertEquals(actualUser.getGender(), user.getGender());
        softAssert.assertAll();
    }

    @Test(dependsOnGroups = "user")
    public void testUpdateUserPatch() {
        ensureAuthAvailable();
        // Update last name and gender
        user.setLastName("Patched");
        user.setGender("male");

        Response response = userService.updateUserPatch(user.getId(), user.getJSON());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(isApiSuccess(response), "Expected API success code for patch call");

        Map<String, Object> hashMap = response.jsonPath().getMap("data");
        actualUser = new User().fromMap(hashMap);

        // Asserts
        softAssert = new SoftAssert();
        softAssert.assertEquals(actualUser.getLastName(), user.getLastName());
        softAssert.assertEquals(actualUser.getGender(), user.getGender());
        softAssert.assertAll();
    }

    @AfterClass(alwaysRun = true)
    public void testDeleteUser() {
        if (user == null) return;

        ensureAuthAvailable();
        Response response = userService.deleteUser(user.getId());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(isApiSuccess(response), "Expected API success code for delete call");
        Assert.assertNull(response.jsonPath().get("data"));
    }

    @DataProvider(name = "user-data")
    public Object[][] createUserData() {
        String uniqueEmail = "restassured." + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        return new Object[][]{
                {"Bright", "Lastname", "male", uniqueEmail, "active"}};
    }

    private boolean isApiSuccess(Response response) {
        int code = response.jsonPath().getInt("code");
        return code >= 200 && code < 300;
    }

    private void ensureAuthAvailable() {
        if (!userService.isAuthConfigured()) {
            throw new SkipException("Write API tests skipped: GoRest token is not configured.");
        }
    }
}