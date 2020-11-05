package com.example.comp90018.Activity.Home;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp90018.Activity.Shake.ShakeResultActivity;
import com.example.comp90018.Activity.Shake.UserViewActivity;
import com.example.comp90018.DataModel.Feed;
import com.example.comp90018.R;

import java.util.ArrayList;

public class HomePageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Feed> feed_array;
    private String tmpLike;
    private int likePosition;
    private ImageButton likeButton;
    TextView likedText;
    TextView commentText;

    public HomePageAdapter(Context c, ArrayList<Feed> data) {
        context = c;
        feed_array = data;
    }

    //setter method to pass the data from fragment to here
    public void HomePageAdapter(ArrayList<Feed> feed_array) {
        this.feed_array = feed_array;
    }

    //this method updates the list view when the array has been changed
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
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
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    //set the view of the list view in home page fragment
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.feed, null, true);

//        //获取最新的feeds_array
//        feed_array=new ArrayList<>();

        //get the feed with location
        final Feed oneFeed = feed_array.get(position);

        //find all UI elements
        ImageButton userProfileImg = rowView.findViewById(R.id.userImage);
        TextView userName = (TextView) rowView.findViewById(R.id.text_userName);
        ImageView photoImg = (ImageView) rowView.findViewById(R.id.photoImage);
        likedText = (TextView) rowView.findViewById(R.id.likedTextView);
        commentText = (TextView) rowView.findViewById(R.id.commentTextView);
        likeButton = (ImageButton) rowView.findViewById(R.id.likeButton);
        ImageButton commentButton = (ImageButton) rowView.findViewById(R.id.commentButton);
        TextView captionText = (TextView) rowView.findViewById(R.id.captionTextView);

        //set text view styles
        TextView likesFixText = (TextView) rowView.findViewById(R.id.likedText);
        likesFixText.setTypeface(likesFixText.getTypeface(), Typeface.BOLD);
        TextView commentFixText = (TextView) rowView.findViewById(R.id.commentText);
        commentFixText.setTypeface(commentFixText.getTypeface(), Typeface.BOLD);

        // set up the name
        userName.setText(oneFeed.getDisplayName());

        // set up the blank caption text in the view
        if (oneFeed.getCaption() != null) {
            captionText.setText(oneFeed.getCaption());
        } else {
            captionText.setText("No caption.");
        }

        //TODO: BEGIN 从数据库中，设置头像，发布的照片
        userProfileImg.setImageBitmap(oneFeed.getUserProfileImg());
        userProfileImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(context, UserViewActivity.class);
                context.startActivity(intent);
            }
        });

        photoImg.setImageBitmap(oneFeed.getPhoto());
//        Bitmap postImage = oneFeed.getPhoto();
//        if (postImage != null) {
//            photoImg.setImageBitmap(BitmapStore.getCroppedBitmap(postImage));
//        }else{
//            Drawable drawable1 = rowView.getResources().getDrawable(R.drawable.photo);
//            Bitmap defaultImage1 = ((BitmapDrawable) drawable1).getBitmap();
//            photoImg.setImageBitmap(defaultImage1);
//        }

        // set up like image
        if(oneFeed.getUser_has_liked()){
            likeButton.setImageDrawable(rowView.getResources().getDrawable(R.drawable.filled_heart));
        }else {
            likeButton.setImageDrawable(rowView.getResources().getDrawable(R.drawable.empty_heart));
        }
        // set up the blank like text in the view
        if (oneFeed.getLike().size() != 0) {
            String likelist=oneFeed.getLike().toString();
            likedText.setText(likelist.substring(1, likelist.length()-1));
        } else {
            likedText.setText("Nobody has liked yet.");
        }

        likeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (!oneFeed.getUser_has_liked()) {
                    likeButton.setImageDrawable(rowView.getResources().getDrawable(R.drawable.filled_heart));
                    oneFeed.setUser_has_liked(true);
                    tmpLike = likedText.getText().toString();
                    likePosition = position;
                    //update liked list
                    Toast.makeText(context.getApplicationContext(), "You liked!", Toast.LENGTH_LONG).show();
                    System.out.println("Like: " + tmpLike);
                    oneFeed.addLike("user3");
                    if (oneFeed.getLike().size() != 0) {
                        String likelist=oneFeed.getLike().toString();
                        likedText.setText(likelist.substring(1, likelist.length()-1));
                    } else {
                        likedText.setText("Nobody has liked yet.");
                    }
                } else {
                    likeButton.setImageDrawable(rowView.getResources().getDrawable(R.drawable.empty_heart));
                    oneFeed.setUser_has_liked(false);
                    Toast.makeText(context.getApplicationContext(), "Cancel like", Toast.LENGTH_LONG).show();
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

        // set up the blank comment text in the view
        if (oneFeed.getComment().size() != 0) {
            String commentlist=oneFeed.getComment().toString().replace(',', '\n');
            commentText.setText(commentlist.substring(1, commentlist.length() - 1));
        } else {
            commentText.setText("Nobody has commented yet.");
        }

        commentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(context, CommentActivity.class);
                context.startActivity(intent);
            }
        });

        return rowView;
    }

}
