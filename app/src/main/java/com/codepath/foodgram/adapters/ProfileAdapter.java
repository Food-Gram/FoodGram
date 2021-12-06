package com.codepath.foodgram.adapters;

import com.codepath.foodgram.models.FoodStorePost;
import com.codepath.foodgram.models.Post;
import com.codepath.foodgram.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>{
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

        private ParseFile image;
        /*
        private ImageButton ibShare;
        private ImageButton ibLike;
        private ImageButton ibComment;
         */

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.Container_friendlist7);
            ivImage_user = itemView.findViewById(R.id.ivImage_menu);
            tvDescription_user = itemView.findViewById(R.id.tvDescription_user);
            tvPostTime = itemView.findViewById(R.id.tvPostTime_user);
            tvPost= itemView.findViewById(R.id.searching_profile);

            tvLike = itemView.findViewById(R.id.tvProfileLike);
            tvComments = itemView.findViewById(R.id.tvProfileComment);
            /*
            ibLike = itemView.findViewById(R.id.ibProfileLikeClick);
            ibComment = itemView.findViewById(R.id.ibProfileComment);
            ibShare = itemView.findViewById(R.id.ibProfileShareClick);
            */

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
            }
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
