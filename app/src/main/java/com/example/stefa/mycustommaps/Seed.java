package com.example.stefa.mycustommaps;


import android.util.Log;

import com.google.firebase.database.IgnoreExtraProperties;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@IgnoreExtraProperties
public class Seed {
    public String title = null;
    public String description = null;
    public Double latitude = 0.0;
    public Double longitude = 0.0;
    public String imageUrl = null;
    public Long createdAt = null;

    public String toString() {
        return "Title: "+title+", Lat: "+latitude+", Lng: "+longitude;
    }

    public Seed(){

    }

    public Seed(String title, String description, Double latitude, Double longitude, String imageUrl, Long createdAt){
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    /**
     * Check if the seed was created within the last 2 days.
     * @return bool
     */
    public boolean isActive()
    {
        // When no start time is defined the seed stays forever.
        if (this.createdAt == null) {
            return true;
        }
        return Math.abs(System.currentTimeMillis() - this.createdAt) <= 172800000;
    }
}

