package com.codepath.foodgram.adapters;

import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.foodgram.details.DetailActivity_OtherStoreProd;
import com.codepath.foodgram.details.DetailActivity_OtherUserProf;
import com.codepath.foodgram.models.FoodStorePost;
import com.codepath.foodgram.models.Post;
import com.codepath.foodgram.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        private ImageButton ibLike;
        private ImageButton ibComment;
        private ImageButton ibLikeClick;
        private ImageButton ibCommentClick;
        private EditText WriteComment;
        private ImageView Send;
        private View divide;
        private RecyclerView rvComment;
        private CommentAdapter adapter;
        private List<Map<String, String>> allComments;




        private boolean like;

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

            ibLike = itemView.findViewById(R.id.ibPostLike);
            ibComment = itemView.findViewById(R.id.ibPostComment);
            ibLikeClick = itemView.findViewById(R.id.ibPostLikeClick);
            ibCommentClick = itemView.findViewById(R.id.ibPostCommentClick);
            rvComment = itemView.findViewById(R.id.rvComment);
            WriteComment = itemView.findViewById(R.id.WriteComment);
            Send = itemView.findViewById(R.id.SubmitComment);
            divide = itemView.findViewById(R.id.divider);

        }

        // bind the view elements to the post
        public void bind(Post post, FoodStorePost Spost, int position) {



            if(post == null) {
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

                // view foof store profile
                tvUsername.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, DetailActivity_OtherStoreProd.class);
                        i.putExtra("user", Parcels.wrap(Spost.getUser()));
                        context.startActivity(i);
                    }
                });


                //Write Comment
                ibComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ibCommentClick.setVisibility(ImageButton.VISIBLE);
                        ibComment.setVisibility(ImageButton.INVISIBLE);
                        WriteComment.setVisibility(EditText.VISIBLE);
                        Send.setVisibility(ImageView.VISIBLE);
                        rvComment.setVisibility(RecyclerView.INVISIBLE);
                    }
                });

                ibCommentClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ibCommentClick.setVisibility(ImageButton.INVISIBLE);
                        ibComment.setVisibility(ImageButton.VISIBLE);
                        WriteComment.setVisibility(EditText.INVISIBLE);
                        Send.setVisibility(ImageView.INVISIBLE);
                        rvComment.setVisibility(RecyclerView.VISIBLE);
                    }
                });

                Send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String comment = WriteComment.getText().toString();
                        if (comment.isEmpty()){
                            Toast.makeText(context, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Map<String, String> Mcomments = new HashMap<>();
                        Mcomments.put("username",ParseUser.getCurrentUser().getUsername());
                        Mcomments.put("comment",comment);
                        Spost.setCommentInfo(Mcomments,Spost.getCommentInfo());
                        Spost.CommentIncrement();
                        Spost.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    Log.e(TAG, "Error while saving", e);
                                    Toast.makeText(context, "Error while saving!", Toast.LENGTH_SHORT).show();
                                }
                                Log.i(TAG, "like remove was successful!!");
                                WriteComment.setText("");
                            }
                        });
                        notifyDataSetChanged();
                        ibCommentClick.setVisibility(ImageButton.INVISIBLE);
                        ibCommentClick.setVisibility(ImageButton.VISIBLE);
                        WriteComment.setVisibility(EditText.INVISIBLE);
                        Send.setVisibility(ImageView.INVISIBLE);
                        rvComment.setVisibility(RecyclerView.VISIBLE);
                    }
                });


                // Display Comment
                allComments = Spost.getCommentInfo();
                if(allComments.size() >= 2) {
                    List<Map<String, String>> twoComments = allComments.subList(allComments.size() - 2, allComments.size());
                    Collections.reverse(twoComments);
                    adapter = new CommentAdapter(context, twoComments);
                }
                else{
                    Collections.reverse(allComments);
                    adapter = new CommentAdapter(context, allComments);
                }
                rvComment.setAdapter(adapter);
                rvComment.setLayoutManager(new LinearLayoutManager(context));

                if(allComments.size() == 0){
                    divide.setVisibility(View.INVISIBLE);
                }
                else{
                    divide.setVisibility(View.VISIBLE);
                }


                List<String> user = Spost.getLikeUsers();

                like = user.contains(ParseUser.getCurrentUser().getUsername());

                if(like) {
                    ibLike.setVisibility(ImageButton.INVISIBLE);
                    ibLikeClick.setVisibility(ImageButton.VISIBLE);
                    ibLikeClick.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Spost.RemoveLikeUsers(ParseUser.getCurrentUser().getUsername(), Spost.getLikeUsers());
                            Spost.LikeDecrement();
                            ibLike.setVisibility(ImageButton.VISIBLE);
                            ibLikeClick.setVisibility(ImageButton.INVISIBLE);
                            Spost.saveInBackground(new SaveCallback() {
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
                            Spost.setLikeUsers(ParseUser.getCurrentUser().getUsername(), Spost.getLikeUsers());
                            Spost.LikeIncrement();
                            ibLikeClick.setVisibility(ImageButton.VISIBLE);
                            ibLike.setVisibility(ImageButton.INVISIBLE);
                            Spost.saveInBackground(new SaveCallback() {
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

                // view food store profile
                tvUsername.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, DetailActivity_OtherUserProf.class);
                        i.putExtra("user", Parcels.wrap(post.getUser()));
                        context.startActivity(i);
                    }
                });


                //Write Comment
                ibComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ibCommentClick.setVisibility(ImageButton.VISIBLE);
                        ibComment.setVisibility(ImageButton.INVISIBLE);
                        WriteComment.setVisibility(EditText.VISIBLE);
                        Send.setVisibility(ImageView.VISIBLE);
                        rvComment.setVisibility(RecyclerView.INVISIBLE);
                    }
                });

                ibCommentClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ibCommentClick.setVisibility(ImageButton.INVISIBLE);
                        ibComment.setVisibility(ImageButton.VISIBLE);
                        WriteComment.setVisibility(EditText.INVISIBLE);
                        Send.setVisibility(ImageView.INVISIBLE);
                        rvComment.setVisibility(RecyclerView.VISIBLE);
                    }
                });


                Send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String comment = WriteComment.getText().toString();
                        if (comment.isEmpty()){
                            Toast.makeText(context, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Map<String, String> Mcomments = new HashMap<>();
                        Mcomments.put("username",ParseUser.getCurrentUser().getUsername());
                        Mcomments.put("comment",comment);
                        post.setCommentInfo(Mcomments,post.getCommentInfo());
                        post.CommentIncrement();
                        post.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    Log.e(TAG, "Error while saving", e);
                                    Toast.makeText(context, "Error while saving!", Toast.LENGTH_SHORT).show();
                                }
                                Log.i(TAG, "like remove was successful!!");
                                WriteComment.setText("");
                            }
                        });
                        notifyDataSetChanged();
                        ibCommentClick.setVisibility(ImageButton.INVISIBLE);
                        ibCommentClick.setVisibility(ImageButton.VISIBLE);
                        WriteComment.setVisibility(EditText.INVISIBLE);
                        Send.setVisibility(ImageView.INVISIBLE);
                        rvComment.setVisibility(RecyclerView.VISIBLE);
                    }
                });


                // Display Comment
                allComments = post.getCommentInfo();
                if(allComments.size() >= 2) {
                    List<Map<String, String>> twoComments = allComments.subList(allComments.size() - 2, allComments.size());
                    Collections.reverse(twoComments);
                    adapter = new CommentAdapter(context, twoComments);
                }
                else{
                    Collections.reverse(allComments);
                    adapter = new CommentAdapter(context, allComments);
                }
                rvComment.setAdapter(adapter);
                rvComment.setLayoutManager(new LinearLayoutManager(context));

                if(allComments.size() == 0){
                    divide.setVisibility(View.INVISIBLE);
                }
                else{
                    divide.setVisibility(View.VISIBLE);
                }

                //like
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