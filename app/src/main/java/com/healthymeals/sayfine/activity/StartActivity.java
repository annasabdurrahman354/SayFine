package com.healthymeals.sayfine.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.activity.authentication.LoginActivity;
import com.healthymeals.sayfine.activity.authentication.RegisterActivity;

import java.io.InputStream;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout lnrStart;
    private Button btnGoToRegister;
    private Button btnGoToLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser() != null){
            DocumentReference docRef = firebaseFirestore.collection("Users").document(mAuth.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            startActivity(new Intent(StartActivity.this, MainActivity.class));
                            finish();
                        } else {
                            lnrStart.setVisibility(View.VISIBLE);
                            mAuth.signOut();
                            Log.d("Check register detail", "Registered but didn't finished the procedure");
                        }
                    } else {
                        Log.d("Check register detail", "Get failed with ", task.getException());
                    }
                }
            });
        }
        else lnrStart.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        lnrStart = findViewById(R.id.lnrStart);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);
        btnGoToLogin = findViewById(R.id.btnGoToLogin);

        btnGoToRegister.setOnClickListener(this);
        btnGoToLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGoToRegister:
                Intent intent1 = new Intent(this, RegisterActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.btnGoToLogin:
                Intent intent2 = new Intent(this, LoginActivity.class);
                startActivity(intent2);
                finish();
                break;
            default:
                break;
        }
    }

}