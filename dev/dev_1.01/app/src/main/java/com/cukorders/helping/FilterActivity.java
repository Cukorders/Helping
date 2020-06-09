package com.cukorders.helping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FilterActivity extends AppCompatActivity {


    private static Context context;
    public static Context filterActivity;
    private RadioButton rb_same,rb_dontMatter;
    private RadioButton age[]=new RadioButton[5];
    public static boolean chkAge[]=new boolean[5];
    private static DatabaseReference databaseReference;
    private static boolean isSame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);

        findViewById(R.id.bt_back).setOnClickListener(onClickListener);

        age[0]=(RadioButton) findViewById(R.id.rb_10s);
        age[1]=(RadioButton) findViewById(R.id.rb_20s);
        age[2]=(RadioButton) findViewById(R.id.rb_30s);
        age[3]=(RadioButton) findViewById(R.id.rb_40s);
        age[4]=(RadioButton) findViewById(R.id.rb_50s);
        rb_dontMatter=(RadioButton) findViewById(R.id.rb_different);
        rb_same=(RadioButton) findViewById(R.id.rb_same);

        findViewById(R.id.rb_different).setOnClickListener(checkGender);
        findViewById(R.id.rb_same).setOnClickListener(checkGender);

        findViewById(R.id.rb_10s).setOnClickListener(checkAge);
        findViewById(R.id.rb_20s).setOnClickListener(checkAge);
        findViewById(R.id.rb_30s).setOnClickListener(checkAge);
        findViewById(R.id.rb_40s).setOnClickListener(checkAge);
        findViewById(R.id.rb_50s).setOnClickListener(checkAge);

        context=this;
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Posting");
        filterActivity=this;
    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_back:
                    startActivity(new Intent(context,MainActivity.class));
                    break;

                case R.id.bt_filter:

                    break;
            }
        }
    };

    View.OnClickListener checkAge=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rb_10s:

                    break;

                case R.id.rb_20s:

                    break;

                case R.id.rb_30s:

                    break;

                case R.id.rb_40s:

                    break;

                case R.id.rb_50s:

                    break;
            }
        }
    };

    View.OnClickListener checkGender=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rb_same:

                    break;

                default:

                    break;
            }
        }
    };


}
