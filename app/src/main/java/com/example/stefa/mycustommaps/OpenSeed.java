package com.example.stefa.mycustommaps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class OpenSeed extends AppCompatActivity {

    protected boolean firstView = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_seed);

        DatabaseReference firebaseDB = FirebaseDatabase.getInstance().getReference();
        final String seedTitle = getIntent().getStringExtra("seed");

        Log.e("OpenSeed", seedTitle);
        Log.e("OpenSeed", firebaseDB.child(seedTitle).toString());

        final TextView textTitle = (TextView)findViewById(R.id.openSeedTitle);
        final ImageView textImage = (ImageView)findViewById(R.id.imageView);
        final TextView textCounter = (TextView)findViewById(R.id.viewCount);

        firebaseDB.child(seedTitle).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Seed seed = dataSnapshot.getValue(Seed.class);

                if (firstView) {
                    seed.increaseViewCount();
                    firstView = false;
                }

                textTitle.setText(seed.title);
                textCounter.setText(String.valueOf(seed.views));
                Picasso.with(OpenSeed.this).load(seed.imageUrl).fit().centerCrop().into(textImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
