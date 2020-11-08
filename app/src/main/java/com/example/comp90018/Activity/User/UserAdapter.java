package com.example.comp90018.Activity.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.comp90018.DataModel.UserModel;
import com.example.comp90018.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<UserModel> mUser;
    protected static final String TAG = "FOLLOW";

    protected FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private StorageReference mStorageImagesRef = FirebaseStorage.getInstance().getReference().child("images");

    public UserAdapter(Context mContext, ArrayList<UserModel> mUser) {
        this.mContext = mContext;
        this.mUser = mUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, viewGroup, false);
        return new UserAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {


        final UserModel userProfile = mUser.get(i);

        viewHolder.btn_follow.setVisibility(View.VISIBLE);

        viewHolder.nickname.setText(userProfile.nickName);
        viewHolder.pet_type.setText(userProfile.species);
        Glide.with(mContext).load(mStorageImagesRef).into(viewHolder.image_profile);
        isFollowing(userProfile.getUid(), viewHolder.btn_follow);

        // If the user is the current user, hide the follow button
        if (userProfile.getUid().equals(db.collection("users").document(mAuth.getUid()))) {
            viewHolder.btn_follow.setVisibility(View.GONE);
        }

        // If the user is not following, add to following document, if following remove from the
        // following document.
        viewHolder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.btn_follow.getText().toString().equals("follow")) {
                    db.document("users/following").set(userProfile.getUid());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nickname;
        public CircleImageView image_profile;
        public Button btn_follow;
        public TextView pet_type;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nickname = itemView.findViewById(R.id.nickname);
            image_profile = itemView.findViewById(R.id.image_profile);
            btn_follow = itemView.findViewById(R.id.btn_follow);
            pet_type = itemView.findViewById(R.id.pet_type);


        }
    }

    // Switch the button text between following and follow
    private void isFollowing(String userid, final Button button) {
        DocumentReference reference = db.document("users/following");
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    button.setText("following");
                } else {
                    button.setText("follow");
                }
            }
        });


    }


}
