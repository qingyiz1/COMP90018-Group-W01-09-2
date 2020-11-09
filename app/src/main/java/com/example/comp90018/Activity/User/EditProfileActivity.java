package com.example.comp90018.Activity.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp90018.Activity.BaseActivity;
import com.example.comp90018.Activity.Home.HomeActivity;
import com.example.comp90018.Activity.Home.HomePageFragment;
import com.example.comp90018.Activity.fragments.ProfileFragment;
import com.example.comp90018.DataModel.UserModel;
import com.example.comp90018.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;

public class EditProfileActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "EditProfileActivity";
    private static final int PICK_IMAGE_REQUEST = 234;

    ImageView close, image_profile;
    MaterialEditText nickname, species, age,sex,bio;
    Button chooseBtn,saveBtn;
    FirebaseUser firebaseUser;
    private Uri filePath;
    private UploadTask uploadTask;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        image_profile = findViewById(R.id.image_profile);
        close = findViewById(R.id.close);
        saveBtn = findViewById(R.id.save);
        chooseBtn = findViewById(R.id.chooseBtn);

        nickname = findViewById(R.id.nickname);
        species = findViewById(R.id.species);
        age = findViewById(R.id.age);
        sex = findViewById(R.id.sex);
        bio = findViewById(R.id.bio);

        close.setOnClickListener(this);
        chooseBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        getUserdata();
    }

    private void getUserdata(){
        DocumentReference docRef = db.collection("users").document(mAuth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        UserModel user = new UserModel();
                        if(document.getData().get("nickName") != null){
                            user.nickName = document.getData().get("nickName").toString();
                            nickname.setText(user.nickName);
                        }
                        if(document.getData().get("sex") != null){
                            user.sex = document.getData().get("sex").toString();
                            sex.setText(user.sex);
                        }
                        if(document.getData().get("species") != null){
                            user.species = document.getData().get("species").toString();
                            species.setText(user.species);
                        }
                        if(document.getData().get("age") != null){
                            user.age = document.getData().get("age").toString();
                            age.setText(user.age);
                        }
                        if(document.getData().get("bio") != null){
                            user.bio = document.getData().get("bio").toString();
                            bio.setText(user.bio);
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void updateDatabase(){

        UserModel user = new UserModel(nickname.getText().toString(),species.getText().toString(),age.getText().toString(),sex.getText().toString(),bio.getText().toString());
        db.collection("users").document(mAuth.getUid())
                .update(user.toMap())
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
    }

    private void uploadImg(){
        if(filePath!=null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference uploadRef = mStorageImagesRef.child(mAuth.getUid());
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

                    new AlertDialog.Builder(EditProfileActivity.this)
                            .setTitle("Success!")
                            .setMessage("Successfully updated Profile!")
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
        }else{
            new AlertDialog.Builder(EditProfileActivity.this)
                    .setTitle("Success!")
                    .setMessage("Successfully updated Profile!")
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
                image_profile.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public void onClick(View view){
        if(view == chooseBtn){
            // open file chooser
            chooseFile();
        }else if(view == saveBtn){
            updateDatabase();
        }else if(view == close){
            onBackPressed();
        }
    }
}