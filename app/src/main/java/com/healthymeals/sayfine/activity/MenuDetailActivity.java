package com.healthymeals.sayfine.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.helper.IntentHelper;
import com.healthymeals.sayfine.model.Menu;

import java.util.HashMap;

public class MenuDetailActivity extends AppCompatActivity {
    private Menu clickedMenu;
    private Button btnMenuOrder;
    private ImageView imgThumb;
    private TextView txtMenuTitle;
    private TextView txtMenuCalorie;
    private TextView txtMenuPrice;
    private TextView txtMenuDescription;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

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

        HashMap<String, Object> map = new HashMap<>();
        Timestamp timestamp = Timestamp.now();
        map.put("userId", mAuth.getUid());
        map.put("menuId", clickedMenu.getId());
        map.put("menuName", clickedMenu.getTitle());
        map.put("verified", false);
        map.put("timestamp", timestamp);

        btnGoFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.put("orderBy", 0);
                firebaseFirestore.collection("Users").document(mAuth.getUid()).collection("Orders").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            HashMap<String, Object> map2 = new HashMap<>();
                            map2.put("lastOrder", timestamp);
                            firebaseFirestore.collection("Users").document(mAuth.getUid()).update(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(@NonNull Void unused) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(clickedMenu.getGoFoodUrl())));
                                    bottomSheetDialog.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        });

        btnGrabFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.put("orderBy", 1);
                firebaseFirestore.collection("Users").document(mAuth.getUid()).collection("Orders").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            HashMap<String, Object> map2 = new HashMap<>();
                            map2.put("lastOrder", timestamp);
                            firebaseFirestore.collection("Users").document(mAuth.getUid()).update(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(@NonNull Void unused) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(clickedMenu.getGrabFoodUrl())));
                                    bottomSheetDialog.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        });

        btnShopeeFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.put("orderBy", 2);
                firebaseFirestore.collection("Users").document(mAuth.getUid()).collection("Orders").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            HashMap<String, Object> map2 = new HashMap<>();
                            map2.put("lastOrder", timestamp);
                            firebaseFirestore.collection("Users").document(mAuth.getUid()).update(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(@NonNull Void unused) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(clickedMenu.getShopeeFoodUrl())));
                                    bottomSheetDialog.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        });

        bottomSheetDialog.show();
    }
}