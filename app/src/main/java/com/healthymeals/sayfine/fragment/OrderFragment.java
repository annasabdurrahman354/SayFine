package com.healthymeals.sayfine.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.adapter.list.OrderListAdapter;
import com.healthymeals.sayfine.model.Menu;
import com.healthymeals.sayfine.model.Order;

import java.util.ArrayList;

public class OrderFragment extends Fragment {
    private RecyclerView recyclerView;
    private OrderListAdapter adapter;
    private ArrayList<Order> list;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        list = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        getOrders();
        adapter = new OrderListAdapter(getContext(), list);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void getOrders() {
        firebaseFirestore.collection("Users").document(mAuth.getUid()).collection("Orders")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Toast.makeText(getParentFragment().getContext(), "Gagal mendapatkan data orderan!", Toast.LENGTH_SHORT).show();
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){
                            Order order;
                            order = new Order(dc.getDocument().getId(), dc.getDocument().getString("userId"), dc.getDocument().getString("menuId"), dc.getDocument().getString("menuName"), ((Number) dc.getDocument().getDouble("orderBy")).intValue(), dc.getDocument().getBoolean("verified"), dc.getDocument().getTimestamp("timestamp"));
                            switch (dc.getType()) {
                                case ADDED:
                                    list.add(order);
                                    break;
                                case MODIFIED:

                                    break;
                                case REMOVED:

                                    break;
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
