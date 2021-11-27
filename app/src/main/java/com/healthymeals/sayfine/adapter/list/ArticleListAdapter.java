package com.healthymeals.sayfine.adapter.list;

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
import com.healthymeals.sayfine.activity.ArticleDetailActivity;
import com.healthymeals.sayfine.activity.ArticleListActivity;
import com.healthymeals.sayfine.helper.IntentHelper;
import com.healthymeals.sayfine.model.Article;

import java.util.List;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.MyViewHolder> {
    private ArticleListActivity activity;
    private Context context;
    private List<Article> mList;

    public ArticleListAdapter(Context context, ArticleListActivity activity, List<Article> mList){
        this.context = context;
        this.activity = activity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ArticleListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.list_article , parent , false);
        return new ArticleListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtArticleTitle.setText(mList.get(position).getTitle());
        holder.txtArticleDate.setText(mList.get(position).getTimestamp().toDate().toString());
        Glide.with(context).load(mList.get(position).getThumbUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imgArticleThumb);
        holder.cardArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.addObjectForKey(mList.get(position), "clickedArticle");
                Intent intent = new Intent (v.getContext(), ArticleDetailActivity.class);
                v.getContext().startActivity(intent);
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
