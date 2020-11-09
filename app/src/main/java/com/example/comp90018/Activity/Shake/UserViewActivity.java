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
        followOrNot(mAuth.getUid(),uid);
        followButton.setOnClickListener(this);

        getData();
    }

    private void followOrNot(final String mAuth, final String uid) {
        db.collection("users").whereEqualTo("uid", mAuth)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        for (QueryDocumentSnapshot doc : value) {
                            final ArrayList<String> followingpeople = (ArrayList<String>) doc.getData().get("following");

                            for (String user : followingpeople) {
                                if (uid.equals(user)) {
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

    private void getData(){
        db.collection("posts").whereEqualTo("authorUid", uid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        posts.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            ArrayList<Map<String,String>> commentTemp = (ArrayList<Map<String, String>>) doc.getData().get("comments");
                            final ArrayList<Comment> comments = new ArrayList<>();
                            final ArrayList<String> likes = new ArrayList<>();

                            for(final Map<String,String> comment: commentTemp){
                                db.collection("users").document(comment.get("author")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                String nickname = document.getData().get("nickName").toString();
                                                comments.add(new Comment(nickname,comment.get("content")));
                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });
                            }
                            ArrayList<String> tempLikes = (ArrayList<String>) doc.getData().get("likes");
                            for(String user : tempLikes){
                                db.collection("users").document(user).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                String nickname = document.getData().get("nickName").toString();
                                                likes.add(nickname);
                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });
                            }

                            Post post = new Post(doc.getData().get("authorUid").toString(),doc.getData().get("authorName").toString(),doc.getData().get("id").toString(),
                                    doc.getData().get("content").toString(),comments,likes, (Timestamp) doc.getData().get("dateCreated"));
                            posts.add(post);
                        }

                        db.collection("users").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                        user = document.toObject(UserModel.class);
                                        Log.d(TAG, "onComplete: "+user.nickName);
                                        int index = browser_list.getFirstVisiblePosition();
                                        View v = browser_list.getChildAt(0);
                                        int top = (v == null) ? 0 : (v.getTop() - browser_list.getPaddingTop());
                                        homepageAdapter = new HomePageAdapter(getBaseContext(),posts,user);
                                        browser_list.setAdapter(homepageAdapter);
                                        browser_list.setSelectionFromTop(index, top);
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });

                    }});
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