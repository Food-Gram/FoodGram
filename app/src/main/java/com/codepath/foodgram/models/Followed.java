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


    public String getSender() {
        return getString(KEY_USER);
    }

    public void setSender(String userId) {
        put(KEY_USER, userId);
    }

    public String getFollowed() {
        return getString(KEY_FOLLOW);
    }

    public void setFollowedNum(String followedId){put(KEY_FOLLOW, followedId);}

}