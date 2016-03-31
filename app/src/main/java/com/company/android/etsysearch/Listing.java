package com.company.android.etsysearch;

/**
 * Created by andytriboletti on 3/31/16.
 */
public class Listing {
    String title;
    String image;

    public Listing(String title, String image) {
        this.image = image;
        this.title = title;
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
}
