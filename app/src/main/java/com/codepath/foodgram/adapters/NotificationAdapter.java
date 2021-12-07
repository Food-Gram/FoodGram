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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.foodgram.R;
import com.codepath.foodgram.details.DetailActivity_OtherStoreProd;
import com.codepath.foodgram.details.DetailActivity_OtherUserProf;
import com.codepath.foodgram.models.Friend;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.List;


//adapter to hold the each user that send friend request to the current user
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{


    public static final String TAG = "NotificationAdapter";

    private Context context;
    private List<ParseUser> users;  //userlist that send friend request


    public NotificationAdapter(Context context, List<ParseUser> users){
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
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
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
        private Button bnAgree;
        private Button bnRefuse;
        private ParseFile image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.Container_friendlist6);
            ivImage_user = itemView.findViewById(R.id.ivUserListIcon2);
            tvUsername = itemView.findViewById(R.id.tvUserListName2);
            bnAgree = itemView.findViewById(R.id.bnAgree);
            bnRefuse = itemView.findViewById(R.id.bnRefuse);

        }

        public void bind(ParseUser user, int position) throws ParseException {

            //Display information
            tvUsername.setText(user.fetchIfNeeded().getUsername());

            Log.i(TAG, "Sender:" + user.fetchIfNeeded().getUsername());

            image = user.getParseFile("icon");
            if (image != null) {
                Glide.with(context).load(image.getUrl()).transform(new CenterInside(),
                        new RoundedCorners(100)).into(ivImage_user);
            }


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


            bnAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AddFriend(user);
                    notifyDataSetChanged();
                    bnAgree.setVisibility(ImageButton.INVISIBLE);
                    bnRefuse.setVisibility(ImageButton.INVISIBLE);
                }
            });

            bnRefuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RefueseFriend(user);
                    notifyDataSetChanged();
                    bnAgree.setVisibility(ImageButton.INVISIBLE);
                    bnRefuse.setVisibility(ImageButton.INVISIBLE);
                }
            });

        }

        private void AddFriend(ParseUser user){

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
                        i.setStatus(1);
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
        }


        private void RefueseFriend(ParseUser user){

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
        }
    }
}
