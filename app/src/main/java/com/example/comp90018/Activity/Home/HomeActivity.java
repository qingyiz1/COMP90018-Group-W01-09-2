package com.example.comp90018.Activity.Home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.comp90018.Activity.Shake.ShakeActivity;
import com.example.comp90018.Activity.fragments.ProfileFragment;
import com.example.comp90018.DataModel.UserModel;
import com.example.comp90018.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class HomeActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "HomeActivity";
    private RelativeLayout main_body;
    private ImageButton button_home;
    private ImageButton button_search;
    private ImageButton button_map;
    private ImageButton button_shake;
    private ImageButton button_profile;
    private LinearLayout nav_bottons;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private UserModel user;

    public UserModel getUser(){
        return this.user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserdata();
        setContentView(R.layout.activity_home);
        initView();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                if(getIntent().getExtras().getBoolean("backToProfile")) {
                    backToProfile();
                }
            } else{
                setMain();
            }
        }

    }

    private void initView(){
        //底部导航栏
        main_body=findViewById(R.id.main_body);
        button_home=findViewById(R.id.button_home);
        button_search=findViewById(R.id.button_search);
        button_map=findViewById(R.id.button_map);
        button_shake=findViewById(R.id.button_shake);
        button_profile=findViewById(R.id.button_profile);

        nav_bottons=findViewById(R.id.nav_bottons);

        button_home.setOnClickListener(this);
        button_search.setOnClickListener(this);
        button_map.setOnClickListener(this);
        button_shake.setOnClickListener(this);
        button_profile.setOnClickListener(this);
        //技巧
        //tv_back.setVisibility(View.GONE);
        //title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
    }

    private void setSelectStatus(int index) {
        switch (index){
            case 0:
                //图片点击选择变换图片，颜色的改变，其他变为原来的颜色，并保持原有的图片
                button_home.setBackgroundColor(Color.parseColor("#fafaaf"));
                //其他的文本颜色不变
                button_search.setBackgroundColor(Color.parseColor("#ffffff"));
                button_map.setBackgroundColor(Color.parseColor("#ffffff"));
                button_shake.setBackgroundColor(Color.parseColor("#ffffff"));
                button_profile.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case 1:
                button_search.setBackgroundColor(Color.parseColor("#fafaaf"));

                button_home.setBackgroundColor(Color.parseColor("#ffffff"));
                button_map.setBackgroundColor(Color.parseColor("#ffffff"));
                button_shake.setBackgroundColor(Color.parseColor("#ffffff"));
                button_profile.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case 2:
                button_map.setBackgroundColor(Color.parseColor("#fafaaf"));

                button_search.setBackgroundColor(Color.parseColor("#ffffff"));
                button_home.setBackgroundColor(Color.parseColor("#ffffff"));
                button_shake.setBackgroundColor(Color.parseColor("#ffffff"));
                button_profile.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case 3:
                button_shake.setBackgroundColor(Color.parseColor("#fafaaf"));

                button_search.setBackgroundColor(Color.parseColor("#ffffff"));
                button_home.setBackgroundColor(Color.parseColor("#ffffff"));
                button_map.setBackgroundColor(Color.parseColor("#ffffff"));
                button_profile.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case 4:
                button_profile.setBackgroundColor(Color.parseColor("#fafaaf"));

                button_search.setBackgroundColor(Color.parseColor("#ffffff"));
                button_home.setBackgroundColor(Color.parseColor("#ffffff"));
                button_shake.setBackgroundColor(Color.parseColor("#ffffff"));
                button_map.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
        }
    }


    private void getUserdata(){
        DocumentReference docRef = db.collection("users").document(mAuth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        user = new UserModel(document.getData().get("nickName").toString(),document.getData().get("sex").toString(),
                                document.getData().get("species").toString(),document.getData().get("age").toString(),document.getData().get("bio").toString());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_body,new HomePageFragment()).commit();
                setSelectStatus(0);
                break;
            case R.id.button_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_body,new SearchFragment()).commit();
                setSelectStatus(1);
                break;
//            case R.id.button_map://////////////////////////////////////////////////////输入fragment或者activity文件名字/////////////
//                //fragment
//                getSupportFragmentManager().beginTransaction().replace(R.id.main_body,new Fragement3()).commit();
//                setSelectStatus(2);
//                //activity
//                Intent intent = new Intent(HomeActivity.this, 名字.class)
//                startActivity(intent);
//                break;
            case R.id.button_shake://////////////////////////////////////////////////////输入fragment或者activity文件名字/////////////
//                //fragment
//                getSupportFragmentManager().beginTransaction().replace(R.id.main_body,new Fragement3()).commit();
//                setSelectStatus(3);
                  //activity
                Intent intent = new Intent(HomeActivity.this, ShakeActivity.class);
                startActivity(intent);
                break;
            case R.id.button_profile:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment fragment = ProfileFragment.newInstance(user);
                ft.replace(R.id.main_body, fragment);
                ft.commit();
                setSelectStatus(4);
                break;
        }
    }

    private void setMain() {
        this.getSupportFragmentManager().beginTransaction().add(R.id.main_body,new HomePageFragment()).commit();
        setSelectStatus(0);
    }

    public void backToProfile(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = ProfileFragment.newInstance(user);
        ft.replace(R.id.main_body, fragment);
        ft.commit();
        setSelectStatus(4);
    }
}