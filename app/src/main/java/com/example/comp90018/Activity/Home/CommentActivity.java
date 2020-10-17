package com.example.comp90018.Activity.Home;

import androidx.appcompat.app.AppCompatActivity;

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

public class CommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        TextView commentTitle = (TextView) findViewById(R.id.commentTextView);
        EditText comments = (EditText) findViewById(R.id.commentEditText);
        String commentText = comments.getText().toString();
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
                //request url
                String url = getResources().getString(R.string.instagram_api_url)
                        + getResources().getString(R.string.instagram_api_media_method)
                        + "id/comments?access_token="
                        + getResources().getString(R.string.instagram_access_token);
                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject jsonResponse = response.getJSONObject("meta");
                                    String errorMsg = jsonResponse.getString("error_message");
                                    String errorType = jsonResponse.getString("error_type");
                                    System.out.println("Comments: " + errorMsg);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                Toast.makeText(getApplicationContext(),
                                        "This request requires scope=comments, but this access token" +
                                                " is not authorized with this scope. Sorry!",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                Volley.newRequestQueue(getApplicationContext()).add(postRequest);
            }
        });
    }
}