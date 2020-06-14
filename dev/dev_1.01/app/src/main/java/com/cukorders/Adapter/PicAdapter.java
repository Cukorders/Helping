package com.cukorders.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.cukorders.helping.InitImg;
import com.cukorders.helping.InitPost;
import com.cukorders.helping.PostContentsViewActivity;
import com.cukorders.helping.R;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.media.CamcorderProfile.get;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class PicAdapter extends PagerAdapter {
    //Put post info in ArrayList
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private static final String TAG = "Pic Adapter";
    private Context mContext;

    PicAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getCount(){
        return mImageUrls.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object){
        return view ==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTOR_CROP);
        imageView.setImageResource(mImageUrls);
        container.addView(imageView,0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

}
