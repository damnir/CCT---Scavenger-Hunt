package com.example.scavengerhunt.Entities;

import android.graphics.drawable.Drawable;

public class Log {

    private String type;
    private String title;
    private String label;
    private String stamp;
    private String description;
    private String image;
    private Artifact artifact;

    public Log() {

    }

    public Log(String type, String title, String label, String stamp,
               String description, Artifact artifact) {
        this.type = type;
        this.title = title;
        this.label = label;
        this.stamp = stamp;
        this.description = description;
        this.artifact = artifact;
    }

    public void setType(String nType) {
        this.type = nType;
    }
    public String getType() {
        return this.type;
    }

    public void setTitle(String nTitle) {
        this.title = nTitle;
    }
    public String getTitle() {
        return this.title;
    }

    public void setLabel(String nLabel) {
        this.label = nLabel;
    }
    public String getLabel() {
        return this.label;
    }

    public void setDescription(String nDescription) {
        this.description = nDescription;
    }
    public String getDescription() {
        return this.description;
    }

    public void setImage(String nImage) {
        this.image = nImage;
    }
    public String getImage() {
        return this.image;
    }

    public void setStamp(String nStamp) { this.stamp = nStamp; }
    public String getStamp() { return this.stamp; }

    public void setArtifact(Artifact artifact) {this.artifact = artifact;}
    public Artifact getArtifact() { return this.artifact; }


}
