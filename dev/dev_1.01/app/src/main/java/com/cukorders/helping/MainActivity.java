package com.cukorders.helping;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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


        //위가 recyclerview 아래가 widget

        fab_plus=(FloatingActionButton) findViewById(R.id.fab_plus);
        fab_write=(FloatingActionButton) findViewById(R.id.fab_post);
        fab_info=(FloatingActionButton) findViewById(R.id.fab_info);
        fab_chat=(FloatingActionButton) findViewById(R.id.fab_chat);
        FabOpen= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabClockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        FabAntiClockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);

        myMission=(Button) findViewById(R.id.myMission);
        currentMission=(Button) findViewById(R.id.currentMission);
        findViewById(R.id.myMission).setOnClickListener(onClickListener);
        findViewById(R.id.currentMission).setOnClickListener(onClickListener);
        findViewById(R.id.fab_post).setOnClickListener(onClickListener);
        findViewById(R.id.fab_info).setOnClickListener(onClickListener);
        findViewById(R.id.fab_chat).setOnClickListener(onClickListener);

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
                    Intent intent1 =new Intent(context,PostActivity.class);
                    Log.e("go to post","go to a posting page");
                    startActivity(intent1);
                    break;

                case R.id.fab_info:
                    Intent intent2=new Intent(context,MyPageActivity.class);
                    startActivity(intent2);
                    break;

                case R.id.fab_chat:

                    break;

            }
        }
    };

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