package com.example.scavengerhunt.Entities;

public class Artifact {

    private String name;
    private String description;
    private String url;
    private Double alt;
    private Double lng;
    //gs://cct-scavenger-hunt.appspot.com/coin.png
    public Artifact() {}

    public Artifact(String name, String description, String url, Double alt, Double lng) {
        this.name = name;
        this.description = description;
        this.url = url;
        this.alt = alt;
        this.lng = lng;
    }

    public void setName(String name) {this.name = name;}
    public String getName() {return this.name;}

    public void setDescription(String description) {this.description = description;}
    public String getDescription() {return this.description;}

    public void setUrl(String url) {this.url = url;}
    public String getUrl() {return this.url;}

    public void setAlt(Double alt) {this.alt = alt;}
    public void setLng(Double lng) {this.lng = lng;}

    public Double getAlt() { return this.alt; }
    public Double getLng() { return this.lng; }

}
