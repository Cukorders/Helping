package com.cukorders.helping;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

public class PostImgView extends AppCompatActivity {
    private static final String TAG = "PostingView";
    private String imageurl1;
    private String imageurl2;
    private String imageurl3;
    private ArrayList<String> mImageUrls = new ArrayList<>();

    private TextView imgNum;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displaypostpic);

        ImageSlider imageSlider = findViewById(R.id.postpicviewpager);

        imgNum = findViewById(R.id.img_num);

        Intent newintent = getIntent();
        ArrayList<String> tempUrls =newintent.getStringArrayListExtra("lists");
        //get image and put in Arraylist
        Log.d(TAG, "chk tempUrls.size(): " + tempUrls.size());
        for(int i=0; i<tempUrls.size(); i++){
            if(tempUrls.size()!=0){
                Log.d(TAG, "chk count now "+ i);
                Log.d(TAG, "chk init tempurls now "+ tempUrls.get(i));
                if(!tempUrls.get(i).equals("default")){
                    mImageUrls.add(tempUrls.get(i).toString());
                    Log.d(TAG, "chk init mimageurls now "+ mImageUrls.get(i));
                }
            }else{

            }
        }
        int tmp=0;
        String index="";
        List<SlideModel> slideModels = new ArrayList<>();
        for(int i=0; i<mImageUrls.size(); i++){
            tmp=i+1;
            slideModels.add(new SlideModel(mImageUrls.get(i),index+tmp));
        }
        imageSlider.setImageList(slideModels,true);

    }

}