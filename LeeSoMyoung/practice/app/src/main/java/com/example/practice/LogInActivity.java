package com.example.practice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    private static final String TAG="LogInActivity";
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth=FirebaseAuth.getInstance();

        findViewById(R.id.bt_okay).setOnClickListener(OnClickListener);
    }
    
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
       // updateUI(currentUser);
    }


    View.OnClickListener OnClickListener=new View.OnClickListener(){
        public void onClick(View v){
            switch(v.getId()){
                case R.id.bt_okay:
                    signIn();
                    break;
            }
        }
    };

    private void signIn(){
        final String email=((EditText) findViewById(R.id.mail)).getText().toString();
        String password=((EditText) findViewById(R.id.pw)).getText().toString();
        if(email.length()>0&&password.length()>0){
        mAuth.signInWithEmailAndPassword(email,password)
    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                Log.d(TAG,"signInWithEmail:success");
                FirebaseUser user=mAuth.getCurrentUser();
                //startToast("로그인 되었습니다.");
               // updateUI(user);
                startMain();
            }
            else{
                if(task.getException()!=null){
                    Log.w(TAG,"signInWithEmail:failure",task.getException());
                   startToast(task.getException().toString());
                }
            }
        }
    });
    }else{
            startToast("이메일 또는 비밀번호를 입력하시오.");
        }
    }
    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    private void startMain(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
