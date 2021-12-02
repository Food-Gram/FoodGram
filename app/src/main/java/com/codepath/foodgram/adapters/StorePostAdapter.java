package com.codepath.foodgram.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codepath.foodgram.R;
import com.codepath.foodgram.models.FoodStorePost;
import com.parse.ParseFile;

import java.util.List;

public class StorePostAdapter extends RecyclerView.Adapter<StorePostAdapter.ViewHolder> {
    private final String TAG = "StorePostAdapter";

    public Context context;
    public List<FoodStorePost> posts;

    public StorePostAdapter( Context context, List<FoodStorePost> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodStorePost post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<FoodStorePost> posts) {
        posts.addAll(posts);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView handle;
        private ImageView ivImage;
        private TextView tvDescription;
        private ProgressBar progressBar;
        private int radius = 30;
        private int margin = 30;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            handle = itemView.findViewById(R.id.handle);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);

        }

        // bind the view elements to the post
        public void bind(FoodStorePost post) {

            // Handle
            handle.setText(post.getUser().getUsername());

            // Image
            ParseFile image = (ParseFile) post.getImage();
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

            // Description
            tvDescription.setText(post.getDescription());
        }
    }
}
