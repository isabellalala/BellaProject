package com.example.stefa.mycustommaps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OpenSeed extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_seed);

        DatabaseReference firebaseDB = FirebaseDatabase.getInstance().getReference();
        final String seedTitle = getIntent().getStringExtra("seed");
        Log.e("OpenSeed", seedTitle);
        final OpenSeed currentView = this;


        Log.e("OpenSeed", firebaseDB.child(seedTitle).toString());
        final TextView textTitle = (TextView)findViewById(R.id.openSeedTitle);
        final TextView textDescription = (TextView)findViewById(R.id.openSeedDes);

        firebaseDB.child(seedTitle).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Seed seed = dataSnapshot.getValue(Seed.class);

                textTitle.setText(seed.title);
                textDescription.setText(seed.description);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
