package com.cukorders.helping;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    // 사용자가 직접 입력하는 부분
    public TextView userNickname;
    private TextView gender,name;
    private Button male,female;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_details);

        userNickname=(TextView) findViewById(R.id.userNickname);



        male=(Button) findViewById(R.id.maleBtn);
        female=(Button) findViewById(R.id.femaleBtn);
}
}
