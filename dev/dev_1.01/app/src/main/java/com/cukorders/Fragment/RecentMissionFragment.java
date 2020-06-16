package com.cukorders.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.cukorders.helping.LoadingActivity;
import com.cukorders.helping.R;
import com.cukorders.helping.RegionalCertificationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static android.app.Activity.RESULT_OK;


public class RecentMissionFragment extends Fragment {
    private static final String TAG ="RecentMissionFragment" ;
    private LinearLayout linearLayout;
    private RecyclerView recentPostListsView;
    private PostAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<InitPost> mPost=new ArrayList<InitPost>();
    private ArrayList<InitPost> tempPost=new ArrayList<InitPost>();
    //DB에서  post 연결
    private DatabaseReference postRef;
    private DatabaseReference userRef;

    //client uid 가져오기
    private String clientUid;

    //userid 가져오기
    private String mUid;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private String mGender;
    private String mAge;
    private RecentMissionFragment context;
    private boolean getUserinfo = false;
    private Button bt_certificate;

    private ImageButton filterButton;
    static  final int REQUEST_CODE = 1;
    public  static boolean[] checked = new boolean[7];

    private Spinner user_locations;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    public static String location_now;
    private DatabaseReference locRef;

    private FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    public static Boolean locCertification;
    public static Context recentMissionFragment;

    //loc관련 변수 선언
    private String myloc;

    public static boolean ifUsernotLogin=false;
    public static boolean iwentToMain=false;

    public RecentMissionFragment(){

    }
    //최근 미션 안에 들어갈 내용 넣음
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.recent_mission,container,false);
        //recyclerview
        recentPostListsView = (RecyclerView) view.findViewById(R.id.main_recyclerview);
        recentPostListsView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recentPostListsView.setLayoutManager(mLayoutManager);
        //맨 밑으로 내려갔을때, 파란색 그라데이션 안뜨도록
        recentPostListsView.scrollToPosition(0);
        context=this;

        linearLayout=(LinearLayout) view.findViewById(R.id.linearLayout);
        mAdapter = new PostAdapter(getActivity(),mPost);
        recentPostListsView.setAdapter(mAdapter);
        recentPostListsView.setItemAnimator(new DefaultItemAnimator());

        //get mUid
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if(mUser==null) {
            Log.e("user not found", "로그인 하지 않은 유저입니다.");
            CustomDialog customDialog = new CustomDialog(RecentMissionFragment.this.getActivity());
            customDialog.show();
            Log.d("유저 지역", "유저 지역: " + ((LoadingActivity) LoadingActivity.loadingActivity).loc.get(0));
            customDialog.setDialogResult(new CustomDialog.GoMain() {
                public void finish(boolean result) {
                    if (result) {
                        //둘러보기를 선택한 경우
                        ifUsernotLogin = true;
                    } else {
                        final Handler newhandler = new Handler();
                        newhandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Do something after 100ms
                                mUid = mUser.getUid();
                                userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mUid);
                            }
                        }, 100);
                    }
                }
            });
        }else{
            mUid = mUser.getUid();
            userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mUid);
        }

        //postref
        postRef = FirebaseDatabase.getInstance().getReference().child("Posting");

        if(mUser==null){
            // 만약 로그인하지 않은 유저: 시작할 때
            Log.d(TAG,"로그인하지 않은 유저입니다.");
            ((LoadingActivity)LoadingActivity.loadingActivity).loc.add(((ChooseTheRegionActivity) ChooseTheRegionActivity.regional_certification1).user_location);
        }else{
            Log.d(TAG,"로그인한 유저입니다.");
            // int size=((LoadingActivity)LoadingActivity.loadingActivity).loc.size();
            HashSet<String> tmp=new HashSet<>(((LoadingActivity)LoadingActivity.loadingActivity).loc);
            ((LoadingActivity)LoadingActivity.loadingActivity).loc=new ArrayList<>(tmp);
            for(int i=0;i<((LoadingActivity)LoadingActivity.loadingActivity).loc.size();++i){
                Log.d(TAG,"loc["+i+"]의 원소: "+((LoadingActivity)LoadingActivity.loadingActivity).loc.get(i));
            }
        }

        //get loc
        locRef=FirebaseDatabase.getInstance().getReference().child("userRegions");
        user_locations=(Spinner) view.findViewById(R.id.user_locations);
        arrayList=new ArrayList<>();

        //중복 제거
        HashSet<String>tmp=new HashSet<>(((LoadingActivity)LoadingActivity.loadingActivity).loc);
        ((LoadingActivity)LoadingActivity.loadingActivity).loc=new ArrayList<>(tmp);
        arrayAdapter=new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,((LoadingActivity)LoadingActivity.loadingActivity).loc);
        user_locations.setAdapter(arrayAdapter);

        locCertification=((LoadingActivity)LoadingActivity.loadingActivity).isCertified[0];

        user_locations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                myloc = ((LoadingActivity) LoadingActivity.loadingActivity).loc.get(position);
                locCertification = ((LoadingActivity) LoadingActivity.loadingActivity).isCertified[position];
                Log.d(TAG, "사용자의 현위치 : " + myloc);
                Log.d(TAG, "사용자의 현위치 위치 인증여부" + locCertification);
                if (firebaseUser != null) {
                    if (!locCertification) {
                        inflater.inflate(R.layout.not_certified, linearLayout, false);
                        Log.d(TAG, "지역 인증하지 않은 유저입니다.");
                        inflater.inflate(R.layout.not_certified, linearLayout, true);
                        bt_certificate = (Button) linearLayout.findViewById(R.id.bt_certificate);
                        linearLayout.findViewById(R.id.bt_certificate).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(RecentMissionFragment.this.getActivity(), RegionalCertificationActivity.class));
                            }
                        });
                    } else {
                        Log.d(TAG, "지역 인증을 한 지역입니다.");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        filterButton = (ImageButton) view.findViewById(R.id.recent_mission_filter);
        //filterBtn
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "now move to filter activity:");
                Intent newIntent = new Intent(getActivity(), FilterActivity.class);
                startActivityForResult(newIntent,REQUEST_CODE);
                /*
                Bundle bundle1 = getArguments();
                checked = bundle1.getBooleanArray("checks");
                 */
            }
        });

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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "I got from filter:");
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                checked = data.getBooleanArrayExtra("checks");
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //get mUser's age and
        //if i get info getUserinfo change to true

        if(mUser==null){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 5000ms
                    if (ifUsernotLogin == false) {
                        //로그인을 했다면
                        mUid = mUser.getUid();
                        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mUid);
                        Query findUser = userRef;
                        findUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String Age = dataSnapshot.child("Age").getValue().toString();
                                String gender = dataSnapshot.child("Gender").getValue().toString();

                                mGender = gender;
                                String temp = "";
                                mAge = temp + Age.charAt(0);
                                Log.d(TAG, "mUid is :" + mUid);
                                Log.d(TAG, "mGender is :" + mGender);
                                Log.d(TAG, "mAge is :" + mAge);

                                if ((!mAge.equals("")) && (!mGender.equals(""))) {
                                    getUserinfo = true;
                                }
                                Log.d(TAG, "check getUserinfo :" + getUserinfo);


                                Log.d(TAG, "not Searching and displaying recyclerview");
                                final ArrayList<InitPost> smallPost = new ArrayList<InitPost>();
                                final ArrayList<InitPost> bottomPost = new ArrayList<InitPost>();
                                final ArrayList<InitPost> finalPost = new ArrayList<InitPost>();
                                Query gotMatched = postRef.orderByChild("location").equalTo(myloc).limitToFirst(100);
                                //Query gotMatched = postRef.orderByChild("isFinished").equalTo("0").limitToFirst(100);
                                gotMatched.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        mPost.clear();
                                        smallPost.clear();
                                        bottomPost.clear();
                                        finalPost.clear();
                                        Log.d(TAG, "Check my dataSnapshot " + dataSnapshot);
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                InitPost post = snapshot.getValue(InitPost.class);//InitPost 객체에 담기
                                                mPost.add(post); //담은 데이터들을 배열 리스트에 넣고 리사이클러 뷰로 보낼 준비하기
                                            }
                                        }
                                        Log.d(TAG, "Check mPost " + mPost);
                                        mAdapter.notifyDataSetChanged();
                                        //종료 된 것들을 먼저 거름, smallPost에 담기
                                        int cnt = 0;
                                        for (InitPost object : mPost) {
                                            Log.d(TAG, "Check how many times i count " + cnt++);
                                            if (object.getIsFinished().equals("0") && object.getAge().contains(mAge)) {
                                                smallPost.add(object);
                                                Log.d(TAG, "Check what is put in smallPost " + object.getPostKey());
                                            }
                                            //담은 데이터들을 배열 리스트에 넣고 리사이클러 뷰로 보낼 준비하기
                                            //매칭이 완료된 것들
                                            //smallPost.add(object);
                                        }
                                        Log.d(TAG, "Check smallPost " + smallPost);
                                        //smallpost에 종료 아직 안된 것들, 내 나이가 포함되어있는 것들이 현재 포함되어있음
                                        //이제 Gender 거를 차례
                                        for (InitPost object : smallPost) {
                                            if (object.getGender().equals("동성")) {
                                                //동성으로 설정해 놓은 경우
                                                if (object.getClientGender().equals(mGender)) {
                                                    Log.d(TAG, "Check what is put in bottomPost " + object.getPostKey());
                                                    bottomPost.add(object);
                                                }
                                            } else {
                                                //무관으로 설정해놓은 경우
                                                Log.d(TAG, "Check what is put in bottomPost " + object.getPostKey());
                                                bottomPost.add(object);
                                            }
                                        }
                                        Log.d(TAG, "Check bottomPost " + bottomPost);
                                        //카테고리에서 거를것들을 넣기
                                        boolean flag = false;
                                        for (InitPost object : bottomPost) {
                                            //만약 아무것도 선택이 안된 것이라면 이 부분에서 filter를 하지 말고 그냥 바로 노출
                                            if (checked[0]) {
                                                //구매 대행(음식)
                                                flag = true;
                                                if (object.getCategory().equals("구매 대행(음식)")) {
                                                    finalPost.add(object);
                                                    Log.d(TAG, "Check what is put in finalPost from 구매대행(음식) " + object.getPostKey());
                                                }
                                            }
                                            if (checked[1]) {
                                                //구매 대행(음식 외 물품)
                                                flag = true;
                                                if (object.getCategory().equals("구매 대행(음식 외 물품)")) {
                                                    finalPost.add(object);
                                                    Log.d(TAG, "Check what is put in finalPost from 구매대행(음식 외)" + object.getPostKey());
                                                }
                                            }
                                            if (checked[2]) {
                                                //배달
                                                flag = true;
                                                if (object.getCategory().equals("배달(음식)")) {
                                                    finalPost.add(object);
                                                    Log.d(TAG, "Check what is put in finalPost from 배달(음식) " + object.getPostKey());
                                                }
                                            }
                                            if (checked[3]) {
                                                //배달(음식 외 물품)
                                                flag = true;
                                                if (object.getCategory().equals("배달(음식 외 물품)")) {
                                                    finalPost.add(object);
                                                    Log.d(TAG, "Check what is put in finalPost from 배달(음식 외) " + object.getPostKey());
                                                }
                                            }
                                            if (checked[4]) {
                                                //청소
                                                flag = true;
                                                if (object.getCategory().equals("청소")) {
                                                    finalPost.add(object);
                                                    Log.d(TAG, "Check what is put in finalPost from 청소 " + object.getPostKey());
                                                }
                                            }
                                            if (checked[5]) {
                                                //과외
                                                flag = true;
                                                if (object.getCategory().equals("과외")) {
                                                    finalPost.add(object);
                                                    Log.d(TAG, "Check what is put in finalPost from 과외 " + object.getPostKey());
                                                }
                                            }
                                            if (checked[6]) {
                                                //기타
                                                flag = true;
                                                if (object.getCategory().equals("기타")) {
                                                    finalPost.add(object);
                                                    Log.d(TAG, "Check what is put in finalPost from 기타 " + object.getPostKey());
                                                }
                                            }
                                        }
                                        if (flag) {
                                            if (finalPost.size() == 0) {
                                                Log.d(TAG, "Check what is showing" + finalPost);
                                                Toast.makeText(getContext(), "해당 조건에 해당하는 게시글이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                            mAdapter = new PostAdapter(getActivity(), finalPost);
                                            recentPostListsView.setAdapter(mAdapter);
                                            mAdapter.notifyDataSetChanged();
                                        } else {
                                            if (bottomPost.size() == 0) {
                                                Log.d(TAG, "Check what is showing" + bottomPost);
                                                Toast.makeText(getContext(), "게시글이 없습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                            mAdapter = new PostAdapter(getActivity(), bottomPost);
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

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    } else {
                        //user want to look around
                        Query gotMatched = postRef.orderByChild("location").equalTo(myloc).limitToFirst(100);
                        gotMatched.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                tempPost.clear();
                                Log.d(TAG, "Check my dataSnapshot " + dataSnapshot);
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        InitPost post = snapshot.getValue(InitPost.class);//InitPost 객체에 담기
                                        tempPost.add(post); //담은 데이터들을 배열 리스트에 넣고 리사이클러 뷰로 보낼 준비하기
                                    }
                                }
                                Log.d(TAG, "Check mPost " + mPost);
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });
                        mAdapter = new PostAdapter(getActivity(), tempPost);
                        recentPostListsView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }, 25000);
        }else{
                //로그인을 했다면
                Query findUser = userRef;
                findUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String Age = dataSnapshot.child("Age").getValue().toString();
                        String gender = dataSnapshot.child("Gender").getValue().toString();
                        mGender = gender;
                        String temp = "";
                        mAge = temp + Age.charAt(0);
                        Log.d(TAG, "mUid is :" + mUid);
                        Log.d(TAG, "mGender is :" + mGender);
                        Log.d(TAG, "mAge is :" + mAge);

                        if ((!mAge.equals("")) && (!mGender.equals(""))) {
                            getUserinfo = true;
                        }
                        Log.d(TAG, "check getUserinfo :" + getUserinfo);


                        Log.d(TAG, "not Searching and displaying recyclerview");
                        final ArrayList<InitPost> smallPost = new ArrayList<InitPost>();
                        final ArrayList<InitPost> bottomPost = new ArrayList<InitPost>();
                        final ArrayList<InitPost> finalPost = new ArrayList<InitPost>();
                        Query gotMatched = postRef.orderByChild("location").equalTo(myloc).limitToFirst(100);
                        //Query gotMatched = postRef.orderByChild("isFinished").equalTo("0").limitToFirst(100);
                        gotMatched.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                mPost.clear();
                                smallPost.clear();
                                bottomPost.clear();
                                finalPost.clear();
                                Log.d(TAG, "Check my dataSnapshot " + dataSnapshot);
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        InitPost post = snapshot.getValue(InitPost.class);//InitPost 객체에 담기
                                        mPost.add(post); //담은 데이터들을 배열 리스트에 넣고 리사이클러 뷰로 보낼 준비하기
                                    }
                                }
                                Log.d(TAG, "Check mPost " + mPost);
                                mAdapter.notifyDataSetChanged();
                                //종료 된 것들을 먼저 거름, smallPost에 담기
                                int cnt = 0;
                                for (InitPost object : mPost) {
                                    Log.d(TAG, "Check how many times i count " + cnt++);
                                    if (object.getIsFinished().equals("0") && object.getAge().contains(mAge)) {
                                        smallPost.add(object);
                                        Log.d(TAG, "Check what is put in smallPost " + object.getPostKey());
                                    }
                                    //담은 데이터들을 배열 리스트에 넣고 리사이클러 뷰로 보낼 준비하기
                                    //매칭이 완료된 것들
                                    //smallPost.add(object);
                                }
                                Log.d(TAG, "Check smallPost " + smallPost);
                                //smallpost에 종료 아직 안된 것들, 내 나이가 포함되어있는 것들이 현재 포함되어있음
                                //이제 Gender 거를 차례
                                for (InitPost object : smallPost) {
                                    if (object.getGender().equals("동성")) {
                                        //동성으로 설정해 놓은 경우
                                        if (object.getClientGender().equals(mGender)) {
                                            Log.d(TAG, "Check what is put in bottomPost " + object.getPostKey());
                                            bottomPost.add(object);
                                        }
                                    } else {
                                        //무관으로 설정해놓은 경우
                                        Log.d(TAG, "Check what is put in bottomPost " + object.getPostKey());
                                        bottomPost.add(object);
                                    }
                                }
                                Log.d(TAG, "Check bottomPost " + bottomPost);
                                //카테고리에서 거를것들을 넣기
                                boolean flag = false;
                                for (InitPost object : bottomPost) {
                                    //만약 아무것도 선택이 안된 것이라면 이 부분에서 filter를 하지 말고 그냥 바로 노출
                                    if (checked[0]) {
                                        //구매 대행(음식)
                                        flag = true;
                                        if (object.getCategory().equals("구매 대행(음식)")) {
                                            finalPost.add(object);
                                            Log.d(TAG, "Check what is put in finalPost from 구매대행(음식) " + object.getPostKey());
                                        }
                                    }
                                    if (checked[1]) {
                                        //구매 대행(음식 외 물품)
                                        flag = true;
                                        if (object.getCategory().equals("구매 대행(음식 외 물품)")) {
                                            finalPost.add(object);
                                            Log.d(TAG, "Check what is put in finalPost from 구매대행(음식 외)" + object.getPostKey());
                                        }
                                    }
                                    if (checked[2]) {
                                        //배달
                                        flag = true;
                                        if (object.getCategory().equals("배달(음식)")) {
                                            finalPost.add(object);
                                            Log.d(TAG, "Check what is put in finalPost from 배달(음식) " + object.getPostKey());
                                        }
                                    }
                                    if (checked[3]) {
                                        //배달(음식 외 물품)
                                        flag = true;
                                        if (object.getCategory().equals("배달(음식 외 물품)")) {
                                            finalPost.add(object);
                                            Log.d(TAG, "Check what is put in finalPost from 배달(음식 외) " + object.getPostKey());
                                        }
                                    }
                                    if (checked[4]) {
                                        //청소
                                        flag = true;
                                        if (object.getCategory().equals("청소")) {
                                            finalPost.add(object);
                                            Log.d(TAG, "Check what is put in finalPost from 청소 " + object.getPostKey());
                                        }
                                    }
                                    if (checked[5]) {
                                        //과외
                                        flag = true;
                                        if (object.getCategory().equals("과외")) {
                                            finalPost.add(object);
                                            Log.d(TAG, "Check what is put in finalPost from 과외 " + object.getPostKey());
                                        }
                                    }
                                    if (checked[6]) {
                                        //기타
                                        flag = true;
                                        if (object.getCategory().equals("기타")) {
                                            finalPost.add(object);
                                            Log.d(TAG, "Check what is put in finalPost from 기타 " + object.getPostKey());
                                        }
                                    }
                                }
                                if (flag) {
                                    if (finalPost.size() == 0) {
                                        Log.d(TAG, "Check what is showing" + finalPost);
                                        Toast.makeText(getContext(), "해당 조건에 해당하는 게시글이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    mAdapter = new PostAdapter(getActivity(), finalPost);
                                    recentPostListsView.setAdapter(mAdapter);
                                    mAdapter.notifyDataSetChanged();
                                } else {
                                    if (bottomPost.size() == 0) {
                                        Log.d(TAG, "Check what is showing" + bottomPost);
                                        Toast.makeText(getContext(), "게시글이 없습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    mAdapter = new PostAdapter(getActivity(), bottomPost);
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
        }
    }
}

