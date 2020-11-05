package com.example.comp90018.Activity.Shake;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.comp90018.Activity.Home.CommentActivity;

import com.example.comp90018.DataModel.Feed;
import com.example.comp90018.R;

import java.util.ArrayList;

public class UserViewActivity extends AppCompatActivity implements View.OnClickListener{
    private  ListView browser_list;
    private View view;
    private ImageView image;
    private TextView user_name, user_info, user_description;
    private Button followButton;
    private ArrayList<Feed> feeds_array=new ArrayList<>();
    private boolean followOrNot;
    private EditText mAmEtMsg;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userview);

        image=findViewById(R.id.iv_personal_icon);

        user_name = findViewById(R.id.user_name);

        user_info = findViewById(R.id.user_info);
        user_description = findViewById(R.id.user_description);
        browser_list=findViewById(R.id.browserListView);
        followButton=findViewById(R.id.follow_button);
        followButton.setOnClickListener(this);
        mAmEtMsg=findViewById(R.id.commentInput);
        mAmEtMsg.setVisibility(View.GONE);
        send=findViewById(R.id.send);
        send.setVisibility(View.GONE);
        //TODO: 从数据库获取用户名、用户信息、用户交友描述
        user_name.setText("user name");
        user_info.setText("user info");
        user_description.setText("user description");
        followOrNot=false;
        if(followOrNot){
            followButton.setText("Following");
        }else{
            followButton.setText("Follow");
        }

        //TODO: END
        browser_list=findViewById(R.id.browserListView);
        browser_list.setAdapter(new MyBaseAdapter(getFeeds_array()));

    }
    private ArrayList<Feed> getFeeds_array(){
        //TODO:  BEGIN 从数据库中，获取
        feeds_array=new ArrayList<>();
        String username="name";
        String context="context";
        String location="location";
        ArrayList<String> likelist=new ArrayList<>();
        ArrayList<String> comment=new ArrayList<>();

        comment.add("user1: comment1");
        comment.add("user2: comment2");

        likelist.add("user1");
        likelist.add("user2");

        //用户是否已经点赞
        Boolean user_has_liked=false;
        //TODO: END
        Drawable drawable = getResources().getDrawable(R.drawable.default_avatar);
        Bitmap touxiang = ((BitmapDrawable) drawable).getBitmap();
        Drawable drawable1 = getResources().getDrawable(R.drawable.photo);
        Bitmap postPhoto = ((BitmapDrawable) drawable1).getBitmap();

        //第一条动态
        Feed feed=new Feed(username, touxiang, postPhoto, comment, likelist,context, user_has_liked);
        //第二条动态....
        feeds_array.add(feed);
        String username2="name2";
        String context2="context2";
        String location2="location2";
        ArrayList<String> likelist2=new ArrayList<>();
        ArrayList<String> comment2=new ArrayList<>();

        comment2.add("user1: comment12222");
        comment2.add("user2: comment22222");

        likelist2.add("user1222");
        likelist2.add("user22222");
        Bitmap touxiang2 = ((BitmapDrawable) drawable).getBitmap();
        Bitmap postPhoto2 = ((BitmapDrawable) drawable1).getBitmap();

        //用户是否已经点赞
        Boolean user_has_liked2=false;
        Feed feed2=new Feed(username2, touxiang2, postPhoto2, comment2, likelist2,context2, user_has_liked2);
        feeds_array.add(feed2);
        return feeds_array;
    }

    @Override
    public void onClick(View view) {
        if(view==followButton){
            if(followOrNot){
                followOrNot=false;
                followButton.setText("Follow");
            }else {
                followOrNot=true;
                followButton.setText("Following");
            }
            //TODO: 客户端传输给服务器，取消关注/关注

        }
    }

    private void onFocusChange(boolean hasFocus) {
        final boolean isFocus = hasFocus;
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        UserViewActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                if (isFocus) {
                    //显示输入法
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    mAmEtMsg.setFocusable(true);
                    mAmEtMsg.requestFocus();
                } else {
                    //隐藏输入法
                    mAmEtMsg.setVisibility(View.GONE);
                    imm.hideSoftInputFromWindow(mAmEtMsg.getWindowToken(), 0);
                }
            }
        }, 100);
    }

    class MyBaseAdapter extends BaseAdapter{
        private ArrayList<Feed> feed_array;
        public MyBaseAdapter(ArrayList<Feed> feed_array){
            this.feed_array=feed_array;
        }
        @Override
        public int getCount() {
            if(feed_array != null) {
                return feed_array.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return feed_array.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {//组装数据
            //final View rowView = mInflater.inflate(R.layout.feed,null);
            final View rowView=View.inflate(UserViewActivity.this,R.layout.feed,null);//在list_item中有两个id,现在要把他们拿过来
            final Feed oneFeed = feed_array.get(position);

            //find all UI elements
            ImageView userProfileImg = (ImageView) rowView.findViewById(R.id.userImage);

            TextView userName = (TextView) rowView.findViewById(R.id.text_userName);
            //TextView locationName = (TextView) rowView.findViewById(R.id.locationTextView);
            ImageView photoImg = (ImageView) rowView.findViewById(R.id.photoImage);

            final TextView likedText = (TextView) rowView.findViewById(R.id.likedTextView);
            final TextView commentText = (TextView) rowView.findViewById(R.id.commentTextView);
            final ImageButton likeButton = (ImageButton) rowView.findViewById(R.id.likeButton);
            ImageButton commentButton = (ImageButton) rowView.findViewById(R.id.commentButton);
            TextView captionText = (TextView) rowView.findViewById(R.id.content_text);

            if(oneFeed.getUser_has_liked()){
                likeButton.setImageDrawable(getResources().getDrawable(R.drawable.filled_heart));
            }else {
                likeButton.setImageDrawable(getResources().getDrawable(R.drawable.empty_heart));
            }

            //set text view styles
            captionText.setTypeface(captionText.getTypeface(), Typeface.BOLD);
            TextView likesFixText = (TextView) rowView.findViewById(R.id.likedText);
            likesFixText.setTypeface(likesFixText.getTypeface(), Typeface.BOLD);
            TextView commentFixText = (TextView) rowView.findViewById(R.id.commentText);
            commentFixText.setTypeface(commentFixText.getTypeface(), Typeface.BOLD);
            userName.setText(oneFeed.getDisplayName());
            //locationName.setText(oneFeed.getLocation());
            captionText.setText(oneFeed.getCaption());

            //TODO: BEGIN 从数据库中，设置头像，发布的照片
            userProfileImg.setImageDrawable(getResources().getDrawable(R.drawable.default_avatar));
            photoImg.setImageDrawable(getResources().getDrawable(R.drawable.next));
            //TODO: END

            if (oneFeed.getLike().size() != 0) {
                String likelist=oneFeed.getLike().toString();
                likedText.setText(likelist.substring(1, likelist.length()-1));
            } else {
                likedText.setText("Nobody has liked yet.");
            }

            // set up the blank comment text in the view
            if (oneFeed.getComment().size() != 0) {
                String commentlist=oneFeed.getComment().toString().replace(',', '\n');
                commentText.setText(commentlist.substring(1, commentlist.length() - 1));
            } else {
                commentText.setText("Nobody has commented yet.");
            }
            likeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    if (!oneFeed.getUser_has_liked()) {
                        likeButton.setImageDrawable(rowView.getResources().getDrawable(R.drawable.filled_heart));
                        //TODO: 方法一：
                        //TODO: 客户端传输数据给服务端，update点赞信息；客户端获取服务端传送的likelist，更新到UI上
//                        feed_array=getFeeds_array();//获取最新的feeds_array
//                        oneFeed.update(feed_array.get(position));
//                        if (oneFeed.getLikelist().size() != 0) {
//                            String likelist=oneFeed.getLikelist().toString();
//                            likedText.setText(likelist.substring(1, likelist.length()-1));
//                        } else {
//                            likedText.setText("Nobody has liked yet.");
//                        }
                        ///////////////////////////////////////////
                        //TODO: 方法二：本地更新（不推荐，仅本地测试）
                        oneFeed.setUser_has_liked(true);
                        oneFeed.addLike("user3");
                        if (oneFeed.getLike().size() != 0) {
                            String likelist=oneFeed.getLike().toString();
                            likedText.setText(likelist.substring(1, likelist.length()-1));
                        } else {
                            likedText.setText("Nobody has liked yet.");
                        }
                        ////////////////////////////////////////////
                    } else {
                        likeButton.setImageDrawable(rowView.getResources().getDrawable(R.drawable.empty_heart));
                        //TODO: 方法一：
                        //TODO: 客户端传输数据给服务端，update点赞信息；客户端获取服务端传送的likelist，更新到UI上
//                        feed_array=getFeeds_array();//获取最新的feeds_array
//                        oneFeed.update(feed_array.get(position));
//                        if (oneFeed.getLikelist().size() != 0) {
//                            String likelist=oneFeed.getLikelist().toString();
//                            likedText.setText(likelist.substring(1, likelist.length()-1));
//                        } else {
//                            likedText.setText("Nobody has liked yet.");
//                        }
                        ///////////////////////////////////////////
                        //TODO: 方法二：本地更新（不推荐，仅本地测试）
                        oneFeed.setUser_has_liked(false);
                        oneFeed.deleteLike("user3");
                        if (oneFeed.getLike().size() != 0) {
                            String likelist=oneFeed.getLike().toString();
                            likedText.setText(likelist.substring(1, likelist.length()-1));
                        } else {
                            likedText.setText("Nobody has liked yet.");
                        }
                    }
                }
            });
            commentButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    mAmEtMsg.setVisibility(View.VISIBLE);
                    send.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(UserViewActivity.this.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            });
            send.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    String comment=mAmEtMsg.getText().toString();
                    if(comment.length()==0){
                        new AlertDialog.Builder(UserViewActivity.this)
                                .setTitle("Error!")
                                .setMessage("No Empty Comment.")
                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                .setPositiveButton(android.R.string.yes, null)
                                // A null listener allows the button to dismiss the dialog and take no further action.
                                //.setNegativeButton(android.R.string.no, null)
                                .setIcon(R.drawable.ic_error)
                                .show();
                    }else {
                        //TODO: 把comment传给服务器
                        feed_array = getFeeds_array();//获取最新的feeds_array
                        oneFeed.update(feed_array.get(position));
                        if (oneFeed.getComment().size() != 0) {
                            oneFeed.addComment(comment);
                            String commentlist = oneFeed.getComment().toString().replace(',', '\n');
                            commentlist = commentlist.substring(1, commentlist.length() - 1);
                            commentText.setText(commentlist);
                        } else {
                            commentText.setText("Nobody has commented yet.");
                        }
                    }
                    send.setVisibility(View.GONE);
                    mAmEtMsg.setVisibility(View.GONE);
                    mAmEtMsg.setText("");
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(UserViewActivity.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mAmEtMsg.getWindowToken(), 0);
                }

            });
            return rowView;
        }
    }


}