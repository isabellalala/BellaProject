package com.example.stefa.mycustommaps;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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
    static final int CAMERA_REQUEST_INTENT = 3322;
    static final int GALLERY_INTENT = 2;
    static String publicImageUrl = null;
    private StorageReference firebaseStorage;
    private ProgressDialog uploadProgressDialog;
    private Uri mCapturedImageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_seed);
        this.firebaseDB = FirebaseDatabase.getInstance().getReference();
        this.firebaseStorage = FirebaseStorage.getInstance().getReference();
        this.uploadProgressDialog = new ProgressDialog(this);
    }

    public void takePhoto(View v) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image File name");
        mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
        startActivityForResult(intentPicture,CAMERA_REQUEST_INTENT);
    }

    public void uploadPhoto(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            uploadImage(data.getData());
        } else if (requestCode == CAMERA_REQUEST_INTENT && resultCode == RESULT_OK) {
            uploadImage(mCapturedImageURI);
        }
    }

    protected void uploadImage(Uri imageUri) {
        if (imageUri == null) {
            Toast.makeText(AddSeedActivity.this, "Couldn't upload image", Toast.LENGTH_LONG).show();
            return;
        }

        uploadProgressDialog.setMessage("Uploading...");
        uploadProgressDialog.show();

        firebaseStorage.child("Photos")
                       .child(imageUri.getLastPathSegment())
                       .putFile(imageUri)
                       .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                publicImageUrl = taskSnapshot.getDownloadUrl().toString();
                Toast.makeText(AddSeedActivity.this, "Upload done", Toast.LENGTH_SHORT).show();
                uploadProgressDialog.dismiss();
            }
        });
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

        if (publicImageUrl == null) {
            Toast.makeText(this, "No image registered", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.e("AddSeedImage", publicImageUrl);

        Seed seed = new Seed(
                seedTitle.getText().toString(),
                seedDes.getText().toString(),
                Double.valueOf(getIntent().getStringExtra("latitude")),
                Double.valueOf(getIntent().getStringExtra("longitude")),
                publicImageUrl
        );

        firebaseDB.child(seed.title).setValue(seed);
        Toast.makeText(this, seed.toString(), Toast.LENGTH_SHORT).show();
        finish();
    }
}
