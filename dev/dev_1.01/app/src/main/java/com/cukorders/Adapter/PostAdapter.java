package com.cukorders.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cukorders.helping.InitPost;
import com.cukorders.helping.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    //Put post info in ArrayList
    private ArrayList<InitPost> initPosts;
    //private Context context;

    public PostAdapter(ArrayList<InitPost> initPosts) {
        this.initPosts = initPosts;
        //this.context = context;
    }


    @NonNull
    @Override
    //viewHolder를 만들어냄
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylce_main,parent, false);
        PostViewHolder holder = new PostViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        //각 아이템 매칭
        Picasso.get().load(initPosts.get(position).getImage1()).into(holder.postPic);
        holder.title.setText(initPosts.get(position).getTitle());
        holder.place.setText(initPosts.get(position).getPlace());
        holder.time.setText(initPosts.get(position).getEndTime());
        holder.likes.setText(initPosts.get(position).getLikes());
        holder.chat_number.setText(initPosts.get(position).getChat_number());
        //onclickListener을 통해서 새로운 창이 열리도록 함
    }

    @Override
    public int getItemCount() {
        // 불러온 리스트의 내용이 0이 아니면 그 사이즈를 불러오고 아니면 0을 사이즈에 넣기
        return (initPosts !=null ? initPosts.size() :0);
    }

    //InitPost에서 생성된 객체들을 불러옴
    public class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView postPic;
        TextView title,place,time,chat_number,likes;
        //생성자
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.main_title_view);
            place = (TextView) itemView.findViewById(R.id.main_post_place);
            time = (TextView) itemView.findViewById(R.id.main_uploaded_time);
            likes = (TextView) itemView.findViewById(R.id.explain_for_small_heart);
            chat_number = (TextView) itemView.findViewById(R.id.explain_for_small_chat);
            postPic = (ImageView) itemView.findViewById(R.id.main_image_view);
        }
    }
}
