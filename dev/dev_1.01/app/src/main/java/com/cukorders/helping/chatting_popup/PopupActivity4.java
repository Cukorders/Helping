package com.cukorders.helping.chatting_popup;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cukorders.helping.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PopupActivity4 extends AppCompatActivity {

    Button okBtn, cancleBtn;
    TextView message;
    TextView message2;
    LinearLayout mlayout;



    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;


    private Button accept_button, decline_button;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String mUid;

    private String chatUid;
    private String postUid;
    private DatabaseReference mPost;
    private DatabaseReference mChats;
    private DatabaseReference mUsers;
    private int money,money1,money2;
    private DatabaseReference Center;

    private DatabaseReference MoneyRef;
    private String Money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        message = findViewById(R.id.txtText);
        message2= findViewById(R.id.txtText2);
        mlayout = findViewById(R.id.notification_icon);

        okBtn = (Button) findViewById(R.id.okBtn);
        cancleBtn = (Button) findViewById(R.id.cancleBtn);




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        accept_button = findViewById(R.id.chattingActivity_accept);
        decline_button = findViewById(R.id.chattingActivity_decline);

        //내 로그인한 uid담기
        // mCurrentUser = mAuth.getCurrentUser();
        // mUid = mCurrentUser.getUid();
        mUid = "TIhMFvxLG9awVpVPN931vwXDUXz2";

        chatUid = "-M9nHD-eVIOkPrdq9IXK";
        //db 테이블 불러오기
        mPost = FirebaseDatabase.getInstance().getReference().child("Posting");
        Center= FirebaseDatabase.getInstance().getReference().child("Center");

        mUsers = FirebaseDatabase.getInstance().getReference().child("Users");


        mChats = FirebaseDatabase.getInstance().getReference().child("Chat_list").child(chatUid);
        MoneyRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mUid);
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
        /*
        Log.d("money","Money의 값: "+Money);
        if(Money.equals("default")){
            money=10000;
        }else{
           // money=Integer.parseInt(Money);
        }
        Log.d("money","money의 값: "+money);

         */

        mChats.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String Clientuid = dataSnapshot.child("ClientUid").getValue().toString();
                final String Helperuid = dataSnapshot.child("HelperUid").getValue().toString();
                final String postuid = dataSnapshot.child("PostUid").getValue().toString();


                mPost.child(postuid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String isMatched = dataSnapshot.child("isMatched").getValue().toString();
                        String isSended = dataSnapshot.child("isSended").getValue().toString();
                        final String price = dataSnapshot.child("price").getValue().toString();
                        String due = dataSnapshot.child("due").getValue().toString();
                        String isFinished = dataSnapshot.child("isFinished").getValue().toString();
                        String isCancled=dataSnapshot.child("isCanceled").getValue().toString();



                        Button.OnClickListener btnListener = new View.OnClickListener(){

                            public void onClick(View v){
                                switch (v.getId()) {
                                    case R.id.okBtn:



                                        Button.OnClickListener btnListener = new View.OnClickListener() {

                                            public void onClick(View v) {
                                                switch (v.getId()) {
                                                    case R.id.okBtn:

                                                        message.setText("보증금은 돌려받고, 물품금액과 보상금이 입금됩니다.");

                                                        Button.OnClickListener btnListener = new View.OnClickListener() {

                                                            public void onClick(View v) {
                                                                switch (v.getId()) {
                                                                    case R.id.okBtn:

                                                                        finish();


                                                                }
                                                            }
                                                        };


                                                        okBtn = (Button) findViewById(R.id.okBtn);
                                                        //cancleBtn = (Button) findViewById(R.id.cancleBtn);
                                                        okBtn.setOnClickListener(btnListener);


                                                }
                                            }
                                        };


                                        okBtn = (Button) findViewById(R.id.okBtn);
                                        //cancleBtn = (Button) findViewById(R.id.cancleBtn);
                                        okBtn.setOnClickListener(btnListener);

                                        Button.OnClickListener btnListener2 = new View.OnClickListener() {

                                            public void onClick(View v) {
                                                switch (v.getId()) {
                                                    case R.id.cancleBtn:

                                                        message.setText("기부완료!");

                                                        Button.OnClickListener btnListener = new View.OnClickListener() {

                                                            public void onClick(View v) {
                                                                switch (v.getId()) {
                                                                    case R.id.okBtn:

                                                                        finish();


                                                                }
                                                            }
                                                        };


                                                        okBtn = (Button) findViewById(R.id.okBtn);
                                                        //cancleBtn = (Button) findViewById(R.id.cancleBtn);
                                                        okBtn.setOnClickListener(btnListener);


                                                }
                                            }
                                        };


                                        cancleBtn = (Button) findViewById(R.id.cancleBtn);
                                        //cancleBtn = (Button) findViewById(R.id.cancleBtn);
                                        cancleBtn.setOnClickListener(btnListener2);


/*
                                        DatabaseReference mCenter=FirebaseDatabase.getInstance().getReference().child("Center").child(postuid);
                                        mCenter.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists()){
                                                    money1 =Integer.parseInt(dataSnapshot.child("Money").getValue().toString());
                                                    money2=Integer.parseInt(dataSnapshot.child("Money2").getValue().toString());
                                                    Log.d("money","money1의 값: "+money1);
                                                    Log.d("money","money2의 값: "+money2);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {}
                                        });*/

                                }
                            }
                        };





                        okBtn = (Button) findViewById(R.id.okBtn);
                        //cancleBtn = (Button) findViewById(R.id.cancleBtn);
                        okBtn.setOnClickListener(btnListener);




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
}


