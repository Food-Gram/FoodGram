package com.codepath.foodgram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("FoodStorePost")
public class FoodStorePost extends ParseObject{

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_CREATED_KEY = "createdAt";
    public static final String KEY_LIKE = "likesCount";
    public static final String KEY_COMMENT = "commentsCount";


    public static final String KEY_SHARE = "shareCount";


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

    public int getCommentCount() { return getInt(KEY_COMMENT); }

    public int getShareCount(){return getInt((KEY_SHARE));}

}