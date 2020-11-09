package com.example.comp90018.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class BaseActivity extends AppCompatActivity {

    // [START declare_database_ref]
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();
    // [END declare_database_ref]
    protected FirebaseAuth mAuth;
    protected StorageReference mStorageRef;
    protected StorageReference mStorageImagesRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Initialize Firebase Storage
        mStorageRef = FirebaseStorage.getInstance().getReference();
        // image storage
        mStorageImagesRef = mStorageRef.child("images");
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
