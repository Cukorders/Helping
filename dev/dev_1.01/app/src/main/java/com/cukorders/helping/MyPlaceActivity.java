package com.cukorders.helping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;

public class MyPlaceActivity extends AppCompatActivity {

    private static Context context;
    private TextView region[]=new TextView[3];
    private Button change[]=new Button[3];
    private Button delete[]=new Button[3];
    private Button profileRegister;
    private String changedLocation;
    public static Context myPlaceActivity;
    public static boolean fromMyPlaceActivity=false;
    private static String user_regions[]=new String[3];
    private static DatabaseReference databaseReference;
    private static FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myplace_setting);

        context=this;
        myPlaceActivity=this;
        databaseReference= FirebaseDatabase.getInstance().getReference().child("userRegions");
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        change[0]=(Button) findViewById(R.id.change1);
        change[1]=(Button) findViewById(R.id.change2);
        change[2]=(Button) findViewById(R.id.change3);
        delete[0]=(Button) findViewById(R.id.delete1);
        delete[1]=(Button) findViewById(R.id.delete2);
        delete[2]=(Button) findViewById(R.id.delete3);
        region[0]=(TextView) findViewById(R.id.region1);
        region[1]=(TextView) findViewById(R.id.region2);
        region[2]=(TextView) findViewById(R.id.region3);
        profileRegister=(Button) findViewById(R.id.profileRegisterBt);

        for(int i=0;i<3;++i){
            change[i].setOnClickListener(onClickListener);
            delete[i].setOnClickListener(onClickListener);
        }

        user_regions=((LoadingActivity)LoadingActivity.loadingActivity).userLoc;
        findViewById(R.id.profileRegisterBt).setOnClickListener(onClickListener);
        findViewById(R.id.bt_back).setOnClickListener(onClickListener);
        for(int i=0;i<3;++i){
            change[i].setOnClickListener(onClickListener);
            delete[i].setOnClickListener(onClickListener);
        }
       checkRegions();
        setTexts();
    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_back:
                    startActivity(new Intent(context,MyPageActivity.class));
                    break;

                case R.id.change1:
                    changeRegion(region[0]);
                    break;

                case R.id.change2:
                    changeRegion(region[1]);
                    break;

                case R.id.change3:
                    changeRegion(region[2]);
                    break;

                case R.id.delete1:
                    delete(0);
                    break;

                case R.id.delete2:
                    delete(1);
                    break;

                case R.id.delete3:
                    delete(2);
                    break;

                case R.id.profileRegisterBt:
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //TODO DB 업데이트하기
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    break;
            }
        }
    };


    private void changeRegion(TextView txt){
        //TODO DB에 지역 교체 업데이트
        fromMyPlaceActivity=true;
        Log.d("fromMyPlaceActivity","fromMyPlaceActivity 값: "+fromMyPlaceActivity);
        startActivity(new Intent(context,ChooseTheRegionActivity.class));
        setTexts();
    }

    private void delete(int index){
        if(count()==1){ // 지역이 하나인데 삭제 시도 → 삭제할 수 없어야 한다.
            Toast.makeText(context,"하나 이상의 지역이 있어야하므로 삭제할 수 없습니다.",Toast.LENGTH_LONG).show();
        } else{
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            checkRegions();
            setTexts();
        }
    }

    private void checkRegions(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String key=snapshot.getKey();
                    HashMap<String,String> info=(HashMap<String,String>) snapshot.getValue();
                    for(int i=0;i<3;++i){
                        user_regions[i]=info.get("Region"+String.valueOf(i+1));
                        Log.d("user_regions","user_regions["+String.valueOf(i)+"]의 값: "+user_regions[i]);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("에러","userRegions 정보를 가져오는데 실패하였습니다.");
            }
        });
    }

    private void setTexts(){
        for(int i=0;i<3;++i)
            region[i].setText((user_regions[i].equals("default")?("지역 "+String.valueOf(i+1)):user_regions[i]));
    }

    private int count(){
        int ret=0;
        for(int i=0;i<3;++i)
            if(user_regions[i].equals("default")){
                ++ret;
            }
        return ret;
    }
}
