package com.kcs.billingapp.models;

/**
 * Created by bhaumik.shah on 03-Aug-17.
 */

public class ResultModel {

    public static final int FAILURE = 0;
    public static final int SUCCESS = 1;
    private int statusCode;
    private String message;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
