package com.cukorders.helping;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChargingActivity extends AppCompatActivity {

    private static FirebaseUser firebaseUser;
    private static DatabaseReference databaseReference;
    private static String uid;
    private static Context context;
    private CircleImageView userProfileImage;
    private int db_point;
    private TextView nickname,current_point,points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charging_point);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        uid=firebaseUser.getUid();
        context=this;

        userProfileImage=(CircleImageView) findViewById(R.id.userProfileImage);
        nickname=(TextView) findViewById(R.id.nickname);
        current_point=(TextView) findViewById(R.id.current_point);
        points=(TextView) findViewById(R.id.points);

        findViewById(R.id.bt_charge).setOnClickListener(onClickListener);
        findViewById(R.id.bt_back).setOnClickListener(onClickListener);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String key=snapshot.getKey();
                    Log.d("key","유저 key: "+key);
                    String image;
                    if(uid.equals(key)){
                        HashMap<String,String> info=(HashMap<String,String>)snapshot.getValue();
                        nickname.setText(info.get("Nickname"));
                        image=snapshot.child("Image").getValue().toString();
                        Picasso.get().load(image).into(userProfileImage);
                        if(info.get("Money").equals("default")){
                            current_point.setText("10000points");
                            db_point=10000;
                        }else{
                            current_point.setText(info.get("Money")+"points");
                            db_point=Integer.parseInt(info.get("Money"));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_charge:
                    if(points.getText().toString().equals("")){
                        AlertDialog.Builder builder=new AlertDialog.Builder(context);
                        builder.setTitle("입력 에러");
                        builder.setMessage("금액을 입력해주세요");
                        builder.setPositiveButton("확인", null).setNegativeButton("취소",null);
                        builder.show();
                    }else{
                    int input=Integer.parseInt(points.getText().toString());
                    if(input<0){
                        AlertDialog.Builder builder=new AlertDialog.Builder(context);
                        builder.setTitle("입력 에러");
                        builder.setMessage("금액은 0 이상이어야 합니다.");
                        builder.setPositiveButton("확인", null).setNegativeButton("취소",null);
                        builder.show();
                    }else if(input%1000>0){
                        AlertDialog.Builder builder=new AlertDialog.Builder(context);
                        builder.setTitle("충전 에러");
                        builder.setMessage("1000원 단위로 충전이 가능합니다. 다시 입력해주세요.");
                        builder.setPositiveButton("확인", null).setNegativeButton("취소",null);
                        builder.show();
                    }else{
                        Map<String,Object> information=new HashMap<String,Object>();
                        information.put("Money",Integer.toString(db_point+input));
                        databaseReference.child(uid).updateChildren(information);
                        Toast.makeText(context,"충전이 완료되었습니다.",Toast.LENGTH_LONG).show();
                        current_point.setText(Integer.toString(db_point+input));
                    }
                    }
                    break;

                case R.id.bt_back:
                    startActivity(new Intent(context,MyPageActivity.class));
                    break;
            }
        }
    };
}
