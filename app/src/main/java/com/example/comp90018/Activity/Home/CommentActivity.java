package com.example.comp90018.Activity.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.comp90018.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class CommentActivity extends AppCompatActivity {
    private String commentText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        TextView commentTitle = (TextView) findViewById(R.id.commentTextView);
        EditText comments = (EditText) findViewById(R.id.commentEditText);
        commentText = comments.getText().toString();
        Button cancel = (Button) findViewById(R.id.cancelButton);

        //cancel button
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                onBackPressed();
            }
        });

        //implement the comment requesting
        Button comment = (Button) findViewById(R.id.commentButton);
        comment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                createIntent(commentText);
            }
        });
    }
    private void createIntent(String string){
        Intent intent = new Intent(CommentActivity.this, HomePageFragment.class);
        intent.setType("String");
        intent.putExtra(Intent.EXTRA_TEXT, string);
        startActivity(Intent.createChooser(intent, "Share to"));
    }
}