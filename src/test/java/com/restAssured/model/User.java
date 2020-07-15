package com.restAssured.model;

import java.util.Map;

import org.json.JSONObject;

public class User {

    private String id;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String status;

    public User() {
    }

    public User(String first_name, String last_name, String gender, String email, String status) {
        this.firstName = first_name;
        this.lastName = last_name;
        this.gender = gender;
        this.email = email;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJSON() {
        return new JSONObject()
                .put("first_name", firstName)
                .put("last_name", lastName)
                .put("gender", gender)
                .put("email", email)
                .put("status", status)
                .toString();
    }

    public User get(Map<String, String> hash) {
        for (Map.Entry<String, String> entry : hash.entrySet()) {
            if (entry.getKey().equals("id"))
                id = entry.getValue();
            else if (entry.getKey().equals("first_name"))
                firstName = entry.getValue();
            else if (entry.getKey().equals("last_name"))
                lastName = entry.getValue();
            else if (entry.getKey().equals("gender"))
                gender = entry.getValue();
            else if (entry.getKey().equals("email"))
                email = entry.getValue();
            else if (entry.getKey().equals("status"))
                status = entry.getValue();
        }
        return this;
    }
}