package com.example.comp90018.Activity.Home;

import android.content.Context;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp90018.R;

import java.util.ArrayList;
import java.util.List;

public class CommolySearchView<T> extends LinearLayout {
    private Context mContext;
    private TextView searchText;
    private EditText mEditText;
    private Button search;
    private SearchAdapter searchAdapter;
    final private int SEARCH_COUNT = 10;
    /**
     * Source data
     */
    private ArrayList<T> mDatas = new ArrayList<T>();
    private ArrayList<T> mDupDatas = new ArrayList<T>();
    /**
     * Search result data
     */
    private ArrayList<T> mFilterDatas = new ArrayList<T>();
    private ArrayList<T> mDupFilterDatas = new ArrayList<T>();

    /**
     * 回调接口
     *
     * @param <T>
     */
    public interface SearchDatas<T> {
        /**
         * @param datas           alldata
         * @param filterdatas     data after selection
         * @param inputstr        input data(search condition)
         * @return
         */
        ArrayList<T> filterDatas(ArrayList<T> datas, ArrayList<T> filterdatas, String inputstr);
    }

    /**
     * callback all data
     */
    private SearchDatas<T> mListener;

    /**
     * set callback
     *
     * @param listener
     */
    public void setSearchDataListener(SearchDatas<T> listener) {
        mListener = listener;

    }

    public CommolySearchView(Context context) {
        this(context, null);
    }

    public CommolySearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommolySearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        // set layout
        LayoutInflater.from(context).inflate(R.layout.searchview_layout, this);
        initView();
    }

    /**
     * initialization
     */
    private void initView() {
        mEditText = (EditText) findViewById(R.id.editText);
        mEditText.getBackground().setColorFilter(getResources().getColor(R.color.actionbar_background), PorterDuff.Mode.SRC_ATOP);
        searchText = (TextView) findViewById(R.id.text_search);
        search = (Button) findViewById(R.id.searchButton);

        search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Cheat!", Toast.LENGTH_SHORT).show();
            }
        });
        // 搜索栏处理事件
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // get data after search
                mFilterDatas = mListener.filterDatas(mDupDatas, mFilterDatas, charSequence.toString());
                if (null != mDatas) {
                    mDatas.clear();
                }
                mDatas.addAll(mFilterDatas);
                searchAdapter.notifyDataSetChanged();
                reSetDatas();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    /**
     * get data fits search result
     *
     * @return
     */
    public List<T> getFilterDatas() {
        return (null != mDupFilterDatas && mDupFilterDatas.size() > 0) ? mDupFilterDatas : mDupDatas;
    }

    /**
     * redeploy data
     */
    private void reSetDatas() {
        if (null != mFilterDatas) {
            if (null != mDupFilterDatas) {
                mDupFilterDatas.clear();
                mDupFilterDatas.addAll(mFilterDatas);
            }
            mFilterDatas.clear();
        }
    }

    /**
     * set source data
     *
     * @param datas
     */
    public void setDatas(ArrayList<T> datas) {
        if (null == datas) {
            return;
        }
        if (null != mDatas) {
            mDatas.clear();
        }
        if (null != mDupDatas) {
            mDupDatas.clear();
        }
        mDatas = datas;
        mDupDatas.addAll(mDatas);
    }

    /**
     * set adapter
     *
     * @param adapter
     */
    public void setAdapter(SearchAdapter adapter) {
        if (null == adapter) {
            return;
        }
        searchAdapter = adapter;
    }

}