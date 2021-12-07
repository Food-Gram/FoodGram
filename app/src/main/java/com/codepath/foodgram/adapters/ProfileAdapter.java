package com.codepath.foodgram.adapters;

import com.codepath.foodgram.models.FoodStorePost;
import com.codepath.foodgram.models.Post;
import com.codepath.foodgram.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>{

    public static final String TAG = "ProfileAcitivity";

    private Context context;
    private List<Post> posts;
    private List<FoodStorePost> storePosts;


    public ProfileAdapter(Context context, List<Post>posts, List<FoodStorePost> foodStoreposts){
        this.context = context;
        this.posts = posts;
        this.storePosts = foodStoreposts;
    }


    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_user, parent, false);
        return new ProfileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ViewHolder holder, int position) {
        if(posts == null){
            FoodStorePost post = storePosts.get(position);
            holder.bind(null,post, position);
        }else{
            Post post = posts.get(position);
            holder.bind(post,null, position);
        }



    }
    public void clear() {
        if(posts == null){
            storePosts.clear();
        }else{
            posts.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(posts == null){
            return storePosts.size();
        }else{
            return posts.size();
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout container;
        private TextView tvPost;
        private TextView tvPostTime;
        private ImageView ivImage_user;
        private TextView tvDescription_user;
        private TextView tvLike;
        private TextView tvComments;
        private ImageButton ibDelete;
        private ParseFile image;
        private ImageButton ibLike;
        private ImageButton ibLikeClick;

        private boolean like;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.Container_friendlist7);
            ivImage_user = itemView.findViewById(R.id.ivImage_menu);
            tvDescription_user = itemView.findViewById(R.id.tvDescription_user);
            tvPostTime = itemView.findViewById(R.id.tvPostTime_user);
            tvPost= itemView.findViewById(R.id.searching_profile);

            tvLike = itemView.findViewById(R.id.tvProfileLike);
            tvComments = itemView.findViewById(R.id.tvPostComment);
            ibDelete = itemView.findViewById(R.id.ibDelete);

            ibLike = itemView.findViewById(R.id.ibProfileLike);
            ibLikeClick = itemView.findViewById(R.id.ibProfileLikeClick);


        }
        public void bind (Post post, FoodStorePost storePost, int position){

            if(posts == null) {
                tvDescription_user.setText(storePost.getDescription());
                int currentPosition = getItemCount() - position;
                tvPost.setText("Post: " + currentPosition);
                tvPostTime.setText(storePost.getUpdatedAt().toString());
                tvLike.setText(String.valueOf(storePost.getLike()));
                tvComments.setText(String.valueOf(storePost.getCommentCount()));

                image = storePost.getImage();
                if (image != null) {
                    Glide.with(context).load(image.getUrl()).into(ivImage_user);
                }

                ibDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        storePost.deleteInBackground();
                        Log.i(TAG, "Item Deleted!");
                        storePosts.remove(position);
                        notifyDataSetChanged();

                    }
                });

                List<String> user = storePost.getLikeUsers();

                like = user.contains(ParseUser.getCurrentUser().getUsername());

                if(like) {
                    ibLike.setVisibility(ImageButton.INVISIBLE);
                    ibLikeClick.setVisibility(ImageButton.VISIBLE);
                    ibLikeClick.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            storePost.RemoveLikeUsers(ParseUser.getCurrentUser().getUsername(), storePost.getLikeUsers());
                            storePost.LikeDecrement();
                            ibLike.setVisibility(ImageButton.VISIBLE);
                            ibLikeClick.setVisibility(ImageButton.INVISIBLE);
                            storePost.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Log.e(TAG, "Error while saving", e);
                                        Toast.makeText(context, "Error while saving!", Toast.LENGTH_SHORT).show();
                                    }
                                    Log.i(TAG, "like remove was successful!!");
                                }
                            });
                            notifyDataSetChanged();
                        }
                    });

                }
                else {
                    ibLikeClick.setVisibility(ImageButton.INVISIBLE);
                    ibLike.setVisibility(ImageButton.VISIBLE);
                    ibLike.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            storePost.setLikeUsers(ParseUser.getCurrentUser().getUsername(), storePost.getLikeUsers());
                            storePost.LikeIncrement();
                            ibLikeClick.setVisibility(ImageButton.VISIBLE);
                            ibLike.setVisibility(ImageButton.INVISIBLE);
                            storePost.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Log.e(TAG, "Error while saving", e);
                                        Toast.makeText(context, "Error while saving!", Toast.LENGTH_SHORT).show();
                                    }
                                    Log.i(TAG, "like save was successful!!");
                                }
                            });
                            notifyDataSetChanged();
                        }
                    });

                }

            } else {
                tvDescription_user.setText(post.getDescription());
                int currentPosition1 = getItemCount() - position;
                tvPost.setText("Post: " + currentPosition1);
                tvPostTime.setText(post.getUpdatedAt().toString());
                tvLike.setText(String.valueOf(post.getLike()));
                tvComments.setText(String.valueOf(post.getCommentCount()));

                image = post.getImage();
                if (image != null) {
                    Glide.with(context).load(image.getUrl()).into(ivImage_user);
                }

                ibDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        post.deleteInBackground();
                        Log.i(TAG, "Item Deleted!");
                        posts.remove(position);
                        notifyDataSetChanged();

                    }
                });

                List<String> user = post.getLikeUsers();

                like = user.contains(ParseUser.getCurrentUser().getUsername());

                if(like) {
                    ibLike.setVisibility(ImageButton.INVISIBLE);
                    ibLikeClick.setVisibility(ImageButton.VISIBLE);
                    ibLikeClick.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            post.RemoveLikeUsers(ParseUser.getCurrentUser().getUsername(), post.getLikeUsers());
                            post.LikeDecrement();
                            ibLike.setVisibility(ImageButton.VISIBLE);
                            ibLikeClick.setVisibility(ImageButton.INVISIBLE);
                            post.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Log.e(TAG, "Error while saving", e);
                                        Toast.makeText(context, "Error while saving!", Toast.LENGTH_SHORT).show();
                                    }
                                    Log.i(TAG, "like remove was successful!!");
                                }
                            });
                            notifyDataSetChanged();
                        }
                    });

                }
                else {
                    ibLikeClick.setVisibility(ImageButton.INVISIBLE);
                    ibLike.setVisibility(ImageButton.VISIBLE);
                    ibLike.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            post.setLikeUsers(ParseUser.getCurrentUser().getUsername(), post.getLikeUsers());
                            post.LikeIncrement();
                            ibLikeClick.setVisibility(ImageButton.VISIBLE);
                            ibLike.setVisibility(ImageButton.INVISIBLE);
                            post.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Log.e(TAG, "Error while saving", e);
                                        Toast.makeText(context, "Error while saving!", Toast.LENGTH_SHORT).show();
                                    }
                                    Log.i(TAG, "like save was successful!!");
                                }
                            });
                            notifyDataSetChanged();
                        }
                    });
                }
            }
        }
    }
}
