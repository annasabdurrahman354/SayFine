package com.healthymeals.sayfine.activity.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.activity.MainActivity;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class OtpActivity extends AppCompatActivity {

    private String OTP;
    private String type;
    private String phoneNumber;
    private Button btnVerify;
    private OtpTextView inputOtp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        mAuth = FirebaseAuth.getInstance();

        OTP = getIntent().getStringExtra("auth");
        type = getIntent().getStringExtra("auth");
        phoneNumber = getIntent().getStringExtra("phoneNumber");

        btnVerify = findViewById(R.id.btnVerify);
        inputOtp = findViewById(R.id.inputOtp);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verification_code = inputOtp.getOTP();
                if(!verification_code.isEmpty() || verification_code.length() < 6){
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTP , verification_code);
                    signIn(credential);
                }else{
                    Toast.makeText(OtpActivity.this, "Mohon masukkan kode OTP terlebih dahulu!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        inputOtp.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }
            @Override
            public void onOTPComplete(String otp) {

            }
        });
    }

    private void signIn(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    if (type == "1"){
                        Intent loginIntent = new Intent(OtpActivity.this , MainActivity.class);
                        startActivity(loginIntent);
                        finish();
                    }
                    else{
                        Intent detailsIntent = new Intent(OtpActivity.this , RegisterDetailsActivity.class);
                        detailsIntent.putExtra("phoneNumber" , phoneNumber);
                        startActivity(detailsIntent);
                        finish();
                    }
                }else{
                    Toast.makeText(OtpActivity.this, "Verifikasi gagal. Masukkan kode OTP yang benar!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}