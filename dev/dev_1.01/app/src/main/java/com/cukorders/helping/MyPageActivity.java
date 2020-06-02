package com.cukorders.helping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MyPageActivity extends AppCompatActivity {

    private static Context context;
    private TextView user_nickname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        context=this;
        findViewById(R.id.bt_fav).setOnClickListener(onClickListener);
        findViewById(R.id.bt_history).setOnClickListener(onClickListener);
        findViewById(R.id.bt_chat).setOnClickListener(onClickListener);
        findViewById(R.id.bt_notice).setOnClickListener(onClickListener);
        findViewById(R.id.bt_profile).setOnClickListener(onClickListener);
        findViewById(R.id.bt_back).setOnClickListener(onClickListener);

        user_nickname=(TextView) findViewById(R.id.userNickname);
    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.bt_fav:
                    startActivity(new Intent(context,MissionActivity.class));
                    break;

                case R.id.bt_notice:
                    startActivity(new Intent(context,NoticeActivity.class));
                    break;

                case R.id.bt_profile:
                    startActivity(new Intent(context,ProfileActivity.class));
                    break;

                case R.id.bt_back:
                    startActivity(new Intent(context,MainActivity.class));
                    break;
            }
        }
    };
}
