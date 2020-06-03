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
import android.widget.Toast;
import android.widget.Toolbar;
import androidx.annotation.NonNull;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab_plus,fab_write,fab_info,fab_chat;
    Animation FabOpen,FabClose,FabClockwise,FabAntiClockwise;
    private Button myMission,currentMission;
    boolean isOpen=false;
    private final Context context=this;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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