package com.restAssured.service;

import io.restassured.specification.RequestSpecification;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BaseService {

    private static final String DEFAULT_BASE_URL = "https://gorest.co.in/public-api/users";
    private static final String BASE_URL_PROPERTY = "gorest.baseUrl";
    private static final String TOKEN_PROPERTY = "gorest.token";
    private static final String TOKEN_ENV = "GOREST_TOKEN";
    private static final String DOTENV_FILE = ".env";

    private static final String ALL_USER_WITH_NAME = "?first_name={first_name}";
    private static final String ALL_USER_ON_PAGE = "?page={page}";
    private static final String USER = "/{id}";

    private final String baseUrl;
    private final String token;

    protected final Logger logger = Logger.getLogger(getClass());

    public BaseService() {
        this.baseUrl = System.getProperty(BASE_URL_PROPERTY, DEFAULT_BASE_URL);
        this.token = readToken();
        RestAssured.baseURI = getBaseUrl();
        RestAssured.defaultParser = Parser.JSON;
    }

    private String readToken() {
        String configuredToken = System.getProperty(TOKEN_PROPERTY);
        if (configuredToken == null || configuredToken.trim().isEmpty()) {
            configuredToken = System.getenv(TOKEN_ENV);
        }
        if (configuredToken == null || configuredToken.trim().isEmpty()) {
            configuredToken = readFromDotEnv(TOKEN_ENV);
        }
        return configuredToken;
    }

    private String readFromDotEnv(String key) {
        Path projectRoot = Path.of(System.getProperty("user.dir"));
        Path envFile = projectRoot.resolve(DOTENV_FILE);
        if (!Files.exists(envFile)) {
            return null;
        }
        try {
            for (String line : Files.readAllLines(envFile)) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#") || !trimmed.contains("=")) {
                    continue;
                }
                String[] parts = trimmed.split("=", 2);
                if (parts[0].trim().equals(key)) {
                    return parts[1].trim();
                }
            }
        } catch (IOException ex) {
            logger.warn("Unable to read " + DOTENV_FILE + " for token configuration", ex);
        }
        return null;
    }

    public boolean isAuthConfigured() {
        return token != null && !token.trim().isEmpty();
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getAllUserWithName() {
        return getBaseUrl() + ALL_USER_WITH_NAME;
    }

    public String getAllUserOnPage() {
        return getBaseUrl() + ALL_USER_ON_PAGE;
    }

    public String getUserUrl() {
        return getBaseUrl() + USER;
    }

    public RequestSpecification requestSpecification() {
        RequestSpecification request = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .queryParam("_format", "json");

        if (isAuthConfigured()) {
            request.queryParam("access-token", token);
        }
        return request;
    }
}