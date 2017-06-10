package com.example.stefa.mycustommaps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@IgnoreExtraProperties
public class Seed {
    public String title = null;
    public String description = null;
    public Double latitude = 0.0;
    public Double longitude = 0.0;
    public String imageUrl = null;

    public String toString() {
        return "Title: "+title+", Lat: "+latitude+", Lng: "+longitude;
    }

    public Seed(){

    }

    public Seed(String title, String description, Double latitude, Double longitude, String imageUrl){
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}

