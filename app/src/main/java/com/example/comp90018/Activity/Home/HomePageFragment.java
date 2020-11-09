package com.example.comp90018.Activity.Home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.comp90018.DataModel.Comment;
import com.example.comp90018.DataModel.Post;
import com.example.comp90018.DataModel.UserModel;
import com.example.comp90018.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

public class HomePageFragment extends Fragment{
    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "HomePageFragment";
    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener listener;
    private static ArrayList<Post> posts = new ArrayList<>();
    private TextView text_home;
    private ImageButton post;
    private ListView mainListView;
    private HomePageAdapter homepageAdapter;
    UserModel user;

    protected FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    // image storage
    private StorageReference mStorageImagesRef = FirebaseStorage.getInstance().getReference().child("images");

    public HomePageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        //import adapter
        mainListView = (ListView) view.findViewById(R.id.browseListView);
        text_home = (TextView) view.findViewById(R.id.text_home);

        post = (ImageButton) view.findViewById(R.id.add_post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to post activity;
                Intent intent = new Intent(getActivity(), PostActivity.class);
                intent.putExtra("NICKNAME", user.nickName);
                startActivity(intent);
            }
        });
        getData();
        return view;
    }



    private void getData(){

        db.collection("posts")
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
                                        int index = mainListView.getFirstVisiblePosition();
                                        View v = mainListView.getChildAt(0);
                                        int top = (v == null) ? 0 : (v.getTop() - mainListView.getPaddingTop());
                                        homepageAdapter = new HomePageAdapter(getActivity(),posts,user);
                                        mainListView.setAdapter(homepageAdapter);
                                        mainListView.setSelectionFromTop(index, top);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    //do load feeds when view is visible to user
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        }
        else {  }
    }

    public interface OnFragmentInteractionListener {

    }

}