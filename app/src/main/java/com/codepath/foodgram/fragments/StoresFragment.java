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
import com.codepath.foodgram.models.Followed;
import com.codepath.foodgram.models.FoodStorePost;
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
public class StoresFragment extends Fragment {

    public static final String TAG = "StoresFragment";
    private RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<FoodStorePost> storePosts;
    protected SwipeRefreshLayout swipeContainer;

    private static int position = 0;

    // onCreateView to inflate the view
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stores, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        rvPosts = view.findViewById(R.id.rvPosts_Store);
        storePosts = new ArrayList<>();

        adapter = new PostsAdapter(getContext(), null, storePosts);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                setPosition(0);
                queryStorePosts();
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

        queryStorePosts();
    }

    public void queryStorePosts() {
        ParseQuery<Followed> query = ParseQuery.getQuery(Followed.class);
        query.whereEqualTo(Followed.KEY_USER, ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Followed>() {
            @Override
            public void done(List<Followed> followeds, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting followed posts", e);
                    return;
                }
                for(Followed follow: followeds){
                    Log.i(TAG, "Followed:" + follow.getParseUser("followedFoodStore"));
                    ParseUser user = follow.getParseUser("followedFoodStore");
                    ParseQuery<FoodStorePost> postParseQuery = new ParseQuery<FoodStorePost>(FoodStorePost.class);
                    postParseQuery.whereEqualTo(FoodStorePost.KEY_AUTHOR, user);
                    postParseQuery.addDescendingOrder(FoodStorePost.KEY_CREATED_KEY);
                    postParseQuery.findInBackground(new FindCallback<FoodStorePost>() {
                        @Override
                        public void done(List<FoodStorePost> posts, ParseException e) {
                            if (e != null) {
                                Log.d(TAG, "Issue with getting posts", e);
                                return;
                            }
                            for (FoodStorePost post : posts) {
                                try {
                                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().fetchIfNeeded().getUsername());
                                } catch (ParseException parseException) {
                                    parseException.printStackTrace();
                                }
                            }
                            storePosts.addAll(posts);
                            Collections.sort(storePosts, new Comparator<FoodStorePost>(){
                                @Override
                                public int compare(FoodStorePost post1, FoodStorePost post2) {
                                    return (post1.getCreatedAt().compareTo(post2.getCreatedAt()) * -1);
                                }
                            });
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

        ParseQuery<FoodStorePost> postParseQuery = new ParseQuery<FoodStorePost>(FoodStorePost.class);
        postParseQuery.whereEqualTo(FoodStorePost.KEY_AUTHOR,ParseUser.getCurrentUser());
        postParseQuery.addDescendingOrder(FoodStorePost.KEY_CREATED_KEY);
        postParseQuery.findInBackground(new FindCallback<FoodStorePost>() {
            @Override
            public void done(List<FoodStorePost> posts, ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Issue with getting posts", e);
                    return;
                }
                for (FoodStorePost post : posts) {
                    try {
                        Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().fetchIfNeeded().getUsername());
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
                }
                storePosts.addAll(posts);
                Collections.sort(storePosts, new Comparator<FoodStorePost>(){
                    @Override
                    public int compare(FoodStorePost post1, FoodStorePost post2) {
                        return (post1.getCreatedAt().compareTo(post2.getCreatedAt()) * -1);
                    }
                });
                adapter.notifyDataSetChanged();
            }
        });
    }


    public static void setPosition(int pos){
        position = pos;
    }
}
