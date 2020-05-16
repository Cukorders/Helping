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

        // 클릭 가능한 버튼들 설정 => OnClickListener에서 클릭됐을 시 action 정의

        findViewById(R.id.bt_finish).setOnClickListener(OnClickListener);
        findViewById(R.id.check).setOnClickListener(OnClickListener);
        findViewById(R.id.male).setOnClickListener(OnClickListener);
        findViewById(R.id.female).setOnClickListener(OnClickListener);

        // 입력된 정보들을 불러 오는 칸
        nickname=(TextView) findViewById(R.id.nickname);
        name=(TextView) findViewById(R.id.name);
        gender=(TextView) findViewById(R.id.gender);
    }

    View.OnClickListener OnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_finish:
                    /* TODO
                       finish_register가 실행되기 전 인증 번호 체크하는 함수 필요
                    * */
                    finish_register(); // 회원가입 완료 버튼 누름 => 회원가입 완료 화면으로 전환.
                    break;

                case R.id.check:
                    /*TODO
                    닉네임 중복 확인
                    * */
                    break;

                case R.id.male:
                    gender.setText("남자");
                    break;

                case R.id.female:
                    gender.setText("여자");
                    break;
            }
        }
    };

    private void finish_register(){
        Intent intent=new Intent(this,LogInActivity.class);
        startActivity(intent);
    }
}
