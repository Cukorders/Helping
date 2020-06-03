package com.cukorders.helping;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab_plus,fab_write,fab_info,fab_chat;
    Animation FabOpen,FabClose,FabClockwise,FabAntiClockwise;
    private Button myMission,currentMission;
    boolean isOpen=false;
    private final Context context=this;
    // private long backKeyPressedTime = 0;
    // 리사이클러 뷰 관련
    RecyclerView recyclerView;
    String s1[], s2[];
    //s1이 게시글 제목, s2가 게시물 작성시간,장소등 description, 게시글 역시 현재는 string에 배열로
    //db 생성하고 게시글 작성가능할 때, 연동시킬것
    int images[] = {};//이미지는 배열로 받았는데, 나중에 db 생성하면 db와 연동할 것
    FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
    private boolean locCertification;
    private TextView nowLocation;
    private LinearLayout linearLayout;
    public String mainLocation; // main에서 regional_Certification2로 넘길때 필요함
    public static Context mainActivity;

    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.main_recyclerview);

        s1 = getResources().getStringArray(R.array.main_title);
        s2 = getResources().getStringArray(R.array.main_description);

        MainAdapter mainAdapter = new MainAdapter(this, s1, s2, images);
        recyclerView.setAdapter(mainAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        locCertification=((RegionActivity)RegionActivity.regional_certification2).isCertified;
        nowLocation=(TextView) findViewById(R.id.nowLocation);
        nowLocation.setText(((ChooseTheRegionActivity)ChooseTheRegionActivity.regional_certification1).userLocation);
        linearLayout=(LinearLayout)findViewById(R.id.notCertified);
        if(firebaseUser==null){
            CustomDialog customDialog=new CustomDialog(context);
            customDialog.show();
        }
        else if(!locCertification){ // 지역 인증을 안 했을 경우
            LayoutInflater inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.not_certified,linearLayout,true);
            findViewById(R.id.bt_certificate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("지역 인증하기","지역 인증 페이지로 넘어갑니다.");
                    startActivity(new Intent(context,RegionActivity.class));
                }
            });
        }

        //위가 recyclerview 아래가 widget
        fab_plus=(FloatingActionButton) findViewById(R.id.fab_plus);
        fab_write=(FloatingActionButton) findViewById(R.id.fab_post);
        fab_info=(FloatingActionButton) findViewById(R.id.fab_info);
        fab_chat=(FloatingActionButton) findViewById(R.id.fab_chat);
        FabOpen= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabClockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        FabAntiClockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);
        linearLayout=(LinearLayout) findViewById(R.id.notCertified);

        myMission=(Button) findViewById(R.id.myMission);
        currentMission=(Button) findViewById(R.id.currentMission);
        mainActivity=this;

        findViewById(R.id.myMission).setOnClickListener(onClickListener);
        findViewById(R.id.currentMission).setOnClickListener(onClickListener);
        findViewById(R.id.fab_post).setOnClickListener(onClickListener);
        findViewById(R.id.fab_info).setOnClickListener(onClickListener);
        findViewById(R.id.fab_chat).setOnClickListener(onClickListener);
        findViewById(R.id.go_to_mypage).setOnClickListener(onClickListener);
        findViewById(R.id.filter).setOnClickListener(onClickListener);

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

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @SuppressLint("LongLogTag")
        @Override
        public void onClick(View v) {
            switch(v.getId()){
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

                case R.id.fab_post:
                    if(firebaseUser==null){
                        caution();
                    }
                    else if(!locCertification){
                        AlertDialog.Builder builder=new AlertDialog.Builder(context);
                        builder.setTitle("지역 인증이 필요한 작업입니다.");
                        builder.setMessage("글 쓰기를 하시려면 지역 인증을 해야합니다.");
                        builder.setPositiveButton("지역인증 하기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO 지역인증 페이지로 넘기기
                                mainLocation=nowLocation.getText().toString();
                                startActivity(new Intent(context,RegionActivity.class));
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

                case R.id.filter:
                    Intent intent3=new Intent(context,FilterActivity.class);
                    Log.d("필터로 이동","필터로 이동");
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

}