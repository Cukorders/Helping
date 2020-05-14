package com.cukorders.helping;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        Handler handler = new Handler();

        // 로딩 화면 2.5초 동안 보여준 후 유저 정보를 받아왔을 시 main으로, 못 받아왔을 시 로그인 화면으로 이동하게 함
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* TODO
                유저 정보를 받아왔을 시 => Main으로 이동(아래 코드)
                유저 정보를 받아오지 못했을 시 => 로그인 및 회원가입 화면으로 전환
                **/

                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2500);
    }
}
