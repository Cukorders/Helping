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

import com.cukorders.helping.InitPost;
import com.cukorders.helping.PostContentsViewActivity;
import com.cukorders.helping.R;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    //Put post info in ArrayList
    private ArrayList<InitPost> initPosts = new ArrayList<>();
    private static final String TAG = "Post Adapter";
    private Context context;
    private DatabaseReference postRef;

    private static final int NORMAL_VIEW =1;
    private static final int ISFINISHED_VIEW =2;
    private int chkState=0;

    private int cnt=0;

    public PostAdapter(Context context, ArrayList<InitPost> initPosts) {
        this.initPosts = initPosts;
        this.context = context;
    }

    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylce_main, parent, false);
        final PostViewHolder holder = new PostViewHolder(view);
        return holder;
        /*
        Log.e(TAG,"SHOW when outside if is loaded"+cnt++);
        if (cnt==ISFINISHED_VIEW) {
            Log.e(TAG,"SHOW when inside of ISFINISHED If is START"+cnt++);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylce_main_isfinished, parent, false);
            final PostViewHolder holder = new PostViewHolder(view);
            Log.e(TAG,"SHOW when inside of ISFINISHED If is END"+cnt++);
            return holder;
        }
        else{
            Log.e(TAG,"SHOW when inside of NORMALSTATE  is START"+cnt++);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylce_main, parent, false);
            final PostViewHolder holder = new PostViewHolder(view);
            Log.e(TAG,"SHOW when inside of NORMALSTATE  is END"+cnt++);
            return holder;
        }
        */
    }


    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, final int position) {
        //각 아이템 매칭


        Picasso.get().load(initPosts.get(position).getImage1()).into(holder.postPic);
        holder.title.setText(initPosts.get(position).getTitle());
        //holder.place.setText(initPosts.get(position).getPlace());
        holder.time.setText(initPosts.get(position).getEndTime());
        holder.likes.setText(initPosts.get(position).getLikes());
        holder.chat_number.setText(initPosts.get(position).getChat_number());
        //holder.postUid.setText(initPosts.get(position).getPostKey());

        Log.e(TAG,"SHOW when it loaded"+cnt++);
        Log.e(TAG,"SHOW position"+initPosts.get(position).getTitle());
        Log.e(TAG,"SHOW initPosts.get(position).getIsMatched()"+initPosts.get(position).getIsMatched());
        Log.e(TAG,"SHOW chkState"+chkState);
        if(!initPosts.get(position).getIsMatched().equals("0")){
            //default 가 0임
            //0이 아니면 매칭이 된 것임
            holder.state_show.setText("매칭완료된 미션");
            holder.state_show.setTextColor(Color.parseColor("#03DAC5"));
            holder.state_show.setVisibility(VISIBLE);
        }else{
            holder.state_show.setVisibility(INVISIBLE);
        }

/*
        Log.e(TAG,"SHOW position"+initPosts.get(position).getTitle());
        Log.e(TAG,"SHOW initPosts.get(position).getIsFinished()"+initPosts.get(position).getIsFinished());
        Log.e(TAG,"SHOW chkState"+chkState);
        if(!(initPosts.get(position).getIsFinished().equals("0"))) {
            //default 가 0임
            //0이 아니면 종료된 것임
            holder.state_show.setText("종료된 미션");
            holder.state_show.setVisibility(VISIBLE);
            //색상 변경하는 걸 하고싶지만 잘 되지 않음....
            chkState=ISFINISHED_VIEW;
            //holder.post_single.setBackgroundResource(R.drawable.stroke_up_and_down_bold_withgraybg);
        }else{
            chkState=NORMAL_VIEW;
        }


 */
        Log.e(TAG,"SHOW position"+initPosts.get(position).getTitle());
        Log.e(TAG,"SHOW chkState"+chkState);

        holder.post_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onclick:clicked on " + initPosts.get(position));
                Intent intent = new Intent(context, PostContentsViewActivity.class);
                //intent.putExtra("postUid",)
                Bundle bundle = new Bundle();
                bundle.putSerializable("useruid",initPosts.get(position).getUid());
                bundle.putSerializable("ismatched",initPosts.get(position).getIsMatched());
                bundle.putSerializable("post",initPosts.get(position).getPostKey());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        // 불러온 리스트의 내용이 0이 아니면 그 사이즈를 불러오고 아니면 0을 사이즈에 넣기
        return (initPosts !=null ? initPosts.size() :0);
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{
        ImageView postPic;
        TextView title,time,chat_number,likes,state_show,place;
        LinearLayout post_single;

        //생성자
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.main_title_view);
            //region = (TextView) itemView.findViewById(R.id.main_post_place);
            place = (TextView) itemView.findViewById(R.id.main_post_place);
            time = (TextView) itemView.findViewById(R.id.main_uploaded_time);
            likes = (TextView) itemView.findViewById(R.id.explain_for_small_heart);
            chat_number = (TextView) itemView.findViewById(R.id.explain_for_small_chat);
            postPic = (ImageView) itemView.findViewById(R.id.main_image_view);
            post_single = (LinearLayout) itemView.findViewById(R.id.single_card);
            state_show = (TextView) itemView.findViewById(R.id.show_mission_state_view);
        }
    }




}