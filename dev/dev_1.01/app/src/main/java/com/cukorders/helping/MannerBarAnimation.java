package com.cukorders.helping;

import android.content.Context;
import android.graphics.Color;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MannerBarAnimation extends Animation {
    private Context context;
    private ProgressBar progressBar;
    private TextView textView;
    private ImageView imageView;
    private float from;
    private float to;

    public MannerBarAnimation(Context context, ProgressBar progressBar, TextView textView, ImageView imageView, float from, float to){
        this.context = context;
        this.progressBar = progressBar;
        this.textView = textView;
        this.imageView = imageView;
        this.from = from;
        this.to = to;
    }

    public void applyTransformation(){
        float value = from + (to - from);
        setContext(value);
    }

    public void setContext(float value){
        progressBar.setProgress((int) value);

        if (value >= 80 && value <= 100){
            textView.setTextColor(Color.parseColor("#00008B"));
            imageView.setImageResource(R.drawable.face_manner_darkblue);
        }
        else if (value >= 60 && value < 80){
            textView.setTextColor(Color.parseColor("#008000"));
            imageView.setImageResource(R.drawable.face_manner_green);
        }
        else if (value >= 30 && value < 60){
            textView.setTextColor(Color.parseColor("#FFA500"));
            imageView.setImageResource(R.drawable.face_manner_orange);
        }
        else {
            textView.setTextColor(Color.parseColor("#FF0000"));
            imageView.setImageResource(R.drawable.face_manner_red);
        }
        textView.setText((int)value+" 점");
    }

}
