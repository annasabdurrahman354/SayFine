package com.healthymeals.sayfine.adapter.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.activity.ArticleDetailActivity;
import com.healthymeals.sayfine.activity.ArticleListActivity;
import com.healthymeals.sayfine.activity.ChatRoomActivity;
import com.healthymeals.sayfine.activity.crud.CrudPacketActivity;
import com.healthymeals.sayfine.helper.IntentHelper;
import com.healthymeals.sayfine.model.Article;
import com.healthymeals.sayfine.model.Chat;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {
    private ChatRoomActivity activity;
    private Context context;
    private List<Chat> mList;
    private String userId;

    public ChatListAdapter(Context context, ChatRoomActivity activity, List<Chat> mList, String userId){
        this.context = context;
        this.activity = activity;
        this.mList = mList;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ChatListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.list_chat , parent , false);
        return new ChatListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (mList.get(position).getUserId().contains(userId) ){
            holder.txtChatSent.setText(mList.get(position).getText());
            holder.txtChatReceived.setVisibility(View.GONE);
            Toast.makeText(context, "aaaData = " + mList.get(position).getUserId() + " User = " + userId, Toast.LENGTH_SHORT).show();
        }
        else if(!mList.get(position).getUserId().contains(userId)){
            holder.txtChatReceived.setText(mList.get(position).getText());
            holder.txtChatSent.setVisibility(View.GONE);
            Toast.makeText(context, "bbbbData = " + mList.get(position).getUserId() + " User = " + userId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtChatReceived;
        TextView txtChatSent;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtChatReceived = itemView.findViewById(R.id.txtChatReceived);
            txtChatSent = itemView.findViewById(R.id.txtChatSent);
        }
    }
}
