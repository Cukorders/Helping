package com.cukorders.helping.chatting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cukorders.helping.R;
import com.cukorders.helping.chatting_popup.PopupActivity;
import com.cukorders.helping.chatting_popup.PopupActivity10;
import com.cukorders.helping.chatting_popup.PopupActivity20;
import com.cukorders.helping.chatting_popup.PopupActivity3;
import com.cukorders.helping.chatting_popup.PopupActivity30;
import com.cukorders.helping.chatting_popup.PopupActivity4;
import com.cukorders.helping.chatting_popup.PopupActivity40;
import com.cukorders.helping.chatting_popup.PopupActivity5;
import com.cukorders.helping.chatting_popup.PopupActivity6;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.VISIBLE;

public class ChattingActivity extends AppCompatActivity {

    private static final String TAG ="ChattingActivity" ;
    private String destinationUid;
    private Button button;
    private EditText editText;

    private String uid= "TIhMFvxLG9awVpVPN931vwXDUXz2";
    private String chatRoomUid;
    private String postUid;

    private RecyclerView recyclerView;

    private TextView post_title;
    private String userProfileNick;
    private CircleImageView post_image;
    private String userProfileImage;
    private ImageView face;
    private ProgressBar pb;
    private Button accept;
    private Button decline;
    private String key;

    private String postKey;

    private DatabaseReference mUserDatabase1, mUserDatabase2, mUserDatabase3;
    private DatabaseReference mRef;
    private DatabaseReference postRef;
    private DatabaseReference profileRef;

    private boolean ifnewChat=false;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" yyyy.MM.dd HH:mm");
    private UserModel destinationUserModel;

    // 거래 관련 버튼 변수 선언

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private DatabaseReference mPost;
    private DatabaseReference mChats;
    private DatabaseReference mUsers;
    private int money;
    private DatabaseReference Center;

    private DatabaseReference MoneyRef;
    private String Money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
/*
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // 채팅을 요구하는 아이디. 즉 단말기에 로그인된 UID

*/
       /* getIncomingIntent();*/
        Log.d(TAG,"see PostUid: "+postUid);
        Log.d(TAG,"see chatRoomUid: "+chatRoomUid);

        button = (Button)findViewById(R.id.chattingActivity_button);
        editText = (EditText)findViewById(R.id.chattingActivity_editText);

        post_title = (TextView) findViewById(R.id.chattingActivity_post_title);
        post_image = (CircleImageView)findViewById(R.id.chattingActivity_post_image);
        face = (ImageView)findViewById(R.id.chattingActivity_face);
        pb = (ProgressBar)findViewById(R.id.chattingActivity_pb);
        accept = (Button)findViewById(R.id.chattingActivity_accept);
        decline = (Button)findViewById(R.id.chattingActivity_decline);
        recyclerView = (RecyclerView)findViewById(R.id.chattingActivity_recyclerview);

        mRef = FirebaseDatabase.getInstance().getReference();
        postRef = mRef.child("Posting");

        postRef.child(postUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.child("title").getValue().toString();
                String client = dataSnapshot.child("uid").getValue().toString();
                post_title.setText(title);
                destinationUid = client;
                Log.d(TAG,"see destinationUid is in comming : "+destinationUid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

 /*       profileRef = mRef.child("Users").child(destinationUid);

        profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProfileImage = dataSnapshot.child("Image").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 버튼을 누르면 대화방이 만들어지거나(새로 대화할 경우) 대화가 추가되는 기능을 만든다
                ChatModel chatModel = new ChatModel();
                //대화방 데이터에 대화참여자들의 정보를 기재
                chatModel.users.put(uid, true);
                chatModel.users.put(destinationUid, true);
                //데이터베이스에 삽입

                mUserDatabase1 = FirebaseDatabase.getInstance().getReference().child("Chat_list");
                mUserDatabase2 = FirebaseDatabase.getInstance().getReference().child("Chat_list_client").child(destinationUid).child(postUid);
                mUserDatabase3 = FirebaseDatabase.getInstance().getReference().child("Chat_list_helper").child(uid);

                HashMap<String, Object> ChatsMap = new HashMap<>();
                HashMap<String, String> ClientChatsMap = new HashMap<>();
                HashMap<String, String> HelperChatsMap = new HashMap<>();

                ChatsMap.put("ClientUid",destinationUid);
                ChatsMap.put("HelperUid",uid);
                ChatsMap.put("recenttime",ServerValue.TIMESTAMP);
                ChatsMap.put("PostUid",postUid);

                // 데이터를 입력 받았다고 체크가 되는 순간에 db에 채팅방 올리는 방법

                if (chatRoomUid == null){
                    button.setEnabled(false); // chatroomUid가 null인지 확인하는 사이엔 버튼을 불활성화해놓는다. 버그를 막기 위해
                    key =  FirebaseDatabase.getInstance().getReference().child("chatrooms").push().getKey();
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(key).setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            checkChatRoom(); // 채팅방의 중복여부를 조사
                        }
                    });

                    ClientChatsMap.put(key,uid);
                    HelperChatsMap.put(postUid,key);

                }else {
                    ChatModel.Comment comment = new ChatModel.Comment();
                    comment.uid = uid;
                    comment.message = editText.getText().toString();
                    comment.timestamp = ServerValue.TIMESTAMP;
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                           /* sendGcm();
                           */ editText.setText(""); // 채팅치고 보낸 후에 채팅 치는 부분 초기화 시켜주는 부분
                        }
                    });

                    ClientChatsMap.put(chatRoomUid,uid);
                    HelperChatsMap.put(postUid,chatRoomUid);
                }

                mUserDatabase1.child(key).setValue(ChatsMap);
                mUserDatabase2.setValue(ClientChatsMap);
                mUserDatabase3.setValue(HelperChatsMap);
            }
        });
        checkChatRoom();
/*
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {*/
                // 수락 거절 관련 onclickListener

                Calendar calendar= Calendar.getInstance();
                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyyMddHHmmss");

                final String time= dateFormat.format(calendar.getTime());

                //db 테이블 불러오기
                mPost = FirebaseDatabase.getInstance().getReference().child("Posting");

                //mUsers=FirebaseDatabase.getInstance().getReference().child("Users");

                Center= FirebaseDatabase.getInstance().getReference().child("Center");

                mChats = FirebaseDatabase.getInstance().getReference().child("Chat_list");

                MoneyRef=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                MoneyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String Money_value = dataSnapshot.child("Money").getValue().toString();
                            if(Money_value.equals("default")){
                                money=10000;
                            }else{
                                money=Integer.parseInt(Money_value);
                            }
                            Log.d("Money","Money_value 값: "+Money_value);
                            Log.d("Money","money 값: "+money);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        /*

        Context context=this;
        Center center=new Center(mPost.toString(),mUid,"123",money,0); // 센터 DB 변수들을 class에 넣음
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference().child("Center").child(String.valueOf(mPost));
        HashMap<String,Object> update=(HashMap<String, Object>) center.toMap();
        Log.d("dbRef","dbRef의 값: "+dbRef);

        dbRef.setValue(update).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                  //  Toast.makeText(this,"센터 db가 성공적으로 업로드 되었습니다.",Toast.LENGTH_LONG).show();
                    Log.e("a post is uploaded", "a post is successfully uploaded");
                } else{
                 //   Toast.makeText(this,"오류가 발생하여 센터 db 업로드에 실패하였습니다. 잠시 후에 다시 시도해주십시오.",Toast.LENGTH_LONG).show();
                }
            }
        });
        */
        /*
        Log.d("money","Money의 값: "+Money);
        if(Money.equals("default")){
            money=10000;
        }else{
           // money=Integer.parseInt(Money);
        }
        Log.d("money","money의 값: "+money);

         */
        Log.e(TAG,"mChat의 값: "+mChats);
        Log.e(TAG,"mChat/chatRoomid의 값: "+mChats.child(chatRoomUid));
                mChats.child(chatRoomUid).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //if(dataSnapshot.exists()){
                        final String Clientuid = dataSnapshot.child("ClientUid").getValue().toString();
                        final String Helperuid = dataSnapshot.child("HelperUid").getValue().toString();
                        String postuid = dataSnapshot.child("PostUid").getValue().toString();


                        mPost.child(postuid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String isMatched=dataSnapshot.child("isMatched").getValue().toString();
                                String isSended=dataSnapshot.child("isSended").getValue().toString();
                                final String price=dataSnapshot.child("price").getValue().toString();
                                final String pay=dataSnapshot.child("pay").getValue().toString();
                                final String cancelTime=dataSnapshot.child("cancelTime").getValue().toString();
                                String isCancled=dataSnapshot.child("isCanceled").getValue().toString();



                                String due=dataSnapshot.child("due").getValue().toString();
                                String isFinished=dataSnapshot.child("isFinished").getValue().toString();




                                if (Clientuid.equals(uid)) {
                                    //내가 의뢰인이 맞을 경우 수락 버튼을 누를 수 있게 된다.

                                    if (!isMatched.equals(Helperuid)) {

                                        accept.setVisibility(VISIBLE);
                                        decline.setVisibility(VISIBLE);
                                        Button accept_button;

                                        Button.OnClickListener btnListener = new View.OnClickListener() {

                                            public void onClick(View v) {
                                                switch (v.getId()) {

                                                    case R.id.chattingActivity_accept:

                                                        if (money >= (Integer.parseInt(price) + Integer.parseInt(pay))) {

                                                            Intent intent = new Intent(ChattingActivity.this, PopupActivity.class);
                                                            startActivityForResult(intent, 1);

                                                        } else if (money < (Integer.parseInt(price) + Integer.parseInt(pay))) {

                                                            Intent intent = new Intent(ChattingActivity.this, PopupActivity3.class);
                                                            startActivityForResult(intent, 1);
                                                        }

                                                        break;

                                                }
                                            }
                                        };

                                        accept_button = (Button) findViewById(R.id.chattingActivity_accept);
                                        accept_button.setOnClickListener(btnListener);


                                    } else if (isMatched.equals(Helperuid)) {


                                        if (isSended.equals("2")) {

                                            accept.setText("거래완료");
                                            decline.setText("미션취소");

                                            if(isCancled.equals("1")){

                                                Intent intent = new Intent(ChattingActivity.this, PopupActivity40.class);
                                                startActivityForResult(intent, 1);

                                            }

                                            Button.OnClickListener btnListener = new View.OnClickListener() {

                                                public void onClick(View v) {
                                                    switch (v.getId()) {

                                                        case R.id.chattingActivity_accept:

                                                            Intent intent = new Intent(ChattingActivity.this, PopupActivity10.class);
                                                            startActivityForResult(intent, 1);

                                                            break;

                                                    }
                                                }
                                            };

                                            accept = (Button) findViewById(R.id.chattingActivity_accept);
                                            accept.setOnClickListener(btnListener);

                                            Button.OnClickListener btnListener2 = new View.OnClickListener() {

                                                public void onClick(View v) {
                                                    switch (v.getId()) {

                                                        case R.id.chattingActivity_decline:

                                                            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
                                                            Long cancel_time = Long.parseLong(simpleDateFormat.format(cancelTime));
                                                            Long cur_time = Long.parseLong(simpleDateFormat.format(time));

                                                            if (cancel_time > cur_time) {


                                                                Intent intent = new Intent(ChattingActivity.this, PopupActivity20.class);
                                                                startActivityForResult(intent, 1);

                                                            } else if (cancel_time < cur_time) {

                                                                Intent intent = new Intent(ChattingActivity.this, PopupActivity30.class);
                                                                startActivityForResult(intent, 1);

                                                            }
                                                            break;
                                                    }
                                                }
                                            };
                                            decline = (Button) findViewById(R.id.chattingActivity_decline);
                                            decline.setOnClickListener(btnListener2);
                                        }
                                    }
                                } else if (Helperuid.equals(uid)) {

                                    if(isFinished.equals("0")){

                                        accept.setVisibility(View.INVISIBLE);
                                        decline.setVisibility(View.INVISIBLE);

                                        if (isSended.equals("1")) {
                                            Intent intent = new Intent(ChattingActivity.this, PopupActivity.class);
                                            startActivityForResult(intent, 1);
                                        }
                                        if (isSended.equals("2")) {

                                            accept.setText("거래완료");
                                            decline.setText("미션취소");

                                            accept.setVisibility(View.INVISIBLE);
                                            decline.setVisibility(VISIBLE);

                                            if(isCancled.equals("1")){
                                                Intent intent = new Intent(ChattingActivity.this, PopupActivity40.class);
                                                startActivityForResult(intent, 1);
                                            }
                                        }

                                        Button.OnClickListener btnListener2 = new View.OnClickListener() {

                                            public void onClick(View v) {
                                                switch (v.getId()) {

                                                    case R.id.chattingActivity_decline:


                                                        Long cancel_time = Long.parseLong(cancelTime);
                                                        Long cur_time = Long.parseLong(time);

                                                        if (cancel_time > cur_time) {

                                                            Intent intent = new Intent(ChattingActivity.this, PopupActivity6.class);
                                                            startActivityForResult(intent, 1);

                                                        } else if (cancel_time < cur_time) {

                                                            Intent intent = new Intent(ChattingActivity.this, PopupActivity5.class);
                                                            startActivityForResult(intent, 1);

                                                        }
                                                        break;
                                                }
                                            }
                                        };


                                        decline = (Button) findViewById(R.id.chattingActivity_decline);
                                        decline.setOnClickListener(btnListener2);


                                    }else if(isFinished.equals("1")){
                                        Intent intent = new Intent(ChattingActivity.this, PopupActivity4.class);
                                        startActivityForResult(intent, 1);
                                    }

                                }else if(isFinished.equals("1")){

                                    Intent intent = new Intent(ChattingActivity.this, PopupActivity4.class);
                                    startActivityForResult(intent, 1);

                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }/*
        });
    }
*/
   /* private void getIncomingIntent(){
        Bundle bundleObject = getIntent().getExtras();
        if(!bundleObject.getString("postUid").isEmpty()){
            postUid=bundleObject.getString("postUid");
            Log.d(TAG,"see PostUid in getIncoming: "+postUid);
        }
      *//*  if(!bundleObject.getString("chatUid").isEmpty()){
            if(bundleObject.getString("chatUid").toString().equals("0")){
                //만들어진 채팅방이 없음
                //chatRoomUid에 0만 들어감
                chatRoomUid="";
                ifnewChat = false;
                Log.d(TAG,"see chatRoomUid in getIncoming: "+chatRoomUid);
            }else{
                //만들어진 채팅방이 있음
                chatRoomUid=bundleObject.getString("chatUid");
                Log.d(TAG,"see chatRoomUid in getIncoming: "+chatRoomUid);
                ifnewChat=true;
            }
        }*//*
    }
*/
   /* void sendGcm(){
        Gson gson = new Gson();

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.to = destinationUserModel.pushToken;
        notificationModel.notifacation.title = "보낸이 아이디";
        notificationModel.notifacation.text = editText.getText().toString();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"),gson.toJson(notificationModel));

        Request request = new Request.Builder()
                .header("Content-Type","application/json")
                .addHeader("Authorization","key=AAAArs07U3c:APA91bEUDG39kMs2yJyKzyluHIHruRA2XvfecbptE_0HHR_sJr04PBvblXRx9gCa4Wag39zGJT5hPvvN_twdMok-wBBosJyDFsTt4K7CFJ755AWgxQ8MzHGsYMdgNflmjteHWGUkDrii")
                .url("https://fcm.googleapis.com/fcm/send")
                .post(requestBody)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });
    }
*/
    void checkChatRoom(){
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 for(DataSnapshot item : dataSnapshot.getChildren()){
                     ChatModel chatModel = item.getValue(ChatModel.class); // 아이디가 존재하는지
                     if(chatModel.users.containsKey(destinationUid)){ //내가 요구하는 아이디가 존재하는지 확인
                         chatRoomUid = item.getKey(); // 채팅방 Uid, 내가 원하는 코멘트를 해당하는 방에 넣어놓기 위함
                         button.setEnabled(true);
                         recyclerView.setLayoutManager(new LinearLayoutManager(ChattingActivity.this));
                         recyclerView.setAdapter(new RecyclerViewAdapter());
                     }
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        List<ChatModel.Comment> comments;
        public RecyclerViewAdapter() {
            comments = new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child("Users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() { // 유저에 대한 정보 가져오기
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    comments.clear(); // 항상 list는 선언한 뒤에 본격적으로 쓰기 전에 clear.
                    userProfileNick = dataSnapshot.child("Nickname").getValue().toString();
                    userProfileImage = dataSnapshot.child("Image").getValue().toString();
                    destinationUserModel = dataSnapshot.getValue(UserModel.class);
                    getMessageList();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            }); // chat rooms 테이블을 chatrooms table 안의 users 변수 안의 uid 값에 따라 정렬.
        }

        void getMessageList(){ // messasge 리스트를 받아오는 애
            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    comments.clear(); // 데이터가 추가될때마다 서버에서 모든 채팅에 대한 대화내용을 다 보내주기 때문에 쌓이기 전에 clear를 해줘야 한다.

                    for(DataSnapshot item : dataSnapshot.getChildren()){
                        comments.add(item.getValue(ChatModel.Comment.class));
                    }

                    notifyDataSetChanged(); // 서버에서 메세지 새로 갱신

                    // 채팅 맨 하단으로 이동
                    recyclerView.scrollToPosition(comments.size()-1);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);

            return new MessageViewHolder(view); // view를 재사용할때 쓰는 홀더
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            MessageViewHolder messageViewHolder = ((MessageViewHolder)holder);

            // 내가 보낸 매세지
            if(comments.get(position).uid.equals(uid)){
                messageViewHolder.textView_message.setText(comments.get(position).message);
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.my_message);
                messageViewHolder.linearLayout_destination.setVisibility(View.INVISIBLE);
                messageViewHolder.textView_message.setTextSize(18);
                messageViewHolder.linearLayout_main.setGravity(Gravity.RIGHT);
                // 상대방이 보낸 메세지
            }else {
                Glide.with(holder.itemView.getContext())
                        .load(userProfileImage)
                        .apply(new RequestOptions().circleCrop())
                        .into(messageViewHolder.imageView_profile);
                messageViewHolder.textview_name.setText(userProfileNick);
                messageViewHolder.linearLayout_destination.setVisibility(View.VISIBLE);
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.their_message);
                messageViewHolder.textView_message.setText(comments.get(position).message);
                messageViewHolder.textView_message.setTextSize(18);
                messageViewHolder.linearLayout_main.setGravity(Gravity.LEFT);
            }
            // database에 (현재시각 - 1970.01.01) 로 계산되는 시간값 제대로 나오게 설정
            long unixTime = (long) comments.get(position).timestamp;
            Date date = new Date(unixTime);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            String time = simpleDateFormat.format(date);
            messageViewHolder.textView_timestamp.setText(time);
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        // Binding 과정
        private class MessageViewHolder extends RecyclerView.ViewHolder {
            public TextView textView_message;
            public TextView textview_name;
            public ImageView imageView_profile;
            public LinearLayout linearLayout_destination;
            public LinearLayout linearLayout_main;
            public TextView textView_timestamp;

            public MessageViewHolder(View view) {
                super(view);
                textView_message = (TextView) view.findViewById(R.id.messageItem_textView_message);
                textview_name = (TextView) view.findViewById(R.id.messageItem_textview_name);
                imageView_profile = (ImageView) view.findViewById(R.id.messageItem_imageview_profile);
                linearLayout_destination = (LinearLayout) view.findViewById(R.id.messageItem_linearlayout_destination);
                linearLayout_main = (LinearLayout) view.findViewById(R.id.messageItem_linearlayout_main);
                textView_timestamp = (TextView) view.findViewById(R.id.messageItem_textView_timestamp);
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fromleft,R.anim.toright);
    }
}
