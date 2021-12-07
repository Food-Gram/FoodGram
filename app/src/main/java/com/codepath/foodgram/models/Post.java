package com.codepath.foodgram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ParseClassName("UserPosts")
public class Post extends ParseObject{
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_CREATED_KEY = "createdAt";
    public static final String KEY_LIKE = "likesCount";
    public static final String KEY_COMMENT = "commentsCount";
    public static final String KEY_LIKEUSER = "likes";
    public static final String KEY_COMMENTINFO = "comments";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_AUTHOR);
    }

    public void setUser(ParseUser parseUser) {
        put(KEY_AUTHOR, parseUser);
    }

    public int getLike(){
        return getInt(KEY_LIKE);}

    public void LikeIncrement(){
        int like = getLike() + 1;
        put(KEY_LIKE, like);}

    public void LikeDecrement(){
        int like = getLike() - 1;
        put(KEY_LIKE, like);}

    public int getCommentCount() { return getInt(KEY_COMMENT); }

    public void CommentIncrement(){
        int comment = getCommentCount() + 1;
        put(KEY_COMMENT, comment);}


    public List<String> getLikeUsers (){return getList(KEY_LIKEUSER);}

    public void setLikeUsers(String username, List<String> users){
        users.add(username);
        put(KEY_LIKEUSER,users);
    }

    public void RemoveLikeUsers(String username, List<String> users){
        users.remove(username);
        put(KEY_LIKEUSER,users);
    }

    public List<Map<String, String>> getCommentInfo() {return getList(KEY_COMMENTINFO);}

    public void setCommentInfo(Map info, List<Map<String,String>> users){
        users.add(info);
        put(KEY_COMMENTINFO,users);
    }

    public void RemoveCommentInfo(Map info, List<String> users){
        users.remove(info);
        put(KEY_COMMENTINFO,users);
    }
}