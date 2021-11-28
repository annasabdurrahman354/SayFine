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
import com.healthymeals.sayfine.model.Promo;

public class PromoDetailActivity extends AppCompatActivity {
    private Promo clickedPromo;
    private ImageView imgThumb;
    private TextView txtPromoTitle;
    private TextView txtPromoDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_detail);
        clickedPromo = (Promo) IntentHelper.getObjectForKey("clickedPromo");

        imgThumb = findViewById(R.id.imgThumb);
        txtPromoTitle = findViewById(R.id.txtPromoTitle);
        txtPromoDescription = findViewById(R.id.txtPromoDescription);

        Glide.with(this).load(clickedPromo.getThumbUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgThumb);
        txtPromoTitle.setText(clickedPromo.getTitle());
        txtPromoDescription.setText(clickedPromo.getDescription());

    }
}