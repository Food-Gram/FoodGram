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

public class MenuAdapter  extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private final String TAG = "MenuAdapter";

    public Context context;
    public List<StoreMenu> menus;

    public MenuAdapter( Context context, List<StoreMenu> menus) {
        this.context = context;
        this.menus = menus;
    }

    @NonNull
    @Override
    public MenuAdapter.ViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StoreMenu menu= menus.get(position);
        holder.bind(menu);
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        menus.clear();
        notifyDataSetChanged();
    }

    /*
    // Add a list of items -- change to type used
    public void addAll(List<Menu> menus) {
        menus.addAll(menus);
        notifyDataSetChanged();
    }*/

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivImage;
        private TextView tvFoodName;
        private TextView tvPrice;
        private ParseFile image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage_menu);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvPrice = itemView.findViewById(R.id.tvPrice);

        }

        // bind the view elements to the post
        public void bind(StoreMenu menu) {
            tvFoodName.setText(menu.getFoodName());
            tvPrice.setText("Price: $ " + menu.getPrice());

            image = menu.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }

        }
    }
}