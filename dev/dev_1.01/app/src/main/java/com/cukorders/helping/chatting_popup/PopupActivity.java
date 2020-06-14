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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.view.View.VISIBLE;


public class PopupActivity extends AppCompatActivity {


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

        mUid="TIhMFvxLG9awVpVPN931vwXDUXz2";

        chatUid = "-M9nHD-eVIOkPrdq9IXK";
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
                        final String price=dataSnapshot.child("price").getValue().toString();
                        final String due=dataSnapshot.child("due").getValue().toString();



                        if (isSended.equals("0")) {
                            message.setText("수락하시겠습니까?");
                            okBtn.setVisibility(VISIBLE);
                            cancleBtn.setVisibility(VISIBLE);




                            Button.OnClickListener btnListener = new View.OnClickListener() {

                                public void onClick(View v) {
                                    switch (v.getId()) {
                                        case R.id.okBtn:



                                            mPost.child(postuid).child("isSended").setValue("1").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });

                                            cancleBtn.setVisibility(View.INVISIBLE);
                                            //message.setText("미션을 수행하십시오");

                                            // message.setText("잠시만 기다리세요");

                                            // Intent intent = new Intent(PopupActivity.this, PopupActivity2.class);
                                            //startActivityForResult(intent, 1);


                                            Intent intent = new Intent(PopupActivity.this, PopupActivity2.class);
                                            startActivityForResult(intent, 1);
                                           //finish();
                                            finish();





/*

                                            if (isSended.equals("2")) {

                                                message.setText("매칭완료!");

                                                Button.OnClickListener btnListener = new View.OnClickListener() {

                                                    public void onClick(View v) {
                                                        switch (v.getId()) {

                                                            case R.id.okBtn:

                                                                message.setText("헬퍼가 미션 수행을 완료하면, 우측 상단에 '미션완료'를 눌러야 보상이 헬퍼에게로 이동합니다");



                                                                Button.OnClickListener btnListener = new View.OnClickListener() {

                                                                    public void onClick(View v) {
                                                                        switch (v.getId()) {

                                                                            case R.id.okBtn:

                                                                                Intent intent = new Intent(PopupActivity.this, Chat2Activity.class);
                                                                                startActivityForResult(intent, 1);


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


                                            } else if (isSended.equals("3")) {

                                                message.setText("헬퍼가 수락을 하지 않아 다른 헬퍼를 알아보세요");

                                            }
                                            break;
                                            */


                                        case R.id.cancleBtn:

                                            //mPost.child(isSended).setValue("3");

                                            //cancleBtn.setVisibility(View.INVISIBLE);
                                            //message.setText("의뢰자는 다른 헬퍼를 찾습니다.");


                                            break;


                                    }
                                }
                            };


                            okBtn = (Button) findViewById(R.id.okBtn);
                            //cancleBtn = (Button) findViewById(R.id.cancleBtn);
                            okBtn.setOnClickListener(btnListener);

                        } else if (isSended.equals("1")&&Helperuid.equals(mUid)) {
                            okBtn.setVisibility(VISIBLE);
                            cancleBtn.setVisibility(VISIBLE);

                            message.setText("의뢰자가 수락하였습니다. 미션을 수행 하시겠습니까?");

                            Button.OnClickListener btnListener2 = new View.OnClickListener() {

                                public void onClick(View v) {
                                    switch (v.getId()) {
                                        case R.id.okBtn:

                                            if(money >= Integer.parseInt(due)){

                                            mPost.child(postuid).child("isSended").setValue("2").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Log.d("Pop up","isSended가 2로 수정되었습니다");
                                                }
                                            });

                                            mPost.child(postuid).child("isMatched").setValue(Helperuid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Log.d("Pop up","isMatched가 헬퍼 uid로 업데이트 되었습니다.");
                                                }
                                            });

                                            message.setText("매칭완료!");

                                            // 돈이 빠지는 시점

                                                DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference().child("Users");
                                               /* final HashMap<String,Object> updating=new HashMap<>();
                                                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                            String key=snapshot.getKey();
                                                            Log.d("check","key의 값: "+key);
                                                            if(key.equals("Money")){
                                                                updating.put("Money",(money-Integer.parseInt(due)));
                                                                Log.d(key,String.valueOf(money));
                                                                Log.d(key,due);
                                                                Log.d(key,"value의 값: "+(money-Integer.parseInt(due)));

                                                            }else{
                                                                updating.put(key,snapshot.getValue().toString());
                                                                Log.d("check","value의 값: "+snapshot.getValue().toString());
                                                            }
                                                        }
                                                        //dbRef=FirebaseDatabase.getInstance().getReference().child("Users").child(Helperuid);
                                                        //update
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                                                });

                                                dbRef.child(Helperuid).updateChildren(updating);*/

                                                dbRef.child(Helperuid).child("Money").setValue((money-Integer.parseInt(due))).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Log.d("Helper Money update","헬퍼 돈이 업데이트 되었습니다.");
                                                    }
                                                });

                                                DatabaseReference mCenter= FirebaseDatabase.getInstance().getReference().child("Center");
                                                mCenter.child(centerKey).child("Money").setValue(due).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Log.d("Center DB","CenterDB - Money의 값: "+(Integer.parseInt(due)));
                                                    }
                                                });
                                                mCenter.child(centerKey).child("HelperUid").setValue(Helperuid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Log.d("Center update","CenterDB-HelperUid의 값: "+Helperuid);
                                                    }
                                                }) ;
                                            okBtn.setVisibility(VISIBLE);

                                            cancleBtn.setVisibility(View.INVISIBLE);
                                            //message.setText("미션을 수행하십시오");


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
                                            }else if(money < Integer.parseInt(due)){
                                                Intent intent = new Intent(PopupActivity.this, PopupActivity3.class);
                                                startActivityForResult(intent, 1);


                                            }

                                        case R.id.cancleBtn:

                                            mPost.child(postuid).child("isSended").setValue("3").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });


                                            // cancleBtn.setVisibility(View.INVISIBLE);
                                            message.setText("의뢰자는 다른 헬퍼를 찾습니다.");

                                            okBtn.setVisibility(VISIBLE);

                                            cancleBtn.setVisibility(View.INVISIBLE);

                                            //Intent intent = new Intent(PopupActivity.this, Chat2Activity.class);
                                            // startActivityForResult(intent, 1);


                                            Button.OnClickListener btnListener2 = new View.OnClickListener() {

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
                                            okBtn.setOnClickListener(btnListener2);


                                            break;

                                    }
                                }
                            };


                            okBtn = (Button) findViewById(R.id.okBtn);
                            //cancleBtn = (Button) findViewById(R.id.cancleBtn);
                            okBtn.setOnClickListener(btnListener2);


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