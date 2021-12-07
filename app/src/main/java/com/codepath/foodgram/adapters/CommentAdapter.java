package com.codepath.foodgram.adapters;

import com.codepath.foodgram.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.foodgram.models.StoreMenu;
import com.parse.ParseFile;

import java.util.List;
import java.util.Map;

public class CommentAdapter  extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private final String TAG = "CommentAdapter";

    public Context context;
    public List<Map<String,String>> comments;

    public CommentAdapter( Context context, List<Map<String,String>> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String,String> comment= comments.get(position);
        holder.bind(comment, position);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        comments.clear();
        notifyDataSetChanged();
    }

    /*
    // Add a list of items -- change to type used
    public void addAll(List<Menu> menus) {
        menus.addAll(menus);
        notifyDataSetChanged();
    }*/

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.comment1Name);
            tvComment = itemView.findViewById(R.id.comment1);
        }

        // bind the view elements to the post
        public void bind(Map<String,String> comment, int position) {

            if( position < 2) {
                tvName.setText(comment.get("username") + ":");
                tvComment.setText(comment.get("comment"));
            }
        }
    }
}