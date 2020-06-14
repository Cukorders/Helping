package com.cukorders.helping.chatting_popup;

import android.os.Bundle;
import android.util.Log;
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


public class PopupActivity20 extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String mUid;

    private String chatUid;
    private String postUid;
    private DatabaseReference mPost;
    private DatabaseReference mChats;

    private int money;
    private String centerKey;

    private DatabaseReference MoneyRef;
    private String Money;


    Button okBtn, cancleBtn;
    TextView message, message2;
    LinearLayout mlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 상태바 제거 ( 전체화면 모드 )
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_popup);

        message = findViewById(R.id.txtText);
        message2 = findViewById(R.id.txtText2);
        mlayout = findViewById(R.id.notification_icon);

        okBtn = (Button) findViewById(R.id.okBtn);
        cancleBtn = findViewById(R.id.cancleBtn);

        //mCurrentUser = mAuth.getCurrentUser();
        //mUid = mCurrentUser.getUid();

        mUid="TIhMFvxLG9awVpVPN931vwXDUXz2";

        chatUid = "-M9nHD-eVIOkPrdq9IXKi";
        centerKey="TffvAWpB%STR+^1B1e*vyx86!z=tV+F0wtnjNyZs8JF2UL04%y";

        //db 테이블 불러오기
        mPost = FirebaseDatabase.getInstance().getReference().child("Posting");
        mChats = FirebaseDatabase.getInstance().getReference().child("Chat_list").child(chatUid);

        MoneyRef= FirebaseDatabase.getInstance().getReference().child("Users").child(mUid);
        MoneyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String Money_value = dataSnapshot.child("Money").getValue().toString();
                    if (Money_value.equals("default")) {
                        money = 10000;
                    } else {
                        money = Integer.parseInt(Money_value);
                    }
                    Log.d("Money", "Money_value 값: " + Money_value);
                    Log.d("Money", "money 값: " + money);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mChats.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Clientuid = dataSnapshot.child("ClientUid").getValue().toString();
                final String Helperuid = dataSnapshot.child("HelperUid").getValue().toString();
                final String postuid = dataSnapshot.child("PostUid").getValue().toString();

                mPost.child(postuid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String isMatched = dataSnapshot.child("isMatched").getValue().toString();
                        final String isSended = dataSnapshot.child("isSended").getValue().toString();
                        final String price = dataSnapshot.child("price").getValue().toString();
                        final String due = dataSnapshot.child("due").getValue().toString();

                        //////

                        Button.OnClickListener btnListener = new View.OnClickListener() {

                            public void onClick(View v) {
                                switch (v.getId()) {
                                    case R.id.okBtn:

                                        cancleBtn.setVisibility(View.INVISIBLE);

                                        message.setText("거래가 취소되었습니다.");
                                        message2.setText("물품금과 보상금 환불 완료.");

                                        // Intent intent = new Intent(PopupActivity.this, PopupActivity2.class);
                                        //startActivityForResult(intent, 1);


                                        mPost.child(postuid).child("isCancled").setValue("1").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });


                                        Button.OnClickListener btnListener2 = new View.OnClickListener() {

                                            public void onClick(View v) {
                                                switch (v.getId()) {
                                                    case R.id.okBtn:

                                                        finish();


                                                }
                                            }
                                        };


                                        okBtn = (Button) findViewById(R.id.okBtn);
                                        //cancleBtn = (Button) findViewById(R.id.cancleBtn);
                                        okBtn.setOnClickListener(btnListener2);


                                        break;

                                }
                            }
                        };

                        okBtn = (Button) findViewById(R.id.okBtn);
                        okBtn.setOnClickListener(btnListener);





                        //





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
    public void mOk (View v){

        finish();
    }

    //취소 버튼 클릭
    public void mCancle (View v){
        finish();
    }

    @Override
    public boolean onTouchEvent (MotionEvent event){
        //바깥레이어 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed () {
        //안드로이드 백버튼 막기
        return;
    }

}
