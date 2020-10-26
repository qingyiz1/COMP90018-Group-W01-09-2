package com.example.comp90018.Activity.Home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp90018.R;

import java.util.ArrayList;

public class HomePageFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private double latitudeCurrent;
    private double longitudeCurrent;
    private OnFragmentInteractionListener listener;
    private static ArrayList<Feed> feeds_array;
    private static ArrayList<Feed> new_feeds_array;
    private TextView text_home;
    private ImageButton post;
    private ListView mainListView;
    private HomePageAdapter homepageAdapter;
    private String [][] users = {{"Joyce", "Good Day", "This is the content."},{"Ann", "Bad Day", "I dont wanna talk."}};
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    public static HomePageFragment newInstance(String param1, String param2) {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HomePageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        //getting GPS location
        GPSTracker gpsTracker = new GPSTracker(this.getActivity());
        // check if GPS enabled
        if (gpsTracker.getIsGPSTrackingEnabled()) {
            latitudeCurrent = gpsTracker.latitude;
            longitudeCurrent = gpsTracker.longitude;
            System.out.println("Location: " + latitudeCurrent + ", " + longitudeCurrent);
        }
        else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }
        //loadFeeds();
        //import adapter
        mainListView = (ListView) view.findViewById(R.id.browseListView);
        homepageAdapter = new HomePageAdapter(getActivity(),getData());
        mainListView.setAdapter(homepageAdapter);
        //loadFeeds();
        homepageAdapter.setFeed_array(getData());
        homepageAdapter.notifyDataSetChanged();
        text_home = (TextView) view.findViewById(R.id.text_home);

        post = (ImageButton) view.findViewById(R.id.add_post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to post activity;
                Intent intent = new Intent(getActivity(), Post.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    //do load feeds when view is visible to user
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        }
        else {  }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (listener != null) {
            listener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    //main requesting data method
//    public void loadFeeds() {
//        String request_url = "https://api.instagram.com/v1/users/self/feed?access_token=25846960.1fb234f.1c7c1f3a4843498f88d0f559ff690eb2";
//        //DEBUG
//        System.out.println("Requesting from: " + request_url);
//        //create a feed array list
//        feeds_array = new ArrayList<>();
//        final Feed feedObj = new Feed();
//        if (homepageAdapter != null) {
//            homepageAdapter.notifyDataSetChanged();
//        }
//    }

    public ArrayList<Feed> getData(){
        return feeds_array;
    }

}