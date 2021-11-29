package com.healthymeals.sayfine.adapter.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.healthymeals.sayfine.GlideApp;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.helper.IntentHelper;
import com.healthymeals.sayfine.model.Order;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {
    private Context context;
    private List<Order> mList;
    private StorageReference storageReference;

    public OrderListAdapter(Context context, List<Order> mList){
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public OrderListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_order_status , parent , false);
        return new OrderListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        storageReference = FirebaseStorage.getInstance().getReference().child("menus/" + mList.get(position).getMenuId() + ".jpg");
        String by = "";
        if (mList.get(position).getOrderBy() == 0){
            by = "Pesan melalui GoFood";
        }
        else if (mList.get(position).getOrderBy() == 1){
            by = "Pesan melalui GrabFood";
        }
        else if (mList.get(position).getOrderBy() == 2){
            by = "Pesan melalui ShopeeFood";
        }
        holder.txtOrderName.setText(mList.get(position).getMenuName());
        holder.txtOrderBy.setText(by);
        holder.txtOrderDate.setText(mList.get(position).getTimestamp().toDate().toString());
        if (mList.get(position).getVerified() == false){
            holder.txtOrderName.setText("Belum Terverifikasi");
        }
        else {
            holder.txtOrderName.setText("Terverifikasi");
            holder.cardOrderStatus.setCardBackgroundColor(Color.GREEN);
        }

        GlideApp.with(context).load(storageReference).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imgOrderThumb);
        holder.imgOrderThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*IntentHelper.addObjectForKey(mList.get(position), "clickedOrder");
                Intent intent = new Intent (v.getContext(), OrderDetailActivity.class);
                v.getContext().startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imgOrderThumb;
        MaterialCardView cardOrderStatus;
        TextView txtOrderName;
        TextView txtOrderDate;
        TextView txtOrderBy;
        TextView txtOrderStatus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgOrderThumb = itemView.findViewById(R.id.imgOrderThumb);
            cardOrderStatus = itemView.findViewById(R.id.cardOrderStatus);
            txtOrderName = itemView.findViewById(R.id.txtOrderName);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            txtOrderBy = itemView.findViewById(R.id.txtOrderBy);
            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
        }
    }
}
