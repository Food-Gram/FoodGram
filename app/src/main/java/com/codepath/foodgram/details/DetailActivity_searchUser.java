package com.codepath.foodgram.details;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.foodgram.R;
import com.codepath.foodgram.adapters.Users2Adapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

//this activity is called when the user click the word 'Friend' on the profile page

public class DetailActivity_searchUser extends AppCompatActivity {

    public static final String TAG = "DetailActivity_searchUser";
    private RecyclerView rvFriendList;
    private Users2Adapter adapter;
    private List<ParseUser> allusers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_friend_list);
        rvFriendList= findViewById(R.id.rvFriendList);

        String UserName = getIntent().getStringExtra("name");

        allusers = new ArrayList<>();
        adapter = new Users2Adapter(this, allusers);
        rvFriendList.setAdapter(adapter);
        rvFriendList.setLayoutManager(new LinearLayoutManager(this));

            queryUsers(UserName);

    }

    private void queryUsers(String UserName) {
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.whereMatches("username", UserName);

        query.findInBackground(new FindCallback<ParseUser>() {
            @SuppressLint("LongLogTag")
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting users", e);
                    return;
                }
                for(ParseUser user: users){
                    Log.i(TAG, "Users:" + user.getUsername());
                    allusers.add(user);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }




}