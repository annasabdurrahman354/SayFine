package com.healthymeals.sayfine.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

        btnMenuOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });
    }

    private void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_menu);

        LinearLayout btnGoFood = bottomSheetDialog.findViewById(R.id.btnGoFood);
        LinearLayout btnGrabFood = bottomSheetDialog.findViewById(R.id.btnGrabFood);
        LinearLayout btnShopeeFood = bottomSheetDialog.findViewById(R.id.btnShopeeFood);

        btnGoFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(clickedMenu.getGoFoodUrl())));
                bottomSheetDialog.dismiss();
            }
        });

        btnGrabFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(clickedMenu.getGrabFoodUrl())));
                bottomSheetDialog.dismiss();
            }
        });

        btnShopeeFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(clickedMenu.getShopeeFoodUrl())));
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }
}