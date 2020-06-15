package com.cukorders.helping;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView textView;
    private ImageView imageView;
    private ProgressBar pb;
    private static FirebaseUser firebaseUser;
    private static DatabaseReference databaseReference;
    private static String uid;
    private CircleImageView profilePic;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        context=this;
        profilePic=(CircleImageView) findViewById(R.id.userProfileImage);

        pb=(ProgressBar) findViewById(R.id.pb);
        textView=(TextView) findViewById(R.id.score);
        imageView=(ImageView) findViewById(R.id.face);
        findViewById(R.id.bt_fav).setOnClickListener(onClickListener);
        findViewById(R.id.bt_history).setOnClickListener(onClickListener);
        findViewById(R.id.bt_chat).setOnClickListener(onClickListener);
        findViewById(R.id.bt_notice).setOnClickListener(onClickListener);
        findViewById(R.id.bt_profile).setOnClickListener(onClickListener);
        findViewById(R.id.bt_back).setOnClickListener(onClickListener);
        findViewById(R.id.bt_choose_region).setOnClickListener(onClickListener);
        findViewById(R.id.bt_faq).setOnClickListener(onClickListener);
        findViewById(R.id.bt_point).setOnClickListener(onClickListener);
        findViewById(R.id.bt_setting).setOnClickListener(onClickListener);
        findViewById(R.id.bt_regional_certification).setOnClickListener(onClickListener);
        findViewById(R.id.bt_logout).setOnClickListener(onClickListener);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseUser!=null){
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
    }

    private void displayMannerBar(){
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String score=dataSnapshot.child("Score").getValue().toString();
                    int value;
                    value= (score.equals("default") ? 100 : Integer.parseInt(score));
                    pb.setProgress(value);
                    textView.setText(value+"점");
                    if (value >= 80 && value <= 100){
                        textView.setTextColor(Color.parseColor("#00008B"));
                        imageView.setImageResource(R.drawable.face_manner_darkblue);
                    }
                    else if (value >= 60 && value < 80){
                        textView.setTextColor(Color.parseColor("#008000"));
                        imageView.setImageResource(R.drawable.face_manner_green);
                    }
                    else if (value >= 30 && value < 60){
                        textView.setTextColor(Color.parseColor("#FFA500"));
                        imageView.setImageResource(R.drawable.face_manner_orange);
                    }
                    else {
                        textView.setTextColor(Color.parseColor("#FF0000"));
                        imageView.setImageResource(R.drawable.face_manner_red);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.bt_point:
                    startActivity(new Intent(context,ChargingActivity.class));
                    break;

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
                    startActivity(new Intent(context,RegionalCertificationActivity.class)); // spinner가 있는 regional_certification xml로 넘어간다.
                    break;

                case R.id.bt_setting:
                    startActivity(new Intent(context,SettingActivity.class));
                    break;

                case R.id.bt_logout:
                    firebaseAuth.signOut();
                    Toast.makeText(context,"로그아웃되었습니다.",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(context,LoadingActivity.class));
                    break;
            }
        }
    };
}