package com.codepath.foodgram.details;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.foodgram.R;
import com.codepath.foodgram.adapters.UsersAdapter;
import com.codepath.foodgram.models.Followed;
import com.codepath.foodgram.models.Friend;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

//this activity is called when the user click the word 'Friend' on the profile page

public class DetailActivity_UserList extends AppCompatActivity {

    public static final String TAG = "Detail_Userlist";
    private RecyclerView rvFriendList;
    private UsersAdapter adapter;
    private List<ParseUser> allusers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_friend_list);
        rvFriendList= findViewById(R.id.rvFriendList);

        String type = getIntent().getStringExtra("type");

        allusers = new ArrayList<>();
        adapter = new UsersAdapter(this, allusers, type);
        rvFriendList.setAdapter(adapter);
        rvFriendList.setLayoutManager(new LinearLayoutManager(this));


        if(type.equals("Friend")){
            queryUsers();
        }
        else if (type.equals("Followed")){
            queryFollowed();
        }else{
            queryFollower();
        }

    }

    private void queryFollower() {
        ParseQuery<Followed> query = ParseQuery.getQuery(Followed.class);
        query.whereEqualTo(Followed.KEY_FOLLOW, ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Followed>() {
            @Override
            public void done(List<Followed> followeds, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting follower", e);
                    return;
                }
                for(Followed followed: followeds){
                    Log.i(TAG, "Follower:" + followed.getParseUser("username") );
                    allusers.add(followed.getParseUser("username"));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void queryFollowed() {
        ParseQuery<Followed> query = ParseQuery.getQuery(Followed.class);
        query.whereEqualTo(Followed.KEY_USER, ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Followed>() {
            @Override
            public void done(List<Followed> followeds, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting followed", e);
                    return;
                }
                for(Followed followed: followeds){
                    Log.i(TAG, "Followed Store:" + followed.getParseUser("followedFoodStore") );
                    allusers.add(followed.getParseUser("followedFoodStore"));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void queryUsers() {
        ParseQuery<Friend> query1 = ParseQuery.getQuery(Friend.class);
        query1.whereEqualTo(Friend.KEY_SENDER, ParseUser.getCurrentUser());
        query1.whereEqualTo(Friend.KEY_STATUS, 1);
        query1.findInBackground(new FindCallback<Friend>() {
            @Override
            public void done(List<Friend> friends, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting friends", e);
                    return;
                }
                for(Friend friend: friends){
                    Log.i(TAG, "Receiver Friend:" + friend.getParseUser("receiverUsername") +", Status :"+ friend.getStatus());
                    allusers.add(friend.getParseUser("receiverUsername"));
                }
            }
        });

        ParseQuery<Friend> query2 = ParseQuery.getQuery(Friend.class);
        query2.whereEqualTo(Friend.KEY_RECEIVER, ParseUser.getCurrentUser());
        query2.whereEqualTo(Friend.KEY_STATUS, 1);
        query2.findInBackground(new FindCallback<Friend>() {
            @Override
            public void done(List<Friend> friends, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting friends", e);
                    return;
                }
                for(Friend friend: friends){
                    Log.i(TAG, "Sender Friend:" + friend.getParseUser("senderUsername") +", Status :"+ friend.getStatus());

                    allusers.add(friend.getParseUser("senderUsername"));


                }
                adapter.notifyDataSetChanged();
            }
        });
    }




}