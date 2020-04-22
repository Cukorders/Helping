package com.example.practice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        findViewById(R.id.bt_okay).setOnClickListener(OnClickListener);
        findViewById(R.id.bt_signin).setOnClickListener(OnClickListener);
    }

    View.OnClickListener OnClickListener=new View.OnClickListener(){
        public void onClick(View v){
            switch(v.getId()){ // 이렇게 작성하는 이유 => 확장성 때문
                case R.id.bt_okay:
                    startSignUpActivity();
                    break;
                case R.id.bt_signin:
                    startLogInActivity();
                    break;
            }

        }
    };

    private void startLogInActivity(){
        Intent intent=new Intent(this,LogInActivity.class);
        startActivity(intent);
    }
    private void startSignUpActivity(){
        Intent intent=new Intent(this,SignUpActivity.class);
        startActivity(intent);
    }
}
