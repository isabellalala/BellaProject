package com.example.stefa.mycustommaps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddSeedActivity extends AppCompatActivity {

    private DatabaseReference firebaseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_seed);
        this.firebaseDB = FirebaseDatabase.getInstance().getReference();
    }

    public void saveSeed(View v) {
        EditText seedTitle = (EditText) findViewById(R.id.seedTitle);
        String latitude = getIntent().getStringExtra("latitude");
        String longitude = getIntent().getStringExtra("longitude");
        Seed seed = new Seed(seedTitle.getText().toString(), Double.valueOf(latitude), Double.valueOf(longitude));

        firebaseDB.child(seed.title).setValue(seed);
        Toast.makeText(this, seed.toString(), Toast.LENGTH_SHORT).show();
    }
}
