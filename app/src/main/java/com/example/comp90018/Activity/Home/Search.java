package com.example.comp90018.Activity.Home;

import android.graphics.Bitmap;

public class Search {
    private String userName;
    private String gender;
    private String imgURL;
    private Bitmap profileImage;
    private String fullName;

    public Search() { }

    public Search(String userName, String gender, String imgURL, Bitmap profileImage) {
        this.userName = userName;
        this.gender = gender;
        this.imgURL = imgURL;
        this.profileImage = profileImage;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

}