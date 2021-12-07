package com.codepath.foodgram.models;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Follow")
public class Followed extends ParseObject {

    public static final String TAG = "Followed";
    public static final String KEY_USER = "username";
    public static final String KEY_FOLLOW = "followedFoodStore";
    //public static final String KEY_CREATED = "createdAt";


    public ParseUser getSender() {
        return getParseUser(KEY_USER);
    }

    public void setSender(ParseUser userId) {
        put(KEY_USER, userId);
    }

    public ParseUser getFollowed() {
        return getParseUser(KEY_FOLLOW);
    }

    public void setFollowed(ParseUser followedId){put(KEY_FOLLOW, followedId);}

}