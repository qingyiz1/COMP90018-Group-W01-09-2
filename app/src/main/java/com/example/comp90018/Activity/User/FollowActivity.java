package com.example.comp90018.Activity.User;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.comp90018.Activity.Home.CommolySearchView;
import com.example.comp90018.Activity.Home.SearchAdapter;
import com.example.comp90018.Activity.Shake.UserViewActivity;
import com.example.comp90018.DataModel.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import com.example.comp90018.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FollowActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<UserModel> users = new ArrayList<>();
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final String TAG = "FollowersActivity";
    private SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        listView = (ListView) findViewById(R.id.followingperson);
        getData();

    }

    private void getData() {

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
                            final ArrayList<String> followingpeople = (ArrayList<String>) doc.getData().get("following");

                            for (String user : followingpeople) {

                                db.collection("users").whereEqualTo("uid", user)
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value,
                                                                @Nullable FirebaseFirestoreException e) {
                                                if (e != null) {
                                                    Log.w(TAG, "Listen failed.", e);
                                                    return;
                                                }
//                                                users.clear();
                                                for (QueryDocumentSnapshot doc : value) {
                                                    UserModel user = new UserModel(doc.get("uid").toString(),doc.get("nickName").toString(),doc.get("species").toString());
                                                    users.add(user);
                                                }
                                                searchAdapter = new SearchAdapter(getBaseContext(), users);
                                                listView.setAdapter(searchAdapter);

                                            }});
                            }
                        }
                    }
                });
    }



}