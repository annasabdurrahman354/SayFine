package com.healthymeals.sayfine.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.helper.IntentHelper;
import com.healthymeals.sayfine.model.Menu;

public class MenuDetailActivity extends AppCompatActivity {
    private Menu clickedMenu;
    private Button btnMenuOrder;
    private ImageView imgThumb;
    private TextView txtMenuTitle;
    private TextView txtMenuCalorie;
    private TextView txtMenuPrice;
    private TextView txtMenuDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);
        clickedMenu = (Menu) IntentHelper.getObjectForKey("clickedMenu");

        btnMenuOrder = findViewById(R.id.btnMenuOrder);
        imgThumb = findViewById(R.id.imgThumb);
        txtMenuTitle = findViewById(R.id.txtMenuTitle);
        txtMenuCalorie = findViewById(R.id.txtMenuCalorie);
        txtMenuPrice = findViewById(R.id.txtMenuPrice);
        txtMenuDescription = findViewById(R.id.txtMenuDescription);

        Glide.with(this).load(clickedMenu.getThumbUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgThumb);
        txtMenuTitle.setText(clickedMenu.getTitle());
        txtMenuCalorie.setText("Kalori " + clickedMenu.getCalorie().toString() + " kkal");
        txtMenuPrice.setText("Rp " + clickedMenu.getPrice().toString());
        txtMenuDescription.setText(clickedMenu.getDescription());

    }
}