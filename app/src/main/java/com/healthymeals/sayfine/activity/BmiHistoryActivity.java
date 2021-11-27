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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.adapter.crud.BmiListAdapter;
import com.healthymeals.sayfine.adapter.crud.CrudPacketAdapter;
import com.healthymeals.sayfine.model.BodyMassIndex;
import com.healthymeals.sayfine.model.Menu;

import java.util.ArrayList;

public class BmiHistoryActivity extends AppCompatActivity {
    private ArrayList<BodyMassIndex> list = new ArrayList<>();
    private BmiListAdapter adapter;
    private RecyclerView recyclerView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_history);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerViewf);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getUserBodyMassIndexs();
        adapter = new BmiListAdapter(this, this, list);
        recyclerView.setAdapter(adapter);
    }

    private void getUserBodyMassIndexs(){
        firebaseFirestore.collection("Users").document(mAuth.getUid()).collection("BodyMassIndexs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("Firestore error", document.getId() + " => " + document.getData());
                        BodyMassIndex bmi = new BodyMassIndex(document.getId(), document.getDouble("height").floatValue(), document.getDouble("weight").floatValue(), document.getTimestamp("timestamp"));
                        list.add(bmi);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(BmiHistoryActivity.this, "Gagal mendapatkan data IMT!", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore error", task.getException().toString());
                    return;
                }
            }
        });
    }
}