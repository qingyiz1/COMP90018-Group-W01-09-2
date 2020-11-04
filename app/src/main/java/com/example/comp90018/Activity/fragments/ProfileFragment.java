package com.example.comp90018.Activity.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.comp90018.Activity.Home.HomeActivity;
import com.example.comp90018.Activity.MainActivity;
import com.example.comp90018.Activity.User.EditProfileActivity;
import com.example.comp90018.DataModel.UserModel;
import com.example.comp90018.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

public class ProfileFragment extends Fragment implements View.OnClickListener  {


    private static final String TAG = "ProfileFragment";
    View view;
    TextView user_name,user_info,user_description;
    Button signOutBtn,editBtn;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private UserModel user;

    private static final String USER_DATA = "user_data";


    public static ProfileFragment newInstance(UserModel userModel) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER_DATA, userModel);
        fragment.setArguments(bundle);

        return fragment;
    }
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        user = (UserModel) getArguments().getSerializable(USER_DATA);
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        user_name = view.findViewById(R.id.user_name);
        user_info = view.findViewById(R.id.user_info);
        user_description = view.findViewById(R.id.user_description);
        user_name.setText(user.nickName);

        editBtn = view.findViewById(R.id.edit_button);
        signOutBtn = view.findViewById(R.id.follow_button);
        editBtn.setOnClickListener(this);
        signOutBtn.setOnClickListener(this);
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
        }
    }



}
