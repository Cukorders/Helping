package com.cukorders.helping.chatting_popup;

import android.content.Intent;
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
import com.cukorders.helping.chatting.ChattingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PopupActivity2 extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String mUid;

    private String chatUid;
    private String postUid;
    private DatabaseReference mPost;
    private DatabaseReference mChats;
    private int money,price,pay,due;
    private DatabaseReference Center;

    private DatabaseReference MoneyRef;
    private String Money,Price,Pay;


    Button okBtn, cancleBtn;
    TextView message;
    LinearLayout mlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 상태바 제거 ( 전체화면 모드 )
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_popup);

        message = findViewById(R.id.txtText);
        mlayout = findViewById(R.id.notification_icon);

        okBtn = (Button) findViewById(R.id.okBtn);
        cancleBtn = (Button) findViewById(R.id.cancleBtn);

        //mCurrentUser = mAuth.getCurrentUser();
        //mUid = mCurrentUser.getUid();

        //postUid=;
        mUid="TIhMFvxLG9awVpVPN931vwXDUXz2";


        chatUid = "-M9nHD-eVIOkPrdq9IXK";

        //db 테이블 불러오기
        mPost = FirebaseDatabase.getInstance().getReference().child("Posting");
        mChats = FirebaseDatabase.getInstance().getReference().child("Chat_list").child(chatUid);
        MoneyRef= FirebaseDatabase.getInstance().getReference().child("Users").child(mUid);
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

        mChats.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String Clientuid = dataSnapshot.child("ClientUid").getValue().toString();
                final String Helperuid = dataSnapshot.child("HelperUid").getValue().toString();
                final String postuid = dataSnapshot.child("PostUid").getValue().toString();

                mPost.child(postuid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String isMatched = dataSnapshot.child("isMatched").getValue().toString();
                        final String isSended = dataSnapshot.child("isSended").getValue().toString();
                        //Center.child()
for(;;){




    if (isSended.equals("2")) {

        message.setText("매칭완료!");

        Button.OnClickListener btnListener = new View.OnClickListener() {

            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.okBtn:

                        message.setText("헬퍼가 미션 수행을 완료하면, 우측 상단에 '미션완료'를 눌러야 보상이 헬퍼에게로 이동합니다");

                        //돈이동 시점
                        final DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference().child("Users").child(Clientuid);
                        mPost.addListenerForSingleValueEvent(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                              if(dataSnapshot.exists()){
                                  Price=dataSnapshot.child(postuid).child("price").getValue().toString();
                                  Pay=dataSnapshot.child(postuid).child("pay").getValue().toString();
                                  price=Integer.parseInt(Price);
                                  pay=Integer.parseInt(Pay);
                                  Log.d("클라이언트 price","클라이언트 price: "+price);
                                  Log.d("클라이언트 pay","클라이언트 pay: "+pay);
                              }
                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError databaseError) {

                          }
                      });


                        dbRef.child("Money").setValue((money-(pay+price))).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("Clients Money update","의뢰인의 돈이 업데이트 되었습니다.");
                            }
                        });

                        DatabaseReference mCenter= FirebaseDatabase.getInstance().getReference().child("Center");
                        String centerKey="TffvAWpB%STR+^1B1e*vyx86!z=tV+F0wtnjNyZs8JF2UL04%y";
                       // int centerMoney=Integer.parseInt(mCenter.child(centerKey).child("Money").toString());

                        DatabaseReference T_T= FirebaseDatabase.getInstance().getReference().child("Posting").child(postuid);

                        T_T.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    due=Integer.parseInt(dataSnapshot.child("due").getValue().toString());
                                    Log.d("due","posting의 due의 값: "+due);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        mCenter.child(centerKey).child("due").setValue(due).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                               // Log.d("Center DB","CenterDB - Money의 값: "+(Integer.parseInt(due)));
                            }
                        });

                        mCenter.child(centerKey).child("pay").setValue(pay).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // Log.d("Center DB","CenterDB - Money의 값: "+(Integer.parseInt(due)));
                            }
                        });

                        mCenter.child(centerKey).child("price").setValue(price).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // Log.d("Center DB","CenterDB - Money의 값: "+(Integer.parseInt(due)));
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



                        break;

                }
            }
        };

        okBtn = (Button) findViewById(R.id.okBtn);
        okBtn.setOnClickListener(btnListener);


    }
    else if(isSended.equals("3")) {

        message.setText("헬퍼가 수락을 하지 않아 다른 헬퍼를 알아보세요");

        mPost.child(postuid).child("isSended").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

        Button.OnClickListener btnListener = new View.OnClickListener() {

            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.okBtn:

                        Intent intent = new Intent(PopupActivity2.this, ChattingActivity.class);
                        startActivityForResult(intent, 1);


                        break;

                }
            }
        };

        okBtn = (Button) findViewById(R.id.okBtn);
        okBtn.setOnClickListener(btnListener);





    }
    break;



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
