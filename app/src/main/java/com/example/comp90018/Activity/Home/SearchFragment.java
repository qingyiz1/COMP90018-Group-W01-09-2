package com.example.comp90018.Activity.Home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.comp90018.Activity.Shake.UserViewActivity;
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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CommolySearchView<Search> mCsvShow;
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
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mCsvShow = (CommolySearchView) view.findViewById(R.id.csv_show);
        listView = (ListView) view.findViewById(R.id.searchList);
        searchAdapter = new SearchAdapter(getActivity(), users);
        listView.setAdapter(searchAdapter);
        initView();
        return view;
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
        }
        else {  }
    }

    private void initData() {
        users = new ArrayList<Search>();

        Search search1 = new Search();
        search1.setUserName("Cindy");
        Drawable drawable1 = getResources().getDrawable(R.drawable.default_avatar);
        Bitmap touxiang1 = ((BitmapDrawable) drawable1).getBitmap();
        search1.setProfileImage(touxiang1);
        search1.setPetType("cat");

        Search search2 = new Search();
        search2.setUserName("Ben");
        Drawable drawable2 = getResources().getDrawable(R.drawable.gallery);
        Bitmap touxiang2 = ((BitmapDrawable) drawable2).getBitmap();
        search2.setProfileImage(touxiang2);
        search2.setPetType("dog");
        users.add(search1);
        users.add(search2);
    }

    private void initView() {
        mCsvShow.setDatas(users);
        mCsvShow.setAdapter(searchAdapter);

        mCsvShow.setSearchDataListener(new CommolySearchView.SearchDatas<Search>() {
            @Override
            public ArrayList<Search> filterDatas(ArrayList<Search> datas, ArrayList<Search> filterdatas, String inputstr) {
                for (int i = 0; i < datas.size(); i++) {
                    // 筛选条件
                    if (((datas.get(i).getUserName()).toLowerCase()).contains(inputstr) || ((datas.get(i).getPetType()).toLowerCase()).contains(inputstr)) {
                        filterdatas.add(datas.get(i));
                    }
                }
                return filterdatas;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO:转去相应用户的userview page
                Intent intent = new Intent(getActivity(), UserViewActivity.class);
                startActivity(intent);
            }
        });
    }

}