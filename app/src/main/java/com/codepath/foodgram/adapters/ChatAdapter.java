package com.codepath.foodgram.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.Container_friendlist7);
            ivImage_user = itemView.findViewById(R.id.ivFriendIcon);
            tvUsername = itemView.findViewById(R.id.tvFriendName);
        }

        public void bind(ParseUser user, int position) throws ParseException {

            //Display information
            tvUsername.setText(user.fetchIfNeeded().getUsername());

            Log.i(TAG, "Friend:" + user.fetchIfNeeded().getUsername());

            image = user.getParseFile("icon");
            if (image != null) {
                Glide.with(context).load(image.getUrl()).transform(new CenterInside(),
                        new RoundedCorners(100)).into(ivImage_user);
            }


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


    }
}
