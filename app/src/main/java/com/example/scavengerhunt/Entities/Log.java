package com.example.scavengerhunt.Entities;

import android.graphics.drawable.Drawable;

public class Log {

    private String type;
    private String title;
    private String label;
    private String stamp;
    private String description;
    private Drawable image;

    public Log() {

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

    public void setImage(Drawable nImage) {
        this.image = nImage;
    }

    public Drawable getImage() {
        return this.image;
    }

    public void setStamp(String nStamp) { this.stamp = nStamp; }

    public String getStamp() { return this.stamp; }


}
