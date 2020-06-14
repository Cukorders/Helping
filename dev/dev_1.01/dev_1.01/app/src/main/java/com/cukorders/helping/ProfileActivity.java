package com.cukorders.helping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private static Context context;
    private TextView age,phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_profile);

        context=this;
        findViewById(R.id.profileRegisterBt).setOnClickListener(onClickListener);
        findViewById(R.id.bt_back).setOnClickListener(onClickListener);
        findViewById(R.id.cameraBtn).setOnClickListener(onClickListener);

        age=(TextView) findViewById(R.id.age);
        phoneNumber=(TextView) findViewById(R.id.phone_number);
    }

    View.OnClickListener onClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_back:
                    startActivity(new Intent(context,MyPageActivity.class));
                    break;

                case R.id.profileRegisterBt:
                    // TODO DB에 수정 내용 업데이트
                    Toast.makeText(context,"프로필 수정이 완료되었습니다.",Toast.LENGTH_LONG).show();
                    break;

                case R.id.cameraBtn:

                    break;
            }
        }
    };
}
