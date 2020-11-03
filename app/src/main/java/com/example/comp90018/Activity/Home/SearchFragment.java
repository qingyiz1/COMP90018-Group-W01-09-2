package com.example.comp90018.Activity.Home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private CommolySearchView<Search> mCsvShow;
    private ListView listView;
    private SearchAdapter searchAdapter;
    private ArrayList<Search> users;

    final private int SEARCH_COUNT = 10;
//    private OnFragmentInteractionListener listener;

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

//    public void recommendation() {
//        users = new ArrayList<>();
//        //pass the data into discovery user array list
//        byte[] bitmapdata = new byte[0];
//        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
//
//        Drawable myDrawable = getResources().getDrawable(R.drawable.default_profile_image);
//        Bitmap defaultImage = ((BitmapDrawable) myDrawable).getBitmap();
//
//        Search searchUser = new Search();
//        searchAdapter.setUsers(users);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        listView = (ListView) view.findViewById(R.id.searchList);
        //import adapter
//        searchAdapter = new SearchAdapter(getActivity(), getData());
//        listView.setAdapter(searchAdapter);

//        search.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//
//            }
//        });
        return view;
    }

//    public void onButtonPressed(Uri uri) {
//        if (listener != null) {
//            listener.onFragmentInteraction(uri);
//        }
//    }
//
//    public ArrayList<Search> getData(){
//        return users;
//    }
//
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
//
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            //recommendation();
//        }
//        else {  }
//    }

    //////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        initData();
        initView();
//        initAdapter();
//        initSearch();
    }

    /**
     * 初始化适配器,一般的扩展只需修改该方法即可
     */
//    private void initAdapter() {
//        searchAdapter = new SearchAdapter(getActivity(), (ArrayList<Search>) users);
//        // 点击事件
////        searchAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                Toast.makeText(getActivity(), mCsvShow.getFilterDatas().get(i).getUserName() + "\n" + mCsvShow.getFilterDatas().get(i).getGender(), Toast.LENGTH_SHORT).show();
////            }
////        });
//    }

    /**
     * 初始化数据
     */
    private void initData() {
        users = new ArrayList<Search>();
        Search search1 = new Search();
        search1.setUserName("Cindy");
        search1.setProfileImageNo(R.drawable.touxiang);
        search1.setGender("Girl");
        Search search2 = new Search();
        search2.setUserName("Ben");
        search2.setProfileImageNo(R.drawable.gallery);
        search2.setGender("Boy");
        users.add(search1);
        users.add(search2);
    }

    /**
     * 初始化view
     */
    private void initView() {
//        mCsvShow = (CommolySearchView) getActivity().findViewById(R.id.csv_show);
        listView = (ListView) getActivity().findViewById(R.id.searchList);
        searchAdapter = new SearchAdapter(getActivity(), users);
        listView.setAdapter(searchAdapter);
        // 设置数据源
//        mCsvShow.setDatas(users);
//        mCsvShow.setAdapter(searchAdapter);
//        // 设置筛选数据
//        mCsvShow.setSearchDataListener(new CommolySearchView.SearchDatas<Search>() {
//            @Override
//            public ArrayList<Search> filterDatas(ArrayList<Search> datas, ArrayList<Search> filterdatas, String inputstr) {
//                for (int i = 0; i < datas.size(); i++) {
//                    // 筛选条件
//                    if ((datas.get(i).getUserName()).contains(inputstr) || datas.get(i).getGender().contains(inputstr)) {
//                        filterdatas.add(datas.get(i));
//                    }
//                }
//                return filterdatas;
//            }
//        });
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getActivity(), mCsvShow.getFilterDatas().get(i).getUserName() + "\n" + mCsvShow.getFilterDatas().get(i).getGender(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    /**
     * 初始化搜索
     */
//    private void initSearch() {
//        listView.setAdapter(searchAdapter);
//        // 设置数据源
//        mCsvShow.setDatas(users);
//        mCsvShow.setAdapter(searchAdapter);
//        // 设置搜索
//        mCsvShow.setSearchDataListener(new CommolySearchView.SearchDatas<Search>() {
//            @Override
//            public ArrayList<Search> filterDatas(ArrayList<Search> datas, ArrayList<Search> filterdatas, String inputstr) {
//                for (int i = 0; i < datas.size(); i++) {
//                    // 筛选条件
//                    if ((datas.get(i).getUserName()).contains(inputstr) || datas.get(i).getGender().contains(inputstr)) {
//                        filterdatas.add(datas.get(i));
//                    }
//                }
//                return filterdatas;
//            }
//        });
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getActivity(), mCsvShow.getFilterDatas().get(i).getUserName() + "\n" + mCsvShow.getFilterDatas().get(i).getGender(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}