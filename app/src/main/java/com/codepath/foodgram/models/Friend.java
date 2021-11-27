package com.codepath.foodgram.models;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("FriendRequest")
public class Friend extends ParseObject {

    public static final String TAG = "Friend class";
    public static final String KEY_SENDER = "senderUsername";
    public static final String KEY_RECEIVER= "receiverUsername";
    public static final String KEY_STATUS = "status";
    //public static final String KEY_CREATED = "createdAt";
    private static int friendNum;

    public String getSender() {
        return getString(KEY_SENDER);
    }

    public void setSender(String userid) {
        put(KEY_SENDER, userid);
    }

    public String getReceiver() {
        return getString(KEY_RECEIVER);
    }

    public void setReceiver(String recieverId) {
        put(KEY_STATUS, recieverId);
    }

    public int getStatus() {
        return getInt(KEY_STATUS);
    }

    public void setStatus(int status) {
        put(KEY_STATUS, status);
    }

    public static int getFriendNum(){
        CountFriend();
        return friendNum;
    }

    private static void CountFriend(){
        // Specify which class to query
        ParseQuery<Friend> query1 = ParseQuery.getQuery(Friend.class);
        query1.whereEqualTo(Friend.KEY_SENDER, ParseUser.getCurrentUser());
        query1.whereEqualTo(Friend.KEY_STATUS, 1);
        query1.addDescendingOrder(Friend.KEY_CREATED_AT);
        query1.findInBackground(new FindCallback<Friend>() {
            @Override
            public void done(List<Friend> friends, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting friends", e);
                    return;
                }
                friendNum = friends.size();
            }
        });
        ParseQuery<Friend> query2 = ParseQuery.getQuery(Friend.class);
        query2.whereEqualTo(Friend.KEY_RECEIVER, ParseUser.getCurrentUser());
        query2.whereEqualTo(Friend.KEY_STATUS, 1);
        query2.addDescendingOrder(Friend.KEY_CREATED_AT);
        query2.findInBackground(new FindCallback<Friend>() {
            @Override
            public void done(List<Friend> friends, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting friends", e);
                    return;
                }
                friendNum += friends.size();
            }
        });

    }
}