package com.cukorders.helping;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LogInActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_authentication);

        // 버튼들이 클릭됐을 시
        findViewById(R.id.bt_login).setOnClickListener(OnClickListener);
        findViewById(R.id.get).setOnClickListener(OnClickListener);
        findViewById(R.id.check).setOnClickListener(OnClickListener);
    }


    View.OnClickListener OnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            /* TODO
            각 버튼 클릭 후 실행될 이벤트들 채워넣기
            **/
            switch(v.getId()){
                case R.id.bt_login:

                    break;

                case R.id.get:

                    break;

                case R.id.check:

                    break;
            }
        }
    };
}