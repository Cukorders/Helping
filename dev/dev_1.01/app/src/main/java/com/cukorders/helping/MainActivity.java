package com.cukorders.helping;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.cukorders.Adapter.PageAdapter;
import com.cukorders.Fragment.MyCallingFragment;
import com.cukorders.Fragment.RecentMissionFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab_plus,fab_write,fab_info,fab_chat;
    Animation FabOpen,FabClose,FabClockwise,FabAntiClockwise;
    private Button myMission,currentMission;
    boolean isOpen=false;
    private final Context context=this;
    private ImageButton filter;
    private FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tab1,tab2;

    private  LinearLayout linearLayout;
    private boolean locationCertification;
    public static Context mainActivity;
    public String mainLocation="";
    private TextView nowLocation;
    private String checkKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        fab_plus=(FloatingActionButton) findViewById(R.id.fab_plus);
        fab_write=(FloatingActionButton) findViewById(R.id.fab_post);
        fab_info=(FloatingActionButton) findViewById(R.id.fab_info);
        fab_chat=(FloatingActionButton) findViewById(R.id.fab_chat);
        FabOpen= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabClockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        FabAntiClockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);
        linearLayout=(LinearLayout) findViewById(R.id.notCertified);

        nowLocation = (TextView) findViewById(R.id.nowLocation);
        myMission=(Button) findViewById(R.id.myMission);
        currentMission=(Button) findViewById(R.id.currentMission);
        //filter=(ImageButton) findViewById(R.id.filter);
        //MainActivity=this;
        findViewById(R.id.fab_post).setOnClickListener(onClickListener);
        findViewById(R.id.fab_info).setOnClickListener(onClickListener);
        findViewById(R.id.fab_chat).setOnClickListener(onClickListener);
        findViewById(R.id.go_to_mypage).setOnClickListener(onClickListener);
        //findViewById(R.id.filter).setOnClickListener(onClickListener);

        locationCertification= true;
        Log.d("Main-locCertification","locCertification: "+locationCertification);

        if(firebaseUser!=null){
        databaseReference= FirebaseDatabase.getInstance().getReference().child("userRegions").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String key= snapshot.getKey();
                    Log.d("value","유저 key: "+key);
                    if(snapshot.getValue().toString().equals(RecentMissionFragment.location_now)){
                        checkKey=key+" state";
                    }else if(key.equals(checkKey)){
                        Log.d("key값","지역인증 여부: "+snapshot.getValue().toString());
                        if(snapshot.getValue().toString().equals("default")||snapshot.getValue().toString().equals("false")){
                            locationCertification=false;
                            Log.d("locationCertification","locationCertification 값: "+locationCertification);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        }
        fab_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isOpen){
                    open();
                }else{
                    close();
                }
            }
        });
        //main
        tabLayout = (TabLayout) findViewById(R.id.tab_Main);
        tab1 = (TabItem) findViewById(R.id.tab_recent_mission);
        tab2 = (TabItem) findViewById(R.id.tab_my_calling);
        viewPager = findViewById(R.id.view_pager);

        setUpViewPager(viewPager);
        tabLayout.setTabTextColors(Color.parseColor("#e1e1e1"),Color.parseColor("#FFFFFF"));
        tabLayout.setupWithViewPager(viewPager);

    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @SuppressLint("LongLogTag")
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.fab_post:
                   // locCertification=((RegionActivity)RegionActivity.regional_certification2).isCertified;
                    if(firebaseUser==null){
                        caution();
                    }
                    else if(!locationCertification){
                        AlertDialog.Builder builder=new AlertDialog.Builder(context);
                        builder.setTitle("지역 인증이 필요한 작업입니다.");
                        builder.setMessage("글 쓰기를 하시려면 지역 인증을 해야합니다.");
                        builder.setPositiveButton("지역인증 하기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               // mainLocation=((ChooseTheRegionActivity)ChooseTheRegionActivity.regional_certification1).userLocation;
                                startActivity(new Intent(context,RegionalCertificationActivity.class));
                            }
                        }).setNegativeButton("취소",null);
                        builder.show();
                    }
                    else{
                    Intent intent1 =new Intent(context,PostActivity.class);
                    Log.e("go to post","go to a posting page");
                    startActivity(intent1);
                    }
                    break;

                case R.id.fab_info:
                    if(firebaseUser==null){
                        caution();
                    }else{
                    Intent intent2=new Intent(context,MissionActivity.class);
                    startActivity(intent2);
                    }
                    break;

                case R.id.fab_chat:
                    if(firebaseUser==null){
                        caution();
                    }else {
                        //TODO 채팅 연결하기
                    }
                    break;

                case R.id.go_to_mypage:
                    if(firebaseUser==null){
                        caution();
                    }else{
                    Intent intent3=new Intent(context,MyPageActivity.class);
                    startActivity(intent3);
                    }
                    break;
/*
                case R.id.filter:
                    Intent intent3=new Intent(context,FilterActivity.class);
                    Log.d("필터로 이동","필터로 이동");
                    startActivity(intent3);
                    break;

 */
            }
        }
    };

    private void caution(){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("로그인이 필요한 작업입니다.");
        builder.setMessage("이 작업을 수행하시려면 로그인이 필요합니다.");
        builder.setPositiveButton("로그인/회원가입 하기",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(context,AuthActivity.class));
                    }
                }).setNegativeButton("취소",null);
        builder.show();
    }
    //tab 이동 설정하는 부분
    private void setUpViewPager(ViewPager viewPager) {
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
        adapter.addFragment(new RecentMissionFragment(),"최근 미션");
        adapter.addFragment(new MyCallingFragment(),"나의 부름");
        viewPager.setAdapter(adapter);

    }
    private void open(){
        fab_write.startAnimation(FabOpen);
        fab_info.startAnimation(FabOpen);
        fab_chat.startAnimation(FabOpen);
        fab_plus.startAnimation(FabClockwise);
        fab_info.setClickable(true);
        fab_write.setClickable(true);
        fab_chat.setClickable(true);
        isOpen=true;
        Log.d("open","open");
    }
    private void close(){
        fab_write.startAnimation(FabClose);
        fab_info.startAnimation(FabClose);
        fab_plus.startAnimation(FabAntiClockwise);
        fab_chat.startAnimation(FabClose);
        fab_info.setClickable(false);
        fab_write.setClickable(false);
        isOpen=false;
        Log.d("close","close");
    }


    /*private void startStartActivity(){
        Intent intent=new Intent(this,StartActivity.class);
        startActivity(intent);
    }

    private void startUserInfo(){
        Intent intent=new Intent(this,UserInfoActivity.class);
        startActivity(intent);
    }*/

   /* @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // 기존 뒤로 가기 버튼의 기능을 막기 위해 주석 처리 또는 삭제

        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지났으면 Toast 출력
        // 2500 milliseconds = 2.5 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            toast.cancel();
            toast = Toast.makeText(this,"이용해 주셔서 감사합니다.",Toast.LENGTH_LONG);
            toast.show();
        }
    }*/
}