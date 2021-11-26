package com.codepath.foodgram.adapters;

import com.codepath.foodgram.models.Post;
import com.codepath.foodgram.R;
import android.content.Context;
import android.content.Intent;
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

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>{
    private Context context;
    private List<Post> posts;


    public ProfileAdapter(Context context, List<Post>posts){
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_user, parent, false);
        return new ProfileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ViewHolder holder, int position) {
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
        private RelativeLayout container;
        private TextView tvPost;
        private TextView tvPostTime;
        private ImageView ivImage_user;
        private TextView tvDescription_user;
        private TextView tvLike;
        private TextView tvComments;

        /*
        private ImageButton ibShare;
        private ImageButton ibLike;
        private ImageButton ibComment;
         */

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.Container_user);
            ivImage_user = itemView.findViewById(R.id.ivImage_user);
            tvDescription_user = itemView.findViewById(R.id.tvDescription_user);
            tvPostTime = itemView.findViewById(R.id.tvPostTime_user);
            tvPost= itemView.findViewById(R.id.tvPost_profile);

            tvLike = itemView.findViewById(R.id.tvProfileLike);
            tvComments = itemView.findViewById(R.id.tvProfileComment);
            /*
            ibLike = itemView.findViewById(R.id.ibProfileLikeClick);
            ibComment = itemView.findViewById(R.id.ibProfileComment);
            ibShare = itemView.findViewById(R.id.ibProfileShareClick);
            */

        }
        public void bind (Post post, int position){

            tvDescription_user.setText(post.getDescription());
            int currentPosition = getItemCount() - position;
            tvPost.setText("Post: "+ currentPosition);
            tvPostTime.setText(post.getUpdatedAt().toString());
            tvLike.setText(post.getLike());
            tvComments.setText(post.getCommentCount());

            ParseFile image = post.getImage();
            if(image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage_user);
            }

            /*
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("Fragment","ProfileFragment");
                    i.putExtra("post", Parcels.wrap(post));
                    i.putExtra("PositionProfile",position);
                    context.startActivity(i);
                }
            });
            */
        }
    }
}
