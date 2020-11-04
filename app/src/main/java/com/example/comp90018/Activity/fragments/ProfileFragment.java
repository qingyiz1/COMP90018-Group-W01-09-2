package com.example.comp90018.Activity.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.comp90018.Activity.MainActivity;
import com.example.comp90018.Activity.User.EditProfileActivity;
import com.example.comp90018.DataModel.UserModel;
import com.example.comp90018.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.comp90018.utils.GlideApp;

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
        loadWithGlide(view,R.id.profile_avatar);
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
            this.onPause();
        }else if(view == followBtn){
            db.collection("users").document(mAuth.getUid()).update("following", FieldValue.arrayUnion(user.uid));
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
                .into(imageView);
        // [END storage_load_with_glide]
    }


    private void getUserdata(String uid){
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        if(document.getData().get("nickName") != null){
                            user.nickName = document.getData().get("nickName").toString();
                            user_name.setText(user.nickName);
                        }
                        if(document.getData().get("sex") != null && document.getData().get("species") != null && document.getData().get("age") != null){
                            user.sex = document.getData().get("sex").toString();
                            user.species = document.getData().get("species").toString();
                            user.age = document.getData().get("age").toString();
                            user_info.setText(user.sex+" "+user.species+", "+user.age+" years old");
                        }
                        if(document.getData().get("bio") != null){
                            user.bio = document.getData().get("bio").toString();
                            user_description.setText(user.bio);
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
}
