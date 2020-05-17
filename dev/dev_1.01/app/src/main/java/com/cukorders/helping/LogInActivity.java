package com.cukorders.helping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private Button mLogoutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finish_newprofile);

        // 버튼들이 클릭됐을 시
        findViewById(R.id.sendBtn).setOnClickListener(OnClickListener);
        findViewById(R.id.LoginBtn).setOnClickListener(OnClickListener);
        findViewById(R.id.check).setOnClickListener(OnClickListener);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mLogoutBtn = findViewById(R.id.logoutBtn);

        mLogoutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mAuth.signOut();
                sendUserToLogin();
            }
        });

    }
    protected void onStart(){
        super.onStart();
        //이미 회원 내역이 있다면 해당 페이지를 건너뛰어야 함 ||로 연결하기
        if(mCurrentUser!=null){
            sendUserToLogin();
        }
    }

    private void sendUserToLogin(){
        Intent profileIntent = new Intent(LogInActivity.this,AuthActivity.class);
        profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(profileIntent);
        finish();
    }

    View.OnClickListener OnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            /* TODO
            각 버튼 클릭 후 실행될 이벤트들 채워넣기.
            **/
            switch(v.getId()){
                case R.id.LoginBtn:

                    break;

                case R.id.sendBtn:

                    break;

                case R.id.check:

                    break;
            }
        }
    };
}