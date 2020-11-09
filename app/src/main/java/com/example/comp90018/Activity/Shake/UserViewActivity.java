package com.example.comp90018.Activity.Shake;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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


import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.comp90018.Activity.BaseActivity;
import com.example.comp90018.Activity.Home.CommentActivity;

import com.example.comp90018.Activity.Home.HomePageAdapter;
import com.example.comp90018.DataModel.Comment;
import com.example.comp90018.DataModel.Feed;
import com.example.comp90018.DataModel.Post;
import com.example.comp90018.DataModel.UserModel;
import com.example.comp90018.R;
import com.example.comp90018.utils.GlideApp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserViewActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "UserViewActivity";
    private  ListView browser_list;
    private View view;
    private ImageView profileImage;
    private TextView user_name, user_info, user_description;
    private Button followButton;
    private ArrayList<Feed> feeds_array=new ArrayList<>();
    private boolean followOrNot;
    private EditText mAmEtMsg;
    private Button send;
    private HomePageAdapter homepageAdapter;
    private String uid;
    private UserModel user = new UserModel();
    ArrayList<Post> posts = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userview);
        uid = getIntent().getStringExtra("USER_UID");
        Log.d(TAG, "onCreate: "+uid);
        getUserdata(uid);

        profileImage = findViewById(R.id.profile_avatar);

        user_name = findViewById(R.id.user_name);
        user_info = findViewById(R.id.user_info);
        user_description = findViewById(R.id.user_description);

        browser_list=findViewById(R.id.browserListView);
        followButton=findViewById(R.id.follow_button);
        followButton.setOnClickListener(this);

        getData();

//        mAmEtMsg=findViewById(R.id.commentInput);
//        mAmEtMsg.setVisibility(View.GONE);
//        send=findViewById(R.id.send);
//        send.setVisibility(View.GONE);

        followOrNot=false;
        if(followOrNot){
            followButton.setText("Following");
//            db.collection("users").document(mAuth.getUid()).update("following", FieldValue.arrayUnion(user.uid));
        }else{
            followButton.setText("Follow");
        }

    }

    private void getUserdata(String uid){
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    if(snapshot.getData().get("nickName") != null){
                        user.nickName = snapshot.getData().get("nickName").toString();
                        user_name.setText(user.nickName);
                    }
                    if(snapshot.getData().get("sex") != null && snapshot.getData().get("species") != null && snapshot.getData().get("age") != null){
                        user.sex = snapshot.getData().get("sex").toString();
                        user.species = snapshot.getData().get("species").toString();
                        user.age = snapshot.getData().get("age").toString();
                        user_info.setText(user.sex+" "+user.species+", "+user.age+" years old");
                    }
                    if(snapshot.getData().get("bio") != null){
                        user.bio = snapshot.getData().get("bio").toString();
                        user_description.setText(user.bio);
                    }
                    loadWithGlide(getWindow().getDecorView().getRootView(),R.id.profile_avatar);
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    private void getData(){

        db.collection("posts").whereEqualTo("authorUid", uid)
//                .whereEqualTo("state", "CA")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        posts.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            ArrayList<Map<String,String>> commentTemp = (ArrayList<Map<String, String>>) doc.getData().get("comments");
                            final ArrayList<Comment> comments = new ArrayList<>();
                            final ArrayList<String> likes = new ArrayList<>();

                            for(final Map<String,String> comment: commentTemp){
                                db.collection("users").document(comment.get("author")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                String nickname = document.getData().get("nickName").toString();
                                                comments.add(new Comment(nickname,comment.get("content")));
                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });
                            }
                            ArrayList<String> tempLikes = (ArrayList<String>) doc.getData().get("likes");
                            for(String user : tempLikes){
                                db.collection("users").document(user).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                String nickname = document.getData().get("nickName").toString();
                                                likes.add(nickname);
                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });
                            }

                            Post post = new Post(doc.getData().get("authorUid").toString(),doc.getData().get("authorName").toString(),doc.getData().get("id").toString(),
                                    doc.getData().get("content").toString(),comments,likes, (Timestamp) doc.getData().get("dateCreated"));
                            posts.add(post);
                        }

                        db.collection("users").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                        user = document.toObject(UserModel.class);
                                        Log.d(TAG, "onComplete: "+user.nickName);
                                        int index = browser_list.getFirstVisiblePosition();
                                        View v = browser_list.getChildAt(0);
                                        int top = (v == null) ? 0 : (v.getTop() - browser_list.getPaddingTop());
                                        homepageAdapter = new HomePageAdapter(getBaseContext(),posts,user);
                                        browser_list.setAdapter(homepageAdapter);
                                        browser_list.setSelectionFromTop(index, top);
                                        //homepageAdapter.notifyDataSetChanged();
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });

                    }});
    }

//    private void getData(){
//
//        db.collection("posts")
//                .whereEqualTo("authorUid", uid)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value,
//                                        @Nullable FirebaseFirestoreException e) {
//                        if (e != null) {
//                            Log.w(TAG, "Listen failed.", e);
//                            return;
//                        }
//                        posts.clear();
//                        for (QueryDocumentSnapshot doc : value) {
//                            ArrayList<Comment> comments = (ArrayList<Comment>) doc.getData().get("comments");
//                            ArrayList<String> likes = (ArrayList<String>) doc.getData().get("likes");
//                            Post post = new Post(doc.getData().get("authorUid").toString(),doc.getData().get("authorName").toString(),doc.getData().get("id").toString(),
//                                    doc.getData().get("content").toString(),comments,likes, (Timestamp) doc.getData().get("dateCreated"));
//                            posts.add(post);
//                        }
//
//                        db.collection("users").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    DocumentSnapshot document = task.getResult();
//                                    if (document.exists()) {
//                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                                        user = document.toObject(UserModel.class);
//                                        homepageAdapter = new HomePageAdapter(UserViewActivity.this,posts,user);
//                                        browser_list.setAdapter(homepageAdapter);
//                                        //homepageAdapter.notifyDataSetChanged();
//                                    } else {
//                                        Log.d(TAG, "No such document");
//                                    }
//                                } else {
//                                    Log.d(TAG, "get failed with ", task.getException());
//                                }
//                            }
//                        });
//
//                    }});
//    }

    @Override
    public void onClick(View view) {
        if(view==followButton){
            if(followOrNot){
                followOrNot=false;
                db.collection("users").document(mAuth.getUid()).update("following", FieldValue.arrayRemove(uid));
                followButton.setText("Follow");
            }else {
                followOrNot=true;
                db.collection("users").document(mAuth.getUid()).update("following", FieldValue.arrayUnion(uid));
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

    public void loadWithGlide(View container, int ID) {
        // [START storage_load_with_glide]
        // Reference to an image file in Cloud Storage
        StorageReference profileReference = mStorageImagesRef.child(uid);

        // ImageView in your Activity
        ImageView imageView = container.findViewById(ID);

        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        GlideApp.with(this /* context */)
                .load(profileReference)
                .placeholder(R.drawable.default_avatar)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
        // [END storage_load_with_glide]
    }

//    class MyBaseAdapter extends BaseAdapter{
//        private ArrayList<Feed> feed_array;
//        public MyBaseAdapter(ArrayList<Feed> feed_array){
//            this.feed_array=feed_array;
//        }
//        @Override
//        public int getCount() {
//            if(feed_array != null) {
//                return feed_array.size();
//            } else {
//                return 0;
//            }
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return feed_array.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {//组装数据
//            //final View rowView = mInflater.inflate(R.layout.feed,null);
//            final View rowView=View.inflate(UserViewActivity.this,R.layout.feed,null);//在list_item中有两个id,现在要把他们拿过来
//            final Feed oneFeed = feed_array.get(position);
//
//            //find all UI elements
//            ImageView userProfileImg = (ImageView) rowView.findViewById(R.id.userImage);
//
//            TextView userName = (TextView) rowView.findViewById(R.id.text_userName);
//            //TextView locationName = (TextView) rowView.findViewById(R.id.locationTextView);
//            ImageView photoImg = (ImageView) rowView.findViewById(R.id.photoImage);
//
//            final TextView likedText = (TextView) rowView.findViewById(R.id.likedTextView);
//            final TextView commentText = (TextView) rowView.findViewById(R.id.commentTextView);
//            final ImageButton likeButton = (ImageButton) rowView.findViewById(R.id.likeButton);
//            ImageButton commentButton = (ImageButton) rowView.findViewById(R.id.commentButton);
//            TextView captionText = (TextView) rowView.findViewById(R.id.content_text);
//
//            if(oneFeed.getUser_has_liked()){
//                likeButton.setImageDrawable(getResources().getDrawable(R.drawable.filled_heart));
//            }else {
//                likeButton.setImageDrawable(getResources().getDrawable(R.drawable.empty_heart));
//            }
//
//            //set text view styles
//            captionText.setTypeface(captionText.getTypeface(), Typeface.BOLD);
//            TextView likesFixText = (TextView) rowView.findViewById(R.id.likedText);
//            likesFixText.setTypeface(likesFixText.getTypeface(), Typeface.BOLD);
//            TextView commentFixText = (TextView) rowView.findViewById(R.id.commentText);
//            commentFixText.setTypeface(commentFixText.getTypeface(), Typeface.BOLD);
//            userName.setText(oneFeed.getDisplayName());
//            //locationName.setText(oneFeed.getLocation());
//            captionText.setText(oneFeed.getCaption());
//
//            //TODO: BEGIN 从数据库中，设置头像，发布的照片
//            userProfileImg.setImageDrawable(getResources().getDrawable(R.drawable.default_avatar));
//            photoImg.setImageDrawable(getResources().getDrawable(R.drawable.next));
//            //TODO: END
//
//            if (oneFeed.getLike().size() != 0) {
//                String likelist=oneFeed.getLike().toString();
//                likedText.setText(likelist.substring(1, likelist.length()-1));
//            } else {
//                likedText.setText("Nobody has liked yet.");
//            }
//
//            // set up the blank comment text in the view
//            if (oneFeed.getComment().size() != 0) {
//                String commentlist=oneFeed.getComment().toString().replace(',', '\n');
//                commentText.setText(commentlist.substring(1, commentlist.length() - 1));
//            } else {
//                commentText.setText("Nobody has commented yet.");
//            }
//            likeButton.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View arg0) {
//                    if (!oneFeed.getUser_has_liked()) {
//                        likeButton.setImageDrawable(rowView.getResources().getDrawable(R.drawable.filled_heart));
//                        //TODO: 方法一：
//                        //TODO: 客户端传输数据给服务端，update点赞信息；客户端获取服务端传送的likelist，更新到UI上
////                        feed_array=getFeeds_array();//获取最新的feeds_array
////                        oneFeed.update(feed_array.get(position));
////                        if (oneFeed.getLikelist().size() != 0) {
////                            String likelist=oneFeed.getLikelist().toString();
////                            likedText.setText(likelist.substring(1, likelist.length()-1));
////                        } else {
////                            likedText.setText("Nobody has liked yet.");
////                        }
//                        ///////////////////////////////////////////
//                        //TODO: 方法二：本地更新（不推荐，仅本地测试）
//                        oneFeed.setUser_has_liked(true);
//                        oneFeed.addLike("user3");
//                        if (oneFeed.getLike().size() != 0) {
//                            String likelist=oneFeed.getLike().toString();
//                            likedText.setText(likelist.substring(1, likelist.length()-1));
//                        } else {
//                            likedText.setText("Nobody has liked yet.");
//                        }
//                        ////////////////////////////////////////////
//                    } else {
//                        likeButton.setImageDrawable(rowView.getResources().getDrawable(R.drawable.empty_heart));
//                        //TODO: 方法一：
//                        //TODO: 客户端传输数据给服务端，update点赞信息；客户端获取服务端传送的likelist，更新到UI上
////                        feed_array=getFeeds_array();//获取最新的feeds_array
////                        oneFeed.update(feed_array.get(position));
////                        if (oneFeed.getLikelist().size() != 0) {
////                            String likelist=oneFeed.getLikelist().toString();
////                            likedText.setText(likelist.substring(1, likelist.length()-1));
////                        } else {
////                            likedText.setText("Nobody has liked yet.");
////                        }
//                        ///////////////////////////////////////////
//                        //TODO: 方法二：本地更新（不推荐，仅本地测试）
//                        oneFeed.setUser_has_liked(false);
//                        oneFeed.deleteLike("user3");
//                        if (oneFeed.getLike().size() != 0) {
//                            String likelist=oneFeed.getLike().toString();
//                            likedText.setText(likelist.substring(1, likelist.length()-1));
//                        } else {
//                            likedText.setText("Nobody has liked yet.");
//                        }
//                    }
//                }
//            });
//            commentButton.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View arg0) {
//                    mAmEtMsg.setVisibility(View.VISIBLE);
//                    send.setVisibility(View.VISIBLE);
//                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(UserViewActivity.this.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//                }
//            });
//            send.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View arg0) {
//                    String comment=mAmEtMsg.getText().toString();
//                    if(comment.length()==0){
//                        new AlertDialog.Builder(UserViewActivity.this)
//                                .setTitle("Error!")
//                                .setMessage("No Empty Comment.")
//                                // Specifying a listener allows you to take an action before dismissing the dialog.
//                                .setPositiveButton(android.R.string.yes, null)
//                                // A null listener allows the button to dismiss the dialog and take no further action.
//                                //.setNegativeButton(android.R.string.no, null)
//                                .setIcon(R.drawable.ic_error)
//                                .show();
//                    }else {
//                        //TODO: 把comment传给服务器
//                        feed_array = getFeeds_array();//获取最新的feeds_array
//                        oneFeed.update(feed_array.get(position));
//                        if (oneFeed.getComment().size() != 0) {
//                            oneFeed.addComment(comment);
//                            String commentlist = oneFeed.getComment().toString().replace(',', '\n');
//                            commentlist = commentlist.substring(1, commentlist.length() - 1);
//                            commentText.setText(commentlist);
//                        } else {
//                            commentText.setText("Nobody has commented yet.");
//                        }
//                    }
//                    send.setVisibility(View.GONE);
//                    mAmEtMsg.setVisibility(View.GONE);
//                    mAmEtMsg.setText("");
//                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(UserViewActivity.this.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(mAmEtMsg.getWindowToken(), 0);
//                }
//
//            });
//            return rowView;
//        }
//    }


}