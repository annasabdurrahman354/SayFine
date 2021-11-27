package com.healthymeals.sayfine.adapter.crud;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.activity.MenuDetailActivity;
import com.healthymeals.sayfine.helper.IntentHelper;
import com.healthymeals.sayfine.model.Menu;
import java.util.ArrayList;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.RecyclerViewHolder> {
    private Context context;
    private ArrayList<Menu> mList;

    public MenuListAdapter(Context context, ArrayList<Menu> mList){
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_menu , parent , false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtMenuTitle.setText(mList.get(position).getTitle());
        holder.txtMenuPrice.setText("Rp " + mList.get(position).getPrice().toString());
        Glide.with(context).load(mList.get(position).getThumbUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imgMenuThumb);
        holder.cardMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.addObjectForKey(mList.get(position), "clickedMenu");
                Intent intent = new Intent (v.getContext(), MenuDetailActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView txtMenuTitle;
        TextView txtMenuPrice;
        ImageView imgMenuThumb;
        MaterialCardView cardMenu;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMenuTitle = itemView.findViewById(R.id.txtMenuTitle);
            txtMenuPrice = itemView.findViewById(R.id.txtMenuPrice);
            imgMenuThumb = itemView.findViewById(R.id.imgMenuThumb);
            cardMenu = itemView.findViewById(R.id.cardMenu);
        }
    }
}