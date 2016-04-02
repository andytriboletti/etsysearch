package com.company.android.etsysearch;


import android.os.Parcel;
import android.os.Parcelable;

public class Listing implements Parcelable {
    String title;
    String image;
    String description;


    public Listing(String title, String image, String description) {
        this.image = image;
        this.title = title;
        this.description=description;

    }

    protected Listing(Parcel in) {
        title = in.readString();
        image = in.readString();
        description = in.readString();
    }

    public static final Creator<Listing> CREATOR = new Creator<Listing>() {
        @Override
        public Listing createFromParcel(Parcel in) {
            return new Listing(in);
        }

        @Override
        public Listing[] newArray(int size) {
            return new Listing[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(image);
        dest.writeString(description);
    }
}
