package com.healthymeals.sayfine.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.adapter.crud.CrudArticleAdapter;
import com.healthymeals.sayfine.model.Article;

import java.util.ArrayList;
import java.util.HashMap;

public class CrudArticleActivity extends AppCompatActivity {
    private TextInputLayout inputTitle;
    private TextInputLayout inputDescription;
    private ImageButton imgThumb;

    private LinearLayout lnrSelected;
    private Button btnCreate;
    private Button btnUpdate;
    private Button btnDelete;
    private Button btnCancel;

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    private Uri thumbUrl;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private CrudArticleAdapter adapter;

    private ArrayList<Article> list;

    public Article selectedArticle;
    public Integer selectedArticleIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_article);
        inputTitle = findViewById(R.id.inputTitle);
        inputDescription = findViewById(R.id.inputDescription);
        imgThumb = findViewById(R.id.imgThumb);

        lnrSelected = findViewById(R.id.lnrSelected);
        btnCreate = findViewById(R.id.btnCreate);
        btnUpdate= findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnCancel = findViewById(R.id.btnCancel);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mengunggah data artikel...");

        firebaseFirestore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        getArticles();
        adapter = new CrudArticleAdapter(this, this , list);
        recyclerView.setAdapter(adapter);

        imgThumb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImagePicker.with(CrudArticleActivity.this)
                        .crop(3f, 2f)
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArticle(
                        inputTitle.getEditText().getText().toString(),
                        inputDescription.getEditText().getText().toString(),
                        thumbUrl
                );
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateArticle(
                        selectedArticleIndex,
                        selectedArticle,
                        inputTitle.getEditText().getText().toString(),
                        inputDescription.getEditText().getText().toString(),
                        thumbUrl
                );
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteArticle(
                        selectedArticleIndex,
                        selectedArticle
                );
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectArticle();
            }
        });
    }

    public void selectArticle(Integer index, Article article){

        selectedArticleIndex = index;
        selectedArticle = article;
        inputTitle.getEditText().setText(article.getTitle());
        inputDescription.getEditText().setText(article.getDescription());
        Glide.with(this).load(article.getThumbUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgThumb);
        lnrSelected.setVisibility(View.VISIBLE);
        btnCreate.setVisibility(View.GONE);

    }

    public void deselectArticle(){
        clearInput();
        lnrSelected.setVisibility(View.GONE);
        btnCreate.setVisibility(View.VISIBLE);
    }

    private void clearInput(){
        selectedArticleIndex = null;
        selectedArticle = null;
        inputTitle.getEditText().setText(null);
        inputDescription.getEditText().setText(null);
        imgThumb.setImageResource(R.drawable.pic_menu_thumbnail);
        thumbUrl = null;
    }

    private void addArticle(String title, String description, Uri thumbUrl){
        if (!title.isEmpty() && !description.isEmpty() && thumbUrl != null){
            String articleId = firebaseFirestore.collection("Articles").document().getId();
            storageReference = FirebaseStorage.getInstance().getReference().child("article/" + articleId + ".jpg");
            storageReference.putFile(thumbUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Timestamp timestamp = Timestamp.now();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("title", title);
                            map.put("description", description);
                            map.put("thumbUrl", uri.toString());
                            map.put("timestamp", timestamp);
                            firebaseFirestore.collection("Articles").document(articleId).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Article article = new Article(articleId, title, description, uri.toString(), timestamp);
                                        adapter.notifyDataSetChanged();
                                        clearInput();
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    progressDialog.show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(CrudArticleActivity.this, "Gagal mengunggah gambar!", Toast.LENGTH_SHORT).show();
                }
            });
        } else Toast.makeText(this, "Isi semua masukkan terlebih dahulu!", Toast.LENGTH_SHORT).show();
    }

    private void updateArticle(Integer index, Article article, String title, String description, Uri thumbUrl){
        if (!title.isEmpty() && !description.isEmpty() && thumbUrl != null){
            storageReference = FirebaseStorage.getInstance().getReference().child("article/" + article.getId() + ".jpg");
            storageReference.putFile(thumbUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Timestamp timestamp = Timestamp.now();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("title", title);
                            map.put("description", description);
                            map.put("thumbUrl", uri.toString());
                            map.put("timestamp", timestamp);
                            firebaseFirestore.collection("Articles").document(article.getId()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Article articleNew = new Article(article.getId(), title, description, uri.toString(), timestamp);
                                        list.set(index.intValue(), articleNew);
                                        adapter.notifyDataSetChanged();
                                        deselectArticle();
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    progressDialog.show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(CrudArticleActivity.this, "Gagal mengunggah gambar!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(!title.isEmpty() && !description.isEmpty() && thumbUrl == null) {
            Timestamp timestamp = Timestamp.now();
            HashMap<String, Object> map = new HashMap<>();
            map.put("title", title);
            map.put("description", description);
            map.put("timestamp", timestamp);
            firebaseFirestore.collection("Articles").document(article.getId()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Article articleNew = new Article(article.getId(), title, description, article.getThumbUrl(), timestamp);
                        list.set(index.intValue(), articleNew);
                        adapter.notifyDataSetChanged();
                        deselectArticle();
                        progressDialog.dismiss();
                    }
                }
            });
        }
        else Toast.makeText(this, "Isi semua masukkan terlebih dahulu!", Toast.LENGTH_SHORT).show();
    }

    public void deleteArticle(Integer index, Article article){
        firebaseFirestore.collection("Articles").document(article.getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            list.remove(index.intValue());
                            adapter.notifyDataSetChanged();
                            deselectArticle();
                            Toast.makeText(CrudArticleActivity.this, "Artikel telah terhapus!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(CrudArticleActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void getArticles() {
        firebaseFirestore.collection("Articles")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            if (progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Toast.makeText(CrudArticleActivity.this, "Gagal mendapatkan data artikel!", Toast.LENGTH_SHORT).show();
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){
                            Article article;
                            article = new Article(dc.getDocument().getId(), dc.getDocument().getString("title"), dc.getDocument().getString("description"), dc.getDocument().getString("thumbUrl"), dc.getDocument().getTimestamp("timestamp"));
                            switch (dc.getType()) {
                                case ADDED:
                                    list.add(article);
                                    break;
                                case MODIFIED:

                                    break;
                                case REMOVED:

                                    break;
                            }
                            adapter.notifyDataSetChanged();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            thumbUrl = data.getData();
            imgThumb.setImageURI(thumbUrl);
        }
        else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }

    }
}