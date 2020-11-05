package com.example.comp90018.DataModel;

import android.graphics.Bitmap;

public class Search {
    private String userName;
    private String petType;
    private Bitmap profileImage;
    private int profileImageNo;

    public Search() { }

    public Search(String userName, String petType, Bitmap profileImage) {
        this.userName = userName;
        this.petType = petType;
        this.profileImage = profileImage;
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

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }
    /**
     * 搜索条件
     *
     * @return
     */
    public String getSearchCondition() {
        StringBuilder searchStr = new StringBuilder();
        searchStr.append(userName);
        searchStr.append(petType);
        return searchStr.toString();
    }
}