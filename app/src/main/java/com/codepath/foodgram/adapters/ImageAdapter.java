package com.codepath.foodgram.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.foodgram.R;
import com.codepath.foodgram.models.Post;
import com.parse.ParseFile;

import java.util.List;

// for Grid View
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;

    public ImageAdapter(Context context, List<Post>posts){
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_view, parent, false);
        return new ImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post, position);

    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView images;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            images = itemView.findViewById(R.id.gridview_images);

        }
        public void bind (Post post, int position){
            ParseFile image = post.getImage();

            if(image != null) {
                Glide.with(context).load(image.getUrl()).into(images);
            }

        }
    }
}

