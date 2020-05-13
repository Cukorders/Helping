package com.cukorders.helping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_details);

        // 클릭 가능한 버튼들 설정 => OnClickListener에서 클릭됐을 시 action 정의
        findViewById(R.id.bt_finish).setOnClickListener(OnClickListener);


    }

    View.OnClickListener OnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_finish:
                    finish_register(); // 회원가입 완료 버튼 누름 => 회원가입 완료 화면으로 전환.
                    break;

            }
        }
    };

    private void finish_register(){
        Intent intent=new Intent(this,FinishRegisterActivity.class);
        startActivity(intent);
    }
}
