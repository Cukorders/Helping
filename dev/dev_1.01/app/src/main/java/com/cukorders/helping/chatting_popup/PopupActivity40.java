package com.cukorders.helping.chatting_popup;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cukorders.helping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PopupActivity40 extends AppCompatActivity {



    private Button accept_button,decline_button;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String mUid;

    private String chatUid;
    private String postUid;
    private DatabaseReference mPost;
    private DatabaseReference mChats;

    Button okBtn, cancleBtn;
    TextView message;
    LinearLayout mlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 상태바 제거 ( 전체화면 모드 )
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_popup40);

        message = findViewById(R.id.txtText);
        mlayout = findViewById(R.id.notification_icon);

        okBtn = (Button) findViewById(R.id.okBtn);
        cancleBtn = (Button) findViewById(R.id.cancleBtn);

        //내 로그인한 uid담기
        // mCurrentUser = mAuth.getCurrentUser();
        // mUid = mCurrentUser.getUid();
        mUid = "TIhMFvxLG9awVpVPN931vwXDUXz2";

        chatUid = "-M9nHD-eVIOkPrdq9IXK";
        //db 테이블 불러오기
        mPost = FirebaseDatabase.getInstance().getReference().child("Posting");

        mChats = FirebaseDatabase.getInstance().getReference().child("Chat_list").child(chatUid);

        mChats.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String Clientuid = dataSnapshot.child("ClientUid").getValue().toString();
                final String Helperuid = dataSnapshot.child("HelperUid").getValue().toString();
                final String postuid = dataSnapshot.child("PostUid").getValue().toString();


                mPost.child(postuid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String isMatched=dataSnapshot.child("isMatched").getValue().toString();
                        String isSended=dataSnapshot.child("isSended").getValue().toString();
                        String isCancled=dataSnapshot.child("isCanceled").getValue().toString();

                        if(isCancled.equals("1")) {

                            message.setText("상대방이 거래를 취소햇습니다.");


                            mPost.child(postuid).child("isSended").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });

                            mPost.child(postuid).child("isFinished").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });



                            Button.OnClickListener btnListener = new View.OnClickListener() {

                                public void onClick(View v) {
                                    switch (v.getId()) {

                                        case R.id.okBtn:



                                            finish();



                                            //Intent intent = new Intent(PopupActivity2.this, Chat2Activity.class);
                                            //startActivityForResult(intent, 1);



                                            break;

                                    }
                                }
                            };

                            okBtn = (Button) findViewById(R.id.okBtn);
                            okBtn.setOnClickListener(btnListener);


                        }//if문 끝







//////

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





    }

















    //동작 버튼 클릭
    public void mOk(View v) {

        finish();
    }

    //취소 버튼 클릭
    public void mCancle(View v){
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

}