package com.cukorders.helping;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.VISIBLE;

public class PostContentsView_requestActivity extends AppCompatActivity {
    private static final String TAG = "PostContentView_request";
    private String postUid;
    private String postKey="";
    private ArrayList<InitPost> mPost;
    private boolean flag=false;

    private ScrollView scrollView;
    private ImageButton backBtn,chatting;
    private ImageView viewImg;
    private CircleImageView postUserImg;
    private TextView reward,deposit,price_post,end_time,cancel_av_time,place_post,post_title,post_content,big_post_title;
    private TextView userUid;
    private LinearLayout postUser;
    private Button chatgobtn;

    private Button missionCancelbtn;
    private ImageButton mylike;
    private String countLikes;

    private DatabaseReference postRef;
    private DatabaseReference mRef;
    private DatabaseReference mimgPostRef;
    private DatabaseReference userforPostRef;
    private String clientUid="";

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String mUid="";

    private String currentTime;
    private Date currentDate;
    private SimpleDateFormat sdf;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_mission_page);

        //currentTime= Calendar.getInstance().getTime().toString();
        sdf = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault());
        currentTime = sdf.format(new Date());
        //currentDate = sdf.format(new Date());

        big_post_title = findViewById(R.id.State_of_Post);
        big_post_title.setText("의뢰중인 미션");
        backBtn = findViewById(R.id.bt_back);
        viewImg = findViewById(R.id.postImages);
        postUserImg = findViewById(R.id.userProfileImage_post);
        reward = findViewById(R.id.reward_money_view);
        deposit = findViewById(R.id.deposit_money_view);
        price_post = findViewById(R.id.price_of_product_view);
        end_time = findViewById(R.id.mission_ended_time);
        cancel_av_time=findViewById(R.id.cancel_available_time);
        place_post = findViewById(R.id.meeting_place);
        post_title = findViewById(R.id.postTitle);
        post_content = findViewById(R.id.postContent);
        userUid = findViewById(R.id.post_written_nick);
        postUser = findViewById(R.id.layout_post_user);

        chatgobtn = findViewById(R.id.chatting_go_btn);
        chatting = findViewById(R.id.chat_icon);
        missionCancelbtn = findViewById(R.id.cancel_missison_btn);
        mylike = findViewById(R.id.mylikes);
        mylike.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mUid = mCurrentUser.getUid();

        mRef = FirebaseDatabase.getInstance().getReference();

        Log.d(TAG,"onCreate:started");
        getIncomingIntent();
        displayPost();
        displayuserPost();

        postUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToprofile();
            }
        });


    }
    private void getIncomingIntent(){
        Log.d(TAG,"getIncomingIntent : check for incoming intents. now Starting");
        Bundle bundleObject = getIntent().getExtras();
        if(!bundleObject.getString("post").isEmpty()){
            postUid=bundleObject.getString("post");

            Log.d(TAG,"getIncomingIntent : check for incoming postUid."+postUid);
        }
        if(!bundleObject.getString("useruid").isEmpty()){
            clientUid=bundleObject.getString("useruid");
            Log.d(TAG,"getIncomingIntent : check for incoming clientUid."+clientUid);
        }
        postRef = mRef.child("Posting").child(postUid);
    }

    private void displayPost() {
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //getdata
                String title = dataSnapshot.child("title").getValue().toString();
                String image = dataSnapshot.child("image1").getValue().toString();
                //미션 완료 시간
                String endtime = dataSnapshot.child("endTime").getValue().toString();
                //취소 가능시간
                String canceltime = dataSnapshot.child("cancelTime").getValue().toString();
                //보상 금액
                String pay = dataSnapshot.child("pay").getValue().toString();
                //보증금
                String due = dataSnapshot.child("due").getValue().toString();
                //물품 예상 금액
                String price = dataSnapshot.child("price").getValue().toString();
                //장소
                String place = dataSnapshot.child("place").getValue().toString();
                //글 설명
                String content = Objects.requireNonNull(dataSnapshot.child("description").getValue()).toString();
                //매칭 여부
                String ismatched = dataSnapshot.child("isMatched").getValue().toString();


                //display
                //글 관련 사진
                Picasso.get().load(image).into(viewImg);
                //글 제목
                post_title.setText(title);
                //글 내용
                post_content.setText(content);
                //보상금
                reward.setText("보상금: " + pay);
                //보증금
                deposit.setText("보증금: " + due);
                //물품 예상 금액
                price_post.setText("물품 가격: " + price);
                //종료 시간, 미션 종료 시간
                end_time.setText("종료 시간: " + endtime);
                //취소 가능 시간
                cancel_av_time.setText("취소 가능 시간: " + canceltime);
                //장소
                place_post.setText("장소: " + place);

                // 사진 누르면 여러개 확인하기

                //우선 매칭이 된 경우
                if(!ismatched.equals("default")){
                        //의뢰인의 취소는 현재 매칭된 헬퍼의 채팅방으로 가서 진행
                        goToClientchat(ismatched);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void displayuserPost(){
        // 사용자 정보 노출되는 부분
        userforPostRef = mRef.child("Users").child(clientUid);
        userforPostRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nick = dataSnapshot.child("Nickname").getValue().toString();
                String score = dataSnapshot.child("Score").getValue().toString();
                String postuserimg = dataSnapshot.child("Image").getValue().toString();
                userUid.setText(nick);
                Picasso.get().load(postuserimg).into(postUserImg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void goToprofile(){
        //Todo 사용자 프로필 누르면 해당 사용자 정보 보는 페이지로 이동 , 수정이 아닌 그저 보는 느낌
        Intent goToProfileintent = new Intent(PostContentsView_requestActivity.this, ProfileActivity.class);
        Log.d(TAG, "check what is useruid_string" + clientUid);
        goToProfileintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goToProfileintent);
    }

    private void viewImg(){
        //postUid
        //Todo img누르면 여러개 이미지 한꺼번에 보이기

    }


    private void goToClientchat(String helperuid){
        //취소하기를 눌렀을 경우, 바로 채팅창으로 이동해야한다.
        //Todo chat버튼 누르면 화면으로 이동하기
        //채팅방에는 상대방의 uid가 들어가 있어야 한다.
        //채팅방 리스트를 보여주는 화면에는 헬퍼의 아이디와
        /*
        Intent goClientChat = new Intent(PostContentsViewActivity.this, );
        Bundle bundle = new Bundle();
        bundle.putSerializable("postUid",postUid);
        bundle.putSerializable("helperuid",helperuid);
        Log.d(TAG, "send postUid to chatting page : " + postUid);
        Log.d(TAG, "send chatUid to chatting page : " + helperUid);
        gohelperChat.putExtras(bundle);
        startActivity(goClientChat);
         */
    }

    private void goTochatList(){
        //만약 의뢰인이라면
        //채팅방 리스트를 보여주는 화면으로 이동하면 된다.
        //채팅방 리스트를 보여주는 화면에는 헬퍼의 아이디와
        /*
        Intent gochatList = new Intent(PostContentsViewActivity.this, );
        Bundle bundle = new Bundle();
        bundle.putSerializable("postUid",postUid);
        Log.d(TAG, "send postUid to chatlist" + postUid);
        gochatList.putExtras(bundle);
        startActivity(gochatList);
         */
    }

}
