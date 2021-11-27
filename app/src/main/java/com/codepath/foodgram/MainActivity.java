package com.codepath.foodgram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.foodgram.fragments.ChatFragment;
import com.codepath.foodgram.fragments.CreateFragment;
import com.codepath.foodgram.fragments.FriendsFragment;
import com.codepath.foodgram.fragments.NotificationFragment;
import com.codepath.foodgram.fragments.ProfileFragment;
import com.codepath.foodgram.fragments.SettingFragment;
import com.codepath.foodgram.fragments.StoreProfileFragment;
import com.codepath.foodgram.fragments.StoresFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "MainActivity";

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigation;

    private DrawerLayout drawer;
    ParseUser currentUser = ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bottom Navigation
        bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_friends:
                        fragment = new FriendsFragment();
                        break;
                    case R.id.action_stores:
                        fragment = new StoresFragment();
                        break;
                    case R.id.action_chat:
                        fragment = new ChatFragment();
                        break;
                    case R.id.action_create:
                        fragment = new CreateFragment();
                        break;
                    default:
                        if(currentUser.get("type").equals("user")) {
                            fragment = new ProfileFragment();
                        }else{
                            fragment = new StoreProfileFragment();
                        }
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        // Set default selection
        if (currentUser.getString("type").equals("FoodStore")) // Food store logged in
            bottomNavigation.setSelectedItemId(R.id.action_friends);
        else // Normal user logged in
            bottomNavigation.setSelectedItemId(R.id.action_friends);


        // Action Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        updateUser(navigationView);
    }


    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer((GravityCompat.START));
        }else{
            super.onBackPressed();
        }
    }


    // selection on action bar
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()) {
            case R.id.nav_Notification:
                fragmentManager.beginTransaction().replace(R.id.flContainer,
                        new NotificationFragment()).commit();
                break;
            case R.id.nav_Setting:
                fragmentManager.beginTransaction().replace(R.id.flContainer,
                        new SettingFragment()).commit();
                break;
            case R.id.nav_Logout:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                ParseUser.logOut();
                break;
        }

        drawer.closeDrawer((GravityCompat.START));
        return true;
    }


    // Update user icon and username to action bar
    public void updateUser(NavigationView navigationView){

        View headerView =navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        ImageView navUserIcon = headerView.findViewById(R.id.nav_usericon);

        navUsername.setText(currentUser.getUsername());
        Glide.with(this).load(currentUser.getParseFile("icon").getUrl())
                .transform(new CenterInside(), new RoundedCorners(1000)).into(navUserIcon);

    }
}