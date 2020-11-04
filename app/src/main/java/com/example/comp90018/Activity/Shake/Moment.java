package com.example.comp90018.Activity.Shake;

import java.util.ArrayList;

public class Moment {
    private String username, context, location;
    private ArrayList<String> likelist, commentlist;
    private boolean user_has_liked;
    public Moment(String username, String context, String location, ArrayList<String> likelist, ArrayList<String> comment, Boolean user_has_liked){
        this.username=username;
        this.context=context;
        this.location=location;
        this.likelist=likelist;
        this.commentlist=comment;
        this.user_has_liked = user_has_liked;
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
    public ArrayList<String> getCommentList(){
        return commentlist;
    }
    public Boolean getUser_has_liked(){return user_has_liked;}
    public void setUser_has_liked(boolean user_has_liked){
        this.user_has_liked=user_has_liked;
    }
    public void addLikeList(String name){
        likelist.add(name);
    }
    public void addCommentList(String comment){
        commentlist.add(comment);
    }
    public void deleteLikeList(String name){
        for(int i=0; i<likelist.size();i++){
            if(likelist.get(i).equals(name)){
                likelist.remove(i);
            }
        }
    }
    public void update(Moment m){
        this.username=m.getUsername();
        this.context=m.getContext();
        this.location=m.getLocation();
        this.likelist=m.getLikelist();
        this.commentlist=m.getCommentList();
        this.user_has_liked = m.getUser_has_liked();
    }

}
