package com.healthymeals.sayfine.adapter.crud;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.healthymeals.sayfine.activity.crud.CrudPacketActivity;
import com.healthymeals.sayfine.model.Packet;

import java.util.List;

public class CrudPacketAdapter extends RecyclerView.Adapter<CrudPacketAdapter.MyViewHolder> {
    private CrudPacketActivity activity;
    private Context context;
    private List<Packet> mList;

    public CrudPacketAdapter(Context context, CrudPacketActivity activity, List<Packet> mList){
        this.context = context;
        this.activity = activity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.list_packet , parent , false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CrudPacketAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtPacketTitle.setText(mList.get(position).getTitle());
        holder.txtPacketAmount.setText(String.valueOf(mList.get(position).getMenuIdList().size()) + " Menu");
        Glide.with(context).load(mList.get(position).getThumbUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imgPacketThumb);
        holder.cardPacket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.selectPacket(Integer.valueOf(position), mList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtPacketTitle;
        TextView txtPacketAmount;
        ImageView imgPacketThumb;
        MaterialCardView cardPacket;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPacketTitle = itemView.findViewById(R.id.txtPacketTitle);
            txtPacketAmount = itemView.findViewById(R.id.txtPacketAmount);
            imgPacketThumb = itemView.findViewById(R.id.imgPacketThumb);
            cardPacket = itemView.findViewById(R.id.cardPacket);
        }
    }
}