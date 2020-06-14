package com.cukorders.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class PicAdapter extends PagerAdapter {
    //Put post info in ArrayList
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private static final String TAG = "Pic Adapter";
    private Context mContext;

    public PicAdapter(Context context, ArrayList<String> mImageUrls){
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
        //imageView.setScaleType(ImageView.ScaleType.CENTOR_CROP);
        //imageView.setImageResource(mImageUrls);
        container.addView(imageView,0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

}
