package com.healthymeals.sayfine.activity.authentication;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.activity.MainActivity;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterDetailsActivity extends AppCompatActivity {

    private static final int EXTERNAL_STORAGE_PERMISSION_CODE = 23;

    private Button btnStart;
    private CircleImageView imgProfile;
    private TextInputLayout inputName;
    private ProgressDialog progressDialog;
    private Uri imageUri;
    private String userId;
    private String name;
    private String phoneNumber;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    public StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);

        imgProfile = findViewById(R.id.imgProfile);
        inputName = findViewById(R.id.inputName);
        btnStart = findViewById(R.id.btnStart);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userId = mAuth.getUid();

        phoneNumber = getIntent().getStringExtra("phoneNumber");
        imageUri = null;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mendaftarkan akun Anda...");

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);

        imgProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImagePicker.with(RegisterDetailsActivity.this)
                        .cropSquare()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });

        btnStart.setOnClickListener(view -> {
            name = inputName.getEditText().getText().toString();
            if (!TextUtils.isEmpty(name)){
                if(imageUri!=null){
                    registerWithProfileImage(imageUri);
                }
                else{
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("name", name);
                    userMap.put("phoneNumber", phoneNumber);
                    userMap.put("profileUrl", null);
                    userMap.put("mainAddressId", null);

                    firebaseFirestore.collection("Users").document(userId).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Pendaftaran sukses!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterDetailsActivity.this , MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "000000000000000000", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Masukkan nama Anda terlebih dahulu!", Toast.LENGTH_LONG).show();
            }


        });
    }

    private void registerWithProfileImage(Uri uri){
        storageReference = FirebaseStorage.getInstance().getReference().child("profiles/" + userId + ".jpg");
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("name", name);
                        userMap.put("phoneNumber", phoneNumber);
                        userMap.put("profileUrl", uri.toString());
                        userMap.put("lastOrder", null);

                        firebaseFirestore.collection("Users").document(userId).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Pendaftaran sukses!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegisterDetailsActivity.this , MainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "000000000000000000", Toast.LENGTH_LONG).show();
                            }
                        });
                        progressDialog.dismiss();
                        Toast.makeText(RegisterDetailsActivity.this, "Pendaftaran sukses!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(RegisterDetailsActivity.this, "Mengunggah foto profil gagal!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
             imageUri = data.getData();
             imgProfile.setImageURI(imageUri);
        }
        else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        }
        else {

        }

    }

}