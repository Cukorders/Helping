package com.cukorders.Adapter;

import android.annotation.SuppressLint;
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
import com.cukorders.helping.PostContentsView_requestActivity;
import com.cukorders.helping.R;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class PostAdapter_history_request extends RecyclerView.Adapter<PostAdapter_history_request.PostViewHolder> {
    //Put post info in ArrayList
    private ArrayList<InitPost> initPosts = new ArrayList<>();
    private static final String TAG = "Post Adapter_history_request";
    private Context context;
    private DatabaseReference postRef;

    public PostAdapter_history_request(Context context, ArrayList<InitPost> initPosts) {
        this.initPosts = initPosts;
        this.context = context;
    }


    @NonNull
    @Override
    //viewHolder를 만들어냄
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylce_main,parent, false);
        final PostViewHolder holder = new PostViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, final int position) {
        //각 아이템 매칭
        Picasso.get().load(initPosts.get(position).getImage1()).into(holder.postPic);
        holder.title.setText(initPosts.get(position).getTitle());
        holder.region.setText(initPosts.get(position).getLocation());
        holder.time.setText(initPosts.get(position).getEndTime());
        holder.likes.setText(initPosts.get(position).getLikes());
        holder.chat_number.setText(initPosts.get(position).getChat_number());
        //holder.postUid.setText(initPosts.get(position).getPostKey());

        if(!initPosts.get(position).getIsMatched().equals("0")){
            //default 가 0임
            //0이 아니면 매칭이 된 것임
            if(initPosts.get(position).getIsFinished().equals("0")){
                //아직 종료는 안된 상태
                holder.state_show.setText("매칭된 미션");
                holder.state_show.setVisibility(VISIBLE);
                holder.state_show.setTextColor(Color.parseColor("#03DAC5"));
            }else{
                //종료된 상태
                holder.state_show.setTextColor(Color.parseColor("#F44336"));
                holder.state_show.setText("종료된 미션");
                holder.state_show.setVisibility(VISIBLE);
            }
        }else{
            holder.state_show.setTextColor(Color.parseColor("#F44336"));
            if(initPosts.get(position).getIsSended().equals("1")){
                //헬퍼의 대답 대기중
                holder.state_show.setText("헬퍼 수락 대기중");
                holder.state_show.setVisibility(VISIBLE);
                holder.state_show.setTextColor(Color.parseColor("#F44336"));
            }else if(initPosts.get(position).getIsSended().equals("3")){
                holder.state_show.setText("수락요청 거절됨");
                holder.state_show.setVisibility(VISIBLE);
            }else{
                holder.state_show.setVisibility(INVISIBLE);
            }
        }

        holder.post_single.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onclick:clicked on " + initPosts.get(position));
                Intent intent = new Intent(context, PostContentsView_requestActivity.class);
                //intent.putExtra("postUid",)
                Bundle bundle = new Bundle();
                bundle.putSerializable("useruid",initPosts.get(position).getUid());
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
        TextView title,time,chat_number,likes,state_show,region;
        LinearLayout post_single;

        //생성자
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.main_title_view);
            region = (TextView) itemView.findViewById(R.id.main_post_place);
            //place = (TextView) itemView.findViewById(R.id.main_post_place);
            time = (TextView) itemView.findViewById(R.id.main_uploaded_time);
            likes = (TextView) itemView.findViewById(R.id.explain_for_small_heart);
            chat_number = (TextView) itemView.findViewById(R.id.explain_for_small_chat);
            postPic = (ImageView) itemView.findViewById(R.id.main_image_view);
            post_single = (LinearLayout) itemView.findViewById(R.id.single_card);
            state_show = (TextView) itemView.findViewById(R.id.show_mission_state_view);
        }
    }
}