package com.example.stefa.mycustommaps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddSeedActivity extends AppCompatActivity {

    private DatabaseReference firebaseDB;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int GALLERI_INTENT = 2;
    static Uri imageUri = null;
    private StorageReference firebaseStorage;
    private ProgressDialog uploadProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_seed);
        this.firebaseDB = FirebaseDatabase.getInstance().getReference();
        this.firebaseStorage = FirebaseStorage.getInstance().getReference();
        this.uploadProgressDialog = new ProgressDialog(this);
    }

    public void takePhoto(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERI_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERI_INTENT && resultCode == RESULT_OK) {
            uploadProgressDialog.setMessage("Uploading...");
            uploadProgressDialog.show();

            imageUri = data.getData();

            firebaseStorage.child("Photos").child(imageUri.getLastPathSegment()).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddSeedActivity.this, "Upload done", Toast.LENGTH_SHORT).show();
                    uploadProgressDialog.dismiss();
                }
            });
        }
    }

    public void saveSeed(View v) {

        EditText seedTitle = (EditText) findViewById(R.id.seedTitle);
        if (seedTitle.getText().toString().length() == 0){
            Toast.makeText(this, "No title registered", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText seedDes = (EditText) findViewById(R.id.seedDescription);
        if (seedDes.getText().toString().length() == 0){
            Toast.makeText(this, "No description registered", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.e("AddSeedImage", imageUri.toString());

        if (imageUri == null) {
            Toast.makeText(this, "No image registered", Toast.LENGTH_SHORT).show();
            return;
        }

        String latitude = getIntent().getStringExtra("latitude");
        String longitude = getIntent().getStringExtra("longitude");
        Seed seed = new Seed(seedTitle.getText().toString(), seedDes.getText().toString(), Double.valueOf(latitude), Double.valueOf(longitude));

        firebaseDB.child(seed.title).setValue(seed);
        Toast.makeText(this, seed.toString(), Toast.LENGTH_SHORT).show();
        finish();
    }
}
