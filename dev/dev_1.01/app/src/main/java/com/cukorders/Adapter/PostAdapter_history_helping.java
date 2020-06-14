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
import com.cukorders.helping.PostContentsView_helpingActivity;
import com.cukorders.helping.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter_history_helping extends RecyclerView.Adapter<PostAdapter_history_helping.PostViewHolder> {
    //Put post info in ArrayList
    private ArrayList<InitPost> initPosts = new ArrayList<>();
    private static final String TAG = "Post Adapter_history_helping";
    private Context context;
    private DatabaseReference postRef;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String mUid="";

    //수행했던 것들과 수행중인 미션 모두 노출됨

    public PostAdapter_history_helping(Context context, ArrayList<InitPost> initPosts) {
        this.initPosts = initPosts;
        this.context = context;
    }


    @NonNull
    @Override
    //viewHolder를 만들어냄
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //set mUid
        mAuth= FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mUid = mCurrentUser.getUid();

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

        //만약 매칭된것만 노출이라면 수락 기다리는 중인 미션은 노출할 필요 없음
/*        if(!initPosts.get(position).getIsSended().equals("1")) {
            //default 가 0임
            //0이 아니면 매칭이 된 것임
            holder.state_show.setText("수락 기다리는 중");
            holder.state_show.setVisibility(VISIBLE);
            holder.state_show.setTextColor(Color.parseColor("#03DAC5"));
        }

 */
        if(!initPosts.get(position).getIsFinished().equals("0")){
            //0이 아니라면 끝난 미션
            holder.post_single.setBackgroundColor(Color.parseColor("#d5d5d5"));
        }

        holder.post_single.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onclick:clicked on " + initPosts.get(position));
                Intent intent = new Intent(context, PostContentsView_helpingActivity.class);
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


    //InitPost에서 생성된 객체들을 불러옴
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