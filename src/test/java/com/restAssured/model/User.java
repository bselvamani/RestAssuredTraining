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

    public User(String firstName, String lastName, String gender, String email, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
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
        String fullName = (firstName == null ? "" : firstName.trim())
                + (lastName == null || lastName.trim().isEmpty() ? "" : " " + lastName.trim());
        return new JSONObject()
                .put("name", fullName.trim())
                .put("gender", gender)
                .put("email", email)
                .put("status", status)
                .toString();
    }

    public User fromMap(Map<String, ?> hash) {
        if (hash == null) {
            return this;
        }
        for (Map.Entry<String, ?> entry : hash.entrySet()) {
            String value = entry.getValue() == null ? null : String.valueOf(entry.getValue());
            if (entry.getKey().equals("id")) {
                id = value;
            } else if (entry.getKey().equals("name")) {
                applyName(value);
            } else if (entry.getKey().equals("first_name")) {
                firstName = value;
            } else if (entry.getKey().equals("last_name")) {
                lastName = value;
            } else if (entry.getKey().equals("gender")) {
                gender = value;
            } else if (entry.getKey().equals("email")) {
                email = value;
            } else if (entry.getKey().equals("status")) {
                status = value;
            }
        }
        return this;
    }

    private void applyName(String name) {
        if (name == null || name.trim().isEmpty()) {
            this.firstName = name;
            this.lastName = null;
            return;
        }

        String[] parts = name.trim().split("\\s+", 2);
        this.firstName = parts[0];
        this.lastName = parts.length > 1 ? parts[1] : "";
    }

    public User get(Map<String, String> hash) {
        return fromMap(hash);
    }
}