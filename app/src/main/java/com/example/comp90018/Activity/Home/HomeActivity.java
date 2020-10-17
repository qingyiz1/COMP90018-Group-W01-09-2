package com.example.comp90018.Activity.Home;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.comp90018.R;


public class HomeActivity extends FragmentActivity implements View.OnClickListener {
    private RelativeLayout main_body;
    private ImageButton button_home;
    private ImageButton button_search;
    private ImageButton button_map;
    private ImageButton button_shake;
    private ImageButton button_profile;
    private LinearLayout nav_bottons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        setMain();
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
            case 1://同理如上
                button_search.setBackgroundColor(Color.parseColor("#fafaaf"));
                //其他的文本颜色不变
                button_home.setBackgroundColor(Color.parseColor("#ffffff"));
                button_map.setBackgroundColor(Color.parseColor("#ffffff"));
                button_shake.setBackgroundColor(Color.parseColor("#ffffff"));
                button_profile.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case 2://同理如上
                button_map.setBackgroundColor(Color.parseColor("#fafaaf"));
                //其他的文本颜色不变
                button_search.setBackgroundColor(Color.parseColor("#ffffff"));
                button_home.setBackgroundColor(Color.parseColor("#ffffff"));
                button_shake.setBackgroundColor(Color.parseColor("#ffffff"));
                button_profile.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case 3://同理如上
                button_shake.setBackgroundColor(Color.parseColor("#fafaaf"));
                //其他的文本颜色不变
                button_search.setBackgroundColor(Color.parseColor("#ffffff"));
                button_home.setBackgroundColor(Color.parseColor("#ffffff"));
                button_map.setBackgroundColor(Color.parseColor("#ffffff"));
                button_profile.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case 4://同理如上
                button_profile.setBackgroundColor(Color.parseColor("#fafaaf"));
                //其他的文本颜色不变
                button_search.setBackgroundColor(Color.parseColor("#ffffff"));
                button_home.setBackgroundColor(Color.parseColor("#ffffff"));
                button_shake.setBackgroundColor(Color.parseColor("#ffffff"));
                button_map.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
        }
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
            case R.id.button_map://////////////////////////////////////////////////////输入fragment文件名字/////////////
                getSupportFragmentManager().beginTransaction().replace(R.id.main_body,new Fragement3()).commit();
                setSelectStatus(2);
                break;
            case R.id.button_shake://////////////////////////////////////////////////////输入fragment文件名字/////////////
                getSupportFragmentManager().beginTransaction().replace(R.id.main_body,new Fragement3()).commit();
                setSelectStatus(3);
                break;
            case R.id.button_profile://////////////////////////////////////////////////////输入fragment文件名字/////////////
                getSupportFragmentManager().beginTransaction().replace(R.id.main_body,new Fragement3()).commit();
                setSelectStatus(4);
                break;
        }
    }

    private void setMain() {
        this.getSupportFragmentManager().beginTransaction().add(R.id.main_body,new HomePageFragment()).commit();
        setSelectStatus(0);
    }
}