package com.cukorders.helping;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            //Thread.sleep(3000); //대기 초 설정
            startActivity(new Intent(SplashActivity.this, LoadingActivity.class));
            finish();
        } catch (Exception e) {
            CustomLog.e("Error", "SplashActivity ERROR", e);
        }
    }

}
