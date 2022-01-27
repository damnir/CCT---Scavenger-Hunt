package com.example.scavengerhunt.Entities;

public class Story {

    private String name;
    private String description;
    private String url;
    private Double lat;
    private Double lng;
    private String stamp;
    //gs://cct-scavenger-hunt.appspot.com/coin.png
    public Story() {}

    public Story(String name, String description, String url, Double lat, Double lng, String stamp) {
        this.name = name;
        this.description = description;
        this.url = url;
        this.lat = lat;
        this.lng = lng;
        this.stamp = stamp;
    }

    public void setName(String name) {this.name = name;}
    public String getName() {return this.name;}

    public void setDescription(String description) {this.description = description;}
    public String getDescription() {return this.description;}

    public void setUrl(String url) {this.url = url;}
    public String getUrl() {return this.url;}

    public void setLat(Double lat) {this.lat = lat;}
    public void setLng(Double lng) {this.lng = lng;}

    public Double getLat() { return this.lat; }
    public Double getLng() { return this.lng; }

    public void setStamp(String stamp) {this.description = stamp;}
    public String getStamp() {return this.stamp;}

}
