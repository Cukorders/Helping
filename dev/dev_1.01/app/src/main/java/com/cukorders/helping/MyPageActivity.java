package com.cukorders.helping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyPageActivity extends AppCompatActivity {

    private static Context context;
    private TextView user_nickname;
    private static FirebaseUser firebaseUser;
    private static DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        context=this;
        findViewById(R.id.bt_fav).setOnClickListener(onClickListener);
        findViewById(R.id.bt_history).setOnClickListener(onClickListener);
        findViewById(R.id.bt_chat).setOnClickListener(onClickListener);
        findViewById(R.id.bt_notice).setOnClickListener(onClickListener);
        findViewById(R.id.bt_profile).setOnClickListener(onClickListener);
        findViewById(R.id.bt_back).setOnClickListener(onClickListener);
        findViewById(R.id.bt_choose_region).setOnClickListener(onClickListener);
        findViewById(R.id.bt_faq).setOnClickListener(onClickListener);
        findViewById(R.id.bt_point).setOnClickListener(onClickListener);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());

        user_nickname=(TextView) findViewById(R.id.user_name);
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
            }
        }
    };
}
