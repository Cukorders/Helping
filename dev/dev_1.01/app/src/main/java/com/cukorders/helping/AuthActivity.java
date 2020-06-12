package com.cukorders.helping;

import android.annotation.SuppressLint;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static android.view.View.VISIBLE;

public class AuthActivity extends AppCompatActivity {

    private String location;
    private boolean locationCertification;
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

    private final String DEFAULT="default";

    //Database
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserDatabase;
    private DatabaseReference mUserUids;
    private DatabaseReference locationDB;

    private final String TAG="AuthActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

       location=((ChooseTheRegionActivity)ChooseTheRegionActivity.regional_certification1).user_location;

        mAuth.setLanguageCode("kr");
        mMessageSentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PhoneNumber = mPhoneText.getText().toString();
                if (PhoneNumber.isEmpty()) {
                    mErrorText.setText("전화번호를 입력해주세요");
                    mErrorText.setVisibility(VISIBLE);
                } else {
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
                if (CodeNumber.isEmpty()) {
                    mErrorText.setText("인증번호를 입력해주세요");
                    mErrorText.setVisibility(VISIBLE);
                } else {
                    mCodeBtn.setEnabled(false);
                    mLoginBtn.setEnabled(false);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, CodeNumber);
                    signInWithPhoneAuthCredential(credential);
                }
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
                //to make first try onVerificationCompleted
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                            }
                        },
                        100000);
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
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                            locationDB=FirebaseDatabase.getInstance().getReference().child("userRegions").child(uid);
                            Log.e("Credential은 생성되었습니다.","Credential이 생성되었습니다");
                            boolean newuser = task.getResult().getAdditionalUserInfo().isNewUser();
                            if (newuser) {
                                //신규 사용자라면
                                HashMap<String, String> userMap = new HashMap<>(); //User table
                                userMap.put("Nickname", "default");
                                userMap.put("Image", "gs://helping-2d860.appspot.com/profile_images/profile image.png");
                                userMap.put("Thumb_img", "default");
                                userMap.put("Gender", "default");
                                userMap.put("Age", "default");
                                userMap.put("Score", "default");
                                userMap.put("Money", "default");

                                HashMap<String,String> locationTable=new HashMap<>(); //location table
                                ((LoadingActivity)LoadingActivity.loadingActivity).loc.add(location); // add a user location to a location arraylist
                                Log.d(TAG,"arrayList에 "+((LoadingActivity)LoadingActivity.loadingActivity).loc.get(0)+"가 추가되었습니다.");
                                locationTable.put("Region1",location);
                                locationTable.put("Region2",DEFAULT);
                                locationTable.put("Region3",DEFAULT);
                                locationTable.put("Region1 state",Boolean.toString(locationCertification));
                                locationTable.put("Region2 state",DEFAULT);
                                locationTable.put("Region3 state",DEFAULT);
                                locationDB.setValue(locationTable).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @SuppressLint("LongLogTag")
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Log.e("the location table is updated.","the location table is successfully updated");
                                        }
                                    }
                                });
                                mUserDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Intent LoginIntent = new Intent(AuthActivity.this, LogInActivity.class);
                                            LoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(LoginIntent);
                                            finish();
                                        }
                                    }
                                });

                            } else {
                                Intent MainIntent = new Intent(AuthActivity.this, MainActivity.class);
                                MainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(MainIntent);
                                finish();
                            }

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
            sendUserToMain();
        }
    }

    private void sendUserToMain(){
        Intent profileIntent = new Intent(AuthActivity.this,MainActivity.class);
        profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(profileIntent);
        finish();
    }
}