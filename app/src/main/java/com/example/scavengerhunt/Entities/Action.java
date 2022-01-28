package com.example.scavengerhunt.Entities;

public class Action {

    private String type;
    private String data1;
    private String data2;

    public Action () {
        data1 = "";
        data2 = "";
    }

    public void setType(String type) { this.type = type; }
    public String getType() { return this.type; }

    public void setData1(String data) { this.data1 = data; }
    public String getData1() { return this.data1; }

    public void setData2(String data) { this.data2 = data; }
    public String getData2() { return this.data2; }



}
