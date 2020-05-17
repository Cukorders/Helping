package com.cukorders.helping;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoadingActivity extends AppCompatActivity {
   // private FirebaseAuth mAuth;
  //  private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        findViewById(R.id.StartButton).setOnClickListener(OnClickListener);

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
        Intent intent=new Intent(this,AuthActivity.class);
        startActivity(intent);
    }
}