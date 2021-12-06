package com.healthymeals.sayfine.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.adapter.crud.CrudOrderListAdapter;
import com.healthymeals.sayfine.adapter.list.MenuListAdapter;
import com.healthymeals.sayfine.helper.IntentHelper;
import com.healthymeals.sayfine.model.Menu;
import com.healthymeals.sayfine.model.Order;
import com.healthymeals.sayfine.model.Packet;

import java.util.ArrayList;

public class PacketDetailListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView txtActionBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private Packet clickedPacket;

    private ArrayList<Menu> list = new ArrayList<>();
    private MenuListAdapter adapter;

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

        clickedPacket = (Packet) IntentHelper.getObjectForKey("clickedPacket");
        getMenus();

        txtActionBar = findViewById(R.id.txtActionBar);
        txtActionBar.setText(clickedPacket.getTitle());
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MenuListAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }

    private void getMenus(){
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

        firebaseFirestore.collection("Menus")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Toast.makeText(PacketDetailListActivity.this, "Gagal mendapatkan data menu!", Toast.LENGTH_SHORT).show();
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){
                            if(clickedPacket.getMenuIdList().contains(dc.getDocument().getId())){
                                Menu menu = new Menu(dc.getDocument().getId(),dc.getDocument().getString("title"), dc.getDocument().getString("description"), dc.getDocument().getString("goFoodUrl"), dc.getDocument().getString("grabFoodUrl"), dc.getDocument().getString("shopeeFoodUrl"), dc.getDocument().getString("thumbUrl"), ((Number) dc.getDocument().getDouble("price")).intValue(), ((Number) dc.getDocument().getDouble("calorie")).intValue(), dc.getDocument().getBoolean("type"));
                                switch (dc.getType()) {
                                    case ADDED:
                                        list.add(menu);
                                        break;
                                    case MODIFIED:

                                        break;
                                    case REMOVED:

                                        break;
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}