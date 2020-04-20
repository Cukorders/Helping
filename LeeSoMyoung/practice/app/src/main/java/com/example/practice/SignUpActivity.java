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

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG="SignUpActivity";
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth=FirebaseAuth.getInstance();

        findViewById(R.id.bt_signUp).setOnClickListener(OnClickListener);
        findViewById(R.id.bt_loginPage).setOnClickListener(OnClickListener);
    }
    
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
       // updateUI(currentUser);
    }


    View.OnClickListener OnClickListener=new View.OnClickListener(){
        public void onClick(View v){
            switch(v.getId()){ // 이렇게 작성하는 이유 => 확장성 때문
                case R.id.bt_signUp:
                    signUp();
                    break;
                case R.id.bt_loginPage:
                    startLogInActivity();
                    break;
            }

        }
    };

    private void signUp(){
        String email=((EditText) findViewById(R.id.mail)).getText().toString();
        String password=((EditText) findViewById(R.id.pw)).getText().toString();
        String pwCheck=((EditText) findViewById(R.id.pwCheck)).getText().toString();
        if(email.length()>0&&password.length()>0&&pwCheck.length()>0){
        if(password.equals(pwCheck)){
        mAuth.createUserWithEmailAndPassword(email,password)
    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                Log.d(TAG,"createUserWithEmail:success");
                FirebaseUser user=mAuth.getCurrentUser();
                startToast("회원 가입이 되었습니다.");
                startLogInActivity();
               // updateUI(user);
            }
            else{
                if(task.getException()!=null){
                    Log.w(TAG,"createUserWithEmail:failure",task.getException());
                   startToast(task.getException().toString());
                }
            }
        }
    }); }else{
            startToast("비밀번호가 일치하지 않습니다.");
        }
    }else{
            startToast("이메일 또는 비밀번호를 입력하시오.");
        }
    }
    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    private void startLogInActivity(){
        Intent intent=new Intent(this,LogInActivity.class);
        startActivity(intent);
    }
}
