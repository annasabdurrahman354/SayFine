package com.healthymeals.sayfine.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.adapter.crud.CrudArticleAdapter;
import com.healthymeals.sayfine.model.Article;

import java.util.ArrayList;

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
    private CrudArticleAdapter adapter;

    private ArrayList<Article> list;

    public Article selectedArticle;
    public Integer selectedArticleIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_menu);
    }
}