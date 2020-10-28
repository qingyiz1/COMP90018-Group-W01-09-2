package com.example.comp90018.Activity.Home;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by songxue on 2/10/2015.
 * This is a class for the feeds which are fetched from instagram. It provides all the necessary
 * attributes and their getter and setter
 */
public class Feed{
    private String displayName;
    private String userProfileImgURL;
    private String photoURL;
    private Bitmap userProfileImg;
    private Bitmap photo;
    private String location;
    private String mediaID;
    private String caption;
    private Boolean user_has_liked;
    private ArrayList<String> comment;
    private ArrayList<String> like;
    private double latitude;
    private double longitude;
    private double distance;

    public Feed(String displayName, String userProfileImgURL, String photoURL, String location, ArrayList<String> comment, ArrayList<String> like, String caption, Boolean user_has_liked) {
        this.displayName = displayName;//"Joyce";
        this.userProfileImgURL = userProfileImgURL;
        this.photoURL = photoURL;
        this.comment = comment;
        this.like = like;
        this.caption = caption;
        this.user_has_liked = user_has_liked;

    }

    public Feed(){
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Boolean getUser_has_liked() {
        return user_has_liked;
    }

    public void setUser_has_liked(Boolean user_has_liked) {
        this.user_has_liked = user_has_liked;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserProfileImgURL() {
        return userProfileImgURL;
    }

    public void setUserProfileImgURL(String userProfileImgURL) {
        this.userProfileImgURL = userProfileImgURL;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public Bitmap getUserProfileImg() {
        return userProfileImg;
    }

    public void setUserProfileImg(Bitmap userProfileImg) {
        this.userProfileImg = userProfileImg;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public ArrayList<String> getComment() {
        return comment;
    }

    public void setComment(ArrayList<String> comment) {
        this.comment = comment;
    }

    public ArrayList<String> getLike() {
        return like;
    }

    public void setLike(ArrayList<String> like) {
        this.like = like;
    }

    public String getMediaID() {
        return mediaID;
    }

    public void setMediaID(String mediaID) {
        this.mediaID = mediaID;
    }

}
