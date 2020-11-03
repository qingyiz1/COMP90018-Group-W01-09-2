package com.example.comp90018.Activity.Home;

import android.content.Context;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.comp90018.DataModel.Search;
import com.example.comp90018.R;

import java.util.ArrayList;
import java.util.List;

public class CommolySearchView<T> extends RelativeLayout {
    private Context mContext;
    private TextView searchText;
    private EditText mEditText;
    private Button search;
    private SearchAdapter searchAdapter;
    private ArrayList<Search> users;
    final private int SEARCH_COUNT = 10;
    /**
     * 数据源
     */
    private ArrayList<T> mDatas = new ArrayList<T>();
    private ArrayList<T> mDupDatas = new ArrayList<T>();
    /**
     * 筛选后的数据源
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
         * 参数一:全部数据,参数二:筛选后的数据,参数三:输入的内容
         *
         * @param datas
         * @param filterdatas
         * @param inputstr
         * @return
         */
        ArrayList<T> filterDatas(ArrayList<T> datas, ArrayList<T> filterdatas, String inputstr);
    }

    /**
     * 回调
     */
    private SearchDatas<T> mListener;

    /**
     * 设置回调
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
        // 绑定布局文件
        LayoutInflater.from(context).inflate(R.layout.searchview_layout, this);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mEditText = (EditText) findViewById(R.id.editText);
        search = (Button) findViewById(R.id.searchButton);
        mEditText.getBackground().setColorFilter(getResources().getColor(R.color.actionbar_background), PorterDuff.Mode.SRC_ATOP);
        searchText = (TextView) findViewById(R.id.text_search);

        // 搜索栏处理事件
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 获取筛选后的数据
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
     * 获取筛选后的数据
     *
     * @return
     */
    public List<T> getFilterDatas() {
        return (null != mDupFilterDatas && mDupFilterDatas.size() > 0) ? mDupFilterDatas : mDupDatas;
    }

    /**
     * 重置数据
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
     * 设置数据源
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
     * 设置适配器
     *
     * @param adapter
     */
    public void setAdapter(SearchAdapter adapter) {
        if (null == adapter) {
            return;
        }
        searchAdapter = adapter;
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
//        private Bitmap drawableToBitamp(Drawable drawable) {
//            if (null == drawable) {
//                return null;
//            }
//            if (drawable instanceof BitmapDrawable) {
//                BitmapDrawable bd = (BitmapDrawable) drawable;
//                return bd.getBitmap();
//            }
//            int w = drawable.getIntrinsicWidth();
//            int h = drawable.getIntrinsicHeight();
//            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(bitmap);
//            drawable.setBounds(0, 0, w, h);
//            drawable.draw(canvas);
//            return bitmap;
//        }
}