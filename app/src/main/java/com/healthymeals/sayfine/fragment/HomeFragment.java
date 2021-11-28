package com.healthymeals.sayfine.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.activity.ArticleListActivity;
import com.healthymeals.sayfine.activity.BmiActivity;
import com.healthymeals.sayfine.activity.MenuListActivity;
import com.healthymeals.sayfine.activity.crud.CrudPacketActivity;
import com.healthymeals.sayfine.adapter.list.MenuListAdapter;
import com.healthymeals.sayfine.adapter.list.PacketListAdapter;
import com.healthymeals.sayfine.adapter.list.PromoListAdapter;
import com.healthymeals.sayfine.model.Packet;
import com.healthymeals.sayfine.model.Promo;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private Button btnMenu;
    private Button btnBmi;
    private Button btnArtikel;
    private RecyclerView recyclerViewPacket;
    private RecyclerView recyclerViewPromo;

    private PacketListAdapter packetAdapter;
    private PromoListAdapter promoAdapter;

    private ArrayList<Packet> packetList = new ArrayList<>();
    private ArrayList<Promo> promoList = new ArrayList<>();

    private FirebaseFirestore firebaseFirestore;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btnMenu = view.findViewById(R.id.btnMenu);
        btnBmi = view.findViewById(R.id.btnBmi);
        btnArtikel = view.findViewById(R.id.btnArticle);
        recyclerViewPacket = view.findViewById(R.id.recyclerViewPacket);
        recyclerViewPromo = view.findViewById(R.id.recyclerViewPromo);

        firebaseFirestore = FirebaseFirestore.getInstance();

        getPackets();
        getPromotions();

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
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Toast.makeText(getContext(), "Gagal mendapatkan data paket!", Toast.LENGTH_SHORT).show();
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){
                            Packet packet;
                            packet = new Packet(dc.getDocument().getId(),dc.getDocument().getString("title"), dc.getDocument().getString("description"), dc.getDocument().getString("thumbUrl"), (ArrayList<String>) dc.getDocument().get("menuIdList"));
                            switch (dc.getType()) {
                                case ADDED:
                                case MODIFIED:
                                case REMOVED:
                                    packetList.clear();
                                    packetList.add(packet);
                                    break;
                            }
                            packetAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void getPromotions() {
        firebaseFirestore.collection("Promotions").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(getContext(), "Gagal mendapatkan data promo!", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore error",error.getMessage());
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()){
                    Promo promo;
                    promo = new Promo(dc.getDocument().getId(),dc.getDocument().getString("title"), dc.getDocument().getString("description"), dc.getDocument().getString("thumbUrl"), dc.getDocument().getTimestamp("timestamp"));
                    switch (dc.getType()) {
                        case ADDED:
                        case REMOVED:
                        case MODIFIED:
                            promoList.clear();
                            promoList.add(promo);
                            break;
                    }
                    promoAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
