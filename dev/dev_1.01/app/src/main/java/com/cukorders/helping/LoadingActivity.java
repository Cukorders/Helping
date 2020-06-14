package com.cukorders.helping;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

   // private static Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        //context=this;
        findViewById(R.id.loading_start).setOnClickListener(OnClickListener);

    }

    View.OnClickListener OnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.loading_start:
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