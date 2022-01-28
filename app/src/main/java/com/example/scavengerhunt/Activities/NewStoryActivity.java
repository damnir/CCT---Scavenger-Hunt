package com.example.scavengerhunt.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.scavengerhunt.Entities.Action;
import com.example.scavengerhunt.Entities.Scavenger;
import com.example.scavengerhunt.Entities.Session;
import com.example.scavengerhunt.Entities.Story;
import com.example.scavengerhunt.Entities.User;
import com.example.scavengerhunt.Firebase.Database;
import com.example.scavengerhunt.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

public class NewStoryActivity extends AppCompatActivity {

    private static final  int GALLERY_REQUEST =1;

    private static final int CAMERA_REQUEST_CODE=1;

    private Uri mImageUri = null;

    private ImageView imageView;
    private EditText title;
    private EditText description;

    FirebaseStorage storage;
    StorageReference storageReference;
    Uri storageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_story);
        imageView = findViewById(R.id.newstory_imageview);
        title = findViewById(R.id.newstory_title);
        description = findViewById(R.id.newstory_description);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
        storage = FirebaseStorage.getInstance();
        //storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mImageUri = getImageUri(this, photo);

            android.util.Log.d("CAMERA", "uri: " + mImageUri);
            try{
                //android.util.Log.d("IMAGE", "URI: " + log.getImage());
                Picasso.get().load(mImageUri).into(imageView);
                //Picasso.get().load("https://i.imgur.com/DvpvklR.png").into(image);
                //Picasso.get().load(R.drawable.instagram_icon_969).into(image);
            }catch (NullPointerException e){
                android.util.Log.d("IMAGE", "Exception: " + e);
            };
            //mImageUri = data.getData();
            //android.util.Log.d("CAMERA", "uri var: " + data.getData());

        }

    }

    private Uri getImageUri(Context applicationContext, Bitmap photo) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(applicationContext.getContentResolver(), photo, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    /*
    public void onNewStoryClick(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }*/

    public void onDoneClick(View v) {
        Story story = new Story();
        story.setName(title.getText().toString());
        story.setDescription(description.getText().toString());
        //story.setUrl(mImageUri.toString());
        story.setLat(Scavenger.getInstance().getaLat());
        story.setLng(Scavenger.getInstance().getLng());
        Log.d("STORY", "lat: " + story.getLat() + " Long: " + story.getLng());
        story.setStamp("New Story Fragment");

        FirebaseStorage str = FirebaseStorage.getInstance();
        StorageReference storageRef = str.getReference();

        Uri uri = Uri.fromFile(new File(getRealPathFromURI(mImageUri)));

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            story.getName()+".pjg");

            ref.putFile(uri)
                    .addOnSuccessListener(
                            taskSnapshot -> {
                                progressDialog.dismiss();

                                Toast
                                        .makeText(NewStoryActivity.this,
                                                "Story Uploaded!!",
                                                Toast.LENGTH_SHORT)
                                        .show();
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        storageUri = uri;
                                        story.setUrl(uri.toString());
                                        Session.getInstance().addStory(story);
                                        com.example.scavengerhunt.Entities.Log log = new com.example.scavengerhunt.Entities.Log();
                                        log.setImage(uri.toString());
                                        log.setLabel(story.getName());
                                        log.setStamp("Story Fragment");
                                        log.setDescription(story.getDescription());
                                        log.setType("story");

                                        Action action = new Action();
                                        action.setType("story");
                                        Database.getInstance().newAction(action);
                                        Session.getInstance().addLog(log);
                                        Database.getInstance().addLog();
                                        Database.getInstance().updateStories();
                                        finish();
                                    }
                                });
                            })

        .addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast
                    .makeText(NewStoryActivity.this,
                            "Failed " + e.getMessage(),
                            Toast.LENGTH_SHORT)
                    .show();
        })
        .addOnProgressListener(
                taskSnapshot -> {
                    double progress
                            = (100.0
                            * taskSnapshot.getBytesTransferred()
                            / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage(
                            "Uploaded "
                                    + (int)progress + "%");
                });



    }

}