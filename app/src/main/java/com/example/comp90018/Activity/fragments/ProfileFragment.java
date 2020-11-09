package com.example.comp90018.Activity.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.comp90018.Activity.Home.HomePageAdapter;
import com.example.comp90018.Activity.MainActivity;
import com.example.comp90018.Activity.User.EditProfileActivity;
import com.example.comp90018.Activity.User.FollowersActivity;
import com.example.comp90018.DataModel.Comment;
import com.example.comp90018.DataModel.Post;
import com.example.comp90018.DataModel.UserModel;
import com.example.comp90018.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.comp90018.utils.GlideApp;

import java.util.ArrayList;
import java.util.Map;

public class ProfileFragment extends Fragment implements View.OnClickListener  {


    private static final String TAG = "ProfileFragment";
    View view;
    TextView user_name,user_info,user_description;
    Button followBtn, signOutBtn,editBtn;
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    // image storage
    private StorageReference mStorageImagesRef = FirebaseStorage.getInstance().getReference().child("images");
    private UserModel user = new UserModel();
    String uid;
    private static final String USER_DATA = "user_data";

    ArrayList<Post> posts = new ArrayList<>();
    private HomePageAdapter homepageAdapter;
    private ListView browser_list;

//    public static ProfileFragment newInstance(UserModel userModel) {
//        ProfileFragment fragment = new ProfileFragment();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(USER_DATA, userModel);
//        fragment.setArguments(bundle);
//        return fragment;
//    }

    public ProfileFragment(String uid) {
        // Required empty public constructor
        this.uid = uid;
        Log.d(TAG,uid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getUserdata(this.uid);
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        user_name = view.findViewById(R.id.user_name);
        user_info = view.findViewById(R.id.user_info);
        user_description = view.findViewById(R.id.user_description);

        followBtn = view.findViewById(R.id.follow_button);
        editBtn = view.findViewById(R.id.edit_button);
        signOutBtn = view.findViewById(R.id.sign_out_button);
        editBtn.setOnClickListener(this);
        signOutBtn.setOnClickListener(this);
        followBtn.setOnClickListener(this);


        browser_list=view.findViewById(R.id.browserListView);

        getData();
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view == signOutBtn){
            mAuth.signOut();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }else if(view == editBtn){
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        }else if(view == followBtn){
            Intent intent = new Intent(getActivity(), FollowersActivity.class);
            startActivity(intent);
//            db.collection("users").document(mAuth.getUid()).update("following", FieldValue.arrayUnion(user.uid));
        }
    }

    public void loadWithGlide(View container, int ID) {
        // [START storage_load_with_glide]
        // Reference to an image file in Cloud Storage
        StorageReference profileReference = mStorageImagesRef.child(mAuth.getUid());

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
                    loadWithGlide(view,R.id.profile_avatar);
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    private void getData(){

        db.collection("posts").whereEqualTo("authorUid", uid)
//                .whereEqualTo("state", "CA")
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
                                        homepageAdapter = new HomePageAdapter(getActivity(),posts,user);
                                        browser_list.setAdapter(homepageAdapter);
                                        browser_list.setSelectionFromTop(index, top);
                                        //homepageAdapter.notifyDataSetChanged();
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
}
