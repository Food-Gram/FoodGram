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
    private static int FollowedNum;
    private static int FollowerNum;

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

    public static int getFollowedNum(){
        CountFollowed();
        return FollowedNum;
    }

    public static int getFollowerNum(){
        CountFollower();
        return FollowerNum;
    }

    private static void CountFollower() {
        ParseQuery<Followed> query = ParseQuery.getQuery(Followed.class);
        query.include(Followed.KEY_FOLLOW);
        query.whereEqualTo(Followed.KEY_FOLLOW, ParseUser.getCurrentUser());
        query.addDescendingOrder(Friend.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Followed>() {
            @Override
            public void done(List<Followed> friends, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting follower", e);
                    return;
                }
                FollowerNum = friends.size();
            }
        });
    }

    private static void CountFollowed(){
        ParseQuery<Followed> query = ParseQuery.getQuery(Followed.class);
        query.whereEqualTo(Followed.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder(Friend.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Followed>() {
            @Override
            public void done(List<Followed> friends, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting followed store", e);
                    return;
                }
                FollowedNum = friends.size();
            }
        });
    }
}