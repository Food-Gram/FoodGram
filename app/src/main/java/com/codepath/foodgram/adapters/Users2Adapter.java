package com.codepath.foodgram.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.foodgram.R;
import com.codepath.foodgram.details.DetailActivity_OtherStoreProd;
import com.codepath.foodgram.details.DetailActivity_OtherUserProf;
import com.codepath.foodgram.models.Followed;
import com.codepath.foodgram.models.Friend;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.List;


// this adapter is used for the user list when the people is trying to search the
// user in the chat fragment apage. a list of users are shown
public class Users2Adapter extends RecyclerView.Adapter<Users2Adapter.ViewHolder> {

    public static final String TAG = "Users2Adapter";

    private Context context;
    private List<ParseUser> users;


    public Users2Adapter(Context context, List<ParseUser> users){
        this.context = context;
        this.users = users;
    }

    // Clean all elements of the recycler
    public void clear() {
        users.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<ParseUser> user) {
        users.addAll(user);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.users_list, parent, false);
        return new Users2Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Users2Adapter.ViewHolder holder, int position) {
        ParseUser user = users.get(position);
        try {
            holder.bind(user, position);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout container;
        private ImageView ivImage_user;
        private TextView tvUsername;
        private Button bnAddfriend;
        private Button bnFollowStore;
        private ParseFile image;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.Container_friendlist7);
            ivImage_user = itemView.findViewById(R.id.ivUserListIcon);
            tvUsername = itemView.findViewById(R.id.tvUserListName);
            bnAddfriend = itemView.findViewById(R.id.bnAddfriend);
            bnFollowStore = itemView.findViewById(R.id.bnFollowStore);
        }

        public void bind(ParseUser user, int position) throws ParseException {

            //Display information
            tvUsername.setText(user.fetchIfNeeded().getUsername());

            image = user.getParseFile("icon");
            if (image != null) {
                Glide.with(context).load(image.getUrl()).transform(new CenterInside(),
                        new RoundedCorners(100)).into(ivImage_user);
            }


            // if the searching user is not ur friend, then ur able to add
            bnAddfriend.setVisibility(ImageButton.VISIBLE);

            // if the searching user is not followed then ur able to add
            bnFollowStore.setVisibility(ImageButton.VISIBLE);


            //onClick
            tvUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (user.getString("type").equals("FoodStore")) {
                        Intent i = new Intent(context, DetailActivity_OtherStoreProd.class);
                        i.putExtra("user", Parcels.wrap(user));
                        context.startActivity(i);
                    } else {
                        Intent i = new Intent(context, DetailActivity_OtherUserProf.class);
                        i.putExtra("user", Parcels.wrap(user));
                        context.startActivity(i);
                    }
                }
            });


            bnAddfriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddFriend(user);
                    Toast.makeText(context.getApplicationContext(), "Add Friend request send!", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    bnAddfriend.setVisibility(ImageButton.INVISIBLE);
                }
            });

            bnFollowStore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddFollow(user);
                    Toast.makeText(context, "Followed", Toast.LENGTH_SHORT).show();
                    bnFollowStore.setVisibility(ImageButton.INVISIBLE);
                }
            });

            checkIfAdded(user);
            checkIfFollowed(user);

        }

        private void AddFollow(ParseUser user) {
            Followed newFollow = new Followed();
            newFollow.setSender(ParseUser.getCurrentUser());
            newFollow.setFollowed(user);
            newFollow.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.d(TAG, "Error while saving");
                        e.printStackTrace();
                        return;
                    }
                    Log.i(TAG, "Success");
                }
            });
        }

        private void AddFriend(ParseUser user){
            Friend newFriendRequest = new Friend();
            newFriendRequest.setReceiver2(user);
            newFriendRequest.setSender2(ParseUser.getCurrentUser());
            newFriendRequest.setStatus(-1);

            Log.i(TAG, "Adding user: " + user.getUsername());
            Log.i(TAG, "current user: " + ParseUser.getCurrentUser().getUsername());

            newFriendRequest.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.d(TAG, "Error while saving");
                        e.printStackTrace();
                        return;
                    }
                    Log.i(TAG, "Success");
                }
            });
        }



        private void checkIfAdded(ParseUser user) {

            ParseQuery<Friend> query1 = ParseQuery.getQuery(Friend.class);
            query1.whereEqualTo(Friend.KEY_SENDER, user);
            query1.whereEqualTo(Friend.KEY_RECEIVER, ParseUser.getCurrentUser());
            query1.whereEqualTo(Friend.KEY_STATUS, 1);

            query1.findInBackground(new FindCallback<Friend>() {
                @Override
                public void done(List<Friend> friends, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issue with getting friends", e);
                        return;
                    }
                    for (Friend i : friends) {
                        bnAddfriend.setVisibility(Button.INVISIBLE);
                    }
                }
            });

            ParseQuery<Friend> query2 = ParseQuery.getQuery(Friend.class);
            query2.whereEqualTo(Friend.KEY_SENDER, ParseUser.getCurrentUser());
            query2.whereEqualTo(Friend.KEY_RECEIVER, user);
            query2.findInBackground(new FindCallback<Friend>() {
                @Override
                public void done(List<Friend> friends, ParseException e) {
                    if(e != null){
                        Log.e(TAG, "Issue with getting friends", e);
                        return;
                    }
                    for(Friend i: friends){
                        bnAddfriend.setVisibility(Button.INVISIBLE);
                    }
                }
            });
        }

        private void checkIfFollowed(ParseUser user) {
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
                    for (Followed i :follow){
                        bnFollowStore.setVisibility(Button.INVISIBLE);
                    }
                }
            });
        }
    }

}
