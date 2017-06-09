package com.example.stefa.mycustommaps;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Seed {
    public String title = null;
    public Double latitude = 0.0;
    public Double longitude = 0.0;

    public String toString() {
        return "Title: "+title+", Lat: "+latitude+", Lng: "+longitude;
    }

    public Seed(){

    }

    public Seed(String title, Double latitude, Double longitude){
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

