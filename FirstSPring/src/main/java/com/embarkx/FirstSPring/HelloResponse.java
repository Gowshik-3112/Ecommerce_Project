package com.embarkx.FirstSPring;

public class HelloResponse {
    //private String message;
    private int responseCode;

    /*public HelloResponse(String message) {
        this.message = message;
    }*/
    public HelloResponse(int responseCode) {
        this.responseCode = responseCode;
    }

    /*public String getMessage() {
        return message;
    }*/

    public int getResponseCode() {
        return responseCode;
    }

   /* public void setMessage(String message) {
        this.message = message;
    }*/
}
