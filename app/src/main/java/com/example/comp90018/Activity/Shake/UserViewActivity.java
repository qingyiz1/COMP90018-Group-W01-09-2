package com.example.comp90018.Activity.Shake;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.comp90018.Activity.BaseActivity;

import com.example.comp90018.Activity.Home.HomePageAdapter;
import com.example.comp90018.DataModel.Comment;
import com.example.comp90018.DataModel.Post;
import com.example.comp90018.DataModel.UserModel;
import com.example.comp90018.R;
import com.example.comp90018.utils.GlideApp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

public class UserViewActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "UserViewActivity";
    private  ListView browser_list;
    private TextView user_name, user_info, user_description;
    private Button followButton;
    private HomePageAdapter homepageAdapter;
    private String uid;
    private UserModel user = new UserModel();
    ArrayList<Post> posts = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userview);
        uid = getIntent().getStringExtra("USER_UID");
        Log.d(TAG, "onCreate: "+uid);
        getUserdata(uid);

        user_name = findViewById(R.id.user_name);
        user_info = findViewById(R.id.user_info);
        user_description = findViewById(R.id.user_description);
        browser_list=findViewById(R.id.browserListView);

        followButton=findViewById(R.id.follow_button);
        followButton.setText("Follow");
        followOrNot();
        followButton.setOnClickListener(this);
    }

    private void followOrNot() {
        db.collection("users").whereEqualTo("uid", mAuth.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        for (QueryDocumentSnapshot doc : value) {
                            final ArrayList<String> followingPeople = (ArrayList<String>) doc.getData().get("following");
                            if(followingPeople != null){
                                if(followingPeople.contains(uid)) {
                                    followButton.setText("Following");
                                }
                            }
                        }
                    }
                });

    }

    private void getUserdata(String uid){
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
                        user_name.setText(user.nickName);
                    }
                    if(snapshot.getData().get("sex") != null && snapshot.getData().get("species") != null && snapshot.getData().get("age") != null){
                        user.sex = snapshot.getData().get("sex").toString();
                        user.species = snapshot.getData().get("species").toString();
                        user.age = snapshot.getData().get("age").toString();
                        user_info.setText(user.sex+" "+user.species+", "+user.age+" years old");
                    }
                    if(snapshot.getData().get("bio") != null){
                        user.bio = snapshot.getData().get("bio").toString();
                        user_description.setText(user.bio);
                    }
                    loadWithGlide(getWindow().getDecorView().getRootView(),R.id.profile_avatar);
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view==followButton){
            if(followButton.getText().equals("Following")){
                db.collection("users").document(mAuth.getUid()).update("following", FieldValue.arrayRemove(uid));
                followButton.setText("Follow");
            }else if(followButton.getText().equals("Follow")){
                if(!uid.equals(mAuth.getUid())){
                    db.collection("users").document(mAuth.getUid()).update("following", FieldValue.arrayUnion(uid));
                    followButton.setText("Following");
                }else{
                    followButton.setText("You can't follow yourself");
                }
            }

        }
    }

    public void loadWithGlide(View container, int ID) {
        // [START storage_load_with_glide]
        // Reference to an image file in Cloud Storage
        StorageReference profileReference = mStorageImagesRef.child(uid);

        // ImageView in your Activity
        ImageView imageView = container.findViewById(ID);

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