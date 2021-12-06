package com.healthymeals.sayfine.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.healthymeals.sayfine.GlideApp;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.activity.BmiActivity;
import com.healthymeals.sayfine.activity.ChatRoomListActivity;
import com.healthymeals.sayfine.activity.OrderedUserListActivity;
import com.healthymeals.sayfine.activity.ProfileSettingActivity;
import com.healthymeals.sayfine.activity.crud.CrudArticleActivity;
import com.healthymeals.sayfine.activity.crud.CrudMenuActivity;
import com.healthymeals.sayfine.activity.crud.CrudPacketActivity;
import com.healthymeals.sayfine.activity.StartActivity;
import com.healthymeals.sayfine.activity.crud.CrudPromoActivity;
import com.healthymeals.sayfine.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {
    private CardView cardAddress;
    private CardView cardBmi;
    private CardView cardCustomerChat;
    private CardView cardDBArticle;
    private CardView cardDBMenu;
    private CardView cardDBOrder;
    private CardView cardDBPacket;
    private CardView cardDBProfile;
    private CardView cardDBPromo;
    private ImageView imgProfile;
    private LinearLayout lnrAdmin;
    private CircleImageView cc;
    private ImageButton btnLogout;
    private TextView txtName;

    private User mUser;
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
        cardCustomerChat = view.findViewById(R.id.cardCustomerChat);
        cardDBArticle = view.findViewById(R.id.cardDatabaseArticle);
        cardDBMenu = view.findViewById(R.id.cardDatabaseMenu);
        cardDBOrder = view.findViewById(R.id.cardDatabaseOrder);
        cardDBPacket = view.findViewById(R.id.cardDatabasePacket);
        cardDBProfile = view.findViewById(R.id.cardProfile);
        cardDBPromo = view.findViewById(R.id.cardDatabasePromo);
        lnrAdmin = view.findViewById(R.id.lnrAdmin);
        imgProfile = view.findViewById(R.id.imgProfile);
        txtName = view.findViewById(R.id.txtName);
        btnLogout = view.findViewById(R.id.btnLogout);

        firebaseFirestore.collection("Users").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        mUser = new User(document.getId(), document.getString("name"), document.getString("profileUrl"), document.getString("phoneNumber"), document.getTimestamp("lastOrder"));
                        txtName.setText(mUser.getName());
                        GlideApp.with(getContext())
                                .asBitmap()
                                .load(mUser.getProfileUrl())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        imgProfile.setImageBitmap(resource);
                                    }
                                });
                        Log.d("Success", "DocumentSnapshot data: " + document.getData());
                        if (!mUser.getPhoneNumber().contains("+628570717273")){
                            lnrAdmin.setVisibility(View.GONE);
                        }
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
        cardCustomerChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatRoomListActivity.class);
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
        cardDBOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderedUserListActivity.class);
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
        cardDBProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileSettingActivity.class);
                startActivity(intent);
            }
        });
        cardDBPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CrudPromoActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), StartActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
