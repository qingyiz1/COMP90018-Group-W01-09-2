package com.example.comp90018.Activity.Shake;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.comp90018.DataModel.UserModel;
import com.example.comp90018.R;
import com.example.comp90018.utils.GlideApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.example.comp90018.Activity.BaseActivity;
import java.util.ArrayList;

public class ShakeResultActivity extends AppCompatActivity{
    TextView information;
    private ImageButton profileImage;

    private static final String TAG = "ShakeResultActivity";
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UserModel user = new UserModel();

    protected StorageReference mStorageImagesRef = FirebaseStorage.getInstance().getReference().child("images");
    protected FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private String uid="0nARU3GtAWh5jXijD8hwtlLMkjc2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_result);


        information=findViewById(R.id.shakePersonInformation);
        profileImage=findViewById(R.id.shakeImageButton);

        //TODO: 数据库传头像，个人信息
        getUserdata(uid);

//        information.setText("name, age, dog, female");
//        profileImage.setImageDrawable(getResources().getDrawable(R.drawable.default_avatar));

//        profileImage.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//                Intent intent = new Intent(ShakeResultActivity.this, UserViewActivity.class);
//                startActivity(intent);
//            }
//        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(ShakeResultActivity.this, UserViewActivity.class);
                intent.putExtra("USER_UID", uid);
                startActivity(intent);
            }
        });

    }

    private void getUserdata(final String uid){
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    if(snapshot.getData().get("nickName") != null){
                        user.nickName = snapshot.getData().get("nickName").toString();
                        information.setText(user.nickName+"\n");
                    }
                    if(snapshot.getData().get("sex") != null && snapshot.getData().get("species") != null && snapshot.getData().get("age") != null){
                        user.sex = snapshot.getData().get("sex").toString();
                        user.species = snapshot.getData().get("species").toString();
                        user.age = snapshot.getData().get("age").toString();
                        information.append(user.sex+" "+user.species+", "+user.age+" years old\n");
                    }
                    if(snapshot.getData().get("bio") != null){
                        user.bio = snapshot.getData().get("bio").toString();
                        information.append(user.bio);
                    }
                    loadWithGlide(profileImage,R.id.shakeImageButton,uid);
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    public void loadWithGlide(ImageButton profileImage, int ID,String userId) {
        // [START storage_load_with_glide]
        // Reference to an image file in Cloud Storage
        StorageReference profileReference = mStorageImagesRef.child(userId);

        // ImageView in your Activity
        ImageButton imageView = findViewById(ID);

        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        GlideApp.with(this /* context */)
                .load(profileReference)
                .placeholder(R.drawable.default_avatar)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
        // [END storage_load_with_glide]
    }

}