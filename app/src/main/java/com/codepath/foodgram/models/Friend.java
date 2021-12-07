package com.codepath.foodgram.models;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


//
@ParseClassName("FriendRequest")
public class Friend extends ParseObject {

    public static final String TAG = "Friendclass";
    public static final String KEY_OBJECTID = "objectId";
    public static final String KEY_SENDER = "senderUsername";
    public static final String KEY_RECEIVER= "receiverUsername";
    public static final String KEY_STATUS = "status";
    //public static final String KEY_CREATED = "createdAt";

    public String getObjectId() {
        return getString(KEY_OBJECTID);
    }

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
        put(KEY_RECEIVER, recieverId);
    }

    public int getStatus() {
        return getInt(KEY_STATUS);
    }

    public void setStatus(int status) {
        put(KEY_STATUS, status);
    }




    public void setSender2(ParseUser user) {
        put(KEY_SENDER, user);
    }

    public void setReceiver2(ParseUser user) {
        put(KEY_RECEIVER, user);
    }


}