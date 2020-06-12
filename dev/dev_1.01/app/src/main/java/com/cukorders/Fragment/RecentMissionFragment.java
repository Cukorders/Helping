package com.cukorders.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

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
    public static Boolean locCertification;
    public static Context recentMissionFragment;
    private static DatabaseReference databaseReference;
    private LinearLayout linearLayout;
    private static Context context;
    private ImageButton filter;
    private Spinner user_locations;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    public static String location_now;
    private DatabaseReference locRef;
    private Button bt_certificate;
    private String checkKey;
    private final String TAG="RecentMissionActivity";

    public static ArrayList<String> loc=new ArrayList<>();

    //DB에서  post 연결
    private DatabaseReference postRef;


    public RecentMissionFragment(){

    }
    //최근 미션 안에 들어갈 내용 넣음
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.recent_mission,container,false);
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
        recentMissionFragment=getContext();
        context=recentMissionFragment;

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

        if(firebaseUser==null){
            arrayList.add(((ChooseTheRegionActivity)ChooseTheRegionActivity.regional_certification1).user_location);
        }else{
            databaseReference=FirebaseDatabase.getInstance().getReference().child("userRegions").child(firebaseUser.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        String key=snapshot.getKey(),val;
                        Log.d(TAG,"key의 값: "+key);
                        val=snapshot.getValue().toString();
                        if(!key.contains("state")&&!val.equals("default")){
                            arrayList.add(val);
                            Log.d(TAG,"arrayList에 "+val+"이 추가되었습니다.");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }

        locCertification=true;

        user_locations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location_now=arrayList.get(position);
               // locCertification=((LoadingActivity)LoadingActivity.loadingActivity).isCertified[position];
                Log.d("사용자 현위치","사용자의 현위치 : "+location_now);
                Log.d("사용자 현위치 인증","사용자의 현위치 위치 인증여부"+locCertification);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(context,"하나 이상의 카테고리를 설정하세요.",Toast.LENGTH_LONG).show();
            }
        });

        if(firebaseUser==null){
            Log.e("user not found","로그인 하지 않은 유저입니다.");
            CustomDialog customDialog=new CustomDialog(recentMissionFragment);
            customDialog.show();
            Log.d("유저 지역","유저 지역: "+arrayList.get(0));
        }else{
            setLocation();
            databaseReference=FirebaseDatabase.getInstance().getReference().child("userRegions").child(firebaseUser.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        String key= snapshot.getKey();
                        Log.d("value","유저 key: "+key);
                        if(snapshot.getValue().toString().equals(location_now)){
                            checkKey=key+" state";
                        }else if(key.equals(checkKey)){
                            if(snapshot.getValue().toString().equals("default")||snapshot.getValue().toString().equals("false")){
                                locCertification=false;
                                Log.d("locCertification","locCertification의 값: "+locCertification);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });

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

