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

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.comp90018.Activity.Shake.UserViewActivity;
import com.example.comp90018.DataModel.Feed;
import com.example.comp90018.DataModel.Post;
import com.example.comp90018.DataModel.UserModel;
import com.example.comp90018.R;
import com.example.comp90018.utils.GlideApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HomePageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Post> posts;
    private String tmpLike;
    private int likePosition;
    private ImageButton likeButton,commentButton;
    TextView userName, likedText, commentText,content, likesFixText, commentFixText;
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
        userName = (TextView) rowView.findViewById(R.id.text_userName);
        likedText = (TextView) rowView.findViewById(R.id.likedTextView);
        commentText = (TextView) rowView.findViewById(R.id.commentTextView);
        likeButton = (ImageButton) rowView.findViewById(R.id.likeButton);
        commentButton = (ImageButton) rowView.findViewById(R.id.commentButton);
        content = (TextView) rowView.findViewById(R.id.content_text);

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

        // get post image
        loadWithGlide(rowView,R.id.photoImage,post.getId());

        // set up like image
        if(post.HasLiked(mAuth.getUid())){
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
                if (!post.HasLiked(mAuth.getUid())) {
                    likeButton.setImageDrawable(rowView.getResources().getDrawable(R.drawable.filled_heart));
                    post.setHasLiked(true);
                    tmpLike = likedText.getText().toString();
                    likePosition = position;
                    //update liked list
                    Toast.makeText(context.getApplicationContext(), "You liked!", Toast.LENGTH_LONG).show();
                    System.out.println("Like: " + tmpLike);

                    post.setLikes(user.getUid());
                    if (post.getLikes().size() != 0) {
                        String likeList = post.getLikes().toString();
                        likedText.setText(likeList.substring(1, likeList.length()-1));

                    } else {
                        likedText.setText("Nobody has liked yet.");
                    }
                } else {
                    likeButton.setImageDrawable(rowView.getResources().getDrawable(R.drawable.empty_heart));
                    post.setHasLiked(false);
                    Toast.makeText(context.getApplicationContext(), "Cancel like", Toast.LENGTH_LONG).show();
                    post.deleteLikes(user.getUid());
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
            String commentList = post.getComments().toString().replace(',', '\n');
            commentText.setText(commentList.substring(1, commentList.length() - 1));
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
                .skipMemoryCache(true)
                .into(imageView);
        // [END storage_load_with_glide]
    }

}
