package com.codepath.foodgram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("UserPosts")
public class Post extends ParseObject{
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_CREATED_KEY = "createdAt";
    public static final String KEY_LIKE = "likesCount";
    public static final String KEY_COMMENT = "commentsCount";

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

    public String getLike() {
        return getString(KEY_LIKE);
    }

    public String getCommentCount() { return getString(KEY_COMMENT); }

}