package com.cukorders.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cukorders.Adapter.PostAdapter;
import com.cukorders.helping.ChooseTheRegionActivity;
import com.cukorders.helping.CustomDialog;
import com.cukorders.helping.FilterActivity;
import com.cukorders.helping.InitPost;
import com.cukorders.helping.R;
import com.cukorders.helping.RegionActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class RecentMissionFragment extends Fragment {

    private RecyclerView recentPostListsView;
    private PostAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<InitPost> mPost=new ArrayList<InitPost>();
    private FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    private Boolean locCertification;
    private static Context context;
    private LinearLayout linearLayout;
    private ImageButton filter;
    private Spinner user_locations;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private String loc;
    private DatabaseReference locRef;
    private Button bt_certificate;

    //DB에서  post 연결
    private DatabaseReference postRef;


    public RecentMissionFragment(){

    }
    //최근 미션 안에 들어갈 내용 넣음
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recent_mission,container,false);
        //recyclerview
        recentPostListsView = (RecyclerView) view.findViewById(R.id.main_recyclerview);
        recentPostListsView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recentPostListsView.setLayoutManager(mLayoutManager);
        //맨 밑으로 내려갔을때, 파란색 그라데이션 안뜨도록
        recentPostListsView.scrollToPosition(0);
        mAdapter = new PostAdapter(mPost);
        recentPostListsView.setAdapter(mAdapter);
        recentPostListsView.setItemAnimator(new DefaultItemAnimator());
        //postref
        postRef = FirebaseDatabase.getInstance().getReference().child("Posting");

        linearLayout=(LinearLayout) view.findViewById(R.id.linearLayout);
        context=getContext();
        loc=((ChooseTheRegionActivity)ChooseTheRegionActivity.regional_certification1).userLocation;
        locCertification=((RegionActivity)RegionActivity.regional_certification2).isCertified;
        Log.d("지역 인증","지역 인증 여부: "+locCertification);

        locRef=FirebaseDatabase.getInstance().getReference().child("userRegions");
        filter=(ImageButton) view.findViewById(R.id.recent_mission_filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("필터","필터 버튼이 클릭되었음.");
                startActivity(new Intent(getContext(), FilterActivity.class));
            }
        });
        user_locations=(Spinner) view.findViewById(R.id.user_locations);
        arrayList=new ArrayList<>();
        arrayList.add(loc);

        if(firebaseUser==null){
            Log.e("user not found","로그인 하지 않은 유저입니다.");
            CustomDialog customDialog=new CustomDialog(context);
            customDialog.show();
            Log.d("유저 지역","유저 지역: "+loc);
        }else{
            setLocation();
            if(!locCertification){
                Log.e("not certified","지역 인증하지 않은 유저입니다.");
                inflater.inflate(R.layout.not_certified,linearLayout,true);
                bt_certificate=(Button) view.findViewById(R.id.bt_certificate);
                bt_certificate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("인증 버튼 클릭","인증 버튼이 클릭되었음.");
                        startActivity(new Intent(context,RegionActivity.class));
                    }
                });
            }
        }
        arrayAdapter=new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,arrayList);
        user_locations.setAdapter(arrayAdapter);

        return view;
    }

    private void setLocation(){
        locRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                int cnt=1;
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String key=snapshot.getKey();
                    HashMap<String,String> info=(HashMap<String,String>) snapshot.getValue();
                    String val=info.get("Region "+String.valueOf(cnt++));
                    if(val!=null&&!val.equals("default")){
                        arrayList.add(val);
                        Log.d("유저 지역 정보","유저 지역 정보: "+val);
                    }
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error in getting the locations","지역 정보를 가져오는데 실패하였습니다.(스피너)");
            }
        });
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

        mAdapter= new PostAdapter(mPost);
        recentPostListsView.setAdapter(mAdapter);
    }

}

