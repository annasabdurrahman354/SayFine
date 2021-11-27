package com.healthymeals.sayfine.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.anujkumarsharma.tooltipprogressbar.TooltipProgressBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.healthymeals.sayfine.R;

import java.sql.Time;
import java.util.HashMap;

public class BmiActivity extends AppCompatActivity {

    private EditText inputBmiWeight;
    private EditText inputBmiHeight;
    private Button btnCalculateBmi;
    private ImageButton btnHistory;
    private TooltipProgressBar pgbBmi;

    private Float height;
    private Float weight;
    private Float bmi;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        btnCalculateBmi = findViewById(R.id.btnCalculateBmi);
        btnHistory = findViewById(R.id.btnHistory);
        inputBmiHeight = findViewById(R.id.inputBmiHeight);
        inputBmiWeight = findViewById(R.id.inputBmiWeight);
        pgbBmi = findViewById(R.id.pgbBmi);

        btnCalculateBmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                height = Float.parseFloat(inputBmiHeight.getText().toString());
                weight = Float.parseFloat(inputBmiWeight.getText().toString());
                bmi = (weight/((height/100f)*(height/100f)));
                pgbBmi.setTopToolTipText("IMT Anda " + String.format("%.02f", bmi.floatValue()));
                pgbBmi.setBottomTooltipSteps((int) Math.round(bmi) - 10);
                pgbBmi.setProgressFillSteps((int) Math.round(bmi) - 10);

                if(bmi < 18.5f){
                    pgbBmi.setBottomToolTipText("Kurang Berat Badan");

                }
                else if(bmi >= 18.5f && bmi <= 22.9f){
                    pgbBmi.setBottomToolTipText("Normal");
                }
                else if(bmi >= 23f && bmi <= 24.9f){
                    pgbBmi.setBottomToolTipText("Kelebihan Berat Badan");
                }
                else if(bmi >= 25f && bmi <= 29.9f){
                    pgbBmi.setBottomToolTipText("Obesitas Tingkat I");
                }
                else if(bmi >= 30f){
                    pgbBmi.setBottomToolTipText("Obesitas Tingkat II");
                }

                HashMap<String, Object> map = new HashMap<>();
                map.put("height", height);
                map.put("weight", weight);
                map.put("timestamp", Timestamp.now());

                firebaseFirestore.collection("Users").document(mAuth.getUid()).collection("BodyMassIndexs").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                        }
                    }
                });
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BmiActivity.this, BmiHistoryActivity.class);
                startActivity(intent);
            }
        });

    }
}