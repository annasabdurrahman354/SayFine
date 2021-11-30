package com.healthymeals.sayfine.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.adapter.list.ArticleListAdapter;
import com.healthymeals.sayfine.adapter.list.ChatRoomListAdapter;
import com.healthymeals.sayfine.model.Article;
import com.healthymeals.sayfine.model.Chat;
import com.healthymeals.sayfine.model.ChatRoom;

import java.util.ArrayList;

public class ChatRoomListActivity extends AppCompatActivity {
    private ArrayList<ChatRoom> list = new ArrayList<>();
    private ChatRoomListAdapter adapter;
    private RecyclerView recyclerView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_list);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getChatRooms();
        adapter = new ChatRoomListAdapter(this, this, list);
        recyclerView.setAdapter(adapter);
    }

    private void getChatRooms(){
        firebaseFirestore.collection("ChatRooms").orderBy("lastChat", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("Firestore error", document.getId() + " => " + document.getData());
                        ChatRoom chatRoom = new ChatRoom(document.getId(), document.getString("userName"),document.getTimestamp("lastChat"));
                        list.add(chatRoom);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(ChatRoomListActivity.this, "Gagal mendapatkan data chat room!", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore error", task.getException().toString());
                    return;
                }
            }
        });
    }
}