package com.healthymeals.sayfine.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.activity.crud.CrudPacketActivity;
import com.healthymeals.sayfine.adapter.list.ChatListAdapter;
import com.healthymeals.sayfine.model.Chat;
import com.healthymeals.sayfine.model.Menu;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatRoomActivity extends AppCompatActivity {

    private EditText inputChat;
    private ImageButton btnSend;
    private RecyclerView recyclerView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private ArrayList<Chat> list = new ArrayList<>();
    private ChatListAdapter adapter;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        btnSend = findViewById(R.id.btnSend);
        inputChat = findViewById(R.id.inputChat);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getChats();
        adapter = new ChatListAdapter(this, this, list, mAuth.getUid());
        recyclerView.setAdapter(adapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendChat();
            }
        });
    }

    private void getChats(){
        firebaseFirestore.collection("ChatRooms").document(mAuth.getUid()).collection("Chats").orderBy("timestamp", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("Firestore error", document.getId() + " => " + document.getData());
                        Chat chat = new Chat(document.getId(),document.getString("userId"), document.getString("text"), document.getTimestamp("timestamp"));
                        list.add(chat);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(ChatRoomActivity.this, "Gagal mendapatkan data chat!", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore error", task.getException().toString());
                    return;
                }
            }
        });
    }

    private void sendChat(){
        if (!inputChat.getText().toString().isEmpty()){
            Timestamp now = Timestamp.now();
            HashMap<String, Object> map = new HashMap<>();
            map.put("userId", mAuth.getUid());
            map.put("text", inputChat.getText().toString());
            map.put("timestamp", now);
            firebaseFirestore.collection("ChatRooms").document(mAuth.getUid()).collection("Chats")
                    .add(map)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            list.add(new Chat(documentReference.getId(), mAuth.getUid(), inputChat.getText().toString(),now));
                            inputChat.setText("");
                            adapter.notifyDataSetChanged();
                            firebaseFirestore.collection("ChatRooms").document(mAuth.getUid())
                                    .update("lastChat", Timestamp.now())
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
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("ERROR", "Error adding document", e);
                        }
                    });
        }
        else Toast.makeText(ChatRoomActivity.this, "Isi chat terlebih dahulu!", Toast.LENGTH_SHORT).show();

    }
}