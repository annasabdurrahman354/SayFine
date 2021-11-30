package com.healthymeals.sayfine.adapter.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.activity.AdminChatRoomActivity;
import com.healthymeals.sayfine.model.Chat;

import java.util.List;

public class AdminChatListAdapter extends RecyclerView.Adapter<AdminChatListAdapter.MyViewHolder> {
    private AdminChatRoomActivity activity;
    private Context context;
    private List<Chat> mList;

    public AdminChatListAdapter(Context context, AdminChatRoomActivity activity, List<Chat> mList){
        this.context = context;
        this.activity = activity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public AdminChatListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.list_chat , parent , false);
        return new AdminChatListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminChatListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (mList.get(position).getCustomer() == false){
            holder.txtChatSent.setText(mList.get(position).getText());
            holder.txtChatReceived.setVisibility(View.GONE);
        }
        else if(mList.get(position).getCustomer() == true){
            holder.txtChatReceived.setText(mList.get(position).getText());
            holder.txtChatSent.setVisibility(View.GONE);
        }
        holder.setIsRecyclable(false);
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
        TextView txtChatReceived;
        TextView txtChatSent;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtChatReceived = itemView.findViewById(R.id.txtChatReceived);
            txtChatSent = itemView.findViewById(R.id.txtChatSent);
        }
    }
}
