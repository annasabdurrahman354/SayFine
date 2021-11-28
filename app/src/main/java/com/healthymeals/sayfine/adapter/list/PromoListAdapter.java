package com.healthymeals.sayfine.adapter.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.activity.PromoDetailActivity;
import com.healthymeals.sayfine.activity.crud.CrudPromoActivity;
import com.healthymeals.sayfine.helper.IntentHelper;
import com.healthymeals.sayfine.model.Promo;

import java.util.List;

public class PromoListAdapter extends RecyclerView.Adapter<PromoListAdapter.MyViewHolder> {
    private Context context;
    private List<Promo> mList;

    public PromoListAdapter(Context context, List<Promo> mList){
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public PromoListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_promo , parent , false);
        return new PromoListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PromoListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(mList.get(position).getThumbUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imgPromoThumb);
        holder.imgPromoThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.addObjectForKey(mList.get(position), "clickedPromo");
                Intent intent = new Intent (v.getContext(), PromoDetailActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imgPromoThumb;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPromoThumb = itemView.findViewById(R.id.imgPromoThumb);
        }
    }
}
