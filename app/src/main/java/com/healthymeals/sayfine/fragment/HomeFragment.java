package com.healthymeals.sayfine.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.healthymeals.sayfine.GlideApp;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.activity.ArticleListActivity;
import com.healthymeals.sayfine.activity.BmiActivity;
import com.healthymeals.sayfine.activity.CustomerChatRoomActivity;
import com.healthymeals.sayfine.activity.MenuListActivity;
import com.healthymeals.sayfine.activity.authentication.LoginActivity;
import com.healthymeals.sayfine.adapter.list.PacketListAdapter;
import com.healthymeals.sayfine.adapter.list.PromoListAdapter;
import com.healthymeals.sayfine.model.Packet;
import com.healthymeals.sayfine.model.Promo;
import com.healthymeals.sayfine.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment {
    private Button btnMenu;
    private Button btnBmi;
    private Button btnArtikel;
    private ImageButton btnChat;
    private RecyclerView recyclerViewPacket;
    private RecyclerView recyclerViewPromo;

    private PacketListAdapter packetAdapter;
    private PromoListAdapter promoAdapter;

    private ArrayList<Packet> packetList = new ArrayList<>();
    private ArrayList<Promo> promoList = new ArrayList<>();

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private User mUser;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btnMenu = view.findViewById(R.id.btnMenu);
        btnBmi = view.findViewById(R.id.btnBmi);
        btnArtikel = view.findViewById(R.id.btnArticle);
        btnChat = view.findViewById(R.id.btnChat);
        recyclerViewPacket = view.findViewById(R.id.recyclerViewPacket);
        recyclerViewPromo = view.findViewById(R.id.recyclerViewPromo);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        getPackets();
        getPromotions();

        firebaseFirestore.collection("Users").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        mUser = new User(document.getId(), document.getString("name"), document.getString("profileUrl"), document.getString("phoneNumber"), document.getTimestamp("lastOrder"));
                        Log.d("Success", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("ERROR", "No such document");
                    }
                } else {
                    Log.d("ERROR", "get failed with ", task.getException());
                }
            }
        });
        packetAdapter = new PacketListAdapter(getContext(), packetList);
        recyclerViewPacket.setHasFixedSize(true);
        recyclerViewPacket.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerViewPacket.setAdapter(packetAdapter);

        promoAdapter = new PromoListAdapter(getContext(), promoList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerViewPromo.setHasFixedSize(true);
        recyclerViewPromo.setLayoutManager(layoutManager);
        recyclerViewPromo.setNestedScrollingEnabled(false);
        recyclerViewPromo.setAdapter(promoAdapter);

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChatRoom();
                Intent intent = new Intent(getActivity(), CustomerChatRoomActivity.class);
                startActivity(intent);
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MenuListActivity.class);
                startActivity(intent);
            }
        });

        btnBmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BmiActivity.class);
                startActivity(intent);
            }
        });

        btnArtikel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ArticleListActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void getPackets() {
        firebaseFirestore.collection("Packets").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Toast.makeText(getContext(), "Gagal mendapatkan data paket!", Toast.LENGTH_SHORT).show();
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){
                            int i;
                            Packet packet;
                            packet = new Packet(dc.getDocument().getId(),dc.getDocument().getString("title"), dc.getDocument().getString("description"), dc.getDocument().getString("thumbUrl"), (ArrayList<String>) dc.getDocument().get("menuIdList"));
                            switch (dc.getType()) {
                                case ADDED:
                                    packetList.clear();
                                    packetList.add(packet);
                                    break;
                                case MODIFIED:
                                    i = packetList.indexOf(packetList.stream().filter(temp -> temp.getId().equals(packet.getId())).findFirst().orElse(null));
                                    packetList.set(i, packet);
                                    break;
                                case REMOVED:
                                    i = packetList.indexOf(packetList.stream().filter(temp -> temp.getId().equals(packet.getId())).findFirst().orElse(null));
                                    packetList.remove(i);
                                    break;
                            }
                            packetAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void getPromotions() {
        firebaseFirestore.collection("Promotions").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(getContext(), "Gagal mendapatkan data promo!", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore error",error.getMessage());
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()){
                    int i;
                    Promo promo;
                    promo = new Promo(dc.getDocument().getId(), dc.getDocument().getString("title"), dc.getDocument().getString("description"), dc.getDocument().getString("thumbUrl"), dc.getDocument().getTimestamp("timestamp"));
                    switch (dc.getType()) {
                        case ADDED:
                            promoList.add(promo);
                            break;
                        case REMOVED:
                            i = promoList.indexOf(promoList.stream().filter(temp -> temp.getId().equals(promo.getId())).findFirst().orElse(null));
                            promoList.remove(i);
                            break;
                        case MODIFIED:
                            i = promoList.indexOf(promoList.stream().filter(temp -> temp.getId().equals(promo.getId())).findFirst().orElse(null));
                            promoList.set(i,promo);
                            break;
                    }
                    promoAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void checkChatRoom(){
        firebaseFirestore.collection("ChatRooms").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("userName", mUser.getName());
                        map.put("lastChat", null);
                        firebaseFirestore.collection("ChatRooms").document(mAuth.getUid())
                                .set(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("SUCCESS", "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("ERROR", "Error updating document", e);
                                    }
                                });
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

    }
}
