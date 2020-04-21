package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab_plus,fab_write,fab_info;
    Animation FabOpen,FabClose,FabClockwise,FabAntiClockwise;
    boolean isOpen=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            startStartActivity();
        }
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
                if(isOpen){
                    fab_write.startAnimation(FabOpen);
                    fab_info.startAnimation(FabOpen);
                    fab_plus.startAnimation(FabClockwise);
                    fab_info.setClickable(true);
                    fab_write.setClickable(true);
                    isOpen=false;
                    Log.d("open","open");
                }else{
                    fab_write.startAnimation(FabClose);
                    fab_info.startAnimation(FabClose);
                    fab_plus.startAnimation(FabAntiClockwise);
                    fab_info.setClickable(false);
                    fab_write.setClickable(false);
                    isOpen=true;
                    Log.d("close","close");
                }
            }
        });
    }

    private void startStartActivity(){
        Intent intent=new Intent(this,StartActivity.class);
        startActivity(intent);
    }
    
}