package com.cukorders.helping;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import com.cukorders.Adapter.PageAdapter;
import com.cukorders.Adapter.PageAdapter_Mycalling;
import com.cukorders.Fragment.HelpingFragment;
import com.cukorders.Fragment.MyCallingFragment;
import com.cukorders.Fragment.RecentMissionFragment;
import com.cukorders.Fragment.RequestingFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toolbar;


public class Main2Activity extends AppCompatActivity {
    FloatingActionButton fab_plus,fab_write,fab_info,fab_chat;
    Animation FabOpen,FabClose,FabClockwise,FabAntiClockwise;
    private Button myMission,currentMission;
    boolean isOpen=false;
    private final Context context=this;

    FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    private boolean locCertification;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tab1,tab2;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Log.d("The main page is started","The main page is sucessfully started");
        //main
        tabLayout = (TabLayout) findViewById(R.id.tab_Main);
        tab1 = (TabItem) findViewById(R.id.tab_recent_mission);
        tab2 = (TabItem) findViewById(R.id.tab_my_calling);
        viewPager = findViewById(R.id.view_pager);

        setUpViewPager(viewPager);
        tabLayout.setTabTextColors(Color.parseColor("#e1e1e1"),Color.parseColor("#FFFFFF"));
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
/*
        //Todo 글쓰기 하고 닫기 하면 갑자기 종료됨
        //로그인 진행 또는 확인 시행
        locCertification=((RegionActivity)RegionActivity.regional_certification2).isCertified;
        if(firebaseUser==null){
            CustomDialog customDialog=new CustomDialog(context);
            customDialog.callFuction();
        }
        else if(!locCertification){ // 로그인은 했지만 지역인증은 하지 않은 케이스

        }
*/
        //widget
        fab_plus=(FloatingActionButton) findViewById(R.id.fab_plus);
        fab_write=(FloatingActionButton) findViewById(R.id.fab_post);
        fab_info=(FloatingActionButton) findViewById(R.id.fab_info);
        fab_chat=(FloatingActionButton) findViewById(R.id.fab_chat);
        FabOpen= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabClockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        FabAntiClockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);
/*
        myMission=(Button) findViewById(R.id.myMission);
        currentMission=(Button) findViewById(R.id.currentMission);
        findViewById(R.id.currentMission).setOnClickListener(onClickListener);
  */
        findViewById(R.id.fab_post).setOnClickListener(onClickListener);
        findViewById(R.id.fab_info).setOnClickListener(onClickListener);
        findViewById(R.id.fab_chat).setOnClickListener(onClickListener);
        findViewById(R.id.go_to_mypage).setOnClickListener(onClickListener);

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

    }
    //tab 이동 설정하는 부분
    private void setUpViewPager(ViewPager viewPager) {
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
        adapter.addFragment(new RecentMissionFragment(),"최근 미션");
        adapter.addFragment(new MyCallingFragment(),"나의 부름");
        viewPager.setAdapter(adapter);

    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @SuppressLint("LongLogTag")
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                /*
                case R.id.myMission:
                    Log.d("myMission is clicked","myMission button is clicked");
                    myMission.setBackgroundColor(Color.parseColor("#70D398"));
                    currentMission.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    break;

                case R.id.currentMission:
                    Log.d("currentMission is clicked","currentMission is clicked");
                    currentMission.setBackgroundColor(Color.parseColor("#70D398"));
                    myMission.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    break;
*/
                case R.id.fab_post:
                    Intent intent1 =new Intent(context,PostActivity.class);
                    Log.e("go to post","go to a posting page");
                    startActivity(intent1);
                    break;

                case R.id.fab_info:

                    Intent intent2=new Intent(context,MissionActivity.class);
                    startActivity(intent2);
                    break;

                case R.id.fab_chat:
                    //TODO 채팅 연결하기
                    break;

                case R.id.go_to_mypage:
                    Intent intent3=new Intent(context,MyPageActivity.class);
                    startActivity(intent3);
                    break;

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

    private void open(){
        fab_write.startAnimation(FabOpen);
        fab_info.startAnimation(FabOpen);
        fab_chat.startAnimation(FabOpen);
        fab_plus.startAnimation(FabClockwise);
        fab_info.setClickable(true);
        fab_write.setClickable(true);
        fab_chat.setClickable(true);
        isOpen=true;
        // Log.d("open","open");
    }
    private void close(){
        fab_write.startAnimation(FabClose);
        fab_info.startAnimation(FabClose);
        fab_plus.startAnimation(FabAntiClockwise);
        fab_chat.startAnimation(FabClose);
        fab_info.setClickable(false);
        fab_write.setClickable(false);
        isOpen=false;
        //  Log.d("close","close");
    }
}