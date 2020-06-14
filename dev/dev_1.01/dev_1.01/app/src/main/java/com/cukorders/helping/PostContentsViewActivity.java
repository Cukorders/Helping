package com.cukorders.helping;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.VISIBLE;
import static java.util.TimeZone.getDefault;

public class PostContentsViewActivity extends AppCompatActivity {
    private static final String TAG = "PostContentViewActivity";
    private String postUid;
    private String postKey="";
    private ArrayList<InitPost> mPost;
    private boolean flag;

    private ScrollView scrollView;
    private ImageButton backBtn,chatting;
    private ImageView viewImg;
    private CircleImageView postUserImg;
    private TextView reward,deposit,price_post,end_time,cancel_av_time,place_post,post_title,post_content,content_msg;
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

    private String chkisMatched="";
    private boolean chkhelperchatMade=false;
    private boolean chkmyPosition=false;
    private boolean chkimgloaded=false;

    //chkmyPosition이 true명 헬퍼임

    //img
    private ArrayList<String> mImageUrls = new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_mission_page);

        //currentTime= Calendar.getInstance().getTime().toString();
        sdf = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault());
        currentTime = sdf.format(new Date());
        //currentDate = sdf.format(new Date());

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

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        if(mCurrentUser != null) {
            mUid = mCurrentUser.getUid(); //Do what you need to do with the id
        } else{
            mUid = "qAT39HFIVmg4RCq3Vmpk2GdM4ra2";
        }

        mRef = FirebaseDatabase.getInstance().getReference();
        //post 내용 보여지는 부분
        getIncomingIntent();
        displayPost();
        displayuserPost();

        //likes 보여지는 부분
        //언데이트 되는 속도와 다른듯함....
        displayLikes();
        mylike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTochklikes();
            }
        });
        updatelikesCount();

        Log.d(TAG,"onCreate:started");

        postUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToprofile();
            }
        });

        chatgobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clientUid.equals(mUid)){
                    //의뢰인이다.
                    Log.d(TAG,"I am client");
                    goTochatList();
                }else{
                    Log.d(TAG,"I am helper");
                    chkmyPosition=true;
                    chatCheck_helper();
                }
            }
        });

        // imageView 보여지기
        viewImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chkimgloaded){
                    Log.d(TAG,"get Image1 from mImageUrls : "+mImageUrls.get(0));
                    Log.d(TAG,"get Image2 from mImageUrls : "+mImageUrls.get(1));
                    Log.d(TAG,"get Image3 from mImageUrls : "+mImageUrls.get(2));

                    Intent showImgIntent = new Intent(PostContentsViewActivity.this,PostImgView.class);
                    //bundle.putParcelableArrayList(mImageUrls);
                    //bundle.putSerializable("ImgLists",mImageUrls);
                    showImgIntent.putStringArrayListExtra("lists",mImageUrls);
                    startActivity(showImgIntent);
                }else{
                    Toast.makeText(PostContentsViewActivity.this, "이미지를 로딩중입니다.", Toast.LENGTH_SHORT).show();
                }
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
            Log.d(TAG,"getIncomingIntent : check for incoming uid."+clientUid);
        }
        if(!bundleObject.getString("ismatched").isEmpty()){
            chkisMatched=bundleObject.getString("ismatched");
            Log.d(TAG,"getIncomingIntent : check for incoming ismatched."+chkisMatched);
        }
        postRef = mRef.child("Posting").child(postUid);
        content_msg = findViewById(R.id.show_post_state_msg);
        if(chkmyPosition){
            if(!chkisMatched.equals(mUid)) {
                //다른 헬퍼와 이 포스팅이 연결된 상태임
                content_msg.setVisibility(VISIBLE);
            }
            //만약에 sended가 1이고, 나한테 보낸거라면
            //content_msg에 수락요청을 받으세요! 라고 보이기
        }
    }

    private void displayPost() {
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //getdata
                String title = dataSnapshot.child("title").getValue().toString();
                String image1 = dataSnapshot.child("image1").getValue().toString();
                String image2 = dataSnapshot.child("image2").getValue().toString();
                String image3 = dataSnapshot.child("image3").getValue().toString();
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
                Picasso.get().load(image1).into(viewImg);
                mImageUrls.add(image1);
                mImageUrls.add(image2);
                mImageUrls.add(image3);
                //Toast.makeText(PostContentsViewActivity.this, "image1 is : this: "+image1, Toast.LENGTH_SHORT).show();
                //Toast.makeText(PostContentsViewActivity.this, "image2 is : this: "+image2, Toast.LENGTH_SHORT).show();
                //Toast.makeText(PostContentsViewActivity.this, "image3 is : this: "+image3, Toast.LENGTH_SHORT).show();
                chkimgloaded=true;

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

                //취소
                //우선 매칭이 된 경우
                if(!ismatched.equals("default")){
                    //헬퍼의 취소는 채팅방 안에서
                    if(chkmyPosition){
                        //헬퍼라면
                        // 사용자 매칭 여부에 따라서 매칭이라면
                        if(ismatched.equals(mUid)){
                            //그리고 만약 사용자가 cancel 가능한 시간 내에 있다면 버튼이 보여지기
                            Toast.makeText(PostContentsViewActivity.this, "Current Time is : "+currentTime, Toast.LENGTH_SHORT).show();
                            boolean chktime=false;
                            try {
                                Date strCancelDate = sdf.parse(canceltime);
                                if(new Date().before(strCancelDate)){
                                    chktime=true;
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if(chktime){
                                missionCancelbtn.setVisibility(VISIBLE);
                                //채팅방으로 이동하도록 해서 취소하도록 하기
                                missionCancelbtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(PostContentsViewActivity.this, "취소할 수 있도록 채팅방으로 이동합니다.", Toast.LENGTH_SHORT).show();
                                        chatCheck_helper();
                                    }
                                });

                            }
                        }
                    }else {
                        //의뢰인의 취소는 현재 매칭된 헬퍼의 채팅방으로 가서 진행
                        goToClientchat(ismatched);
                    }
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
        Log.d(TAG, "chk clientUid" + clientUid);
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
    private void displayLikes(){
        final DatabaseReference chkmyLikes = FirebaseDatabase.getInstance().getReference().child("Postlikes");
        Query query = chkmyLikes.child(mUid);
        query.orderByChild(postUid).equalTo(postUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "check what is in dataSnapshot:" + dataSnapshot);
                if(dataSnapshot.exists()){
                    mylike.setImageResource(R.drawable.heart_icon);
                    flag=true;
                }else{
                    mylike.setImageResource(R.drawable.heart_icon2);
                    flag=false;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void goTochklikes() {
        Log.d(TAG, "get mUid :" + mUid);
        Log.d(TAG, "get mPostUid :" + postUid);
        Log.d(TAG, "check what is in flag:" + flag);
        DatabaseReference chkmyLikes = FirebaseDatabase.getInstance().getReference().child("Postlikes").child(mUid);
        if (flag == true) {
            //존재한다면
            mylike.setImageResource(R.drawable.heart_icon2);
            Log.d(TAG, "already it's in your likes:" + postUid);
            Log.d(TAG, "Now deleting" + postUid);
            chkmyLikes.child(postUid).removeValue();
            Toast.makeText(PostContentsViewActivity.this, "관심목록에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            flag = false;
        } else {
            mylike.setImageResource(R.drawable.heart_icon);
            chkmyLikes.child(postUid).setValue(postUid).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "I put it in your likes :" + postUid);
                    }
                }
            });
            Toast.makeText(PostContentsViewActivity.this, "관심목록에 추가되었습니다.", Toast.LENGTH_SHORT).show();
            flag = true;
        }
        Log.d(TAG, "check what is in flag:" + flag);
    }

    private void updatelikesCount(){
        //likes의 카운트를 증가시킨다
        final long[] count = {0};
        Query query = FirebaseDatabase.getInstance().getReference("Postlikes");
        query.orderByChild(postUid).equalTo(postUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    count[0] = dataSnapshot.getChildrenCount();
                    Log.d(TAG, "check what is count:" + dataSnapshot.getChildrenCount());
                    Log.d(TAG, "check what is long count:" + count[0]);
                    postRef.child("likes").setValue(Long.toString(count[0]));
                    //countLikes = Long.toString(count);
                }else {
                    Log.d(TAG, "check what is count:" + dataSnapshot.getChildrenCount());
                    Log.d(TAG, "check what is long count:" + count[0]);
                    postRef.child("likes").setValue("0");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        Log.d(TAG, "check what is in likes inside likes:" + countLikes);
    }

    private void goToprofile(){
        //Todo 사용자 프로필 누르면 해당 사용자 정보 보는 페이지로 이동 , 수정이 아닌 그저 보는 느낌
        Intent goToProfileintent = new Intent(PostContentsViewActivity.this, ProfileViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("clientUid",clientUid);
        Log.d(TAG, "check what is Clientuid_string" + clientUid);
        goToProfileintent.putExtras(bundle);
        goToProfileintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goToProfileintent);
    }


    private void chatCheck_helper(){
        final DatabaseReference getChat = FirebaseDatabase.getInstance().getReference().child("chat_list_helper").child(mUid);
        Log.d(TAG, "check if helper has sent message before");
        Query query = getChat;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child(postUid).exists()){
                    Log.d(TAG, "user has never sent message to client");
                    goTohelperchat("");
                }else{
                    Log.d(TAG, "user has sent message to client before");
                    String chat_number = dataSnapshot.child(postUid).getValue().toString();
                    goTohelperchat(chat_number);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void goTohelperchat(String chatUid){
        //Todo chat버튼 누르면 화면으로 이동하기
        //만약 헬퍼라면
        //그냥 채팅방 화면으로 넘겨주되, 넘어가야 될 내용은 chatting방 uid
        /*
        Intent gohelperChat = new Intent(PostContentsViewActivity.this, );
        Bundle bundle = new Bundle();
        bundle.putSerializable("postUid",postUid);
        bundle.putSerializable("chatUid",chatUid);
        Log.d(TAG, "send postUid to chatting page : " + postUid);
        Log.d(TAG, "send chatUid to chatting page : " + chatUid);
        gohelperChat.putExtras(bundle);
        startActivity(gohelperChat);
         */
    }

    private void goToClientchat(String helperuid){
        //취소하기를 눌렀을 경우, 바로 채팅창으로 이동해야한다.!!!

        //Todo chat버튼 누르면 화면으로 이동하기
        //채팅방에는 상대방의 uid가 들어가 있어야 한다.
        //채팅방에는 현재 상대방의 정보가 들어가게 되고, orderbyChild(헬퍼 UID)를 통해서 채팅방 고유번호를 찾기
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
