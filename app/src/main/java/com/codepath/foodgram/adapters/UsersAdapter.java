package com.codepath.foodgram.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.foodgram.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{
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
            holder.bind(user);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.Container_friendlist);
            ivImage_user = itemView.findViewById(R.id.ivUserListIcon);
            tvUsername = itemView.findViewById(R.id.tvUserListName);
            bnUnfriend = itemView.findViewById(R.id.bnUnfriend);


        }
        public void bind (ParseUser user) throws ParseException {

            tvUsername.setText(user.fetchIfNeeded().getUsername());

            image = user.getParseFile("icon");
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage_user);
            }

        }
    }
}