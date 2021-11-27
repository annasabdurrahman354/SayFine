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
import com.healthymeals.sayfine.adapter.list.ArticleListAdapter;
import com.healthymeals.sayfine.model.Article;

import java.util.ArrayList;

public class ArticleListActivity extends AppCompatActivity {
    private ArrayList<Article> list = new ArrayList<>();
    private ArticleListAdapter adapter;
    private RecyclerView recyclerView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getArticle();
        adapter = new ArticleListAdapter(this, this, list);
        recyclerView.setAdapter(adapter);
    }

    private void getArticle(){
        firebaseFirestore.collection("Articles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("Firestore error", document.getId() + " => " + document.getData());
                        Article article = new Article(document.getId(), document.getString("title"), document.getString("description"), document.getString("thumbUrl"),document.getTimestamp("timestamp"));
                        list.add(article);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(ArticleListActivity.this, "Gagal mendapatkan data IMT!", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore error", task.getException().toString());
                    return;
                }
            }
        });
    }
}