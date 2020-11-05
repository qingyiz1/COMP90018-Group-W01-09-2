package com.example.comp90018.DataModel;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by songxue on 2/10/2015.
 * This is a class for the feeds which are fetched from instagram. It provides all the necessary
 * attributes and their getter and setter
 */
public class Feed{
    private String displayName;
    private Bitmap userProfileImg;
    private Bitmap photo;
    private String caption;
    private Boolean user_has_liked;
    private ArrayList<String> comment;
    private ArrayList<String> like;

    public Feed(String displayName, Bitmap profileImg, Bitmap postPhoto, ArrayList<String> comment, ArrayList<String> like, String caption, Boolean user_has_liked) {
        this.displayName = displayName;
        this.userProfileImg=profileImg;
        this.photo=postPhoto;
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

    public void addComment(String comment) {
        this.comment.add(comment);
    }

    public ArrayList<String> getLike() {
        return like;
    }

    public void addLike(String name) {
        this.like.add(name);
    }

    public void deleteLike(String name){
        for(int i=0; i<this.like.size();i++){
            if(this.like.get(i).equals(name)){
                this.like.remove(i);
            }
        }
    }

    public void update(Feed f){
        this.displayName=f.getDisplayName();
        this.userProfileImg=f.getUserProfileImg();
        this.photo=f.getPhoto();
        this.comment = f.getComment();
        this.caption = f.getCaption();
        this.like=f.getLike();
        this.user_has_liked = f.getUser_has_liked();
    }

}
