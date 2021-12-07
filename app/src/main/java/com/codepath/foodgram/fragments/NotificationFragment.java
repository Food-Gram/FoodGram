package com.codepath.foodgram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.foodgram.R;
import com.codepath.foodgram.adapters.NotificationAdapter;
import com.codepath.foodgram.adapters.PostsAdapter;
import com.codepath.foodgram.models.Friend;
import com.codepath.foodgram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

//this frament is the notification fragment. in this fragment the user can see the
// friend request that send to this user.
public class NotificationFragment extends Fragment {

    private final String TAG = "NotificationFragment";
    private RecyclerView rvNotification;
    protected NotificationAdapter adapter;
    protected List<ParseUser> allRequests;
    protected SwipeRefreshLayout swipeContainer;


    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvNotification = view.findViewById(R.id.rvNotification);

        // 1. Create the data source
        allRequests = new ArrayList<>();

        // 2. Create the adapter
        adapter = new NotificationAdapter(getContext(), allRequests);

        // Lookup the swipe container view
//        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.searching_Container);
//        // Setup refresh listener which triggers new data loading
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Your code to refresh the list here.
//                // Make sure you call swipeContainer.setRefreshing(false)
//                // once the network request has completed successfully.
//                adapter.clear();
//                queryRequest();
//                swipeContainer.setRefreshing(false);
//            }
//        });
        // Configure the refreshing colors
//        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);

        // 3. Set the adapter on the recycler view
        rvNotification.setAdapter(adapter);

        // 4. Set the layout manager on the recycler view
        rvNotification.setLayoutManager(new LinearLayoutManager(getContext()));

        queryRequest();
    }

    //search the friend requests that send to the current user
    public void queryRequest() {
        ParseQuery<Friend> query1 = ParseQuery.getQuery(Friend.class);
        query1.whereEqualTo(Friend.KEY_RECEIVER, ParseUser.getCurrentUser());
        query1.whereEqualTo(Friend.KEY_STATUS, -1);
        query1.findInBackground(new FindCallback<Friend>() {
            @Override
            public void done(List<Friend> friends, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting requests", e);
                    return;
                }
                for(Friend friend: friends){
                    try {
                        Log.i(TAG, "Who send Friend request:" + friend.getParseUser("senderUsername").fetchIfNeeded().getUsername() +", Status :"+ friend.getStatus());
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
                    allRequests.add(friend.getParseUser("senderUsername"));
                }
                adapter.notifyDataSetChanged();
            }
        });
        Log.i(TAG, "# of request:" + allRequests.size());


    }
}