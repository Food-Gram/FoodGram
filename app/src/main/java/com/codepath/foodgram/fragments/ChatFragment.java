package com.codepath.foodgram.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.codepath.foodgram.LoginActivity;
import com.codepath.foodgram.R;
import com.codepath.foodgram.adapters.ChatAdapter;
import com.codepath.foodgram.adapters.NotificationAdapter;
import com.codepath.foodgram.details.DetailActivity_UserList;
import com.codepath.foodgram.details.DetailActivity_searchUser;
import com.codepath.foodgram.models.Friend;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    public static final String TAG = "ChatFragment";

    // these are for searching user
    private TextView tvSearchName; //searching username input
    private Button btnSearch;      //seach button

    //these are for the chatting friend list
    private RecyclerView rvFriends;
    protected ChatAdapter adapter;
    protected List<ParseUser> allFriends;



    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvSearchName = view.findViewById(R.id.searchID);
        btnSearch = view.findViewById(R.id.button_searchUser);

        rvFriends = view.findViewById(R.id.rvChatFriends);

        allFriends = new ArrayList<>();

        adapter = new ChatAdapter(getContext(), allFriends);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, tvSearchName.getText().toString());
                Intent i = new Intent(getContext(), DetailActivity_searchUser.class);
                i.putExtra("name", tvSearchName.getText().toString()); //pass the input user ID
                startActivity(i);
            }
        });

        rvFriends.setAdapter(adapter);

        // 4. Set the layout manager on the recycler view
        rvFriends.setLayoutManager(new LinearLayoutManager(getContext()));

        queryFriends();

    }

    private void queryFriends() {
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
                    allFriends.add(friend.getParseUser("receiverUsername"));
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
                    allFriends.add(friend.getParseUser("senderUsername"));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


}