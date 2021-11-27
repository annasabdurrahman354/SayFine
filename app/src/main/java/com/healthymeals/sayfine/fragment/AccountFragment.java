package com.healthymeals.sayfine.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.activity.BmiActivity;
import com.healthymeals.sayfine.activity.CrudArticleActivity;
import com.healthymeals.sayfine.activity.CrudMenuActivity;
import com.healthymeals.sayfine.activity.CrudPacketActivity;

public class AccountFragment extends Fragment {
    private CardView cardAddress;
    private CardView cardBmi;
    private CardView cardDBArticle;
    private CardView cardDBMenu;
    private CardView cardDBOrder;
    private CardView cardDBPacket;
    private CardView cardDBPromo;
    private ImageView imgProfile;
    private TextView txtName;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        cardBmi = view.findViewById(R.id.cardBmi);
        cardDBArticle = view.findViewById(R.id.cardDatabaseArticle);
        cardDBMenu = view.findViewById(R.id.cardDatabaseMenu);
        cardDBOrder = view.findViewById(R.id.cardDatabaseOrder);
        cardDBPacket = view.findViewById(R.id.cardDatabasePacket);
        cardDBPromo = view.findViewById(R.id.cardDatabasePromo);
        imgProfile = view.findViewById(R.id.imgProfile);
        txtName = view.findViewById(R.id.txtName);

        firebaseFirestore.collection("Users").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        txtName.setText(document.getString("name"));
                        Glide.with(view.getContext()).load(document.getString("profileUrl")).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgProfile);
                        Log.d("Success", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("ERROR", "No such document");
                    }
                } else {
                    Log.d("ERROR", "get failed with ", task.getException());
                }
            }
        });
        cardBmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BmiActivity.class);
                startActivity(intent);
            }
        });
        cardDBArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CrudArticleActivity.class);
                startActivity(intent);
            }
        });
        cardDBMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CrudMenuActivity.class);
                startActivity(intent);
            }
        });
        cardDBPacket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CrudPacketActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
