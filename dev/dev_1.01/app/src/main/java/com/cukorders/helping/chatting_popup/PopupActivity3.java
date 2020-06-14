package com.cukorders.helping.chatting_popup;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cukorders.helping.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PopupActivity3 extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;


    private Button okBtn,cancleBtn;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String mUid;

    private String chatUid;
    private String postUid;
    private DatabaseReference mPost;
    private DatabaseReference mChats;
    private DatabaseReference mUsers;
    private int money;

    private DatabaseReference MoneyRef;
    private String Money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup3);

        okBtn = findViewById(R.id.okBtn);
        cancleBtn = findViewById(R.id.cancleBtn);

        //내 로그인한 uid담기
        // mCurrentUser = mAuth.getCurrentUser();
        // mUid = mCurrentUser.getUid();
        mUid="TIhMFvxLG9awVpVPN931vwXDUXz2";

        chatUid = "-M9nHD-eVIOkPrdq9IXK";
        //db 테이블 불러오기
        mPost = FirebaseDatabase.getInstance().getReference().child("Posting");

        mUsers= FirebaseDatabase.getInstance().getReference().child("Users");




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
                String postuid = dataSnapshot.child("PostUid").getValue().toString();


                mPost.child(postuid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String isMatched=dataSnapshot.child("isMatched").getValue().toString();
                        String isSended=dataSnapshot.child("isSended").getValue().toString();
                        final String price=dataSnapshot.child("price").getValue().toString();

                        cancleBtn.setVisibility(View.INVISIBLE);

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


        recyclerView = (RecyclerView) findViewById(R.id.chattingActivity_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        String[] myDataset={"test1","test2","test3"};
    }

}


