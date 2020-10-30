package com.example.comp90018.Activity.Shake;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.comp90018.Activity.Home.Feed;
import com.example.comp90018.Activity.Home.HomePageAdapter;
import com.example.comp90018.R;

import java.util.ArrayList;

public class UserViewActivity extends AppCompatActivity {
    private  ListView browser_list;
    private View view;
    private TextView user_name, user_info, user_description;
    private ArrayList<Moment> feeds_array=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userview);

        user_name = findViewById(R.id.user_name);

        user_info = findViewById(R.id.user_info);
        user_description = findViewById(R.id.user_description);
        browser_list=findViewById(R.id.browserListView);

        user_name.setText("user name");
        user_info.setText("user info");
        user_description.setText("user description");

        browser_list=findViewById(R.id.browserListView);
        browser_list.setAdapter(new MyBaseAdapter(getFeeds_array()));

    }
    private ArrayList<Moment> getFeeds_array(){
        String username="name";
        String context="context";
        String location="location";
        ArrayList<String> likelist=new ArrayList<>();
        ArrayList<String> comment=new ArrayList<>();

        comment.add("comment1");

        likelist.add("user1");
//        Boolean user_has_liked=true;
        Moment feed=new Moment(username, context, location, likelist, comment);
        feeds_array.add(feed);
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
        public View getView(int position, View convertView, ViewGroup parent) {//组装数据
            View rowView=View.inflate(UserViewActivity.this,R.layout.feed,null);//在list_item中有两个id,现在要把他们拿过来
            final Moment oneFeed = feed_array.get(position);

            //find all UI elements
            ImageView userProfileImg = (ImageView) rowView.findViewById(R.id.userImage);
            TextView userName = (TextView) rowView.findViewById(R.id.text_userName);
            TextView locationName = (TextView) rowView.findViewById(R.id.locationTextView);
            ImageView photoImg = (ImageView) rowView.findViewById(R.id.photoImage);
            final TextView likedText = (TextView) rowView.findViewById(R.id.likedTextView);
            TextView commentText = (TextView) rowView.findViewById(R.id.commentTextView);
            final ImageButton likeButton = (ImageButton) rowView.findViewById(R.id.likeButton);
            final ImageButton commentButton = (ImageButton) rowView.findViewById(R.id.commentButton);
            TextView captionText = (TextView) rowView.findViewById(R.id.captionTextView);

            //set text view styles
            TextView captionFixText = (TextView) rowView.findViewById(R.id.captionText);
            captionFixText.setTypeface(captionFixText.getTypeface(), Typeface.BOLD);
            TextView likesFixText = (TextView) rowView.findViewById(R.id.likedText);
            likesFixText.setTypeface(likesFixText.getTypeface(), Typeface.BOLD);
            TextView commentFixText = (TextView) rowView.findViewById(R.id.commentText);
            commentFixText.setTypeface(commentFixText.getTypeface(), Typeface.BOLD);

            //set user profile image, user name, location name, photo image
//        userProfileImg.setImageBitmap(oneFeed.getUserProfileImg());
            userName.setText(oneFeed.getUsername());
            locationName.setText(oneFeed.getLocation());
            captionText.setText(oneFeed.getContext());
            if (oneFeed.getLikelist() != null) {
                likedText.setText(oneFeed.getLikelist().toString().replace(',', ' '));
            } else {
                likedText.setText("Nobody has liked yet.");
            }

            // set up the blank comment text in the view
            if (oneFeed.getComment() != null) {
                commentText.setText(oneFeed.getComment().toString().replace(',', ' ').substring(1,
                        oneFeed.getComment().toString().length() - 1));
            } else {
                commentText.setText("Nobody has commented yet.");
            }
            //组装玩开始返回
            return rowView;
        }
    }


}