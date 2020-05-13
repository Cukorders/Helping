package com.cukorders.helping;

import android.app.MediaRouteButton;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class AuthActivity extends AppCompatActivity {
    private LinearLayout mPhoneLayout;
    private LinearLayout mCodeLayout;

    private EditText mPhoneText;
    private EditText mCodeText;

    //메세지 인증받는 버튼
    private Button mSendBtn;

    //
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    protected void onCrete(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_authentication);

        mPhoneText = (EditText) findViewById(R.id.phoneEditText);
        mCodeText = (EditText) findViewById(R.id.codeEditText);

        mSendBtn = (Button) findViewById(R.id.sendBtn);

        mSendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // prevent user from pushing btn again
                mPhoneText.setEnabled(false);
                mSendBtn.setEnabled(false);

                String phoneNumber = mPhoneText.getText().toString();

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,
                        60,
                        TimeUnit.SECONDS,
                        AuthActivity.this,
                        mCallbacks
                );
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }
        };
    }

}
