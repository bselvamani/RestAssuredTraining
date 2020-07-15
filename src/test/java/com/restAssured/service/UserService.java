package com.restAssured.service;

import io.restassured.response.Response;

public class UserService extends BaseService {

    private Response response;

    public Response getAllUser() {
        logger.info("Request for all users");
        response = requestSpecification().get(getBaseUrl());
        logger.info(response.then().log().all());
        return response;
    }

    public Response getUser(String id) {
        logger.info("Request for the user: " + id);
        response = requestSpecification()
                .pathParam("id", id)
                .get(getUserUrl());
        logger.info(response.then().log().all());
        return response;
    }

    public Response getAllUserWithName(String first_name) {
        logger.info("Request for all user with their first name as " + first_name);
        response = requestSpecification()
                .pathParam("first_name", first_name)
                .get(getAllUserWithName());

        logger.info(response.then().log().all());
        return response;
    }

    public Response getAllUserOnPage(String page) {
        logger.info("Request for all user on the Page#: " + page);
        response = requestSpecification()
                .pathParam("page", "page")
                .get(getAllUserOnPage());

        logger.info(response.then().log().all());
        return response;
    }

    public Response createUser(String payload) {
        logger.info("Request for Creating an user");
        response = requestSpecification()
                .body(payload)
                .post(getBaseUrl());
        logger.info(response.then().log().all());
        return response;
    }

    public Response updateUser(String id, String payload) {
        logger.info("Request for Updating an user " + id);
        response = requestSpecification()
                .pathParam("id", id)
                .body(payload)
                .put(getUserUrl());
        logger.info(response.then().log().all());
        return response;
    }

    public Response updateUserPatch(String id, String payload) {
        logger.info("Request for Updating an user " + id + " with patch");
        response = requestSpecification()
                .pathParam("id", id)
                .body(payload)
                .patch(getUserUrl());
        logger.info(response.then().log().all());
        return response;
    }

    public Response deleteUser(String id) {
        logger.info("Request for deleting an user " + id);
        response = requestSpecification()
                .pathParam("id", id)
                .delete(getUserUrl());
        logger.info(response.then().log().all());
        return response;
    }
}