package com.example.stefa.mycustommaps;


import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@IgnoreExtraProperties
public class Seed {
    public String title = null;
    public Double latitude = 0.0;
    public Double longitude = 0.0;
    public int views = 0;
    public String imageUrl = null;
    public Long expireAt = null;

    public String toString() {
        return "Title: "+title+", Lat: "+latitude+", Lng: "+longitude;
    }

    public Seed(){

    }

    public Seed(String title, Double latitude, Double longitude, String imageUrl, Long expireAt){
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageUrl = imageUrl;
        this.expireAt = expireAt;
    }

    public void increaseExpireDate()
    {
        if (this.expireAt == null) {
            return;
        }
        // Add 1 hour.
        this.expireAt += 3600000;
        FirebaseDatabase.getInstance().getReference().child(this.title).child("expireAt").setValue(this.expireAt);
    }

    /**
     * Check if the seed was created within the last 2 days.
     * @return bool
     */
    public boolean isActive()
    {
        // When no start time is defined the seed stays forever.
        if (this.expireAt == null) {
            return true;
        }
        return this.expireAt - System.currentTimeMillis() > 0;
    }

    /**
     * Format the expired time in hours
     * @return String
     */
    public String expireForHumans()
    {
        if (this.expireAt == null) {
            return "I NEVER DIE";
        }
        Long miliDiff = this.expireAt - System.currentTimeMillis();
        Long HoursDiff = TimeUnit.MILLISECONDS.toHours(miliDiff);
        Long minDiff = TimeUnit.MILLISECONDS.toMinutes(miliDiff - TimeUnit.HOURS.toMillis(HoursDiff));

        return String.format("%dh %dm", HoursDiff, minDiff);
    }

    public int increaseViewCount()
    {
        this.views += 1;
        FirebaseDatabase.getInstance().getReference().child(this.title).child("views").setValue(this.views);
        return this.views;
    }
}

