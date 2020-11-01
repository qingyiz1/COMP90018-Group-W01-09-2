package com.example.comp90018.Activity.Shake;

import java.util.ArrayList;

public class Moment {
    private String username, context, location;
    private ArrayList<String> likelist, comment;
    public Moment(String username, String context, String location, ArrayList<String> likelist, ArrayList<String> comment){
        this.username=username;
        this.context=context;
        this.location=location;
        this.likelist=likelist;
        this.comment=comment;
    }
    public String getUsername(){
        return username;
    }
    public String getContext(){
        return context;
    }
    public String getLocation(){
        return location;
    }
    public ArrayList<String> getLikelist(){
        return likelist;
    }
    public ArrayList<String> getComment(){
        return comment;
    }
}
