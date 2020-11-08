package com.example.comp90018.Activity.Home;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.comp90018.Activity.Shake.UserViewActivity;
import com.example.comp90018.DataModel.Comment;
import com.example.comp90018.DataModel.Post;
import com.example.comp90018.DataModel.UserModel;
import com.example.comp90018.R;
import com.example.comp90018.utils.GlideApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class HomePageAdapter extends BaseAdapter {
    private static final String TAG = "HomePageAdapter";
    private Context context;
    private ArrayList<Post> posts;
    private String tmpLike;
    private int likePosition;
    private ImageButton userProfileImg,likeButton,commentButton;
    TextView userName, likedText,content, likesFixText, commentFixText, commentTextView,commentList;
    UserModel user;
    View rowView;
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    // image storage
    private StorageReference mStorageImagesRef = FirebaseStorage.getInstance().getReference().child("images");

    public HomePageAdapter(Context c, ArrayList<Post> data, UserModel user) {
        context = c;
        posts = data;
        this.user = user;
    }

    //this method updates the list view when the array has been changed
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(posts != null) {
            return posts.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        return posts.get(i);
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
        rowView = inflater.inflate(R.layout.feed, null, true);

        //get the feed with location
        final Post post = posts.get(position);

        //find all UI elements
        userProfileImg = rowView.findViewById(R.id.userImage);
        userName = (TextView) rowView.findViewById(R.id.text_userName);
        likedText = rowView.findViewById(R.id.likedTextView);
        commentList =  rowView.findViewById(R.id.commentList);
        likeButton = (ImageButton) rowView.findViewById(R.id.likeButton);
        commentButton = (ImageButton) rowView.findViewById(R.id.button_comment);
        content = (TextView) rowView.findViewById(R.id.content_text);
        commentTextView = rowView.findViewById(R.id.commentTextView);
        //set text view styles
        likesFixText = (TextView) rowView.findViewById(R.id.likedText);
        likesFixText.setTypeface(likesFixText.getTypeface(), Typeface.BOLD);
        commentFixText = (TextView) rowView.findViewById(R.id.commentText);
        commentFixText.setTypeface(commentFixText.getTypeface(), Typeface.BOLD);

        // set up the name
        userName.setText(post.getAuthorName());

        // set up the blank content text in the view
        if (post.getContent() != null) {
            content.setText(post.getContent());
        } else {
            content.setText("No content.");
        }

        // get user profile image
        loadWithGlide(rowView,R.id.userImage,post.getAuthorUid());
        userProfileImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(context, UserViewActivity.class);
                intent.putExtra("USER_UID", post.getAuthorUid());
                context.startActivity(intent);
            }
        });


        // get post image
        loadWithGlide(rowView,R.id.photoImage,post.getId());

        // set up like image
        if(post.HasLiked(user.nickName)){
            likeButton.setImageDrawable(rowView.getResources().getDrawable(R.drawable.filled_heart));
        }else {
            likeButton.setImageDrawable(rowView.getResources().getDrawable(R.drawable.empty_heart));
        }

        // set up the blank like text in the view
        if (post.getLikes().size() != 0) {
            String likeList = post.getLikes().toString();
            likedText.setText(likeList.substring(1, likeList.length()-1));
        } else {
            likedText.setText("Nobody has liked yet.");
        }

        likeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (!post.HasLiked(user.nickName)) {
                    likeButton.setImageDrawable(rowView.getResources().getDrawable(R.drawable.filled_heart));

                    db.collection("posts").document(post.getId()).update("likes", FieldValue.arrayUnion(mAuth.getUid()));
                    likePosition = position;
                    //update liked list
                    Toast.makeText(context.getApplicationContext(), "You liked!", Toast.LENGTH_LONG).show();

                    if (post.getLikes().size() != 0) {
                        String likeList = post.getLikes().toString();
                        likedText.setText(likeList.substring(1, likeList.length()-1));
                    } else {
                        likedText.setText("Nobody has liked yet.");
                    }
                } else {
                    likeButton.setImageDrawable(rowView.getResources().getDrawable(R.drawable.empty_heart));

                    Toast.makeText(context.getApplicationContext(), "Cancel like", Toast.LENGTH_LONG).show();
                    db.collection("posts").document(post.getId()).update("likes", FieldValue.arrayRemove(mAuth.getUid()));
                    if (post.getLikes().size() != 0) {
                        String likeList = post.getLikes().toString();
                        likedText.setText(likeList.substring(1, likeList.length()-1));
                    } else {
                        likedText.setText("Nobody has liked yet.");
                    }
                }
            }
        });

        // set up the blank comment text in the view
        if (post.getComments().size() != 0) {
            StringBuilder commentString = new StringBuilder("");
            Log.d(TAG, "getView: "+commentString.toString());
            for(int i = 0;i<post.getComments().size();i++){
                commentString.append(post.getComments().get(i).author+": "+post.getComments().get(i).content+'\n');
                Log.d(TAG, "getView: "+post.getComments().size());
            }
            commentList.setText(commentString.toString());
        } else {
            commentList.setText("Nobody has commented yet.");
        }

        commentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("POST_ID",post.getId());
                context.startActivity(intent);
            }
        });

        return rowView;
    }

    public void loadWithGlide(View container, int ID, String picId) {
        // [START storage_load_with_glide]
        // Reference to an image file in Cloud Storage
        StorageReference profileReference = mStorageImagesRef.child(picId);

        // ImageView in your Activity
        ImageView imageView = container.findViewById(ID);

        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        GlideApp.with(context /* context */)
                .load(profileReference)
                .placeholder(R.drawable.default_avatar)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
        // [END storage_load_with_glide]
    }

}
