package com.example.comp90018.Activity.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.comp90018.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

public class Post extends AppCompatActivity {
    private TextView pagePost;
    private TextView postTitle;
    private EditText textField=null;
    private String postText;
    private TextView photoTitle;
    public ImageView imageview = null;
    private Bitmap rawBitmap = null;
    private Bitmap thumbnail = null;
    private Button btnPost = null;
    private Button btnCancel =null;
    private String filePath = "";
    private final static int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        pagePost = (TextView) findViewById(R.id.page_post);
        postTitle = (TextView) findViewById(R.id.text_post_text);
        textField = (EditText) findViewById(R.id.text_field);
        postText = textField.getText().toString();
        photoTitle = (TextView) findViewById(R.id.text_photo_text);
        imageview = (ImageView) findViewById(R.id.imageView1);

        // get the bitmap from file
        Intent intent = getIntent();
        filePath = intent.getStringExtra("post_img");
        rawBitmap = BitmapFactory.decodeFile(filePath);
        thumbnail = rawBitmap;
        imageview.setImageBitmap(thumbnail);

        //post button
        btnPost = (Button) findViewById(R.id.button_post);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createInstagramIntent(filePath);
            }
        });

        //cancel button
        btnCancel = (Button) findViewById(R.id.button_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
    }


    private void createInstagramIntent(String filePath){
        Intent instagram = new Intent(android.content.Intent.ACTION_SEND);
        instagram.setType("image/*");
        File file = new File(filePath);
        Uri uri = Uri.fromFile(file);
        instagram.putExtra(Intent.EXTRA_STREAM, uri);
        instagram.putExtra(Intent.EXTRA_TEXT, postText);
        instagram.setPackage("com.instagram.android");

        startActivity(Intent.createChooser(instagram, "Share to"));
    }

}