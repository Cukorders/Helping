package com.cukorders.helping;

import android.app.MediaRouteButton;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;

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

public class AuthActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private LinearLayout mCodeLayout;

    private EditText mPhoneText;
    private EditText mCodeText;

    //메세지 인증받는 버튼
    private Button mMessageSendBtn;
    //code 인증 버튼
    private Button mCodeBtn;
    //login 버튼
    private Button mLoginBtn;
    //에러 메세지 내보내는 것
    private TextView mLoginFeedbackText;

    //callbacks
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    protected void onCrete(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_authentication);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mCodeLayout = findViewById(R.id.codelayout);
        mPhoneText = findViewById(R.id.phoneEditText);
        mCodeText = findViewById(R.id.codeEditText);
        mCodeBtn = findViewById(R.id.codeBtn);
        mLoginBtn = findViewById(R.id.LoginBtn);
        mMessageSendBtn = findViewById(R.id.sendBtn);

        mLoginFeedbackText = findViewById(R.id.login_form_feedback);

        mMessageSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_number = mPhoneText.getText().toString();
                //check error
                if (phone_number.isEmpty()) {
                    mLoginFeedbackText.setText("전화번호를 입력해주세요");
                    mLoginFeedbackText.setVisibility(View.VISIBLE);
                } else {
                    mMessageSendBtn.setEnabled(false);
                    String phoneNumber = mPhoneText.getText().toString();
                    //  TODO 어떻게 DATABASE 로 연결하는가
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,
                            60,
                            TimeUnit.SECONDS,
                            AuthActivity.this,
                            mCallbacks
                    );
                }
            }
        });
        mCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = mCodeText.getText().toString();
                if (otp.isEmpty()) {
                    mLoginFeedbackText.setText("인증번호를 입력해주세요");
                    mLoginFeedbackText.setVisibility(View.VISIBLE);
                } else {
                    String verificationCode = mCodeText.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                    signInWithPhoneAuthCredential(credential);
                    mCodeBtn.setEnabled(false);
                }
            }

        });
    }


    //check if user is already logged in
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //if logged in
                            // Sign in success, update UI with the signed-in user's information
                            //TODO 프로필이 저장되어있는 기존 회원인지 확인 하고 저장되어있으면 main으로 아니면 profile 만드는 화면으로
                            mLoginBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent mainIntent = new Intent(AuthActivity.this, AuthActivity.class);
                                    startActivity(mainIntent);
                                    finish();
                                }
                            });
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                mLoginFeedbackText.setText("로그인에 실패하셨습니다.\n 다시 시도 해주세요");
                                mLoginFeedbackText.setVisibility(View.VISIBLE);
                            }
                        }
                        mCodeBtn.setEnabled(false);
                    }
                });
    }

    @Override
    protected void onStart(){
        super.onStart();
            if(mCurrentUser!=null){
                sendUserToHome();
            }
    }

    public void sendUserToHome() {
        Intent homeIntent = new Intent(AuthActivity.this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }
}


