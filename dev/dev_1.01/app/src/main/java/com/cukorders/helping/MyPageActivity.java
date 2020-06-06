package com.cukorders.helping;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPageActivity extends AppCompatActivity {

    private static Context context;
    private TextView user_nickname;
    private static FirebaseUser firebaseUser;
    private static DatabaseReference databaseReference;
    private static String uid;
    private CircleImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        context=this;
        profilePic=(CircleImageView) findViewById(R.id.userProfileImage);

        findViewById(R.id.bt_fav).setOnClickListener(onClickListener);
        findViewById(R.id.bt_history).setOnClickListener(onClickListener);
        findViewById(R.id.bt_chat).setOnClickListener(onClickListener);
        findViewById(R.id.bt_notice).setOnClickListener(onClickListener);
        findViewById(R.id.bt_profile).setOnClickListener(onClickListener);
        findViewById(R.id.bt_back).setOnClickListener(onClickListener);
        findViewById(R.id.bt_choose_region).setOnClickListener(onClickListener);
        findViewById(R.id.bt_faq).setOnClickListener(onClickListener);
        findViewById(R.id.bt_point).setOnClickListener(onClickListener);
        findViewById(R.id.bt_regional_certification).setOnClickListener(onClickListener);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        uid=firebaseUser.getUid();
        Log.d("유저 UID","유저 UID: "+uid);

        user_nickname=(TextView) findViewById(R.id.user_name);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String key=snapshot.getKey();
                    Log.d("key","유저 key: "+key);
                    String image;
                    if(uid.equals(key)){
                    HashMap<String,String> info=(HashMap<String,String>)snapshot.getValue();
                    user_nickname.setText(info.get("Nickname"));
                    image=snapshot.child("Image").getValue().toString();
                    Picasso.get().load(image).into(profilePic);
                    }
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error in getting the nickname","유저 닉네임을 불러오는데 실패하였습니다.");
            }
        });
    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.bt_fav:
                    startActivity(new Intent(context,MissionActivity.class));
                    break;

                case R.id.bt_notice:
                    startActivity(new Intent(context,NoticeActivity.class));
                    break;

                case R.id.bt_profile:
                    startActivity(new Intent(context,ProfileActivity.class));
                    break;

                case R.id.bt_back:
                    startActivity(new Intent(context,MainActivity.class));
                    break;

                case R.id.bt_choose_region:
                    startActivity(new Intent(context,MyPlaceActivity.class));
                    break;

                case R.id.bt_faq:
                    startActivity(new Intent(context,FAQActivity.class));
                    break;

                case R.id.bt_history:
                    startActivity(new Intent(context,HistoryActivity.class));
                    break;

                case R.id.bt_regional_certification:
                    startActivity(new Intent(context,RegionActivity.class));
                    break;
            }
        }
    };
}
