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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.activity.CrudArticleActivity;
import com.healthymeals.sayfine.model.Article;
import java.util.List;

public class CrudArticleAdapter extends RecyclerView.Adapter<CrudArticleAdapter.MyViewHolder> {
    private CrudArticleActivity activity;
    private Context context;
    private List<Article> mList;

    public CrudArticleAdapter(Context context, CrudArticleActivity activity, List<Article> mList){
        this.context = context;
        this.activity = activity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public CrudArticleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.list_article , parent , false);
        return new CrudArticleAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CrudArticleAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtArticleTitle.setText(mList.get(position).getTitle());
        holder.txtArticleDate.setText(mList.get(position).getTimestamp().toDate().toString());
        Glide.with(context).load(mList.get(position).getThumbUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imgArticleThumb);
        holder.cardArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.selectArticle(Integer.valueOf(position), mList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtArticleTitle;
        TextView txtArticleDate;
        ImageView imgArticleThumb;
        MaterialCardView cardArticle;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtArticleTitle = itemView.findViewById(R.id.txtArticleTitle);
            txtArticleDate = itemView.findViewById(R.id.txtArticleDate);
            imgArticleThumb = itemView.findViewById(R.id.imgArticleThumb);
            cardArticle = itemView.findViewById(R.id.cardArticle);
        }
    }
}
