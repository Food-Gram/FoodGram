package com.codepath.foodgram.models;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Rating")
public class RateStore extends ParseObject {

    public static final String TAG = "Rating";
    public static final String KEY_USER = "username";
    public static final String KEY_STORE = "FoodStore";
    public static final String Key_RATE = "rating";
    //public static final String KEY_CREATED = "createdAt";


    public String getUser() {
        return getString(KEY_USER);
    }

    public void setUser(String userId) {
        put(KEY_USER, userId);
    }

    public String getStore() {
        return getString(KEY_STORE);
    }

    public void setStore(String storeId){put(KEY_STORE, storeId);}

    public int getRate() {
        return getInt(Key_RATE);
    }

    public void setRate(int rating){put(Key_RATE, rating);}

}