package com.healthymeals.sayfine.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.helper.IntentHelper;
import com.healthymeals.sayfine.model.Article;
import com.healthymeals.sayfine.model.Menu;

public class ArticleDetailActivity extends AppCompatActivity {
    private Article clickedArticle;
    private ImageView imgThumb;
    private TextView txtArticleTitle;
    private TextView txtArticleDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        clickedArticle = (Article) IntentHelper.getObjectForKey("clickedArticle");

        imgThumb = findViewById(R.id.imgThumb);
        txtArticleTitle = findViewById(R.id.txtArticleTitle);
        txtArticleDescription = findViewById(R.id.txtArticleDescription);

        Glide.with(this).load(clickedArticle.getThumbUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgThumb);
        txtArticleTitle.setText(clickedArticle.getTitle());
        txtArticleDescription.setText(clickedArticle.getDescription());

    }
}