package com.example.comp90018.Activity.Shake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.comp90018.Activity.Home.CommentActivity;

import com.example.comp90018.R;

import java.util.ArrayList;

public class UserViewActivity extends AppCompatActivity {
    private  ListView browser_list;
    private View view;
    private ImageView image;
    private TextView user_name, user_info, user_description;
    private ArrayList<Moment> feeds_array=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userview);

        image=findViewById(R.id.iv_personal_icon);

        user_name = findViewById(R.id.user_name);

        user_info = findViewById(R.id.user_info);
        user_description = findViewById(R.id.user_description);
        browser_list=findViewById(R.id.browserListView);
        //TODO: 从数据库获取用户名、用户信息、用户交友描述
        user_name.setText("user name");
        user_info.setText("user info");
        user_description.setText("user description");
        //TODO: END
        browser_list=findViewById(R.id.browserListView);
        browser_list.setAdapter(new MyBaseAdapter(getFeeds_array()));

    }
    private ArrayList<Moment> getFeeds_array(){
        //TODO:  BEGIN 从数据库中，获取
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

        //第一条动态
        Moment feed=new Moment(username, context, location, likelist, comment, user_has_liked);
        feeds_array.add(feed);
        //第二条动态....
        // Moment feed=new Moment(username, context, location, likelist, comment, user_has_liked);
        // feeds_array.add(feed);
        return feeds_array;
    }
    class MyBaseAdapter extends BaseAdapter{
        private ArrayList<Moment> feed_array;
        public MyBaseAdapter(ArrayList<Moment> feed_array){
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
            return null;//names [position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {//组装数据
            final View rowView=View.inflate(UserViewActivity.this,R.layout.feed,null);//在list_item中有两个id,现在要把他们拿过来
            final Moment oneFeed = feed_array.get(position);

            //find all UI elements
            ImageView userProfileImg = (ImageView) rowView.findViewById(R.id.userImage);

            TextView userName = (TextView) rowView.findViewById(R.id.text_userName);
            //TextView locationName = (TextView) rowView.findViewById(R.id.locationTextView);
            ImageView photoImg = (ImageView) rowView.findViewById(R.id.photoImage);

            final TextView likedText = (TextView) rowView.findViewById(R.id.likedTextView);
            final TextView commentText = (TextView) rowView.findViewById(R.id.commentTextView);
            final ImageButton likeButton = (ImageButton) rowView.findViewById(R.id.likeButton);
            ImageButton commentButton = (ImageButton) rowView.findViewById(R.id.commentButton);
            TextView captionText = (TextView) rowView.findViewById(R.id.captionTextView);

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
            userName.setText(oneFeed.getUsername());
            //locationName.setText(oneFeed.getLocation());
            captionText.setText(oneFeed.getContext());

            //TODO: BEGIN 从数据库中，设置头像，发布的照片
            userProfileImg.setImageDrawable(getResources().getDrawable(R.drawable.touxiang));
            photoImg.setImageDrawable(getResources().getDrawable(R.drawable.next));
            //TODO: END

            if (oneFeed.getLikelist().size() != 0) {
                String likelist=oneFeed.getLikelist().toString();
                likedText.setText(likelist.substring(1, likelist.length()-1));
            } else {
                likedText.setText("Nobody has liked yet.");
            }

            // set up the blank comment text in the view
            if (oneFeed.getCommentList().size() != 0) {
                String commentlist=oneFeed.getCommentList().toString().replace(',', '\n');
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
                        feed_array=getFeeds_array();//获取最新的feeds_array
                        oneFeed.update(feed_array.get(position));
                        if (oneFeed.getLikelist().size() != 0) {
                            String likelist=oneFeed.getLikelist().toString();
                            likedText.setText(likelist.substring(1, likelist.length()-1));
                        } else {
                            likedText.setText("Nobody has liked yet.");
                        }
                        ///////////////////////////////////////////
                        //TODO: 方法二：本地更新（不推荐，仅本地测试）
                        oneFeed.setUser_has_liked(true);
                        oneFeed.addLikeList("user3");
                        if (oneFeed.getLikelist().size() != 0) {
                            String likelist=oneFeed.getLikelist().toString();
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
                        oneFeed.deleteLikeList("user3");
                        if (oneFeed.getLikelist().size() != 0) {
                            String likelist=oneFeed.getLikelist().toString();
                            likedText.setText(likelist.substring(1, likelist.length()-1));
                        } else {
                            likedText.setText("Nobody has liked yet.");
                        }
                    }
                }
            });
            commentButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    Intent intent = new Intent(UserViewActivity.this, CommentActivity.class);
                    UserViewActivity.this.startActivity(intent);
                    //TODO: 传输数据给服务端
                    //.....
                    //TODO: END
                    feed_array=getFeeds_array();//获取最新的feeds_array
                    oneFeed.update(feed_array.get(position));
                    if (oneFeed.getCommentList().size() != 0) {
                        String commentlist=oneFeed.getCommentList().toString().replace(',', '\n');
                        commentText.setText(commentlist.substring(1, commentlist.length() - 1));
                    } else {
                        commentText.setText("Nobody has commented yet.");
                    }
                }
            });
            return rowView;
        }
    }


}