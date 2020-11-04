package com.example.comp90018.DataModel;

import android.net.Uri;
import com.google.firebase.Timestamp;
import java.io.Serializable;

public class Post implements Serializable {
    Timestamp dateCreated;
    String id,authorName,authorUid, content;
    Uri profile_pic, content_pic;
    Comment[] comment;

    public Post(String authorUid,String content,Timestamp dateCreated){
        this.id = authorUid+dateCreated;
        this.dateCreated = dateCreated;
        this.authorUid = authorUid;
        this.content = content;
    }

    public String getId(){
        return this.id;
    }

}
