package com.codepath.foodgram.adapters;

import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.foodgram.models.FoodStorePost;
import com.codepath.foodgram.models.Post;
import com.codepath.foodgram.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.parse.ParseFile;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private final String TAG = "PostsAdapter";

    public Context context;
    public List<Post> posts;
    public List<FoodStorePost> StorePosts;

    public PostsAdapter( Context context, List<Post> posts, List<FoodStorePost> Sposts) {
        this.context = context;
        this.posts = posts;
        this.StorePosts = Sposts;
    }

    @NonNull
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.posts, parent, false);
        return new PostsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position) {
        if(posts == null){
            FoodStorePost post = StorePosts.get(position);
            holder.bind(null,post, position);
        }else{
            Post post = posts.get(position);
            holder.bind(post,null, position);
        }
    }

    @Override
    public int getItemCount() {
        if(posts == null){
            return StorePosts.size();
        }else{
            return posts.size();
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        if(posts == null){
            StorePosts.clear();
        }else{
            posts.clear();
        }
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout container;
        private ImageView ivIcon;
        private TextView tvUsername;
        private TextView tvPostTime;
        private ImageView ivImage;
        private TextView tvDescription;
        private TextView tvLike;
        private TextView tvComment;
        private ProgressBar progressBar;
        private ParseFile icon;
        private ParseFile image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.rvConatiner);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvPostTime = itemView.findViewById(R.id.tvPostTime);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);

            tvLike = itemView.findViewById(R.id.tvPostLike);
            tvComment = itemView.findViewById(R.id.tvPostComment);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);

        }

        // bind the view elements to the post
        public void bind(Post post, FoodStorePost Spost, int position) {

            if(posts == null) {
                tvDescription.setText(Spost.getDescription());
                tvUsername.setText(Spost.getUser().getUsername());
                tvPostTime.setText(Spost.getUpdatedAt().toString());
                tvLike.setText(String.valueOf(Spost.getLike()));
                tvComment.setText(String.valueOf(Spost.getCommentCount()));

                icon = Spost.getUser().getParseFile("icon");
                if(icon != null) {
                    Glide.with(context).load(icon.getUrl()).transform(new CenterInside(), new RoundedCorners(100)).into(ivIcon);
                }

                // Image
                ParseFile image = (ParseFile) Spost.getImage();
                if (image != null) {
                    Glide.with(context).load(image.getUrl())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(ivImage);
                }
            }
            else{
                tvDescription.setText(post.getDescription());
                tvUsername.setText(post.getUser().getUsername());
                tvPostTime.setText(post.getUpdatedAt().toString());
                tvLike.setText(String.valueOf(post.getLike()));
                tvComment.setText(String.valueOf(post.getCommentCount()));

                icon = post.getUser().getParseFile("icon");
                if(icon != null) {
                    Glide.with(context).load(icon.getUrl()).transform(new CenterInside(), new RoundedCorners(100)).into(ivIcon);
                }

                // Image
                image = (ParseFile) post.getImage();
                if (image != null) {
                    Glide.with(context).load(image.getUrl())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(ivImage);
                }
            }
        }
    }
}