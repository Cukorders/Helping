package com.cukorders.helping;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MannerBarActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextView textView;
    ImageView imageView;

    @Override
    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.custom_manner_bar);

        progressBar.findViewById(R.id.pb);
        textView.findViewById(R.id.score);
        imageView.findViewById(R.id.face);

        progressBar.setMax(100);

        progressAnimation();
    }

    public void progressAnimation(){
        MannerBarAnimation anim = new MannerBarAnimation(this, progressBar, textView, imageView, 0, 70);
        progressBar.setAnimation(anim);

    }

}