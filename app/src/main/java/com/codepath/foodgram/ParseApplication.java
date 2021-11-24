package com.codepath.foodgram;

import android.app.Application;

import com.codepath.foodgram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register your Parse models
        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("ZdOgcA47mjusdkhurFZbGioYSxewwoyWTawOU95K")
                .clientKey("IyHqbCN2W4P1izfMlT9ujr6nJelRwYGpzjnJe8YA")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
