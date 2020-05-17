package com.cukorders.helping;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static android.view.View.VISIBLE;

public class AuthActivity extends AppCompatActivity {
    private EditText mPhoneText;
    private EditText mCodeText;

    private Button mMessageSentBtn;
    private Button mCodeBtn;

    private Button mLoginBtn;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private TextView mErrorText;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_authentication);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mPhoneText = findViewById(R.id.phoneEditText);
        mCodeText = findViewById(R.id.codeEditText);
        mMessageSentBtn = findViewById(R.id.sendBtn);
        mCodeBtn = findViewById(R.id.codeBtn);
        mLoginBtn = findViewById(R.id.LoginBtn);
        mErrorText = findViewById(R.id.login_form_feedback);

        mAuth.setLanguageCode("kr");
        mMessageSentBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String PhoneNumber = mPhoneText.getText().toString();
                if(PhoneNumber.isEmpty()) {
                    mErrorText.setText("전화번호를 입력해주세요");
                    mErrorText.setVisibility(VISIBLE);
                }else {
                    mMessageSentBtn.setEnabled(false);
                    mCodeText.setVisibility(VISIBLE);
                    mCodeBtn.setVisibility(VISIBLE);
                    mLoginBtn.setVisibility(VISIBLE);
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            PhoneNumber,       //User Phone Number
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            AuthActivity.this,
                            mCallbacks);        // OnVerificationStateChangedCallbacks
                }
            }
        });

        mCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CodeNumber = mCodeText.getText().toString();
                if(CodeNumber.isEmpty()){
                    mErrorText.setText("인증번호를 입력해주세요");
                    mErrorText.setVisibility(VISIBLE);
                }else {
                    mCodeBtn.setEnabled(false);
                    mLoginBtn.setEnabled(false);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,CodeNumber);
                    signInWithPhoneAuthCredential(credential);
                }
                mCodeBtn.setEnabled(true);
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                //automatically verified
                //instantly verified
                mCodeBtn.setEnabled(false);
                mLoginBtn.setEnabled(false);
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                mErrorText.setText("인증번호가 틀립니다");
                mErrorText.setVisibility(VISIBLE);
                mMessageSentBtn.setEnabled(true);
            }
            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, token);
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
    }


    //check if user is signed in
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if logged in
                            FirebaseUser user = task.getResult().getUser();
                            Intent LoginIntent = new Intent(AuthActivity.this,LogInActivity.class);
                            startActivity(LoginIntent);
                            finish();
                            // ...
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                mErrorText.setText("로그인에 오류가 있습니다. 재실행해주세요");
                                mErrorText.setVisibility(VISIBLE);
                            }
                        }

                    }
                });
    }

    protected void onStart(){
        super.onStart();
        if(mCurrentUser!=null){
            Intent LoginIntent = new Intent(AuthActivity.this,MainActivity.class);
            LoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            LoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(LoginIntent);
        }
    }
}