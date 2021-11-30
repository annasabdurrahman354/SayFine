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
import com.healthymeals.sayfine.adapter.list.CustomerChatListAdapter;
import com.healthymeals.sayfine.model.Chat;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomerChatRoomActivity extends AppCompatActivity {

    private EditText inputChat;
    private ImageButton btnSend;
    private RecyclerView recyclerView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private ArrayList<Chat> list = new ArrayList<>();
    private CustomerChatListAdapter adapter;

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
        adapter = new CustomerChatListAdapter(this, this, list);
        recyclerView.setAdapter(adapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendChat();
            }
        });
    }

    private void getChats(){
        firebaseFirestore.collection("ChatRooms").document(mAuth.getUid()).collection("Chats").orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Toast.makeText(CustomerChatRoomActivity.this, "Gagal mendapatkan data chat!", Toast.LENGTH_SHORT).show();
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){
                            Chat chat = new Chat(dc.getDocument().getId(), dc.getDocument().getBoolean("isCustomer"), dc.getDocument().getString("text"), dc.getDocument().getTimestamp("timestamp"));
                            switch (dc.getType()) {
                                case ADDED:
                                    list.add(chat);
                                    break;
                                case MODIFIED:

                                    break;
                                case REMOVED:

                                    break;
                            }
                            adapter.notifyDataSetChanged();
                        }
                        recyclerView.scrollToPosition(list.size() - 1);
                    }
                });
    }

    private void sendChat(){
        if (!inputChat.getText().toString().isEmpty()){
            Timestamp now = Timestamp.now();
            HashMap<String, Object> map = new HashMap<>();
            map.put("isCustomer", true);
            map.put("text", inputChat.getText().toString());
            map.put("timestamp", now);
            firebaseFirestore.collection("ChatRooms").document(mAuth.getUid()).collection("Chats")
                    .add(map)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            /*list.add(new Chat(documentReference.getId(), true, inputChat.getText().toString(),now));*/
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
        else Toast.makeText(CustomerChatRoomActivity.this, "Isi chat terlebih dahulu!", Toast.LENGTH_SHORT).show();

    }
}