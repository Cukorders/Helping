package com.cukorders.helping;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cukorders.Adapter.PicAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.view.View.VISIBLE;

public class PostImgView extends AppCompatActivity {
    private static final String TAG = "PostingView";
    private String imageurl1;
    private String imageurl2;
    private String imageurl3;
    private ArrayList<String> mImageUrls;

    private TextView imgNum;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displaypostpic_recycle);

        imgNum = findViewById(R.id.img_num);

        Intent newintent = getIntent();

        ArrayList<String> tempUrls =newintent.getStringArrayListExtra("lists");
        Log.d(TAG,"get Image1 from mImageUrls : "+mImageUrls.get(0));
        Log.d(TAG,"get Image2 from mImageUrls : "+mImageUrls.get(1));
        Log.d(TAG,"get Image3 from mImageUrls : "+mImageUrls.get(2));

        //get image and put in Arraylist
        for(int i=0; i<3; i++){
            if(!tempUrls.isEmpty()){
                Log.d(TAG, "chk how many time: " + i);
                if(!tempUrls.get(i).equals("default")){
                    mImageUrls.add(tempUrls.get(i).toString());
                }
            }else{
                Log.d(TAG, "chk how many time: " + i);

            }
        }
        initRecyclerView();
    }

    private void initRecyclerView(){
        //postUid
        //Todo img누르면 여러개 이미지 한꺼번에 보이기
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = findViewById(R.id.postView_recyclerview);
        recyclerView.setLayoutManager(layoutManager);
        PicAdapter adapter = new PicAdapter(this, mImageUrls);
        recyclerView.setAdapter(adapter);
    }

}
