package com.example.comp90018.DataModel;

import android.net.Uri;
import com.google.firebase.Timestamp;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Post implements Serializable {
    Timestamp dateCreated;
    String id;

    public String getAuthorName() {
        return authorName;
    }

    String authorName;

    public String getAuthorUid() {
        return authorUid;
    }

    String authorUid;

    public String getContent() {
        return content;
    }

    String content;

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(String user) {
       this.likes.add(user);
    }

    public void deleteLikes(String user) {
        this.likes.remove(user);
    }


    private ArrayList<String> likes;
    Uri profile_pic, content_pic;

    public ArrayList<Comment> getComments() {
        return comments;
    }

    private ArrayList<Comment> comments;

    public boolean HasLiked(String uid) {
        if(likes.contains(uid)){
            return hasLiked =  true;
        }else{
            return hasLiked = false;
        }
    }

    public void setHasLiked(boolean hasLiked) {
        this.hasLiked = hasLiked;
    }

    boolean hasLiked;

    public Post(){

    }

    public Post(String authorUid, String authorName, String id, String content, ArrayList<Comment> comments, ArrayList<String> likes, Timestamp dateCreated){
        this.id = id;
        this.authorName = authorName;
        this.dateCreated = dateCreated;
        this.authorUid = authorUid;
        this.content = content;
        this.comments = comments;
        this.likes = likes;
    }

    public String getId(){
        return this.id;
    }

    public Map<String, Object> toMap() {
        // Create a new user with a first and last name
        Map<String, Object> newPost = new HashMap<>();
        if(this.id != null){
            newPost.put("id", this.id);
        }
        if(this.authorName != null){
            newPost.put("authorName", this.authorName);
        }
        if(this.authorUid != null){
            newPost.put("authorUid", this.authorUid);
        }
        if(this.content != null){
            newPost.put("content", this.content);
        }
        if(this.dateCreated != null){
            newPost.put("dateCreated", this.dateCreated);
        }
        if(this.profile_pic != null){
            newPost.put("sex",this.profile_pic);
        }
        if(this.content_pic != null){
            newPost.put("species",this.content_pic);
        }
        if(this.comments != null){
            newPost.put("comments", this.comments);
        }
        if(this.likes != null){
            newPost.put("likes", this.likes);
        }
        return newPost;
    }

    public void setId(String id) {
        this.id = id;
    }
}
