package com.example.comp90018.DataModel;

// [START blog_user_class]

import android.util.Log;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserModel implements Serializable {

    public String birthday,email,nickName,password,sex, species,age, bio,uid;
    protected static final String TAG = "UserModel";
    public Timestamp dateCreated;

    public UserModel() {
        // Default constructor required for calls to DataSnapshot.getValue(UserModel.class)
    }

    public UserModel(String nickName, String species, String age, String sex, String bio){
        this.nickName = nickName;
        this.sex = sex;
        this.species = species;
        this.age = age;
        this.bio = bio;
    }

    public UserModel(String email, String password, String nickName,Timestamp dateCreated){
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.dateCreated = dateCreated;
    }

//    public void getNickname() {
//
//        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                        nickName =document.getString("nickName");
//                        setNickName(nickName);
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//
//
//            }
//
//        });
//
//    }

    public Map<String, Object> toMap() {
        // Create a new user with a first and last name
        Map<String, Object> newUser = new HashMap<>();
        if(this.birthday != null){
            newUser.put("birthday", this.birthday);
        }
        if(this.birthday != null){
            newUser.put("email", this.email);
        }
        if(this.birthday != null){
            newUser.put("nickName", this.nickName);
        }
        if(this.birthday != null){
            newUser.put("dateCreated", this.dateCreated);
        }
        if(this.birthday != null){
            newUser.put("password", this.password);
        }
        if(this.sex != null){
            newUser.put("sex",this.sex);
        }
        if(this.species != null){
            newUser.put("species",this.species);
        }
        if(this.age != null){
            newUser.put("age",this.age);
        }
        if(this.bio != null){
            newUser.put("bio",this.bio);
        }
        return newUser;
    }




    public void setNickName(String nickName){
        this.nickName = nickName;
        //Log.d(TAG,nickName);
    }


    public String getUid() {
        if(this.uid == null){
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
// [END blog_user_class]