package com.example.comp90018.DataModel;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Search {
    private String userName;
    private String gender;
    private Bitmap profileImage;
    private String fullName;
    private int profileImageNo;

    public Search() { }

    public Search(String userName, String gender, Bitmap profileImage) {
        this.userName = userName;
        this.gender = gender;
        this.profileImage = profileImage;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getProfileImageNo() {
        return profileImageNo;
    }

    public void setProfileImageNo(int profileImageNo) {
        this.profileImageNo = profileImageNo;
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

}