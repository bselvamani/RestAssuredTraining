package com.restAssured.service;

import io.restassured.specification.RequestSpecification;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.apache.log4j.Logger;

public class BaseService {

    private final String url = "https://gorest.co.in";
    private final String url_path = "/public-api/users";
    private final String token = "9lNXzGMtU8jruZIkPon33hzm_5MBShnKgT5g";

    private String all_user_with_name = "?first_name={first_name}";
    private String all_user_on_page = "?page={page}";
    private String user = "/{id}";

    public final Logger logger = Logger.getLogger(BaseService.class);

    public BaseService() {
        RestAssured.baseURI = getBaseUrl();
        RestAssured.defaultParser = Parser.JSON;
    }

    public String getBaseUrl() {
        return url + url_path;
    }

    public String getAllUserWithName() {
        return getBaseUrl() + all_user_with_name;
    }

    public String getAllUserOnPage() {
        return getBaseUrl() + all_user_on_page;
    }

    public String getUserUrl() {
        return getBaseUrl() + user;
    }

    public RequestSpecification requestSpecification() {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .queryParam("_format", "json")
                .queryParam("access-token", token);
    }
}