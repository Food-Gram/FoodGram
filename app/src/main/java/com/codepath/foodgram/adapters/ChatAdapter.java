package com.codepath.foodgram.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.foodgram.R;
import com.codepath.foodgram.details.DetailActivity_Chat;
import com.codepath.foodgram.details.DetailActivity_OtherStoreProd;
import com.codepath.foodgram.details.DetailActivity_OtherUserProf;
import com.codepath.foodgram.models.ChatMsg;
import com.codepath.foodgram.models.FoodStorePost;
import com.codepath.foodgram.models.Friend;
import com.codepath.foodgram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{




    public static final String TAG = "ChatAdapter";

    private Context context;
    private List<ParseUser> users;  //userlist that send friend request


    public ChatAdapter(Context context, List<ParseUser> users){
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
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_item_chat_fragment, parent, false);
        return new ChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
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
        private ParseFile image;
        private TextView tvTime;
        private TextView tvLastMsg;

        private List<ChatMsg> allmsg;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.Container_friendlist7);
            ivImage_user = itemView.findViewById(R.id.ivFriendIcon);
            tvUsername = itemView.findViewById(R.id.tvFriendName);
            tvTime = itemView.findViewById(R.id.tvChatTime);
            tvLastMsg = itemView.findViewById(R.id.chatLastMsg);
        }

        public void bind(ParseUser user, int position) throws ParseException {

            allmsg = new ArrayList<>();

            //Display information
            tvUsername.setText(user.fetchIfNeeded().getUsername());

            Log.i(TAG, "Friend:" + user.fetchIfNeeded().getUsername());

            image = user.getParseFile("icon");
            if (image != null) {
                Glide.with(context).load(image.getUrl()).transform(new CenterInside(),
                        new RoundedCorners(100)).into(ivImage_user);
            }

            queryTime(user);

            //onClick
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, DetailActivity_Chat.class);
                    Log.i(TAG, "User in this case is :   " + user.getUsername());
                    i.putExtra("user", Parcels.wrap(user));
                    context.startActivity(i);
                }
            });

        }

        private void queryTime(ParseUser user) {
            ParseQuery<ChatMsg> query1 = ParseQuery.getQuery(ChatMsg.class);
            query1.whereEqualTo(ChatMsg.KEY_SENDER, ParseUser.getCurrentUser());
            query1.whereEqualTo(ChatMsg.KEY_RECEIVER, user);
            query1.addDescendingOrder(ChatMsg.KEY_CREATED);
            query1.findInBackground(new FindCallback<ChatMsg>() {
                @Override
                public void done(List<ChatMsg> msgs, ParseException e) {
                    if(e != null){
                        Log.e(TAG, "Issue with getting friends", e);
                        return;
                    }
                    for (ChatMsg i : msgs) {
                        allmsg.add(i);
                    }
                    if(allmsg.size()>0) {
                        Collections.sort(allmsg, new Comparator<ChatMsg>() {
                            @Override
                            public int compare(ChatMsg msg1, ChatMsg msg2) {
                                return (msg1.getCreatedAt().compareTo(msg2.getCreatedAt()) * -1);
                            }
                        });
                        tvTime.setText(allmsg.get(0).getCreatedAt().toString());
                        tvLastMsg.setText(allmsg.get(0).getContent());
                    }
                }
            });
            ParseQuery<ChatMsg> query2 = ParseQuery.getQuery(ChatMsg.class);
            query2.whereEqualTo(ChatMsg.KEY_SENDER, user);
            query2.whereEqualTo(ChatMsg.KEY_RECEIVER, ParseUser.getCurrentUser());
            query2.addDescendingOrder(ChatMsg.KEY_CREATED);
            query2.findInBackground(new FindCallback<ChatMsg>() {
                @Override
                public void done(List<ChatMsg> msgs, ParseException e) {
                    if(e != null){
                        Log.e(TAG, "Issue with getting friends", e);
                        return;
                    }
                    for (ChatMsg i : msgs) {
                        allmsg.add(i);
                    }

                    if(allmsg.size()>0) {
                        Collections.sort(allmsg, new Comparator<ChatMsg>() {
                            @Override
                            public int compare(ChatMsg msg1, ChatMsg msg2) {
                                return (msg1.getCreatedAt().compareTo(msg2.getCreatedAt()) * -1);
                            }
                        });
                        tvTime.setText(allmsg.get(0).getCreatedAt().toString());
                        tvLastMsg.setText(allmsg.get(0).getContent());
                    }
                }
            });

        }

    }
}
