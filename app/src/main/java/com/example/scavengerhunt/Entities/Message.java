package com.example.scavengerhunt.Entities;

public class Message {

    private String stamp;
    private String message;

    public Message(String stamp, String message) {
        this.stamp = stamp;
        this.message = message;
    }

    public Message () {

    }

    public void setStamp(String stamp) { this.stamp = stamp; }
    public void setMessage(String message) { this.message = message; }

    public String getStamp() { return this.stamp; }
    public String getMessage() { return this.message; }


}
