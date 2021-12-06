package com.healthymeals.sayfine.adapter.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.storage.FirebaseStorage;
import com.healthymeals.sayfine.GlideApp;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.activity.OrderedUserListActivity;
import com.healthymeals.sayfine.activity.crud.CrudOrderListActivity;
import com.healthymeals.sayfine.helper.IntentHelper;
import com.healthymeals.sayfine.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderedUserListAdapter extends RecyclerView.Adapter<OrderedUserListAdapter.MyViewHolder> {
    private OrderedUserListActivity activity;
    private Context context;
    private List<User> mList;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    public OrderedUserListAdapter(Context context, OrderedUserListActivity activity, List<User> mList){
        this.context = context;
        this.activity = activity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public OrderedUserListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.list_chat_room , parent , false);
        return new OrderedUserListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderedUserListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy, HH:mm:ss");
        String strDate = dateFormat.format(mList.get(position).getLastOrder().toDate());

        holder.txtCustomerName.setText(mList.get(position).getName());
        holder.txtLastChat.setText(strDate);

        GlideApp.with(context)
                .asBitmap()
                .load(mList.get(position).getProfileUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        holder.imgCustomerProfile.setImageBitmap(resource);
                    }
                });

        holder.cardCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.addObjectForKey(mList.get(position), "clickedOrderedUser");
                Intent intent = new Intent (v.getContext(), CrudOrderListActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtCustomerName;
        TextView txtLastChat;
        CircleImageView imgCustomerProfile;
        CardView cardCustomer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCustomerName = itemView.findViewById(R.id.txtCustomerName);
            txtLastChat = itemView.findViewById(R.id.txtLastChat);
            imgCustomerProfile = itemView.findViewById(R.id.imgCustomerProfile);
            cardCustomer = itemView.findViewById(R.id.cardCustomer);
        }
    }
}
