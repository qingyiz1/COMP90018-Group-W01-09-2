package com.example.comp90018.DataModel;

import android.net.Uri;
import com.google.firebase.Timestamp;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Post implements Serializable {
    Timestamp dateCreated;
    String id,authorName,authorUid, content;
    Uri profile_pic, content_pic;
    Comment[] comment;

    public Post(String authorUid,String content,Timestamp dateCreated){
        this.dateCreated = dateCreated;
        this.authorUid = authorUid;
        this.content = content;
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
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
        return newPost;
    }

}
