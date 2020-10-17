package com.example.comp90018.Activity.Home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.comp90018.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class HomePageFragment extends Fragment {
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
    private ListView browseListView;
    private HomePageAdapter homepageAdapter;
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
        loadFeeds();
        browseListView = (ListView) view.findViewById(R.id.browseListView);
        homepageAdapter = new HomePageAdapter(getActivity(),getData());
        browseListView.setAdapter(homepageAdapter);
        loadFeeds();
        homepageAdapter.setFeed_array(getData());
        homepageAdapter.notifyDataSetChanged();

        text_home = (TextView) view.findViewById(R.id.text_home);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        post = (ImageButton) getActivity().findViewById(R.id.add_post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to post activity;
                Intent intent = new Intent(getActivity(), Post.class);
                getActivity().startActivity(intent);
            }
        });
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
    public void loadFeeds() {
        String request_url = "https://api.instagram.com/v1/users/self/feed?access_token=25846960.1fb234f.1c7c1f3a4843498f88d0f559ff690eb2";
        //DEBUG
        System.out.println("Requesting from: " + request_url);
        //create a feed array list
        feeds_array = new ArrayList<>();
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, request_url, (String)null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //get the feed array
                            JSONArray array = response.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                //create one feed obj
                                final Feed feedObj = new Feed();
                                //get one feed
                                JSONObject oneFeed = array.getJSONObject(i);
                                //get the location block
                                if (!oneFeed.isNull("location")) {
                                    JSONObject locationJSON = oneFeed.getJSONObject("location");
                                    //get the location string
                                    if (locationJSON != null) {
                                        String location = locationJSON.getString("name");
                                        feedObj.setLocation(location);
                                        double longitude = locationJSON.getDouble("longitude");
                                        feedObj.setLongitude(longitude);
                                        double latitude = locationJSON.getDouble("latitude");
                                        feedObj.setLatitude(latitude);
                                        double distance = Math.pow(Math.pow((latitude - latitudeCurrent),2) + Math.pow((longitude - longitudeCurrent),2),0.5);
                                        feedObj.setDistance(distance);
                                        //DEBUG
                                        System.out.println("FEED: location = " + distance);
                                    }
                                }else{
                                    feedObj.setLocation("");
                                    feedObj.setDistance(Math.pow((Math.pow(180.0, 2) + Math.pow(360.0, 2)), 0.5));
                                }
                                //get caption block
                                if (!oneFeed.isNull("caption")) {
                                    JSONObject captionJSON = oneFeed.getJSONObject("caption");
                                    //get the caption string
                                    String caption = captionJSON.getString("text");
                                    feedObj.setCaption(caption);
                                }
                                //get the comment block
                                JSONObject commentsJSON = oneFeed.getJSONObject("comments");
                                //get comment counts
                                int commentsCount = commentsJSON.getInt("count");
                                //get comment content
                                if (commentsCount > 0){
                                    ArrayList<String> comments = new ArrayList<>();
                                    //get comment data
                                    JSONArray commentArray = commentsJSON.getJSONArray("data");
                                    for (int j = 0; j < commentArray.length(); j++){
                                        JSONObject oneComment = commentArray.getJSONObject(j);
                                        //get comment text
                                        String commentText = oneComment.getString("text");
                                        JSONObject commentFrom = oneComment.getJSONObject("from");
                                        String commentName = commentFrom.getString("username");
                                        String comment = commentName + ": " + commentText + "\n";
                                        //DEBUG
                                        System.out.println("FEED: comment = " + comment);
                                        comments.add(comment);
                                    }
                                    feedObj.setComment(comments);
                                } else {
                                    feedObj.setComment(null);
                                }
                                //get the likes block
                                JSONObject likesJSON = oneFeed.getJSONObject("likes");
                                //get likes count
                                int likesCount = likesJSON.getInt("count");
                                //get likes content
                                if (likesCount > 0) {
                                    ArrayList<String > likes = new ArrayList<>();
                                    if (likesCount <= 4) {
                                        //get likes data
                                        JSONArray likeArray = likesJSON.getJSONArray("data");
                                        for (int k = 0; k < likeArray.length(); k++) {
                                            JSONObject oneLike = likeArray.getJSONObject(k);
                                            //get like name
                                            String likeName = oneLike.getString("username");
                                            likes.add(likeName);
                                            //DEBUG
                                            System.out.println("FEED: like = " + likeName);
                                        }
                                    }else {
                                        String likedString = Integer.toString(likesCount) + " likes";
                                        likes.add(likedString);
                                    }
                                    feedObj.setLike(likes);

                                } else {
                                    feedObj.setLike(null);
                                }
                                //get user like bool
                                Boolean user_has_liked = oneFeed.getBoolean("user_has_liked");
                                feedObj.setUser_has_liked(user_has_liked);
                                //get the image block
                                JSONObject imageJSON = oneFeed.getJSONObject("images");
                                //get the standard resoultion block
                                JSONObject standardResolution = imageJSON.getJSONObject("standard_resolution");
                                //get the image url
                                String imageURL = standardResolution.getString("url");
                                //DEBUG
                                System.out.println("FEED: image = " + imageURL);
                                feedObj.setPhotoURL(imageURL);
                                if(homepageAdapter != null) {
                                    homepageAdapter.notifyDataSetChanged();
                                }
                                //fetch the image
                                ImageRequest imgRequest = new ImageRequest(imageURL, new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap response) {
                                        //do something with the bitmap
                                        feedObj.setPhoto(response);
                                        if(homepageAdapter != null) {
                                            homepageAdapter.notifyDataSetChanged();
                                        }
                                    }
                                },0,0, ImageView.ScaleType.FIT_XY, Bitmap.Config.RGB_565,
                                        new Response.ErrorListener(){
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                error.printStackTrace();
                                            }

                                        });
                                if (imgRequest != null) {
                                    Volley.newRequestQueue(getActivity()).add(imgRequest);
                                }
                                //get the media id
                                String mediaID = oneFeed.getString("id");
                                feedObj.setMediaID(mediaID);
                                //DEBUG
                                System.out.println("FEED: id = " + mediaID);
                                //get user name
                                JSONObject userJSON = oneFeed.getJSONObject("user");
                                String userName = userJSON.getString("username");
                                String userProfileImageURL = userJSON.getString("profile_picture");
                                feedObj.setDisplayName(userName);
                                //DEBUG
                                System.out.println("FEED: name = " + userName);
                                ImageRequest profileImgRequest =
                                        new ImageRequest(userProfileImageURL, new Response.Listener<Bitmap>() {
                                            @Override
                                            public void onResponse(Bitmap response) {
                                                //do something with the bitmap
                                                Bitmap croppedBitmap = BitmapStore.getCroppedBitmap(response);
                                                feedObj.setUserProfileImg(croppedBitmap);
                                                if(homepageAdapter != null) {
                                                    homepageAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        },0,0, ImageView.ScaleType.FIT_XY, Bitmap.Config.RGB_565,
                                                new Response.ErrorListener(){
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        error.printStackTrace();
                                                    }

                                                });
                                if (profileImgRequest != null) {
                                    Volley.newRequestQueue(getActivity()).add(profileImgRequest);
                                }


                                //add feed object into arraylist
                                feeds_array.add(feedObj);

                                if(homepageAdapter != null) {
                                    homepageAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Network failure",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        error.printStackTrace();
                        Toast.makeText(getActivity(),
                                "Network failure",
                                Toast.LENGTH_LONG).show();
                    }
                });
        if (jsonRequest != null) {
            Volley.newRequestQueue(getActivity()).add(jsonRequest);
        }
    }

    public ArrayList<Feed> getData(){
        return feeds_array;
    }

}