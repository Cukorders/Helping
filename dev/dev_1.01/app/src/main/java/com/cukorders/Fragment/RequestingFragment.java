package com.cukorders.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cukorders.Adapter.PostAdapter_request;
import com.cukorders.helping.AuthActivity;
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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestingFragment extends Fragment {

    private static final String TAG = "RequestingFragment";
    private static final String notSended = "0";
    private static final String onlyClientSented = "1";
    private static final String matching = "2";
    private static final String finished = "3";

    private RecyclerView recentPostListsView;
    private PostAdapter_request mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<InitPost> mPost=new ArrayList<InitPost>();

    private DatabaseReference mClientPostRef;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String mUid="";

    private boolean flag=false;
    //todo 의뢰중인 미션
    public RequestingFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.requesting_missions,container,false);
        //recyclerview
        recentPostListsView = (RecyclerView) view.findViewById(R.id.requesting_recyclerview);
        recentPostListsView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recentPostListsView.setLayoutManager(mLayoutManager);
        //맨 밑으로 내려갔을때, 파란색 그라데이션 안뜨도록
        recentPostListsView.scrollToPosition(0);

        mAdapter = new PostAdapter_request(getActivity(),mPost);
        recentPostListsView.setAdapter(mAdapter);
        recentPostListsView.setItemAnimator(new DefaultItemAnimator());

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        if(mCurrentUser != null) {
            mUid = mCurrentUser.getUid(); //Do what you need to do with the id
            flag=true;
        } else{
            caution();
        }

        /*
        if(mCurrentUser.getUid()==null){
            mUid="TIhMFvxLG9awVpVPN931vwXDUXz2";
            caution();
        }else{
            mUid = mCurrentUser.getUid(); //Do what you need to do with the id
        }
        if(mUid!=""){
            flag=true;
        }
         */
        //client postref
        mClientPostRef = FirebaseDatabase.getInstance().getReference().child("Posting");

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        //의뢰한 것들 중에서
        // 상단부는 매칭이 된 부분
        // 하단부는 매칭이 안된 부분
        //TODO 현재 내 uid와 같지 않은것도 mPOst에 들어간거 같음 확인해보기
        Log.d(TAG, "Check my muid " + mUid);
            Query notMatched = mClientPostRef.orderByChild("uid").equalTo(mUid).limitToFirst(100);
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
                            ArrayList<InitPost> bottomPost = new ArrayList<InitPost>();
                            smallPost.clear();
                            bottomPost.clear();
                            for (InitPost object : mPost) {
                                Log.d(TAG, "Check my mPost " + mPost);
                                Log.d(TAG, "Check mUid " + mUid);
                                Log.d(TAG, "Check uid " + object.getUid().equals(mUid));
                                    //담은 데이터들을 배열 리스트에 넣고 리사이클러 뷰로 보낼 준비하기
                                    //매칭이 완료된 것들
                                if (!object.getIsFinished().equals("0")) {
                                    //이미 종료된 미션들
                                    bottomPost.add(object);
                                } else {
                                    //매칭은 되었지만, 끝나진 않은 것들
                                    smallPost.add(object);
                                }
                            }
                            smallPost.addAll(bottomPost);
                            mAdapter = new PostAdapter_request(getActivity(), smallPost);
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
    private void caution(){
        AlertDialog.Builder builder=new AlertDialog.Builder(RequestingFragment.this.getActivity());
        builder.setTitle("로그인이 필요한 작업입니다.");
        builder.setMessage("이 작업을 수행하시려면 로그인이 필요합니다.");
        builder.setPositiveButton("로그인/회원가입 하기",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(RequestingFragment.this.getActivity(), AuthActivity.class));
                    }
                }).setNegativeButton("취소",null);
        builder.show();
    }
}