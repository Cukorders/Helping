package com.cukorders.helping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    public static Context loadingActivity;
    public static String userLoc[]=new String[3];
    public static boolean isCertified[]=new boolean[3];
    public static int loc_cnt=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        loadingActivity=this;
        findViewById(R.id.StartButton).setOnClickListener(OnClickListener);
        for(int i=0;i<3;++i) {
            userLoc[i] = "default";
            isCertified[i]=false;
        }
    }

    View.OnClickListener OnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.StartButton:
                    goNextPage();
                    break;
            }
        }
    };

    private void goNextPage(){
        Intent intent=new Intent(this, ChooseTheRegionActivity.class);
        startActivity(intent);
    }
}