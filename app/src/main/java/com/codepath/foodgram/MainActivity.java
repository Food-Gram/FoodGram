package com.codepath.foodgram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.foodgram.fragments.ChatFragment;
import com.codepath.foodgram.fragments.FeedFragment;
import com.codepath.foodgram.fragments.ProfileFragment;
import com.codepath.foodgram.fragments.StoreFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        Menu menu = bottomNavigation.getMenu();
        MenuItem feed = menu.findItem(R.id.action_feed);
        MenuItem profile = menu.findItem(R.id.action_profile);
        MenuItem store = menu.findItem(R.id.action_store);

        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_feed:
                        fragment = new FeedFragment();
                        break;
                    case R.id.action_profile:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.action_store:
                        fragment = new StoreFragment();
                        break;
                    default:
                        fragment = new ChatFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        ParseUser currentUser = ParseUser.getCurrentUser();

        // Set default selection
        if (currentUser.getString("type").equals("FoodStore")) { // Food store logged in
            feed.setVisible(false);
            profile.setVisible(false);
            store.setVisible(true);
            bottomNavigation.setSelectedItemId(R.id.action_store);
        }
        else { // Normal user logged in
            feed.setVisible(true);
            profile.setVisible(true);
            store.setVisible(false);
            bottomNavigation.setSelectedItemId(R.id.action_feed);
        }

    }

}