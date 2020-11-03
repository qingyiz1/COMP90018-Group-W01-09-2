package com.example.comp90018.Activity.Home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.comp90018.DataModel.Search;
import com.example.comp90018.R;

import java.util.ArrayList;

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
    public void recommendation() {
        users = new ArrayList<>();
        //pass the data into discovery user array list
        byte[] bitmapdata = new byte[0];
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);

        Drawable myDrawable = getResources().getDrawable(R.drawable.default_profile_image);
        Bitmap defaultImage = ((BitmapDrawable) myDrawable).getBitmap();

        Search searchUser = new Search();
        searchAdapter.setUsers(users);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        search = (Button) view.findViewById(R.id.searchButton);
        searchInput = (EditText) view.findViewById(R.id.editText);
        searchInput.getBackground().setColorFilter(getResources().getColor(R.color.actionbar_background), PorterDuff.Mode.SRC_ATOP);
        searchText = (TextView) view.findViewById(R.id.text_search);
        listView = (ListView) view.findViewById(R.id.searchList);
        //import adapter
        searchAdapter = new SearchAdapter(getActivity(), getData());
        listView.setAdapter(searchAdapter);

        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
//                final String searchQuery = searchInput.getText().toString();
//                if (searchQuery.length() != 0) {
//                    String request_url = view.getResources().getString(R.string.instagram_api_url)
//                            + view.getResources().getString(R.string.instagram_api_users_method)
//                            + "search?access_token="
//                            + view.getResources().getString(R.string.instagram_access_token)
//                            + "&q="
//                            + searchQuery
//                            + "&count="
//                            + SEARCH_COUNT;
//                    System.out.println("Search URL: " + request_url);
//                    users = new ArrayList<>();
//                    searchAdapter.setUsers(users);
//                    if (searchAdapter != null) {
//                        searchAdapter.notifyDataSetChanged();
//                    }
//                } else {
//                    Toast.makeText(getActivity(),
//                            "Input is empty, showing recommended users",
//                            Toast.LENGTH_LONG).show();
//                    //recommendation();
//                    if (searchAdapter != null) {
//                        searchAdapter.notifyDataSetChanged();
//                    }
//                }
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