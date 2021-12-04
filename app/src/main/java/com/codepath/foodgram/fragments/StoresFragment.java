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
import com.codepath.foodgram.adapters.StorePostAdapter;
import com.codepath.foodgram.models.FoodStorePost;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoresFragment extends Fragment {

    public static final String TAG = "StoresFragment";
    private RecyclerView rvPosts;
    protected StorePostAdapter adapter;
    protected List<FoodStorePost> storePosts;
    protected SwipeRefreshLayout swipeContainer;

    // onCreateView to inflate the view
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stores, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvPosts = view.findViewById(R.id.rvPosts);
        storePosts = new ArrayList<>();
        adapter = new StorePostAdapter(getContext(), storePosts);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                adapter.clear();
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
        ParseQuery<FoodStorePost> postParseQuery = new ParseQuery<FoodStorePost>(FoodStorePost.class);
        postParseQuery.include(FoodStorePost.KEY_AUTHOR);
        postParseQuery.setLimit(20);

        postParseQuery.findInBackground(new FindCallback<FoodStorePost>() {
            @Override
            public void done(List<FoodStorePost> posts, ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Issue with getting posts", e);
                    return;
                }
                storePosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }

}
