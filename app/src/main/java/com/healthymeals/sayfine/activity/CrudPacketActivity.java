package com.healthymeals.sayfine.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.adapter.crud.CrudPacketAdapter;
import com.healthymeals.sayfine.adapter.crud.MenuSelectListAdapter;
import com.healthymeals.sayfine.model.Menu;
import com.healthymeals.sayfine.model.Packet;

import java.util.ArrayList;
import java.util.HashMap;

public class CrudPacketActivity extends AppCompatActivity {
    private AlertDialog.Builder dialog;
    private LayoutInflater inflater;
    private View dialogView;
    private RecyclerView recyclerViewSelectMenu;

    private TextInputLayout inputTitle;
    private TextInputLayout inputDescription;
    private TextInputLayout inputMenuList;
    private TextInputEditText inputMenuListE;
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
    private CrudPacketAdapter adapter;
    private MenuSelectListAdapter adapterSelectMenu;

    private ArrayList<Packet> list = new ArrayList<>();
    private ArrayList<Menu> menuList = new ArrayList<>();
    private ArrayList<String> menuListChecked = new ArrayList<>();

    public Packet selectedPacket;
    public Integer selectedPacketIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_packet);
        inputTitle = findViewById(R.id.inputTitle);
        inputDescription = findViewById(R.id.inputDescription);
        inputMenuList = findViewById(R.id.inputMenuList);
        inputMenuListE = findViewById(R.id.test);
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
        progressDialog.setMessage("Mengunggah data packet...");

        firebaseFirestore = FirebaseFirestore.getInstance();
        getMenus();
        getPackets();
        adapter = new CrudPacketAdapter(this, this , list);
        recyclerView.setAdapter(adapter);

        imgThumb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImagePicker.with(CrudPacketActivity.this)
                        .crop(4f, 2f)
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });

        inputMenuListE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSelectMenu();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPacket(
                        inputTitle.getEditText().getText().toString(),
                        inputDescription.getEditText().getText().toString(),
                        thumbUrl,
                        menuListChecked
                );
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePacket(
                        selectedPacketIndex,
                        selectedPacket,
                        inputTitle.getEditText().getText().toString(),
                        inputDescription.getEditText().getText().toString(),
                        thumbUrl,
                        menuListChecked
                );
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePacket(
                        selectedPacketIndex,
                        selectedPacket
                );
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectPacket();
            }
        });
    }

    public void selectPacket(Integer index, Packet packet){
        selectedPacketIndex = index;
        selectedPacket = packet;
        menuListChecked = packet.getMenuIdList();
        String text = "";
        for(int i = 0; i < menuList.size(); i++){
            if(menuListChecked.contains(menuList.get(i).getId()))
                text = text + " " + menuList.get(i).getTitle() + ",";
        }
        inputMenuList.getEditText().setText(text);
        inputTitle.getEditText().setText(packet.getTitle());
        inputDescription.getEditText().setText(packet.getDescription());
        Glide.with(this).load(packet.getThumbUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgThumb);
        lnrSelected.setVisibility(View.VISIBLE);
        btnCreate.setVisibility(View.GONE);
    }

    private void deselectPacket(){
        clearInput();
        lnrSelected.setVisibility(View.GONE);
        btnCreate.setVisibility(View.VISIBLE);
    }

    private void clearInput(){
        selectedPacketIndex = null;
        selectedPacket = null;
        inputTitle.getEditText().setText(null);
        inputDescription.getEditText().setText(null);
        inputMenuList.getEditText().setText(null);
        menuListChecked.clear();
        imgThumb.setImageResource(R.drawable.pic_thumbnail);
        thumbUrl = null;
    }

    private void addPacket(String title, String description, Uri thumbUrl, ArrayList<String> menuListId){
        if (!title.isEmpty() && !description.isEmpty() && thumbUrl != null){
            String packetId = firebaseFirestore.collection("Packets").document().getId();
            storageReference = FirebaseStorage.getInstance().getReference().child("packets/" + packetId + ".jpg");
            storageReference.putFile(thumbUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("title", title);
                            map.put("description", description);
                            map.put("thumbUrl", uri.toString());
                            map.put("menuIdList", menuListId);
                            firebaseFirestore.collection("Packets").document(packetId).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                    Toast.makeText(CrudPacketActivity.this, "Gagal mengunggah gambar!", Toast.LENGTH_SHORT).show();
                }
            });
        } else Toast.makeText(this, "Isi semua masukkan terlebih dahulu!", Toast.LENGTH_SHORT).show();
    }

    private void updatePacket(Integer index, Packet packet, String title, String description, Uri thumbUrl, ArrayList<String> menuIdList){
        if (!title.isEmpty() && !description.isEmpty() && thumbUrl != null){
            storageReference = FirebaseStorage.getInstance().getReference().child("packets/" + packet.getId() + ".jpg");
            storageReference.putFile(thumbUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("title", title);
                            map.put("description", description);
                            map.put("thumbUrl", uri.toString());
                            map.put("menuIdList", menuIdList);
                            firebaseFirestore.collection("Packets").document(packet.getId()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Packet packetNew = new Packet(packet.getId(), title, description, uri.toString(), menuIdList);
                                        list.set(index.intValue(), packetNew);
                                        adapter.notifyDataSetChanged();
                                        deselectPacket();
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
                    Toast.makeText(CrudPacketActivity.this, "Gagal mengunggah gambar!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(!title.isEmpty() && !description.isEmpty() && thumbUrl == null) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("title", title);
            map.put("description", description);
            map.put("menuIdList", menuIdList);
            firebaseFirestore.collection("Packets").document(packet.getId()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Packet packetNew = new Packet(packet.getId(), title, description, packet.getThumbUrl(), menuIdList);
                        list.set(index.intValue(), packetNew);
                        adapter.notifyDataSetChanged();
                        deselectPacket();
                        progressDialog.dismiss();
                    }
                }
            });
        }
        else Toast.makeText(this, "Isi semua masukkan terlebih dahulu!", Toast.LENGTH_SHORT).show();
    }

    private void deletePacket(Integer index, Packet packet){
        firebaseFirestore.collection("Packets").document(packet.getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            list.remove(index.intValue());
                            adapter.notifyDataSetChanged();
                            deselectPacket();
                            Toast.makeText(CrudPacketActivity.this, "Paket telah terhapus!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(CrudPacketActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getPackets() {
        firebaseFirestore.collection("Packets")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            if (progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Toast.makeText(CrudPacketActivity.this, "Gagal mendapatkan data paket!", Toast.LENGTH_SHORT).show();
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){
                            Packet packet;
                            packet = new Packet(dc.getDocument().getId(),dc.getDocument().getString("title"), dc.getDocument().getString("description"), dc.getDocument().getString("thumbUrl"), (ArrayList<String>) dc.getDocument().get("menuIdList"));
                            switch (dc.getType()) {
                                case ADDED:
                                    list.add(packet);
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

    private void getMenus() {
        firebaseFirestore.collection("Menus").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Firestore error", document.getId() + " => " + document.getData());
                                Menu menu = new Menu(document.getId(), document.getString("title"), document.getString("description"), null, null, null, document.getString("thumbUrl"),((Number) document.getDouble("price")).intValue(), ((Number) document.getDouble("calorie")).intValue(),document.getBoolean("type"));
                                menuList.add(menu);
                            }
                        } else {
                            Toast.makeText(CrudPacketActivity.this, "Gagal mendapatkan data menu!", Toast.LENGTH_SHORT).show();
                            Log.e("Firestore error", task.getException().toString());
                            return;
                        }
                    }
                });
    }

    private void dialogSelectMenu() {
        dialog = new AlertDialog.Builder(CrudPacketActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_select_menu, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setTitle("Pilih Menu");

        recyclerViewSelectMenu = dialogView.findViewById(R.id.recyclerViewSelectMenu);
        recyclerViewSelectMenu.setHasFixedSize(true);
        recyclerViewSelectMenu.setLayoutManager(new LinearLayoutManager(this));
        adapterSelectMenu = new MenuSelectListAdapter(CrudPacketActivity.this, this , menuList, menuListChecked);
        recyclerViewSelectMenu.setAdapter(adapterSelectMenu);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = "";
                for(int i = 0; i < menuList.size(); i++){
                    if(menuListChecked.contains(menuList.get(i).getId()))
                    text = text + " " + menuList.get(i).getTitle() + ",";
                }
                inputMenuList.getEditText().setText(text);
                dialog.dismiss();
            }

        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
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