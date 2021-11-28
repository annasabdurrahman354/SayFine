package com.healthymeals.sayfine.adapter.crud;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.activity.crud.CrudPromoActivity;
import com.healthymeals.sayfine.model.Promo;

import java.util.List;

public class CrudPromoAdapter extends RecyclerView.Adapter<CrudPromoAdapter.MyViewHolder> {
    private CrudPromoActivity activity;
    private Context context;
    private List<Promo> mList;

    public CrudPromoAdapter(Context context, CrudPromoActivity activity, List<Promo> mList){
        this.context = context;
        this.activity = activity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public CrudPromoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.list_promo , parent , false);
        return new CrudPromoAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CrudPromoAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(mList.get(position).getThumbUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imgPromoThumb);
        holder.imgPromoThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.selectPromotion(Integer.valueOf(position), mList.get(position));
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
