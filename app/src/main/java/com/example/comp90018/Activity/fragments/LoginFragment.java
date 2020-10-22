package com.example.comp90018.Activity.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.comp90018.Activity.Home.HomeActivity;
import com.example.comp90018.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    View view;
    EditText login_email,login_password;
    Button login_btn;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        login_email = view.findViewById(R.id.login_email);
        login_password = view.findViewById(R.id.login_password);
        login_btn = view.findViewById(R.id.btn_login);
        login_btn.setOnClickListener(this);
        checkUserState();
        return view;
    }

    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Success!")
                                    .setMessage("Successfully logged in")
                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Direct to Homepage if log in successfully
                                            Intent intent= new Intent(getActivity(), HomeActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    })
                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    //.setNegativeButton(android.R.string.no, null)
                                    .setIcon(R.drawable.ic_create_success)
                                    .show();
                        } else {
                            // If sign in fails, display a message to the user.
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Error!")
                                    .setMessage("Authentication failed.")
                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    .setPositiveButton(android.R.string.yes, null)
                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    //.setNegativeButton(android.R.string.no, null)
                                    .setIcon(R.drawable.ic_error)
                                    .show();
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = login_email.getText().toString();
        if (TextUtils.isEmpty(email)) {
            login_email.setError("Required.");
            valid = false;
        } else {
            login_email.setError(null);
        }

        String password = login_password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            login_password.setError("Required.");
            valid = false;
        } else {
            login_password.setError(null);
        }

        return valid;
    }



    private void checkUserState(){
        if(mAuth.getCurrentUser() != null){
            Intent intent= new Intent(getActivity(),HomeActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onClick(View view) {
        if(view == login_btn){
            signIn(login_email.getText().toString(),login_password.getText().toString());
        }
    }
}