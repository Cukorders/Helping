package com.cukorders.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cukorders.Adapter.PostAdapter;
import com.cukorders.helping.InitPost;
import com.cukorders.helping.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class RecentMissionFragment extends Fragment {

    private RecyclerView recentPostListsView;
    private PostAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<InitPost> mPost=new ArrayList<InitPost>();
    //DB에서  post 연결
    private DatabaseReference postRef;


    public RecentMissionFragment(){

    }
    //최근 미션 안에 들어갈 내용 넣음
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recent_mission,container,false);
        //recyclerview
        recentPostListsView = (RecyclerView) view.findViewById(R.id.main_recyclerview);
        recentPostListsView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recentPostListsView.setLayoutManager(mLayoutManager);
        //맨 밑으로 내려갔을때, 파란색 그라데이션 안뜨도록
        recentPostListsView.scrollToPosition(0);

        mAdapter = new PostAdapter(getActivity(),mPost);
        recentPostListsView.setAdapter(mAdapter);
        recentPostListsView.setItemAnimator(new DefaultItemAnimator());

        //postref
        postRef = FirebaseDatabase.getInstance().getReference().child("Posting");
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아옴
                mPost.clear();
                //mPost=null;//기존 배열 리스트가 없도록 초기화
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    //반복을 통해서 안에 있는 데이터리스트들을 추출하기
                    InitPost post = snapshot.getValue(InitPost.class);//InitPost 객체에 담기
                    mPost.add(post); //담은 데이터들을 배열 리스트에 넣고 리사이클러 뷰로 보낼 준비하기
                }
                //데이터 저장 및 새로고침
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비 가져오는 중 에러 발생시
                Log.e("RecentMissionFragment", String.valueOf(databaseError.toException()));
            }
        });

        mAdapter= new PostAdapter(getActivity(),mPost);
        recentPostListsView.setAdapter(mAdapter);
    }

}

