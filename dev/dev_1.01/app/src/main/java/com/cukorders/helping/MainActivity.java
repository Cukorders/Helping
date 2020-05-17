package com.cukorders.helping;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab_plus,fab_write,fab_info;
    Animation FabOpen,FabClose,FabClockwise,FabAntiClockwise;
    boolean isOpen=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab_plus=(FloatingActionButton) findViewById(R.id.fab_plus);
        fab_write=(FloatingActionButton) findViewById(R.id.fab_write);
        fab_info=(FloatingActionButton) findViewById(R.id.fab_info);
        FabOpen= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabClockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        FabAntiClockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);

        fab_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isOpen){
                    open();
                }else{
                    close();
                }
            }
        });


        findViewById(R.id.myMission).setOnClickListener(onClickListener);
        findViewById(R.id.currentMission).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.myMission:

                    break;

                case R.id.currentMission:

                    break;
            }
        }
    };

    private void open(){
        fab_write.startAnimation(FabOpen);
        fab_info.startAnimation(FabOpen);
        fab_plus.startAnimation(FabClockwise);
        fab_info.setClickable(true);
        fab_write.setClickable(true);
        isOpen=true;
        // Log.d("open","open");
    }
    private void close(){
        fab_write.startAnimation(FabClose);
        fab_info.startAnimation(FabClose);
        fab_plus.startAnimation(FabAntiClockwise);
        fab_info.setClickable(false);
        fab_write.setClickable(false);
        isOpen=false;
        //  Log.d("close","close");
    }

}