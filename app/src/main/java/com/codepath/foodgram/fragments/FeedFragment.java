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

import com.codepath.foodgram.Adapter.FeedAdapter;
import com.codepath.foodgram.Model.Feed;
import com.codepath.foodgram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    private final String TAG = "FeedFragment";
    private RecyclerView rvFeed;
    protected FeedAdapter adapter;
    protected List<Feed> feed;

    // onCreateView to inflate the view
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compose, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvFeed = view.findViewById(R.id.rvFeed);

        // 1. Create the data source
        feed = new ArrayList<>();

        // 2. Create the adapter
        adapter = new FeedAdapter(getContext(), feed);

        // 3. Set the adapter on the recycler view
        rvFeed.setAdapter(adapter);

        // 4. Set the layout manager on the recycler view
        rvFeed.setLayoutManager(new LinearLayoutManager(getContext()));

        queryPost();
    }

    public void queryPost() {
        ParseQuery<Feed> postParseQuery = new ParseQuery<Feed>(Feed.class);
        postParseQuery.include(Feed.KEY_AUTHOR);
        postParseQuery.setLimit(20);

        postParseQuery.findInBackground(new FindCallback<Feed>() {
            @Override
            public void done(List<Feed> posts, ParseException e) {

                if (e != null) {
                    Log.e(TAG, "Error with query");
                    e.printStackTrace();
                    return;
                }

                adapter.clear();
                adapter.addAll(posts);

                for (int i = 0; i < posts.size(); i++ ){
                    Feed post = posts.get(i);
                    Log.d(TAG, "Post: " + post.getDescription() + "username: " + post.getUser().getUsername() + "image url: " + post.getImage().getUrl());
                }
            }
        });
    }

}
