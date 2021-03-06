//package com.codepath.foodgram.fragments;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.codepath.foodgram.R;
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class FriendsFragment extends Fragment {
//
//    public static final String TAG = "FriendsFragment";
//
//    public FriendsFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_friends, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//    }
//
//}

package com.codepath.foodgram.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.codepath.foodgram.R;
import com.codepath.foodgram.adapters.PostsAdapter;
import com.codepath.foodgram.models.FoodStorePost;
import com.codepath.foodgram.models.Friend;
import com.codepath.foodgram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */

// this fragment is about the friends' posts

public class FriendsFragment extends Fragment {

    private final String TAG = "FriendsFragment";
    private RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    protected SwipeRefreshLayout swipeContainer;


    // onCreateView to inflate the view
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvPosts = view.findViewById(R.id.rvPosts);

        // 1. Create the data source
        allPosts = new ArrayList<>();

        // 2. Create the adapter
        adapter = new PostsAdapter(getContext(), allPosts, null);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.searching_Container);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                adapter.clear();
                queryUsers();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // 3. Set the adapter on the recycler view
        rvPosts.setAdapter(adapter);

        // 4. Set the layout manager on the recycler view
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        queryUsers();
    }

    public void queryUsers() {
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
                    ParseUser user = friend.getParseUser("receiverUsername");
                    querypost(user);
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
                    ParseUser user =  friend.getParseUser("senderUsername");
                    querypost(user);
                }
                querypost(ParseUser.getCurrentUser());
            }
        });
    }

    private void querypost(ParseUser user){
        ParseQuery<Post> postParseQuery = new ParseQuery<Post>(Post.class);
        postParseQuery.whereEqualTo(Post.KEY_AUTHOR, user);
        postParseQuery.addDescendingOrder(FoodStorePost.KEY_CREATED_KEY);
        postParseQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Post post : posts) {
                    try {
                        Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().fetchIfNeeded().getUsername());
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
                }
                allPosts.addAll(posts);
                Collections.sort(allPosts, new Comparator<Post>(){
                    @Override
                    public int compare(Post post1, Post post2) {
                        return (post1.getCreatedAt().compareTo(post2.getCreatedAt()) * -1);
                    }
                });
                adapter.notifyDataSetChanged();
            }
        });
    }
}
