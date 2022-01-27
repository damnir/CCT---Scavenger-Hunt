package com.example.scavengerhunt.Entities;

public class Site {

    private String name;
    private String description;
    private String imageUri;
    private Double lat;
    private Double lng;
    private Boolean finalSite;
    private Boolean visited;

    public Site() {}

    public Site(String name, String description, String imageUri, Boolean finalSite,
                Double lat, Double lng) {

        this.name = name;
        this.description = description;
        this.imageUri = imageUri;
        this.finalSite = finalSite;
        this.lat = lat;
        this.lng = lng;
        this.visited = false;
    }

    public void setName(String name) { this.name = name; }
    public String getName() { return this.name; }

    public void setDescription(String description) { this. description = description; }
    public String getDescription() { return this.description; }

    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
    public String getImageUri() { return this.imageUri;}

    public void setFinalSite(Boolean bool) { this.finalSite = bool; }
    public Boolean getFinalSite() { return this.finalSite; }

    public void setLat(Double lat) { this.lat = lat; }
    public void setLng(Double lng) { this.lng = lng; }

    public Double getLat() { return this.lat; }
    public Double getLng() { return this.lng; }

    public Boolean getVisited() { return this.visited; }
    public void setVisited(Boolean visited) {this.visited = visited; }



}
