package com.codepath.foodgram.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.foodgram.R;
import com.codepath.foodgram.adapters.UsersAdapter;
import com.codepath.foodgram.models.Friend;
import com.codepath.foodgram.models.StoreMenu;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity_FriendList extends AppCompatActivity {

    public static final String TAG = "Detail_Friendlist";
    private RecyclerView rvFriendList;
    private UsersAdapter adapter;
    private List<ParseUser> allfriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_friend_list);
        rvFriendList= findViewById(R.id.rvFriendList);

        allfriends = new ArrayList<>();
        adapter = new UsersAdapter(this, allfriends);
        rvFriendList.setAdapter(adapter);

        rvFriendList.setLayoutManager(new LinearLayoutManager(this));

        questUsers();
    }

    private void questUsers() {
        ParseQuery<Friend> query1 = ParseQuery.getQuery(Friend.class);
        query1.whereEqualTo(Friend.KEY_SENDER, ParseUser.getCurrentUser());
        query1.whereEqualTo(Friend.KEY_STATUS, 1);

        System.out.println(ParseUser.getCurrentUser().getUsername());
        query1.findInBackground(new FindCallback<Friend>() {
            @Override
            public void done(List<Friend> friends, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting friends", e);
                    return;
                }
                for(Friend friend: friends){
                    Log.i(TAG, "Friend:" + friend.getReceiver() );
                    allfriends.add(friend.getParseUser("recieverUsername"));
                }

                System.out.println(friends.size());
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
                    Log.i(TAG, "Friend:" + friend.getSender() +", Status :"+ friend.getStatus());
                    allfriends.add(friend.getParseUser("senderUsername"));

                }
                System.out.println(friends.size());
                adapter.notifyDataSetChanged();
            }
        });


    }
}