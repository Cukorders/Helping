package com.cukorders.helping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    // 사용자가 직접 입력하는 부분
    public TextView nickname;
    private TextView gender,name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_details);


        // 입력된 정보들을 불러 오는 칸

    }

}
