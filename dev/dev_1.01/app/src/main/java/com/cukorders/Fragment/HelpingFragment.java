package com.cukorders.Fragment;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.cukorders.Adapter.PostAdapter_helping;
import com.cukorders.helping.InitPost;
import com.cukorders.helping.PostContentsView_helpingActivity;
import com.cukorders.helping.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpingFragment extends Fragment {

    private static final String TAG = "HelpingFragment";
    private static final String notSended = "0";
    private static final String onlyClientSented = "1";
    private static final String matching = "2";
    private static final String finished = "3";

    private RecyclerView recentPostListsView;
    private PostAdapter_helping mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<InitPost> mPost=new ArrayList<InitPost>();

    private DatabaseReference mHelperPostRef;
    private DatabaseReference getPostuidRef;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String mUid="";

    private String currentTime;
    private SimpleDateFormat sdf;

    //todo 의뢰중인 미션
    public HelpingFragment(){

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

        mAdapter = new PostAdapter_helping(getActivity(),mPost);
        recentPostListsView.setAdapter(mAdapter);
        recentPostListsView.setItemAnimator(new DefaultItemAnimator());

        //set mUid
        mAuth= FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mUid = mCurrentUser.getUid();

        //client postref
        mHelperPostRef = FirebaseDatabase.getInstance().getReference().child("Posting");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        /*
        private static final String notSended = "0";
        private static final String onlyClientSented = "1";
        private static final String matching = "2";
        private static final String finished = "3";
        */
        //헬퍼의 수행중인 미션은 isSended가 2로 시작하면서 helper인 나의uid가 들어가 있는 테이블만 불러온다.
        //String chkdidiaccept = notSended;
        //String chkdidheaccept = onlyClientSented+mUid;
        String chkdidweMatched = matching+mUid;
        //String chkfinished = finished+mUid;

        //아직 파란 상자
        Query gotMatched = mHelperPostRef.orderByChild("isSended").startAt(chkdidweMatched).endAt(chkdidweMatched+"\uf8ff").limitToFirst(100);
        gotMatched.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPost.clear();
                Log.d(TAG, "Check my dataSnapshot " + dataSnapshot);
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        InitPost post = snapshot.getValue(InitPost.class);//InitPost 객체에 담기
                        mPost.add(post); //담은 데이터들을 배열 리스트에 넣고 리사이클러 뷰로 보낼 준비하기
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비 가져오는 중 에러 발생시
                Log.e(TAG, String.valueOf(databaseError.toException()));
            }
        });
        mAdapter = new PostAdapter_helping(getActivity(), mPost);
        recentPostListsView.setAdapter(mAdapter);
    }
}