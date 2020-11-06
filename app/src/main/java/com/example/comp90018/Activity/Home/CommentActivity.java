package com.example.comp90018.Activity.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.comp90018.Activity.BaseActivity;
import com.example.comp90018.DataModel.Comment;
import com.example.comp90018.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Map;

public class CommentActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "CommentActivity";
    private String postId;
    EditText commentContent;
    ListView comments;
    Button cancelBtn, commentBtn;
    ArrayList<Comment>commentData = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        postId = getIntent().getExtras().getString("POST_ID");
        getComments();

        commentContent = findViewById(R.id.commentEditText);
        comments = findViewById(R.id.commentContents);
        cancelBtn = findViewById(R.id.comment_button_cancel);
        commentBtn = findViewById(R.id.button_comment);

        cancelBtn.setOnClickListener(this);
        commentBtn.setOnClickListener(this);

    }

    private void getComments(){
        db.collection("posts").document(postId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    ArrayList<Map<String,String>> commentTemp = (ArrayList<Map<String, String>>) snapshot.getData().get("comments");
                    final ArrayList<Comment> commentData = new ArrayList<>();
                    for(final Map<String,String> comment: commentTemp){
                        db.collection("users").document(comment.get("author")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                        String nickname = document.getData().get("nickName").toString();
                                        commentData.add(new Comment(nickname,comment.get("content")));
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                    comments.setAdapter(new ArrayAdapter<Comment>(CommentActivity.this, R.layout.comments, R.id.comments_comment, commentData));
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

    }

    private void updateComments(){
        db.collection("posts").document(postId)
                .update("comments", FieldValue.arrayUnion(new Comment(mAuth.getUid(),commentContent.getText().toString())))
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        new AlertDialog.Builder(CommentActivity.this)
                                .setTitle("Success!")
                                .setMessage("Successfully posted!")
                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Direct to Homepage
                                        commentContent.setText("");
                                    }
                                })
                                // A null listener allows the button to dismiss the dialog and take no further action.
                                //.setNegativeButton(android.R.string.no, null)
                                .setIcon(R.drawable.ic_create_success)
                                .show();
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if(view == commentBtn){
            updateComments();
        }else if(view == cancelBtn){
            onBackPressed();
        }
    }
}