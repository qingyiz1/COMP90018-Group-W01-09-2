package com.example.comp90018.Activity.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp90018.Activity.BaseActivity;
import com.example.comp90018.DataModel.Post;
import com.example.comp90018.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class PostActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "PostActivity";
    private EditText postContent;
    private Button insertBtn, postBtn, cancelBtn;
    public ImageView postPic;
    private static final int PICK_IMAGE_REQUEST = 234;
    Post post;
    UploadTask uploadTask;
    Uri filePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        postContent = findViewById(R.id.post_content);
        postPic = findViewById(R.id.post_picture);

        insertBtn = findViewById(R.id.button_insert);
        postBtn = findViewById(R.id.button_post);
        cancelBtn = findViewById(R.id.button_cancel);

        insertBtn.setOnClickListener(this);
        postBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

    }


    private void submitPost(){
        this.post = new Post(mAuth.getUid(),postContent.getText().toString() ,Timestamp.now());
        post.setId(db.collection("posts").document().getId());
        db.collection("posts").document(post.getId())
                .set(post.toMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        uploadImg();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

        db.collection("users").document(Objects.requireNonNull(mAuth.getUid())).update("posts", FieldValue.arrayUnion(post.getId()));
    }

    private void uploadImg(){
        if(filePath!=null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference uploadRef = mStorageImagesRef.child(post.getId());
            uploadTask = uploadRef.putFile(filePath);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),exception.getMessage(),Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    progressDialog.dismiss();

                    new AlertDialog.Builder(PostActivity.this)
                            .setTitle("Success!")
                            .setMessage("Successfully posted!")
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Direct to Homepage
                                    finish();
                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            //.setNegativeButton(android.R.string.no, null)
                            .setIcon(R.drawable.ic_create_success)
                            .show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100* taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage((int) progress+"% finished");
                }
            });
        }


    }

    private void chooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select an Image"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && data.getData() != null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                postPic.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view){
        if(view == insertBtn){
            // open file chooser
            chooseFile();
        }else if(view == postBtn){
            submitPost();
        }else if(view == cancelBtn){
            onBackPressed();
        }
    }
}