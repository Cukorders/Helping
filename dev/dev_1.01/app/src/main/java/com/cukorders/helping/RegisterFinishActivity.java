package com.cukorders.helping;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterFinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finish_newprofile);

        // 회원 가입이 완료됐다는 메시지를 뜨게 한다.
        Toast.makeText(this,"회원 가입이 완료되었습니다.",Toast.LENGTH_LONG).show();
        Handler handler = new Handler();

        // 가입 완료 시 2.5초 동안 가입 완료 화면 표시 후 main 화면으로 자동으로 넘어간다.
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2500);

    }
}
