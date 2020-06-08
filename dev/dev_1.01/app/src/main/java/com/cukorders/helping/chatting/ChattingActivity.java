package com.cukorders.helping.chatting;

import android.os.Bundle;
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
import com.cukorders.helping.PostActivity;
import com.cukorders.helping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

public class ChattingActivity extends AppCompatActivity {

    private String destinationUid;
    private Button button;
    private EditText editText;

    private String uid;
    private String chatRoomUid;
    private String postUid = "eUcg_lhlRaRnnO@vhRVw9hkI-TxG6jy0D67REvFIOn9_dcdztZ";

    private RecyclerView recyclerView;

    private TextView userProfileNick;
    private ImageView userProfileImage;
    private ImageView face;
    private ProgressBar pb;
    private Button accept;
    private Button decline;

    private DatabaseReference mUserDatabase1, mUserDatabase2, mUserDatabase3;


    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" yyyy.MM.dd HH:mm");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // 채팅을 요구하는 아이디. 즉 단말기에 로그인된 UID[
        destinationUid = getIntent().getStringExtra("destinationUid"); // 채팅을 당하는 아이디
        button = (Button)findViewById(R.id.chattingActivity_button);
        editText = (EditText)findViewById(R.id.chattingActivity_editText);

        userProfileNick = (TextView) findViewById(R.id.chattingActivity_userProfileNick);
        userProfileImage = (ImageView)findViewById(R.id.chattingActivity_userProfileImage);
        face = (ImageView)findViewById(R.id.chattingActivity_face);
        pb = (ProgressBar)findViewById(R.id.chattingActivity_pb);
        accept = (Button)findViewById(R.id.chattingActivity_accept);
        decline = (Button)findViewById(R.id.chattingActivity_decline);

        recyclerView = (RecyclerView)findViewById(R.id.chattingActivity_recyclerview);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 버튼을 누르면 대화방이 만들어지거나(새로 대화할 경우) 대화가 추가되는 기능을 만든다
                ChatModel chatModel = new ChatModel();
                //대화방 데이터에 대화참여자들의 정보를 기재
                chatModel.users.put(uid, true);
                chatModel.users.put(destinationUid, true);
                //데이터베이스에 삽입

                mUserDatabase1 = FirebaseDatabase.getInstance().getReference().child("Chat_list").child(destinationUid).child(postUid);
                mUserDatabase2 = FirebaseDatabase.getInstance().getReference().child("Chat_list_client").child(destinationUid).child(postUid);
                mUserDatabase3 = FirebaseDatabase.getInstance().getReference().child("Chat_list_helper").child(destinationUid).child(postUid);

                HashMap<String, String> ChatsMap = new HashMap<>();
                HashMap<String, String> ClientChatsMap = new HashMap<>();
                HashMap<String, String> HelperChatsMap = new HashMap<>();
                PostActivity post = new PostActivity();

                ChatsMap.put("Client Uid",destinationUid);
                ChatsMap.put("Helper Uid",uid);
                ChatsMap.put("recent time","");
                ChatsMap.put("Post Uid",postUid);

                ClientChatsMap.put(chatRoomUid,uid);

                HelperChatsMap.put(postUid,chatRoomUid);

                mUserDatabase1.setValue(ChatsMap);
                mUserDatabase2.setValue(ClientChatsMap);
                mUserDatabase3.setValue(HelperChatsMap);

                // 데이터를 입력 받았다고 체크가 되는 순간에 db에 채팅방 올리는 방법

                if (chatRoomUid == null){
                    button.setEnabled(false); // chatroomUid가 null인지 확인하는 사이엔 버튼을 불활성화해놓는다. 버그를 막기 위해
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            checkChatRoom(); // 채팅방의 중복여부를 조사
                        }
                    });
                }else {
                    ChatModel.Comment comment = new ChatModel.Comment();
                    comment.uid = uid;
                    comment.message = editText.getText().toString();
                    comment.timestamp = ServerValue.TIMESTAMP;
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            editText.setText(""); // 채팅치고 보낸 후에 채팅 치는 부분 초기화 시켜주는 부분
                        }
                    });
                }
            }
        });
    }

    void checkChatRoom(){
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("Users/"+uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 for(DataSnapshot item : dataSnapshot.getChildren()){
                     ChatModel chatModel = item.getValue(ChatModel.class); // 아이디가 존재하는지
                     if(chatModel.users.containsKey(destinationUid)){ //내가 요구하는 아이디가 존재하는지 확인
                         chatRoomUid = item.getKey(); // 채팅방 아이디, 내가 원하는 코멘트를 해당하는 방에 넣어놓기 위함
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
        UserModel userModel;
        public RecyclerViewAdapter() {
            comments = new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child("Users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() { // 유저에 대한 정보 가져오기
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    comments.clear(); // 항상 list는 선언한 뒤에 본격적으로 쓰기 전에 clear.
                    userModel = dataSnapshot.getValue(UserModel.class);
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
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.rightbuuble);
                messageViewHolder.linearLayout_destination.setVisibility(View.INVISIBLE);
                messageViewHolder.textView_message.setTextSize(25);
                messageViewHolder.linearLayout_main.setGravity(Gravity.RIGHT);
                // 상대방이 보낸 메세지
            }else {
                Glide.with(holder.itemView.getContext())
                        .load(userModel.profileImageUrl)
                        .apply(new RequestOptions().circleCrop())
                        .into(messageViewHolder.imageView_profile);
                messageViewHolder.textview_name.setText(userModel.userName);
                messageViewHolder.linearLayout_destination.setVisibility(View.VISIBLE);
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.leftbubble);
                messageViewHolder.textView_message.setText(comments.get(position).message);
                messageViewHolder.textView_message.setTextSize(25);
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

    private String getRandomString(int length) {
        final String characters="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890!@#$%";
        StringBuilder stringBuilder=new StringBuilder();
        while(length-- >0){
            Random random=new Random();
            stringBuilder.append(characters.charAt(random.nextInt(characters.length())));
        }
        return stringBuilder.toString();
    }
}
