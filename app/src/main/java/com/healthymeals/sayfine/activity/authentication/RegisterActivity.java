package com.healthymeals.sayfine.activity.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.healthymeals.sayfine.R;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private CountryCodePicker inputCountryCode;
    private EditText inputPhoneNumber;
    private Button btnGoToLogin;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        btnRegister = findViewById(R.id.btnRegister);
        btnGoToLogin = findViewById(R.id.btnGoToLogin);
        inputCountryCode = findViewById(R.id.inputCountryCode);
        inputPhoneNumber = findViewById(R.id.inputPhoneNumber);

        btnRegister.setOnClickListener(this);
        btnGoToLogin.setOnClickListener(this);

        inputCountryCode.registerCarrierNumberEditText(inputPhoneNumber);

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(getApplicationContext(), "Kode OTP telah terkirim!", Toast.LENGTH_SHORT).show();

                Intent otpIntent = new Intent(RegisterActivity.this , OtpActivity.class);
                otpIntent.putExtra("auth" , s);
                otpIntent.putExtra("type" , "0");
                otpIntent.putExtra("phoneNumber" , inputCountryCode.getFullNumberWithPlus());
                startActivity(otpIntent);


            }
        };

    }

    private void signIn(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Task succesfull", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGoToLogin:
                Intent intent1 = new Intent(this, LoginActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.btnRegister:
                String phoneNumber = inputCountryCode.getFullNumberWithPlus().toString();
                if (!phoneNumber.isEmpty()){
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(phoneNumber)
                            .setTimeout(60L , TimeUnit.SECONDS)
                            .setActivity(RegisterActivity.this)
                            .setCallbacks(mCallBacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }else{
                    Toast.makeText(getApplicationContext(), "Masukkan nomor telepon terlebih dahulu!", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
}