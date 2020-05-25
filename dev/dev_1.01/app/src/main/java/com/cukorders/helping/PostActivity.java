package com.cukorders.helping;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PostActivity extends AppCompatActivity {
    private Context context=this;
    private boolean sameGender;
    private boolean ageChecked[]=new boolean[5];
    private Button bt_same,bt_dontMind,age10,age20,age30,age40,age50;

    @SuppressLint("LongLogTag")
    private static void init_ageChecked(boolean ageChecked[]){
        Log.d("init the array ageChecked","init the array ageChecked");
        for(int i=0;i<5;++i)
            ageChecked[i]=false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_post);

        findViewById(R.id.back_button_write_post).setOnClickListener(onClickListener);
        findViewById(R.id.bt_finish).setOnClickListener(onClickListener);
        findViewById(R.id.bt_post).setOnClickListener(onClickListener);

        findViewById(R.id.bt_same).setOnClickListener(checkGender);
        findViewById(R.id.bt_dontMind).setOnClickListener(checkGender);

        findViewById(R.id.button10s).setOnClickListener(checkAge);
        findViewById(R.id.button20s).setOnClickListener(checkAge);
        findViewById(R.id.button30s).setOnClickListener(checkAge);
        findViewById(R.id.button40s).setOnClickListener(checkAge);
        findViewById(R.id.button50s).setOnClickListener(checkAge);

        bt_same=(Button) findViewById(R.id.bt_same);
        bt_dontMind=(Button) findViewById(R.id.bt_dontMind);
        age10=(Button) findViewById(R.id.button10s);
        age20=(Button) findViewById(R.id.button20s);
        age30=(Button) findViewById(R.id.button30s);
        age40=(Button) findViewById(R.id.button40s);
        age50=(Button) findViewById(R.id.button50s);

        sameGender=false;
        init_ageChecked(ageChecked);
    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.back_button_write_post:
                    startActivity(new Intent(context,MainActivity.class));
                    break;

                case R.id.bt_finish:

                    break;
            }
        }
    };

    View.OnClickListener checkGender=new View.OnClickListener() {
        @SuppressLint("LongLogTag")
        @Override
        public void onClick(View v) {
            switch(v.getId()){
            case R.id.bt_same:
                Log.d("a same gender button is clicked","a same gender button is clicked");
                sameGender=true;
                Log.e("a value of sameGender","a value of sameGender is "+sameGender);
                bt_same.setBackgroundColor(Color.parseColor("#70D398"));
                bt_dontMind.setBackgroundColor(Color.parseColor("#e1e1e1"));
                break;

                case R.id.bt_dontMind:
                    Log.d("a don't mind button is clicked","a don't mind button is clicked");
                    sameGender=false;
                    Log.e("a value of sameGender","a value of sameGender is "+sameGender);
                    bt_dontMind.setBackgroundColor(Color.parseColor("#70D398"));
                    bt_same.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    break;
        }
        }
    };

    View.OnClickListener checkAge=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button10s:
                    Log.d("a button 10s is clicked","a button 10s is clicked");
                    init_ageChecked(ageChecked);
                    ageChecked[0]=true;
                    Log.e("an age value","an age value is ageChecked[0]. and its value is "+ageChecked[0]);
                    age10.setBackgroundColor(Color.parseColor("#70D398"));
                    age20.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    age30.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    age40.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    age50.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    break;

                case R.id.button20s:
                    Log.d("a button 20s is clicked","a button 20s is clicked");
                    init_ageChecked(ageChecked);
                    ageChecked[1]=true;
                    Log.e("an age value","an age value is ageChecked[1]. and its value is "+ageChecked[1]);
                    age20.setBackgroundColor(Color.parseColor("#70D398"));
                    age10.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    age30.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    age40.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    age50.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    break;

                case R.id.button30s:
                    Log.d("a button 30s is clicked","a button 30s is clicked");
                    init_ageChecked(ageChecked);
                    ageChecked[2]=true;
                    Log.e("an age value","an age value is ageChecked[2]. and its value is "+ageChecked[2]);
                    age30.setBackgroundColor(Color.parseColor("#70D398"));
                    age20.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    age10.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    age40.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    age50.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    break;

                case R.id.button40s:
                    Log.d("a button 40s is clicked","a button 40s is clicked");
                    init_ageChecked(ageChecked);
                    ageChecked[3]=true;
                    Log.e("an age value","an age value is ageChecked[3]. and its value is "+ageChecked[3]);
                    age40.setBackgroundColor(Color.parseColor("#70D398"));
                    age20.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    age30.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    age10.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    age50.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    break;

                default:
                    Log.d("a button 50s is clicked","a button 50s is clicked");
                    init_ageChecked(ageChecked);
                    ageChecked[4]=true;
                    Log.e("an age value","an age value is ageChecked[4]. and its value is "+ageChecked[4]);
                    age50.setBackgroundColor(Color.parseColor("#70D398"));
                    age20.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    age30.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    age40.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    age10.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    break;
            }
        }
    };
}
