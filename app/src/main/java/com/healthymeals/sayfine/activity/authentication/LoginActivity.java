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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hbb20.CountryCodePicker;
import com.healthymeals.sayfine.R;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private Button btnGoToRegister;
    private CountryCodePicker inputCountryCode;
    private EditText inputPhoneNumber;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        btnLogin = findViewById(R.id.btnLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);
        inputCountryCode = findViewById(R.id.inputCountryCode);
        inputPhoneNumber = findViewById(R.id.inputPhoneNumber);
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

                Intent otpIntent = new Intent(LoginActivity.this , OtpActivity.class);
                otpIntent.putExtra("auth" , s);
                otpIntent.putExtra("type" , "1");
                otpIntent.putExtra("phoneNumber" , inputCountryCode.getFullNumberWithPlus());
                startActivity(otpIntent);
            }
        };

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = inputCountryCode.getFullNumberWithPlus().toString().trim();
                if (!phoneNumber.isEmpty()){
                    CollectionReference cref= firebaseFirestore.collection("Users");
                    Query q1 = cref.whereEqualTo("phoneNumber", phoneNumber);
                    q1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            boolean isExisting = false;
                            for (DocumentSnapshot ds : queryDocumentSnapshots) {
                                String temp;
                                temp = ds.getString("phoneNumber");
                                if (phoneNumber.equals(temp.trim())) {
                                    isExisting = true;
                                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                                            .setPhoneNumber(phoneNumber)
                                            .setTimeout(60L , TimeUnit.SECONDS)
                                            .setActivity(LoginActivity.this)
                                            .setCallbacks(mCallBacks)
                                            .build();
                                    PhoneAuthProvider.verifyPhoneNumber(options);
                                }
                            }
                            if (!isExisting) {
                                Toast.makeText(getApplicationContext(), "Nomor Anda belum terdafarkan!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(getApplicationContext(), "Masukkan nomor telepon terlebih dahulu!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent1);
            }
        });
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
}