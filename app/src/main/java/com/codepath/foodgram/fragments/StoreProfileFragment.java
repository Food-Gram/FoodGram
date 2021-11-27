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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.foodgram.R;
import com.codepath.foodgram.adapters.ImageAdapter;
import com.codepath.foodgram.adapters.ProfileAdapter;
import com.codepath.foodgram.models.Followed;
import com.codepath.foodgram.models.FoodStorePost;
import com.codepath.foodgram.models.Friend;
import com.codepath.foodgram.models.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class StoreProfileFragment extends Fragment {

    public static final String TAG = "StoreProfileFragment";
    private ImageView ivUserIcon;
    private TextView tvUsername;
    private TextView tvPostNum;
    private TextView tvFriendNum;
    private TextView tvFollower;
    private TextView tvPhoneNum;
    private TextView tvAddress;
    private RecyclerView rvProfile;



    private SwipeRefreshLayout swipeContainer;
    private static int position = 0;

    private ProfileAdapter adapter;
    private ImageAdapter Iadapter;
    private List<FoodStorePost> allposts;

    private BottomNavigationView profileNavigation;

    public StoreProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivUserIcon = view.findViewById(R.id.ivStoreUserIcon);
        tvUsername = view.findViewById(R.id.tvStorePost_profile);
        tvPostNum = view.findViewById(R.id.tvStorePostNum);
        tvFollower = view.findViewById(R.id.tvStoreFollower);
        tvFriendNum = view.findViewById(R.id.tvStoreFriendNum);
        tvPhoneNum = view.findViewById(R.id.tvPhoneNum);
        tvAddress = view.findViewById(R.id.tvAddress);
        rvProfile = view.findViewById(R.id.rvStoreProfile);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        // Basic information of current user
        ParseUser currentUser = ParseUser.getCurrentUser();
        tvUsername.setText(currentUser.getUsername());
        Glide.with(getContext()).load(currentUser.getParseFile("icon").getUrl())
                .transform(new CenterInside(), new RoundedCorners(100)).into(ivUserIcon);
        tvFriendNum.setText("Friends : "+ Friend.getFriendNum());
        tvFollower.setText("Followers : "+ Followed.getFollowerNum());
        tvAddress.setText("Phone : " + currentUser.getString("storeAddress"));
        tvPhoneNum.setText("Address : " + currentUser.getString("phoneNum"));
        // Bottom Navigation
        profileNavigation = view.findViewById(R.id.StoreprofileNavigation);
        Menu menu = profileNavigation.getMenu();

        profileNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_post_history:
                        /*
                        allposts = new ArrayList<>();
                        adapter = new ProfileAdapter(getContext(), null, allposts);
                        rvProfile.setAdapter(adapter);
                        rvProfile.setLayoutManager(new LinearLayoutManager(getContext()));

                        queryPosts("Profile");
                        refresh("Profile");
                        */
                        break;
                    case R.id.action_menu:

                        /*
                        allposts = new ArrayList<>();

                        Iadapter = new ImageAdapter(getContext(), allposts);
                        rvProfile.setAdapter(Iadapter);
                        rvProfile.setLayoutManager(new GridLayoutManager(getContext(),3));

                        queryPosts("Image");
                        refresh("Image");
                        */

                        break;
                }
                return true;
            }
        });

        //default selection
        profileNavigation.setSelectedItemId(R.id.action_menu);
        menu.findItem(R.id.action_grid_posts).setVisible(false);

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
        ParseQuery<FoodStorePost> query = new ParseQuery<FoodStorePost>(FoodStorePost.class);
        query.include(FoodStorePost.KEY_AUTHOR);
        query.whereEqualTo(FoodStorePost.KEY_AUTHOR, ParseUser.getCurrentUser());
        query.addDescendingOrder(FoodStorePost.KEY_CREATED_KEY);

        query.findInBackground(new FindCallback<FoodStorePost>() {
            @Override
            public void done(List<FoodStorePost> posts, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
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

    public static void setPosition(int pos){
        position = pos;
    }
}