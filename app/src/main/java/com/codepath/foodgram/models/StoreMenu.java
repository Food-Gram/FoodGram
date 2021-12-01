package com.codepath.foodgram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("FoodStoreMenu")
public class StoreMenu extends ParseObject {
    public static final String KEY_NAME= "foodName";
    public static final String KEY_IMAGE = "foodImage";
    public static final String KEY_STORE = "store";
    public static final String KEY_CREATED_KEY = "createdAt";
    public static final String KEY_PRICE = "price";

    public String getFoodName() {
        return getString(KEY_NAME);
    }

    public void setFoodName(String foodName) {
        put(KEY_NAME, foodName);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getStore() {
        return getParseUser(KEY_STORE);
    }

    public void setStore(ParseUser parseUser) {
        put(KEY_STORE, parseUser);
    }

    public double getPrice(){
        return getDouble(KEY_PRICE);}

    public void setPrice(double price) {
        put(KEY_PRICE, price);
    }
}
