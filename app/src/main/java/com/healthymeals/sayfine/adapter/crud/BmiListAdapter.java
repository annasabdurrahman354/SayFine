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
import com.healthymeals.sayfine.activity.BmiHistoryActivity;
import com.healthymeals.sayfine.activity.CrudArticleActivity;
import com.healthymeals.sayfine.activity.MenuDetailActivity;
import com.healthymeals.sayfine.helper.IntentHelper;
import com.healthymeals.sayfine.model.BodyMassIndex;
import com.healthymeals.sayfine.model.Menu;

import java.util.ArrayList;
import java.util.List;

public class BmiListAdapter extends RecyclerView.Adapter<BmiListAdapter.RecyclerViewHolder> {
    private Context context;
    private BmiHistoryActivity activity;
    private List<BodyMassIndex> mList;

    public BmiListAdapter(Context context, BmiHistoryActivity activity, List<BodyMassIndex> mList){
        this.context = context;
        this.activity = activity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.list_bmi , parent , false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtBmiHeight.setText("Tinggi " + mList.get(position).getHeight().toString() + " cm");
        holder.txtBmiWeight.setText("Berat " + mList.get(position).getMass().toString() + " kg");
        holder.txtBmiDate.setText(mList.get(position).getTimestamp().toDate().toString());

        Float height = mList.get(position).getHeight()/100f;
        Float weight = mList.get(position).getMass();
        Float bmi = (weight/(height*height));
        holder.txtBmi.setText(String.format("%.02f", bmi));

        if(bmi < 18.5f){
            holder.txtBmiStatus.setText("Kurang Berat Badan");
        }
        else if(bmi >= 18.5f && bmi <= 22.9f){
            holder.txtBmiStatus.setText("Normal");
        }
        else if(bmi >= 23f && bmi <= 24.9f){
            holder.txtBmiStatus.setText("Kelebihan Berat Badan");
        }
        else if(bmi >= 25f && bmi <= 29.9f){
            holder.txtBmiStatus.setText("Obesitas Tingkat I");
        }
        else if(bmi <= 30f){
            holder.txtBmiStatus.setText("Obesitas Tingkat II");
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView txtBmi;
        TextView txtBmiHeight;
        TextView txtBmiWeight;
        TextView txtBmiDate;
        TextView txtBmiStatus;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBmi = itemView.findViewById(R.id.txtBmi);
            txtBmiHeight = itemView.findViewById(R.id.txtBmiHeight);
            txtBmiWeight = itemView.findViewById(R.id.txtBmiWeight);
            txtBmiDate = itemView.findViewById(R.id.txtBmiDate);
            txtBmiStatus = itemView.findViewById(R.id.txtBmiStatus);
        }
    }
}