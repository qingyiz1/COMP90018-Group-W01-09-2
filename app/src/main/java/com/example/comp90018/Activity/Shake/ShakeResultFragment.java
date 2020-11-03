package com.example.comp90018.Activity.Shake;

import android.os.Bundle;

//import androidx.fragment.app.Fragment;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.comp90018.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShakeResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShakeResultFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView information;
    ImageButton image;
    View view;


    public ShakeResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShakeResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShakeResultFragment newInstance(String param1, String param2) {
        ShakeResultFragment fragment = new ShakeResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shake_result, container, false);
        information=view.findViewById(R.id.shakePersonInformation);
        image=view.findViewById(R.id.shakeImageButton);
        //TODO: 数据库传头像，个人信息
        information.setText("name, age, dog, female");
        image.setImageDrawable(getResources().getDrawable(R.drawable.touxiang));

        return inflater.inflate(R.layout.fragment_shake_result, container, false);
    }

    @Override
    public void onClick(View view) {
        if(view == image){
            //TODO: 点击头像，跳到UserViewActivity页面
        }
    }
}