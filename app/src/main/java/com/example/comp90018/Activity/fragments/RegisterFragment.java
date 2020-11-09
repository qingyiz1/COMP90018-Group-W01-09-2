package com.example.comp90018.Activity.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.example.comp90018.Activity.Home.HomeActivity;
import com.example.comp90018.Activity.User.EditProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.comp90018.DataModel.UserModel;
import com.example.comp90018.R;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "RegisterFragment";
    View view;
    Button btn_register;
    EditText register_name,register_email,register_password,register_password_check;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register, container, false);
        register_name = view.findViewById(R.id.register_name);
        register_email = view.findViewById(R.id.register_email);
        register_password = view.findViewById(R.id.register_password);
        register_password_check = view.findViewById(R.id.register_password_check);
        btn_register = view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);

        return view;
    }

    public void updateDatabase(String filePath){
        UserModel newUser = new UserModel(register_email.getText().toString(),register_password.getText().toString(), register_name.getText().toString(),"cat",Timestamp.now());
        db.collection("users").document(filePath)
                .set(newUser);
    }

    protected void createAccount(String email, String password) {
        if (!validateForm()) {
            return;
        }
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            updateDatabase(mAuth.getUid());

                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Success!")
                                    .setMessage("New account Successfully created!")
                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    })
                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    //.setNegativeButton(android.R.string.no, null)
                                    .setIcon(R.drawable.ic_create_success)
                                    .show();

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Error!")
                                    .setMessage(task.getException().toString())
                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, null)
                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    //.setNegativeButton(android.R.string.no, null)
                                    .setIcon(R.drawable.ic_error)
                                    .show();
                        }
                            //updateUI(null);
                    }
                });

        // [END create_user_with_email]
    };


    private boolean validateForm() {
        boolean valid = true;
        Log.d(TAG, "validateForm: ");
        String email = register_email.getText().toString();
        if (TextUtils.isEmpty(email)) {
            register_email.setError("Required.");
            valid = false;
        } else {
            register_email.setError(null);
        }

        String nickName = register_name.getText().toString();
        if (TextUtils.isEmpty(nickName)) {
            register_name.setError("Required.");
            valid = false;
        } else {
            register_name.setError(null);
        }

        String password = register_password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            register_password.setError("Required.");
            valid = false;
        } else if(!register_password.getText().toString().equals(register_password_check.getText().toString())){
            register_password.setError("Password not match.");
            register_password_check.setError("Password not match.");
            valid = false;
        }else{
            register_password.setError(null);
            register_password_check.setError(null);
        }

        return valid;
    }


    @Override
    public void onClick(View view) {
        if (view == btn_register) {
            createAccount(register_email.getText().toString(),register_password.getText().toString());
        }
    }
}