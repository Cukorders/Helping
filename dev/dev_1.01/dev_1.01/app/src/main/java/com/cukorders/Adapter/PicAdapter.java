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

public class PicAdapter extends RecyclerView.Adapter<PicAdapter.PicViewHolder> {
    //Put post info in ArrayList
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private static final String TAG = "Pic Adapter";
    private Context context;

    public PicAdapter(Context context,  ArrayList<String> mImageUrls) {
        this.mImageUrls = mImageUrls;
        this.context = context;
    }

    public PicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.displaypostpic, parent, false);
        final PicViewHolder holder = new PicViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PicViewHolder holder, final int position) {
        //각 아이템 매칭
        Picasso.get().load(mImageUrls.get(position)).into(holder.DisplayPic);



    }


    @Override
    public int getItemCount() {
        // 불러온 리스트의 내용이 0이 아니면 그 사이즈를 불러오고 아니면 0을 사이즈에 넣기
        return (mImageUrls !=null ? mImageUrls.size() : 0);
    }

    public class PicViewHolder extends RecyclerView.ViewHolder{
        ImageView DisplayPic;

        //생성자
        public PicViewHolder(@NonNull View itemView) {
            super(itemView);
            DisplayPic = (ImageView) itemView.findViewById(R.id.displayPic);
        }
    }




}
