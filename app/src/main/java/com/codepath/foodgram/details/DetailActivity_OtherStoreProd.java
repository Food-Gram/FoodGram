package com.codepath.foodgram.details;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.foodgram.R;
import com.codepath.foodgram.adapters.MenuAdapter;
import com.codepath.foodgram.adapters.ProfileAdapter;
import com.codepath.foodgram.models.Followed;
import com.codepath.foodgram.models.FoodStorePost;
import com.codepath.foodgram.models.Friend;
import com.codepath.foodgram.models.StoreMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity_OtherStoreProd extends AppCompatActivity {

    public static final String TAG = "OtherStoreProfile";

    private ParseUser user;
    private ImageView ivUserIcon;
    private TextView tvUsername;
    private TextView tvPostNum;
    private TextView tvFriendNum;
    private TextView tvFollower;
    private TextView tvPhoneNum;
    private TextView tvAddress;
    private TextView tvMenuNum;
    private RatingBar rating;
    private RecyclerView rvProfile;
    private int friendNum;

    private SwipeRefreshLayout swipeContainer;
    private static int position = 0;

    private ProfileAdapter adapter;
    private MenuAdapter Iadapter;
    private List<FoodStorePost> allposts;
    private List<StoreMenu> allmenus;
    private Context context;

    private BottomNavigationView profileNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_other_store_prod);
        ivUserIcon = findViewById(R.id.ivStoreUserIcon);
        tvUsername = findViewById(R.id.tvStorePost_profile);
        tvPostNum = findViewById(R.id.tvStorePostNum);
        tvFollower = findViewById(R.id.tvStoreFollower);
        tvFriendNum = findViewById(R.id.tvStoreFriendNum);
        tvPhoneNum = findViewById(R.id.tvPhoneNum);
        tvAddress = findViewById(R.id.tvAddress);
        rvProfile = findViewById(R.id.rvStoreProfile);
        tvMenuNum = findViewById(R.id.tvStoreMenuNum);
        rating = findViewById(R.id.ratingBar);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.searching_Container);
        context = this;

        // Basic information of other user
        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

        tvUsername.setText(user.getUsername());
        Glide.with(context).load(user.getParseFile("icon").getUrl())
                .transform(new CenterInside(), new RoundedCorners(100)).into(ivUserIcon);

        queryFriend();
        queryFollower();

        tvAddress.setText("Address : " + user.getString("storeAddress"));
        tvPhoneNum.setText("Phone : " + user.getString("phoneNum"));
        rating.setRating((float) user.getDouble("rating"));


        // Bottom Navigation
        profileNavigation = findViewById(R.id.StoreprofileNavigation);
        Menu menu = profileNavigation.getMenu();

        profileNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_post_history:
                        allposts = new ArrayList<>();
                        adapter = new ProfileAdapter(context, null, allposts);
                        rvProfile.setAdapter(adapter);
                        rvProfile.setLayoutManager(new LinearLayoutManager(context));
                        queryPosts("posts");
                        refresh("posts");
                        break;
                    default :
                        allmenus = new ArrayList<>();
                        Iadapter = new MenuAdapter(context, allmenus);
                        rvProfile.setAdapter(Iadapter);
                        rvProfile.setLayoutManager(new LinearLayoutManager(context));
                        queryMenus();
                        refresh("Image");
                        queryPosts("1");
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
                if(string.equals("posts")) {
                    adapter.clear();
                    setPosition(0);
                    queryPosts("posts");
                }else{
                    Iadapter.clear();
                    setPosition(0);
                    queryMenus();
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

    // search for menus
    private void queryMenus() {
        ParseQuery<StoreMenu> query = new ParseQuery<StoreMenu>(StoreMenu.class);
        query.whereEqualTo(StoreMenu.KEY_STORE, user);
        query.addDescendingOrder(StoreMenu.KEY_CREATED_KEY);

        query.findInBackground(new FindCallback<StoreMenu>() {
            @Override
            public void done(List<StoreMenu> menus, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (StoreMenu menu : menus){
                    Log.i(TAG, "Menu: " + menu.getFoodName() + ", price: " + menu.getPrice());
                }
                allmenus.addAll(menus);
                tvMenuNum.setText("Menus : " + String.valueOf(allmenus.size()));
                Iadapter.notifyDataSetChanged();
                rvProfile.smoothScrollToPosition(position);
            }
        });

    }

    // search for posts
    private void queryPosts(String string) {
        ParseQuery<FoodStorePost> query = new ParseQuery<FoodStorePost>(FoodStorePost.class);
        query.include(FoodStorePost.KEY_AUTHOR);
        query.whereEqualTo(FoodStorePost.KEY_AUTHOR, user);
        query.addDescendingOrder(FoodStorePost.KEY_CREATED_KEY);

        query.findInBackground(new FindCallback<FoodStorePost>() {
            @Override
            public void done(List<FoodStorePost> posts, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (FoodStorePost post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }

                if(string.equals("posts")) {
                    allposts.addAll(posts);
                    adapter.notifyDataSetChanged();
                }

                tvPostNum.setText("Posts : " + String.valueOf(posts.size()));
                rvProfile.smoothScrollToPosition(position);
            }
        });

    }

    private void queryFriend() {
        // Specify which class to query
        ParseQuery<Friend> query1 = ParseQuery.getQuery(Friend.class);
        query1.whereEqualTo(Friend.KEY_SENDER, user);
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
        query2.whereEqualTo(Friend.KEY_RECEIVER, user);
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

    private void queryFollower() {
        ParseQuery<Followed> query = ParseQuery.getQuery(Followed.class);
        query.whereEqualTo(Followed.KEY_FOLLOW, user);
        query.addDescendingOrder(Friend.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Followed>() {
            @Override
            public void done(List<Followed> follow, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting followed store", e);
                    return;
                }
                tvFollower.setText("Followed : "+ follow.size());
            }
        });
    }

    public static void setPosition(int pos){
        position = pos;
    }
}
