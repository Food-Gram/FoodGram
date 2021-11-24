package com.codepath.foodgram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.codepath.foodgram.fragments.ChatFragment;
import com.codepath.foodgram.fragments.CreateFragment;
import com.codepath.foodgram.fragments.FriendsFragment;
import com.codepath.foodgram.fragments.ProfileFragment;
import com.codepath.foodgram.fragments.StoresFragment;
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
                        fragment = new ProfileFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        ParseUser currentUser = ParseUser.getCurrentUser();

        // Set default selection
        if (currentUser.getString("type").equals("FoodStore")) // Food store logged in
            bottomNavigation.setSelectedItemId(R.id.action_profile);
        else // Normal user logged in
            bottomNavigation.setSelectedItemId(R.id.action_friends);

    }

}