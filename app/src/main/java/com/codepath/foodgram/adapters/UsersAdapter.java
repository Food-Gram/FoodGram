package com.codepath.foodgram.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.foodgram.R;
import com.codepath.foodgram.details.DetailActivity_FriendList;
import com.codepath.foodgram.models.Friend;
import com.codepath.foodgram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{

    public static final String TAG = "UsersAdapter";

    private Context context;
    private List<ParseUser> users;



    public UsersAdapter(Context context, List<ParseUser> users){
        this.context = context;
        this.users = users;

    }

    @NonNull
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_list, parent, false);
        return new UsersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.ViewHolder holder, int position) {
        ParseUser user = users.get(position);
        try {
            holder.bind(user, position);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout container;
        private ImageView ivImage_user;
        private TextView tvUsername;
        private Button bnUnfriend;
        private ParseFile image;
        private List<ParseObject> friend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.Container_friendlist);
            ivImage_user = itemView.findViewById(R.id.ivUserListIcon);
            tvUsername = itemView.findViewById(R.id.tvUserListName);
            bnUnfriend = itemView.findViewById(R.id.bnUnfriend);
            friend = new ArrayList<>();


        }
        public void bind (ParseUser user, int position) throws ParseException {

            tvUsername.setText(user.fetchIfNeeded().getUsername());

            image = user.getParseFile("icon");
            if (image != null) {
                Glide.with(context).load(image.getUrl()).transform(new CenterInside(),
                        new RoundedCorners(100)).into(ivImage_user);
            }

            bnUnfriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    users.remove(position);
                    QueryFriend(user);
                    Log.i(TAG, "Friend Deleted!");
                    notifyDataSetChanged();
                }
            });
        }

        private void QueryFriend(ParseUser user) {

            ParseQuery<Friend> query1 = ParseQuery.getQuery(Friend.class);
            query1.whereEqualTo(Friend.KEY_SENDER, user);
            query1.whereEqualTo(Friend.KEY_RECEIVER, ParseUser.getCurrentUser());
            query1.findInBackground(new FindCallback<Friend>() {
                @Override
                public void done(List<Friend> friends, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issue with getting friends", e);
                        return;
                    }
                    for (Friend i : friends) {
                        i.setStatus(0);
                        i.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    Log.e(TAG, "Error while saving", e);
                                }
                                Log.i(TAG, "Friend status changed successful!!");
                            }
                        });
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
                        i.setStatus(0);
                        i.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    Log.e(TAG, "Error while saving", e);
                                }
                                Log.i(TAG, "Post save was successful!!");
                            }
                        });
                    }
                }
            });
        }
    }
}