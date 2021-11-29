package com.healthymeals.sayfine.activity.crud;

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
import com.healthymeals.sayfine.adapter.crud.CrudPromoAdapter;
import com.healthymeals.sayfine.model.Promo;

import java.util.ArrayList;
import java.util.HashMap;

public class CrudPromoActivity extends AppCompatActivity {
    private TextInputLayout inputTitle;
    private TextInputLayout inputDescription;
    private ImageButton imgThumb;
    private Timestamp timestamp;
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
    private CrudPromoAdapter adapter;

    private ArrayList<Promo> list = new ArrayList<>();

    public Promo selectedPromo;
    public Integer selectedPromoIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_promo);
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
        progressDialog.setMessage("Mengunggah data promo...");

        firebaseFirestore = FirebaseFirestore.getInstance();
        getPromotions();
        adapter = new CrudPromoAdapter(this, this , list);
        recyclerView.setAdapter(adapter);

        imgThumb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImagePicker.with(CrudPromoActivity.this)
                        .crop(4f, 2f)
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPromotion(
                        inputTitle.getEditText().getText().toString(),
                        inputDescription.getEditText().getText().toString(),
                        thumbUrl
                );
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePromotion(
                        selectedPromoIndex,
                        selectedPromo,
                        inputTitle.getEditText().getText().toString(),
                        inputDescription.getEditText().getText().toString(),
                        thumbUrl
                );
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePromotion(
                        selectedPromoIndex,
                        selectedPromo
                );
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectPromotion();
            }
        });
    }

    public void selectPromotion(Integer index, Promo promo){
        selectedPromoIndex = index;
        selectedPromo = promo;
        inputTitle.getEditText().setText(promo.getTitle());
        inputDescription.getEditText().setText(promo.getMenuId());
        Glide.with(this).load(promo.getThumbUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgThumb);
        lnrSelected.setVisibility(View.VISIBLE);
        btnCreate.setVisibility(View.GONE);
    }

    private void deselectPromotion(){
        clearInput();
        lnrSelected.setVisibility(View.GONE);
        btnCreate.setVisibility(View.VISIBLE);
    }

    private void clearInput(){
        selectedPromoIndex = null;
        selectedPromo = null;
        inputTitle.getEditText().setText(null);
        inputDescription.getEditText().setText(null);
        imgThumb.setImageResource(R.drawable.pic_thumbnail);
        thumbUrl = null;
    }

    private void addPromotion(String title, String description, Uri thumbUrl){
        if (!title.isEmpty() && !description.isEmpty() && thumbUrl != null){
            String promoId = firebaseFirestore.collection("Promotions").document().getId();
            storageReference = FirebaseStorage.getInstance().getReference().child("promotions/" + promoId + ".jpg");
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
                            firebaseFirestore.collection("Promotions").document(promoId).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
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
                    Toast.makeText(CrudPromoActivity.this, "Gagal mengunggah gambar!", Toast.LENGTH_SHORT).show();
                }
            });
        } else Toast.makeText(this, "Isi semua masukkan terlebih dahulu!", Toast.LENGTH_SHORT).show();
    }

    private void updatePromotion(Integer index, Promo promo, String title, String description, Uri thumbUrl){
        if (!title.isEmpty() && !description.isEmpty() && thumbUrl != null){
            storageReference = FirebaseStorage.getInstance().getReference().child("promotions/" + promo.getId() + ".jpg");
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
                            firebaseFirestore.collection("Promotions").document(promo.getId()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Promo promoNew = new Promo(promo.getId(), title, description, uri.toString(), timestamp);
                                        list.set(index.intValue(), promoNew);
                                        adapter.notifyDataSetChanged();
                                        deselectPromotion();
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
                    Toast.makeText(CrudPromoActivity.this, "Gagal mengunggah gambar!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(!title.isEmpty() && !description.isEmpty() && thumbUrl == null) {
            Timestamp timestamp = Timestamp.now();
            HashMap<String, Object> map = new HashMap<>();
            map.put("title", title);
            map.put("description", description);
            map.put("timestamp", timestamp);
            firebaseFirestore.collection("Promotions").document(promo.getId()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Promo promoNew = new Promo(promo.getId(), title, description, promo.getThumbUrl(), timestamp);
                        list.set(index.intValue(), promoNew);
                        adapter.notifyDataSetChanged();
                        deselectPromotion();
                        progressDialog.dismiss();
                    }
                }
            });
        }
        else Toast.makeText(this, "Isi semua masukkan terlebih dahulu!", Toast.LENGTH_SHORT).show();
    }

    public void deletePromotion(Integer index, Promo promo){
        firebaseFirestore.collection("Promotions").document(promo.getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            list.remove(index.intValue());
                            adapter.notifyDataSetChanged();
                            deselectPromotion();
                            Toast.makeText(CrudPromoActivity.this, "Promo telah terhapus!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(CrudPromoActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getPromotions() {
        firebaseFirestore.collection("Promotions")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            if (progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Toast.makeText(CrudPromoActivity.this, "Gagal mendapatkan data promo!", Toast.LENGTH_SHORT).show();
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){
                            Promo promo;
                            promo = new Promo(dc.getDocument().getId(), dc.getDocument().getString("title"), dc.getDocument().getString("description"), dc.getDocument().getString("thumbUrl"), dc.getDocument().getTimestamp("timestamp"));
                            switch (dc.getType()) {
                                case ADDED:
                                    list.add(promo);
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