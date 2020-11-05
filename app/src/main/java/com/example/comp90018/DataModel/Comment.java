package com.example.comp90018.DataModel;

import java.io.Serializable;

public class Comment implements Serializable {
    public String author, content;

    public Comment(String author,String content){
        this.author = author;
        this.content = content;
    }

    public Comment(){

    }
}
