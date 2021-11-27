package com.healthymeals.sayfine.activity.crud;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
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
import com.healthymeals.sayfine.adapter.crud.CrudMenuAdapter;
import com.healthymeals.sayfine.model.Menu;

import java.util.ArrayList;
import java.util.HashMap;

public class CrudMenuActivity extends AppCompatActivity {
    private TextInputLayout inputTitle;
    private TextInputLayout inputDescription;
    private TextInputLayout inputGoFoodUrl;
    private TextInputLayout inputGrabFoodUrl;
    private TextInputLayout inputShopeeFoodUrl;
    private TextInputLayout inputPrice;
    private TextInputLayout inputCalorie;
    private RadioGroup rdgType;
    private RadioButton rdbWeightGain;
    private RadioButton rdbWeightLoss;
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
    private CrudMenuAdapter adapter;

    private ArrayList<Menu> list = new ArrayList<>();
    private Boolean menuType;

    public Menu selectedMenu;
    public Integer selectedMenuIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_menu);
        inputTitle = findViewById(R.id.inputTitle);
        inputDescription = findViewById(R.id.inputDescription);
        inputGoFoodUrl = findViewById(R.id.inputGoFoodUrl);
        inputGrabFoodUrl = findViewById(R.id.inputGrabFoodUrl);
        inputShopeeFoodUrl = findViewById(R.id.inputShopeeFoodUrl);
        inputPrice = findViewById(R.id.inputPrice);
        inputCalorie = findViewById(R.id.inputCalorie);
        rdgType = findViewById(R.id.rdgType);
        rdbWeightGain = findViewById(R.id.rdbWeightGain);
        rdbWeightLoss = findViewById(R.id.rdbWeightLoss);
        imgThumb = findViewById(R.id.imgThumb);

        rdgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rdbWeightGain:
                        menuType = true;
                        break;
                    case R.id.rdbWeightLoss:
                        menuType = false;
                        break;
                }
            }
        });

        lnrSelected = findViewById(R.id.lnrSelected);
        btnCreate = findViewById(R.id.btnCreate);
        btnUpdate= findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnCancel = findViewById(R.id.btnCancel);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mengunggah data menu...");

        firebaseFirestore = FirebaseFirestore.getInstance();
        getMenus();
        adapter = new CrudMenuAdapter(this, this , list);
        recyclerView.setAdapter(adapter);

        imgThumb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImagePicker.with(CrudMenuActivity.this)
                        .crop(4f, 2f)
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMenu(
                        inputTitle.getEditText().getText().toString(),
                        inputDescription.getEditText().getText().toString(),
                        inputGoFoodUrl.getEditText().getText().toString(),
                        inputGrabFoodUrl.getEditText().getText().toString(),
                        inputShopeeFoodUrl.getEditText().getText().toString(),
                        thumbUrl,
                        Integer.valueOf(inputPrice.getEditText().getText().toString()),
                        Integer.valueOf(inputCalorie.getEditText().getText().toString()),
                        menuType
                );
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMenu(
                        selectedMenuIndex,
                        selectedMenu,
                        inputTitle.getEditText().getText().toString(),
                        inputDescription.getEditText().getText().toString(),
                        inputGoFoodUrl.getEditText().getText().toString(),
                        inputGrabFoodUrl.getEditText().getText().toString(),
                        inputShopeeFoodUrl.getEditText().getText().toString(),
                        thumbUrl,
                        Integer.valueOf(inputPrice.getEditText().getText().toString()),
                        Integer.valueOf(inputCalorie.getEditText().getText().toString()),
                        menuType
                );
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMenu(
                        selectedMenuIndex,
                        selectedMenu
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

    public void selectMenu(Integer index, Menu menu){
        selectedMenuIndex = index;
        selectedMenu = menu;
        inputTitle.getEditText().setText(menu.getTitle());
        inputDescription.getEditText().setText(menu.getDescription());
        inputGoFoodUrl.getEditText().setText(menu.getGoFoodUrl());
        inputGrabFoodUrl.getEditText().setText(menu.getGrabFoodUrl());
        inputShopeeFoodUrl.getEditText().setText(menu.getShopeeFoodUrl());
        inputPrice.getEditText().setText(menu.getPrice().toString());
        inputCalorie.getEditText().setText(menu.getCalorie().toString());
        if (menu.getType() == true){
            rdbWeightGain.setChecked(true);
        }
        else{
            rdbWeightLoss.setChecked(true);
        }
        Glide.with(this).load(menu.getThumbUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgThumb);
        lnrSelected.setVisibility(View.VISIBLE);
        btnCreate.setVisibility(View.GONE);
    }

    private void deselectArticle(){
        clearInput();
        lnrSelected.setVisibility(View.GONE);
        btnCreate.setVisibility(View.VISIBLE);
    }

    private void clearInput(){
        selectedMenuIndex = null;
        selectedMenu = null;
        inputTitle.getEditText().setText(null);
        inputDescription.getEditText().setText(null);
        inputGoFoodUrl.getEditText().setText(null);
        inputGrabFoodUrl.getEditText().setText(null);
        inputShopeeFoodUrl.getEditText().setText(null);
        inputPrice.getEditText().setText(null);
        inputCalorie.getEditText().setText(null);
        rdgType.clearCheck();
        menuType = null;
        imgThumb.setImageResource(R.drawable.pic_thumbnail);
        thumbUrl = null;
    }

    private void addMenu(String title, String description, String goFoodUrl, String grabFoodUrl, String shopeeFoodUrl, Uri thumbUrl, Integer price, Integer calorie, Boolean type){
        if (!title.isEmpty() && !description.isEmpty() && thumbUrl != null){
            String menuId = firebaseFirestore.collection("Menus").document().getId();
            storageReference = FirebaseStorage.getInstance().getReference().child("menus/" + menuId + ".jpg");
            storageReference.putFile(thumbUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("title", title);
                            map.put("description", description);
                            map.put("goFoodUrl", goFoodUrl);
                            map.put("grabFoodUrl", grabFoodUrl);
                            map.put("shopeeFoodUrl", shopeeFoodUrl);
                            map.put("thumbUrl", uri.toString());
                            map.put("price", price);
                            map.put("calorie", calorie);
                            map.put("type", type);
                            firebaseFirestore.collection("Menus").document(menuId).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                    Toast.makeText(CrudMenuActivity.this, "Gagal mengunggah gambar!", Toast.LENGTH_SHORT).show();
                }
            });
        } else Toast.makeText(this, "Isi semua masukkan terlebih dahulu!", Toast.LENGTH_SHORT).show();
    }

    private void updateMenu(Integer index, Menu menu, String title, String description, String goFoodUrl, String grabFoodUrl, String shopeeFoodUrl, Uri thumbUrl, Integer price, Integer calorie, Boolean type){
        if (!title.isEmpty() && !description.isEmpty() && !goFoodUrl.isEmpty() && !grabFoodUrl.isEmpty() && !shopeeFoodUrl.isEmpty() && price != null && calorie != null && type != null && thumbUrl != null){
            storageReference = FirebaseStorage.getInstance().getReference().child("menus/" + menu.getId() + ".jpg");
            storageReference.putFile(thumbUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("title", title);
                            map.put("description", description);
                            map.put("goFoodUrl", goFoodUrl);
                            map.put("grabFoodUrl", grabFoodUrl);
                            map.put("shopeeFoodUrl", shopeeFoodUrl);
                            map.put("thumbUrl", uri.toString());
                            map.put("price", price);
                            map.put("calorie", calorie);
                            map.put("type", type);
                            firebaseFirestore.collection("Menus").document(menu.getId()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Menu menuNew = new Menu(menu.getId(), title, description, goFoodUrl, grabFoodUrl, shopeeFoodUrl, uri.toString(), price, calorie, type);
                                        list.set(index.intValue(), menuNew);
                                        adapter.notifyDataSetChanged();
                                        deselectArticle();
                                        progressDialog.dismiss();
                                        rdgType.clearCheck();
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
                    Toast.makeText(CrudMenuActivity.this, "Gagal mengunggah gambar!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(!title.isEmpty() && !description.isEmpty() && !goFoodUrl.isEmpty() && !grabFoodUrl.isEmpty() && !shopeeFoodUrl.isEmpty() && price != null && calorie != null && type != null && thumbUrl == null) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("title", title);
            map.put("description", description);
            map.put("goFoodUrl", goFoodUrl);
            map.put("grabFoodUrl", grabFoodUrl);
            map.put("shopeeFoodUrl", shopeeFoodUrl);
            map.put("price", price);
            map.put("calorie", calorie);
            map.put("type", type);
            firebaseFirestore.collection("Menus").document(menu.getId()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Menu menuNew = new Menu(menu.getId(), title, description, goFoodUrl, grabFoodUrl, shopeeFoodUrl, menu.getThumbUrl(), price, calorie, type);
                        list.set(index.intValue(), menuNew);
                        adapter.notifyDataSetChanged();
                        deselectArticle();
                        progressDialog.dismiss();
                        rdgType.clearCheck();
                    }
                }
            });
        }
        else Toast.makeText(this, "Isi semua masukkan terlebih dahulu!", Toast.LENGTH_SHORT).show();
    }

    private void deleteMenu(Integer index, Menu menu){
        firebaseFirestore.collection("Menus").document(menu.getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            list.remove(index.intValue());
                            adapter.notifyDataSetChanged();
                            deselectArticle();
                            Toast.makeText(CrudMenuActivity.this, "Menu telah terhapus!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(CrudMenuActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getMenus() {
        firebaseFirestore.collection("Menus")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            if (progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Toast.makeText(CrudMenuActivity.this, "Gagal mendapatkan data menu!", Toast.LENGTH_SHORT).show();
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){
                            Menu menu;
                            menu = new Menu(dc.getDocument().getId(),dc.getDocument().getString("title"), dc.getDocument().getString("description"), dc.getDocument().getString("goFoodUrl"), dc.getDocument().getString("grabFoodUrl"), dc.getDocument().getString("shopeeFoodUrl"), dc.getDocument().getString("thumbUrl"), ((Number) dc.getDocument().getDouble("price")).intValue(), ((Number) dc.getDocument().getDouble("calorie")).intValue(), dc.getDocument().getBoolean("type"));
                            switch (dc.getType()) {
                                case ADDED:
                                    list.add(menu);
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