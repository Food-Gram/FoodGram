package com.codepath.foodgram.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.foodgram.R;
import com.codepath.foodgram.adapters.ImageAdapter;
import com.codepath.foodgram.adapters.ProfileAdapter;
import com.codepath.foodgram.details.DetailActivity_UserList;
import com.codepath.foodgram.models.Followed;
import com.codepath.foodgram.models.Friend;
import com.codepath.foodgram.models.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    private ImageView ivUserIcon;
    private TextView tvUsername;
    private TextView tvPostNum;
    private TextView tvFriendNum;
    private TextView tvFollowed;
    private RecyclerView rvProfile;

    private SwipeRefreshLayout swipeContainer;
    private static int position = 0;
    private int friendNum;

    private ProfileAdapter adapter;
    private ImageAdapter Iadapter;
    private List<Post> allposts;

    private BottomNavigationView profileNavigation;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivUserIcon = view.findViewById(R.id.searching_UserIcon);
        tvUsername = view.findViewById(R.id.searching_profile);
        tvPostNum = view.findViewById(R.id.searching_PostNum);
        tvFollowed = view.findViewById(R.id.searching_Followed);
        tvFriendNum = view.findViewById(R.id.searching_FriendNum);
        rvProfile = view.findViewById(R.id.rvProfile);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.searching_Container);

        // Basic information of current user
        ParseUser currentUser = ParseUser.getCurrentUser();
        tvUsername.setText(currentUser.getUsername());
        Glide.with(getContext()).load(currentUser.getParseFile("icon").getUrl())
                .transform(new CenterInside(), new RoundedCorners(100)).into(ivUserIcon);
        queryFriend();
        queryFollowed();

        // Bottom Navigation
        profileNavigation = view.findViewById(R.id.searching_Navigation);
        Menu menu = profileNavigation.getMenu();


        profileNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_post_history:
                        allposts = new ArrayList<>();
                        adapter = new ProfileAdapter(getContext(), allposts, null);
                        rvProfile.setAdapter(adapter);
                        rvProfile.setLayoutManager(new LinearLayoutManager(getContext()));

                        queryPosts("Profile");
                        refresh("Profile");
                        break;
                    case R.id.action_grid_posts:
                        allposts = new ArrayList<>();

                        Iadapter = new ImageAdapter(getContext(), allposts);
                        rvProfile.setAdapter(Iadapter);
                        rvProfile.setLayoutManager(new GridLayoutManager(getContext(),3));

                        queryPosts("Image");
                        refresh("Image");
                        break;
                }
                return true;
            }
        });

        // Default selection
        profileNavigation.setSelectedItemId(R.id.action_grid_posts);
        menu.findItem(R.id.action_menu).setVisible(false);

        // Friend list detail
        tvFriendNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), DetailActivity_UserList.class);
                i.putExtra("type", "Friend");
                startActivity(i);
            }
        });

        //Followed list detail
        tvFollowed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(getContext(), DetailActivity_UserList.class);
                i.putExtra("type", "Followed");
                startActivity(i);
            }
        });
    }

    private void refresh(String string){
        // swipe to refresh
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(string.equals("Profile")) {
                    adapter.clear();
                    setPosition(0);
                    queryPosts("Profile");
                }else{
                    Iadapter.clear();
                    setPosition(0);
                    queryPosts("Image");
                }
                swipeContainer.setRefreshing(false);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }



    private void queryPosts(String string) {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_AUTHOR);
        query.whereEqualTo(Post.KEY_AUTHOR, ParseUser.getCurrentUser());
        // make most recently post show first
        query.addDescendingOrder(Post.KEY_CREATED_KEY);

        if(string.equals("Image")) { query.setLimit(9);}
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for(Post post: posts){
                    Log.i(TAG, "Post:" + post.getDescription() + ", username:" + post.getUser() );
                }
                allposts.addAll(posts);
                tvPostNum.setText("Posts : " + String.valueOf(allposts.size()));
                
                if(string.equals("Profile")) {
                    adapter.notifyDataSetChanged();
                }else{
                    Iadapter.notifyDataSetChanged();
                }
                
                rvProfile.smoothScrollToPosition(position);
            }
        });

    }

    private void queryFriend() {
        // Specify which class to query
        ParseQuery<Friend> query1 = ParseQuery.getQuery(Friend.class);
        query1.whereEqualTo(Friend.KEY_SENDER, ParseUser.getCurrentUser());
        query1.whereEqualTo(Friend.KEY_STATUS, 1);
        query1.addDescendingOrder(Friend.KEY_CREATED_AT);
        query1.findInBackground(new FindCallback<Friend>() {
            @Override
            public void done(List<Friend> friends, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting friends", e);
                    return;
                }
                for(Friend friend: friends){
                    Log.i(TAG, "Friend:" + friend.getReceiver() + ", Status :"+ friend.getStatus());

                }
                friendNum = friends.size();
            }
        });
        ParseQuery<Friend> query2 = ParseQuery.getQuery(Friend.class);
        query2.whereEqualTo(Friend.KEY_RECEIVER, ParseUser.getCurrentUser());
        query2.whereEqualTo(Friend.KEY_STATUS, 1);
        query2.addDescendingOrder(Friend.KEY_CREATED_AT);
        query2.findInBackground(new FindCallback<Friend>() {
            @Override
            public void done(List<Friend> friends, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting friends", e);
                    return;
                }
                for(Friend friend: friends){
                    Log.i(TAG, "Friend:" + friend.getSender() +", Status :"+ friend.getStatus());
                }
                friendNum += friends.size();
                tvFriendNum.setText("Friends : "+ friendNum);
            }
        });
    }

    private void queryFollowed() {
        ParseQuery<Followed> query = ParseQuery.getQuery(Followed.class);
        query.whereEqualTo(Followed.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder(Friend.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Followed>() {
            @Override
            public void done(List<Followed> follow, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting followed store", e);
                    return;
                }
                tvFollowed.setText("Followed : "+ follow.size());
            }
        });
    }

    public static void setPosition(int pos){
        position = pos;
    }
}
