package com.healthymeals.sayfine.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.adapter.list.ChatRoomListAdapter;
import com.healthymeals.sayfine.adapter.list.OrderedUserListAdapter;
import com.healthymeals.sayfine.model.ChatRoom;
import com.healthymeals.sayfine.model.User;

import java.util.ArrayList;

public class OrderedUserListActivity extends AppCompatActivity {
    private ArrayList<User> list = new ArrayList<>();
    private OrderedUserListAdapter adapter;
    private RecyclerView recyclerView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_user_list);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getOrderedUsers();
        adapter = new OrderedUserListAdapter(this, this, list);
        recyclerView.setAdapter(adapter);
    }

    private void getOrderedUsers(){
        firebaseFirestore.collection("Users").whereNotEqualTo("lastOrder", null).orderBy("lastOrder", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("Firestore error", document.getId() + " => " + document.getData());
                        User user = new User(document.getId(), document.getString("name"),document.getString("profileUrl"), document.getString("phoneNumber"), document.getTimestamp("lastOrder"));
                        list.add(user);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(OrderedUserListActivity.this, "Gagal mendapatkan data user!", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore error", task.getException().toString());
                    return;
                }
            }
        });
    }
}