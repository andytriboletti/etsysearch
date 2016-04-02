package com.company.android.etsysearch;


public class Listing {
    String title;
    String image;
    String description;


    public Listing(String title, String image, String description) {
        this.image = image;
        this.title = title;
        this.description=description;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
