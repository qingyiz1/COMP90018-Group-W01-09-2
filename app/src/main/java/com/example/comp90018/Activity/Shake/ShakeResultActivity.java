package com.example.comp90018.Activity.Shake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.comp90018.R;

public class ShakeResultActivity extends AppCompatActivity{
    TextView information;
    ImageButton image;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_result);
        information=findViewById(R.id.shakePersonInformation);
        image=findViewById(R.id.shakeImageButton);
        //TODO: 数据库传头像，个人信息
        information.setText("name, age, dog, female");
        image.setImageDrawable(getResources().getDrawable(R.drawable.touxiang));

        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(ShakeResultActivity.this, UserViewActivity.class);
                startActivity(intent);
            }
        });
    }

}