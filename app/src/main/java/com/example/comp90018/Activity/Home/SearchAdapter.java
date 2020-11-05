package com.example.comp90018.Activity.Home;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.comp90018.DataModel.Search;
import com.example.comp90018.R;

import java.util.ArrayList;

public class SearchAdapter extends BaseAdapter {
    private ArrayList<Search> users;
    private Context context;
    private ImageView userImg;
    private TextView userName;
    private TextView petType;

    public SearchAdapter(Context c, ArrayList<Search> user){
        context = c;
        users = user;
    }

    public void setUsers(ArrayList<Search> users) {
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

        View rowView = inflater.inflate(R.layout.search, null, true);
        Search userProfile = users.get(position);

        userImg = (ImageView) rowView.findViewById(R.id.userImg);
        userName = (TextView) rowView.findViewById(R.id.userName);
        userName.setTypeface(userName.getTypeface(), Typeface.BOLD);
        petType = (TextView) rowView.findViewById(R.id.pet_type);
        String[] fullUserName = userProfile.getUserName().toString().split("@");
        userName.setText(fullUserName[0]);
        String[] putType = userProfile.getPetType().toString().split("@");
        petType.setText(putType[0]);

        userImg.setImageBitmap(userProfile.getProfileImage());
//        Bitmap profileImage = userProfile.getProfileImage();
//        if (profileImage != null) {
//            userImg.setImageBitmap(BitmapStore.getCroppedBitmap(profileImage));
//        }else{
//            Drawable drawable = rowView.getResources().getDrawable(R.drawable.touxiang);
//            Bitmap defaultImage = ((BitmapDrawable) drawable).getBitmap();
//            userImg.setImageBitmap(defaultImage);
//        }

        return rowView;
    }
}
