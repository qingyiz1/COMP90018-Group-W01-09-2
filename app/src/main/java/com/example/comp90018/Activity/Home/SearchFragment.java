package com.example.comp90018.Activity.Home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView searchText;
    private EditText searchInput;
    private Button search;
    private ListView listView;
    private SearchAdapter searchAdapter;
    private ArrayList<Search> users;
    final private int SEARCH_COUNT = 10;
    private OnFragmentInteractionListener listener;
    //ParseUser是Parse Server and Dashboard的功能，
    // ParseUser是用户数据的本地表示形式，可以从Parse云中进行保存和检索user。
    //可改动
//    private ParseUser currentUser = ParseUser.getCurrentUser();
//    private ArrayList<ParseUser> weightedUsers;
//    private ParseUser userToWeight;
//    private int weight;
//    HashMap<ParseUser, Integer> usersMap;

    public SearchFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
//        recommendation();
    }
    /*
        This method queries the data stored in the parse database and passes on the list obtained
        to process the data to assign weights and determine priority. Once the sorted list of users
        is returned, it passes the list to the adapter to display on the list view for search page.
         */
//    public void recommendation(){
//        if(currentUser.get("Gender").toString() != "Blank") {
//            users = new ArrayList<>();
//            ParseQuery<ParseUser> query = ParseUser.getQuery();
//            query.whereNotEqualTo("username", currentUser.getUsername().toString());
//            query.findInBackground(new FindCallback<ParseUser>() {
//                public void done(List<ParseUser> objects, ParseException e) {
//                    if (e == null) {
//                        Log.e("Search", objects.toString());
//                        calculateWeight((ArrayList<ParseUser>) objects);
//                        ArrayList<ParseUser> parseUserDB = weightedUsers;
//                        //pass the data into discovery user array list
//                        for (int i = 0; i < parseUserDB.size(); i++) {
//                            ParseUser tmpUser = parseUserDB.get(i);
//                            String username = tmpUser.getUsername();
//                            String gender = tmpUser.get("Gender").toString();
//                            ParseFile imageFile = (ParseFile) tmpUser.get("Image");
//                            Bitmap profileImage;
//                            if (imageFile != null) {
//                                byte[] bitmapdata = new byte[0];
//                                try {
//                                    bitmapdata = imageFile.getData();
//                                } catch (ParseException eg) {
//                                    eg.printStackTrace();
//                                }
//                                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
//                                profileImage = bitmap;
//                            } else {
//                                Drawable myDrawable = getResources().getDrawable(R.drawable.default_profile_image);
//                                Bitmap defaultImage = ((BitmapDrawable) myDrawable).getBitmap();
//                                profileImage = defaultImage;
//                            }
//                            Search searchUser = new Search();
//                            searchUser.setProfileImage(profileImage);
//                            searchUser.setUserName(username);
//                            searchUser.setGender(gender);
//                            if(!currentUser.getUsername().equals(searchUser.getUserName())) {
//                                users.add(searchUser);
//                            }
//                        }
//                        searchAdapter.setUsers(users);
//                        searchAdapter.notifyDataSetChanged();
//                    } else {
//                        // Something went wrong.
//                        Log.e("Search", "Exception thrown");
//                    }
//                }
//            });
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        search = (Button) view.findViewById(R.id.searchButton);
        searchInput = (EditText) view.findViewById(R.id.editText);
        searchInput.getBackground().setColorFilter(getResources().getColor(R.color.actionbar_background), PorterDuff.Mode.SRC_ATOP);
        searchText = (TextView) view.findViewById(R.id.text_search);
        listView = (ListView) view.findViewById(R.id.searchList);
        //if(users != null) {
        searchAdapter = new SearchAdapter(getActivity(), getData());
        listView.setAdapter(searchAdapter);
        //}

        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final String searchQuery = searchInput.getText().toString();
                if (searchQuery.length() != 0) {
                    String request_url = view.getResources().getString(R.string.instagram_api_url)
                            + view.getResources().getString(R.string.instagram_api_users_method)
                            + "search?access_token="
                            + view.getResources().getString(R.string.instagram_access_token)
                            + "&q="
                            + searchQuery
                            + "&count="
                            + SEARCH_COUNT;
                    System.out.println("Search URL: " + request_url);
                    users = new ArrayList<>();
//                    JsonObjectRequest jsonRequest = new JsonObjectRequest
//                            (Request.Method.GET, request_url, (String) null, new Response.Listener<JSONObject>() {
//                                @Override
//                                public void onResponse(JSONObject response) {
//                                    try {
//                                        JSONArray array = response.getJSONArray("data");
//                                        for (int i = 0; i < array.length(); i++) {
//                                            final Search oneUser = new Search();
//                                            JSONObject oneResult = array.getJSONObject(i);
//                                            String username_result = oneResult.getString("username").toString();
//                                            String fullname_result = oneResult.getString("full_name").toString();
//                                            String userImgURL_result = oneResult.getString("profile_picture").toString().replace("\\", "");
//                                            System.out.println("Search IMAGE: " + userImgURL_result);
//                                            oneUser.setFullName(fullname_result);
//                                            oneUser.setUserName(username_result);
//                                            oneUser.setGender("Secret");
//                                            oneUser.setImgURL(userImgURL_result);
//
//                                            //set the image to profile image
//                                            ImageRequest profileImgRequest =
//                                                    new ImageRequest(userImgURL_result, new Response.Listener<Bitmap>() {
//                                                        @Override
//                                                        public void onResponse(Bitmap response) {
//                                                            //do something with the bitmap
//                                                            oneUser.setProfileImage(response);
//
//                                                            if (searchAdapter != null) {
//                                                                searchAdapter.notifyDataSetChanged();
//                                                            }
//                                                        }
//                                                    }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.RGB_565,
//                                                            new Response.ErrorListener() {
//                                                                @Override
//                                                                public void onErrorResponse(VolleyError error) {
//                                                                    error.printStackTrace();
//                                                                }
//                                                            });
//                                            if (profileImgRequest != null) {
//                                                Volley.newRequestQueue(getActivity()).add(profileImgRequest);
//                                            }
//                                            //add parse user to array list
//                                            users.add(oneUser);
//                                            if (searchAdapter != null) {
//                                                searchAdapter.notifyDataSetChanged();
//                                            }
//                                        }
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }, new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    error.printStackTrace();
//                                    Toast.makeText(getActivity(),
//                                            "Network failure",
//                                            Toast.LENGTH_LONG).show();
//                                }
//                            });
//                    Volley.newRequestQueue(getActivity()).add(jsonRequest);
                    searchAdapter.setUsers(users);
                    if (searchAdapter != null) {
                        searchAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getActivity(),
                            "Input is empty, showing recommended users",
                            Toast.LENGTH_LONG).show();
                    //recommendation();
                    if (searchAdapter != null) {
                        searchAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        return view;
    }

    //verify whether the person has specified a valid gender
    private boolean verifyGender(String userGender){
        if((userGender.equals("Male")) || (userGender.equals("Female"))) {
            return true;
        }
        return false;
    }

    //calculate the weights assigned to Gender
//    private void calculateWeight(ArrayList<ParseUser> listOfUsers){
//        usersMap = new HashMap<>();
//        weightedUsers = new ArrayList<>();
//        for(int i = 0; i< listOfUsers.size();i++) {
//            userToWeight = listOfUsers.get(i);
//            weight = 1;
//            String currentUserGender = currentUser.get("Gender").toString();
//            String recommendedUserGender = userToWeight.get("Gender").toString();
//            boolean currentUserPresent = verifyGender(currentUserGender);
//            boolean weighedUserPresent = verifyGender(recommendedUserGender);
//
//            if (currentUserPresent) {
//                if (weighedUserPresent) {
//                    if (currentUserPresent && weighedUserPresent) {
//                        if (currentUserGender.equals(recommendedUserGender)) {
//                            weight += 3;
//                        }
//                    }
//                }
//            }
//        }
//        sortbyWeights();
//    }

    //sort the Users based on the weights granted to them by verifying their
    // City and gender.
//    private void sortbyWeights(){
//        List<Map.Entry<ParseUser,Integer>> list = new LinkedList<Map.Entry<ParseUser, Integer>>(usersMap.entrySet());
//        Collections.sort(list, new Comparator<Map.Entry<ParseUser, Integer>>() {
//            @Override
//            public int compare(Map.Entry<ParseUser, Integer> lhs, Map.Entry<ParseUser, Integer> rhs) {
//                return (rhs.getValue()).compareTo(lhs.getValue());
//            }
//        });
//        Map<ParseUser,Integer> sortedUserList = new LinkedHashMap<ParseUser, Integer>();
//        for(Map.Entry<ParseUser, Integer> entry : list){
//            sortedUserList.put(entry.getKey(), entry.getValue());
//            weightedUsers.add(entry.getKey());
//        }
//        //Collections.reverse(weightedUsers);
//    }

    public void onButtonPressed(Uri uri) {
        if (listener != null) {
            listener.onFragmentInteraction(uri);
        }
    }

    public ArrayList<Search> getData(){
        return users;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //recommendation();
        }
        else {  }
    }
}