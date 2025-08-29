package com.example.javaapp.model;

public class GenerateWebhookRequest {
    private String name;
    private String regNo;
    private String email;

    public GenerateWebhookRequest(String name, String regNo, String email) {
        this.name = name;
        this.regNo = regNo;
        this.email = email;
    }

}
