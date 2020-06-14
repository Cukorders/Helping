package com.cukorders.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cukorders.Adapter.PostAdapter_helping;
import com.cukorders.Adapter.PostAdapter_history_request;
import com.cukorders.Adapter.PostAdapter_request;
import com.cukorders.helping.InitPost;
import com.cukorders.helping.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestingHistoryFragment extends Fragment {

    private static final String TAG = "RequestingFragment";

    private RecyclerView recentPostListsView;
    private PostAdapter_history_request mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<InitPost> mPost=new ArrayList<InitPost>();

    private DatabaseReference mClientPostRef;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String mUid="";

    //todo 의뢰중인 미션
    public RequestingHistoryFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_mission,container,false);
        //recyclerview
        recentPostListsView = (RecyclerView) view.findViewById(R.id.history_recyclerview);
        recentPostListsView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recentPostListsView.setLayoutManager(mLayoutManager);
        //맨 밑으로 내려갔을때, 파란색 그라데이션 안뜨도록
        recentPostListsView.scrollToPosition(0);

        mAdapter = new PostAdapter_history_request(getActivity(),mPost);
        recentPostListsView.setAdapter(mAdapter);
        recentPostListsView.setItemAnimator(new DefaultItemAnimator());

        //set mUid
        mAuth= FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        if(mCurrentUser != null) {
            mUid = mCurrentUser.getUid(); //Do what you need to do with the id
        }else{
            //TODO mUid 가져오기
            //mUid = "TIhMFvxLG9awVpVPN931vwXDUXz2";
        }

        //client postref
        mClientPostRef = FirebaseDatabase.getInstance().getReference().child("Posting");

        return view;
    }


    @Override
    public void onStart() {
        //TODO 여기도 오류 있음
        super.onStart();
        // 상단부는 매칭이 된 부분
        // 하단부는 매칭이 안된 부분
        Log.d(TAG, "Check my uid " + mUid);
        Query notMatched=mClientPostRef.orderByChild("uid").equalTo(mUid).limitToFirst(100);
        notMatched.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPost.clear();
                Log.d(TAG, "Check my dataSnapshot " + dataSnapshot);
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.d(TAG, "Check my snapshot " + snapshot);
                        //mPost = snapshot.getValue(InitPost.class);//InitPost 객체에 담기
                        Log.d(TAG, "Check my post " + mPost);

                        InitPost post = snapshot.getValue(InitPost.class);//InitPost 객체에 담기
                        mPost.add(post); //담은 데이터들을 배열 리스트에 넣고 리사이클러 뷰로 보낼 준비하기
                    }
                    mAdapter.notifyDataSetChanged();

                    Log.d(TAG, "Check my mPost " + mPost);
                    ArrayList<InitPost> smallPost = new ArrayList<InitPost>();
                    ArrayList<InitPost> underPost = new ArrayList<InitPost>();
                    ArrayList<InitPost> bottomPost = new ArrayList<InitPost>();
                    smallPost.clear();
                    underPost.clear();
                    bottomPost.clear();
                    for (InitPost object : mPost) {
                        Log.d(TAG, "Check my mPost " + mPost);
                        Log.d(TAG, "Check mUid " + mUid);
                        Log.d(TAG, "Check my check we contain mUid " + object.getIsSended().contains(mUid));
                        Log.d(TAG, "Check uid " + object.getUid().equals(mUid));
                        if (!object.getIsMatched().equals("0")) {
                            //담은 데이터들을 배열 리스트에 넣고 리사이클러 뷰로 보낼 준비하기
                            //매칭이 완료된 것들
                            if(!object.getIsFinished().equals("0")){
                                //이미 종료된 미션들
                                bottomPost.add(object);
                            }else{
                                //종료는 안되었지만 매칭된 상태
                                smallPost.add(object);
                            }
                        }else{
                            //매칭이 아직 안된 것
                            underPost.add(object);
                        };
                    }
                    smallPost.addAll(underPost);
                    smallPost.addAll(bottomPost);
                    mAdapter = new PostAdapter_history_request(getActivity(), smallPost);
                    recentPostListsView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비 가져오는 중 에러 발생시
                Log.e(TAG, String.valueOf(databaseError.toException()));
            }
        });
    }
}
