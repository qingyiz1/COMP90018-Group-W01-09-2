package com.example.comp90018.Activity.Home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.comp90018.Activity.Shake.UserViewActivity;
import com.example.comp90018.DataModel.UserModel;
import com.example.comp90018.R;
import com.example.comp90018.utils.GlideApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SearchAdapter extends BaseAdapter {
    private static final String TAG = "SearchAdapter";
    private ArrayList<UserModel> users;
    private Context context;
    private ImageView userImg;
    private TextView userName;
    private TextView petType;
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();

    // image storage
    private StorageReference mStorageImagesRef = FirebaseStorage.getInstance().getReference().child("images");
    View rowView;

    public SearchAdapter(Context c, ArrayList<UserModel> user){
        context = c;
        users = user;
    }

    public void setUsers(ArrayList<UserModel> users) {
        this.users = users;
    }

    @Override
    public int getCount() {
        if(users == null) {
            return 0;
        }
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    /*
    sets the Profile image of the recommended users, their username, gender
    and city registered on our application.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        rowView = inflater.inflate(R.layout.search, null, true);
        final UserModel userProfile = users.get(position);

        userImg = rowView.findViewById(R.id.userImg);
        userName = (TextView) rowView.findViewById(R.id.userName);
        userName.setTypeface(userName.getTypeface(), Typeface.BOLD);
        petType = (TextView) rowView.findViewById(R.id.pet_type);
        userName.setText(userProfile.nickName);
        petType.setText(userProfile.species);

        Log.d(TAG, "getView: "+userProfile.getUid());
        loadWithGlide(rowView,R.id.userImg,userProfile.getUid());
        userImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(context, UserViewActivity.class);
                intent.putExtra("USER_UID", userProfile.getUid());
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
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
        // [END storage_load_with_glide]
    }
}
