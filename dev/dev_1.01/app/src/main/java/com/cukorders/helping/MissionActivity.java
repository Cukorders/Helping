package com.cukorders.helping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MissionActivity extends AppCompatActivity {
    private final Context context=this;
    //관심목록

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_interesting_mission);
        findViewById(R.id.bt_back).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_back:
                    Intent intent=new Intent(context,MainActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
}
