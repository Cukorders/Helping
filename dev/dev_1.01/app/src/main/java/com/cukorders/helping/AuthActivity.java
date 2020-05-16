package com.cukorders.helping;

import android.app.MediaRouteButton;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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

    private Button mLoginBtn;
    //에러 메세지 내보내는 것
    private TextView mLoginFeedbackText;

    //callbacks
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private FirebaseAuth mAuth;

    protected void onCrete(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_authentication);

       findViewById(R.id.get).setOnClickListener(OnClickListener);
       findViewById(R.id.check).setOnClickListener(OnClickListener);
       findViewById(R.id.bt_login).setOnClickListener(OnClickListener);

       /* mMessageSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_number = mPhoneText.getText().toString();
                    //check error
                    if(phone_number.isEmpty()){
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

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            //automatically phoneNumber is verified
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
                mCodeLayout.setVisibility(View.VISIBLE);
                mCodeLayout.setEnabled(false);
                mLoginBtn.setVisibility(View.VISIBLE);
            }
            @Override
            //verification is failed
            public void onVerificationFailed(FirebaseException e) {
                mLoginFeedbackText.setText("인증번호가 틀립니다.");
                mLoginFeedbackText.setVisibility(View.VISIBLE);
            }

            public void OnCodeSent(String verificationId,PhoneAuthProvider.ForceResendingToken token) {
                //Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                mCodeLayout.setVisibility(View.VISIBLE);
                mLoginBtn.setVisibility(View.VISIBLE);
                //mMessageSendBtn.setEnabled(true);
            }
    };
*/
    }

    View.OnClickListener OnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.get:

                    break;

                case R.id.check:

                    break;

                case R.id.bt_login:

                    break;
            }
        }
    };

    //check if user is already logged in
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //if logged in
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();

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
                            // 에러가 나고 난 뒤에 다시 재전송 할수 잇도록 버튼이 업데이트 되는가?
                            mLoginFeedbackText.setText("로그인에 실패하셨습니다.\n 다시 시도 해주세요");
                            mLoginFeedbackText.setVisibility(View.VISIBLE);

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

}
