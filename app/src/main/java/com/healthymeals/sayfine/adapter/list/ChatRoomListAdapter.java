package com.healthymeals.sayfine.adapter.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.healthymeals.sayfine.GlideApp;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.activity.AdminChatRoomActivity;
import com.healthymeals.sayfine.activity.ArticleDetailActivity;
import com.healthymeals.sayfine.activity.ArticleListActivity;
import com.healthymeals.sayfine.activity.ChatRoomListActivity;
import com.healthymeals.sayfine.helper.IntentHelper;
import com.healthymeals.sayfine.model.Article;
import com.healthymeals.sayfine.model.ChatRoom;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoomListAdapter extends RecyclerView.Adapter<ChatRoomListAdapter.MyViewHolder> {
    private ChatRoomListActivity activity;
    private Context context;
    private List<ChatRoom> mList;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    public ChatRoomListAdapter(Context context, ChatRoomListActivity activity, List<ChatRoom> mList){
        this.context = context;
        this.activity = activity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ChatRoomListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.list_chat_room , parent , false);
        return new ChatRoomListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtCustomerName.setText(mList.get(position).getUserName());
        holder.txtLastChat.setText(mList.get(position).getLastChat().toDate().toString());

        GlideApp.with(context)
                .asBitmap()
                .load(firebaseStorage.getReference("profiles/"+mList.get(position).getId()+".jpg"))
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
                IntentHelper.addObjectForKey(mList.get(position), "clickedChatRoom");
                Intent intent = new Intent (v.getContext(), AdminChatRoomActivity.class);
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
