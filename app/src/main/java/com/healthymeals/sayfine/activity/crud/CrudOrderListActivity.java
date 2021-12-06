package com.healthymeals.sayfine.activity.crud;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.adapter.crud.CrudOrderListAdapter;
import com.healthymeals.sayfine.helper.IntentHelper;
import com.healthymeals.sayfine.model.Order;
import com.healthymeals.sayfine.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class CrudOrderListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private User clickedOrderedUser;
    private String customerId = "";

    private ArrayList<Order> list = new ArrayList<>();
    private CrudOrderListAdapter adapter;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_user_list);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        clickedOrderedUser = (User) IntentHelper.getObjectForKey("clickedOrderedUser");
        customerId = clickedOrderedUser.getId().trim();
        getOrders();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CrudOrderListAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }

    private void getOrders(){
       /*firebaseFirestore.collection("ChatRooms").document(customerId.toString()).collection("Chats").orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }
                        for (QueryDocumentSnapshot doc : value) {
                            Chat chat = new Chat(doc.getId(), doc.getBoolean("isCustomer"), doc.getString("text"), doc.getTimestamp("timestamp"));
                            list.add(chat);
                            adapter.notifyDataSetChanged();
                        }
                        recyclerView.scrollToPosition(value.size() - 1);
                        Log.d("TAG", "Current cites in CA: " + "cities");
                    }
                });*/

        /*firebaseFirestore.collection("ChatRooms").document("customerId").collection("Chats").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("Firestore error", document.getId() + " => " + document.getData());
                        Chat chat = new Chat(document.getId(), document.getBoolean("isCustomer"), document.getString("text"), document.getTimestamp("timestamp"));
                        list.add(chat);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(AdminChatRoomActivity.this, "Gagal mendapatkan data artikel!", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore error", task.getException().toString());
                    return;
                }
            }
        });*/

        firebaseFirestore.collection("Users").document(customerId.toString()).collection("Orders").orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Toast.makeText(CrudOrderListActivity.this, "Gagal mendapatkan data chat!", Toast.LENGTH_SHORT).show();
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){
                            Order order = new Order(dc.getDocument().getId(), dc.getDocument().getString("userId"), dc.getDocument().getString("menuId"), dc.getDocument().getString("menuName"), ((Number) dc.getDocument().getDouble("orderBy")).intValue(), dc.getDocument().getBoolean("verified"), dc.getDocument().getTimestamp("timestamp"));
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