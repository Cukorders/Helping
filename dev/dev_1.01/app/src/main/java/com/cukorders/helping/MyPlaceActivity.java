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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MyPlaceActivity extends AppCompatActivity {

    private static Context context;
    private TextView region[]=new TextView[3];
    private Button change[]=new Button[3];
    private Button delete[]=new Button[3];
    private Button profileRegister;
    private String changedLocation;
    public static Context myPlaceActivity;
    //public static boolean fromMyPlaceActivity=false;
    private static ArrayList<String> user_regions=new ArrayList<>();
    private static DatabaseReference databaseReference;
    private static FirebaseUser firebaseUser;
    private static String uid;
    private static boolean check[]=new boolean[3];
    public static int changingIndex;
    private final static String TAG="MyPlaceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myplace_setting);

        context=this;
        myPlaceActivity=this;
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        uid=firebaseUser.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("userRegions").child(uid);

     /*   databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String key=snapshot.getKey(),val;
                    Log.d(TAG,"key의 값: "+key);
                    val=snapshot.getValue().toString();
                    if(!key.contains("state")&&!val.equals("default")){
                        user_regions.add(val);
                        Log.d(TAG,"arrayList에 "+val+"이 추가되었습니다.");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });*/

        // 중복 제거
        HashSet<String>tmp=new HashSet<String>(((LoadingActivity)LoadingActivity.loadingActivity).loc);
        user_regions=new ArrayList<String>(tmp);
        Log.d(TAG,"user_regions의 크기: "+String.valueOf(user_regions.size()));
        for(int i=0;i<user_regions.size();++i){
            Log.d(TAG,"user_regions의 원소: "+user_regions.get(i));
            Log.d(TAG,"loc의 원소: "+((LoadingActivity)LoadingActivity.loadingActivity).loc.get(i));
        }
//        setTexts();

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

        findViewById(R.id.profileRegisterBt).setOnClickListener(onClickListener);
        findViewById(R.id.bt_back).setOnClickListener(onClickListener);
        for(int i=0;i<3;++i){
            change[i].setOnClickListener(onClickListener);
            delete[i].setOnClickListener(onClickListener);
        }
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
                    changingIndex=0;
                    changeRegion(region[0],0);
                    setTexts();
                    break;

                case R.id.change2:
                    changingIndex=1;
                    changeRegion(region[1],1);
                    break;

                case R.id.change3:
                    changingIndex=2;
                    changeRegion(region[2],2);
                    break;

                case R.id.delete1:
                    if(user_regions.size()==1){
                        Toast.makeText(context,"지역은 하나 이상 설정되어있어야 합니다.",Toast.LENGTH_LONG).show();
                    }else{
                        delete(0);
                    }
                    break;

                case R.id.delete2:
                    delete(1);
                    break;

                case R.id.delete3:
                    delete(2);
                    break;

                case R.id.profileRegisterBt:
                    HashMap<String,Object> update=new HashMap<>();
                    databaseReference= FirebaseDatabase.getInstance().getReference().child("userRegions").child(firebaseUser.getUid());
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                String key= snapshot.getKey();
                                Log.d("value","유저 key: "+key);
                                if(key.contains("state")){
                                    ((LoadingActivity)LoadingActivity.loadingActivity).isCertified[(key.charAt(6)-'0')-1]=snapshot.getValue().toString().equals("true")?true:false;
                                Log.d("check","check의 값: "+(snapshot.getValue().toString().equals("true")?true:false));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                    for(int i=0;i<3;++i){
                        Log.d(TAG,"region의 값"+region[i]);
                        update.put("Region"+String.valueOf(i+1),(region[i].getText().toString().contains("지역")||region[i].getText().toString().contains("default")?"default":region[i].getText().toString()));
                        update.put("Region"+String.valueOf(i+1)+" state",(((LoadingActivity)LoadingActivity.loadingActivity).isCertified[i]?"true":"default"));
                    }
                    databaseReference.setValue(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context,"지역 변경이 되었습니다.",Toast.LENGTH_LONG).show();
                                Log.e("a post is uploaded", "a post is successfully uploaded");
                                startActivity(new Intent(context,MyPageActivity.class));
                            } else{
                                Toast.makeText(context,"오류가 발생하여 지역 추가에 실패하였습니다. 잠시 후에 다시 시도해주십시오.",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    break;
            }
        }
    };


    private void changeRegion(TextView txt,int index){
      //  fromMyPlaceActivity=true;
       // Log.d("fromMyPlaceActivity","fromMyPlaceActivity 값: "+fromMyPlaceActivity);
        startActivity(new Intent(context,ChangeTheRegionActivity.class));
        setTexts();
    }

    private void delete(int index){
        if(index<((LoadingActivity)LoadingActivity.loadingActivity).loc.size()){
        ((LoadingActivity)LoadingActivity.loadingActivity).loc.remove(index);
        Log.d(TAG,"loc의 "+index+"번째 원소가 삭제되었습니다.");
        Log.d(TAG,"loc의 크기: "+((LoadingActivity)LoadingActivity.loadingActivity).loc.size());
        HashSet<String>tmp=new HashSet<String>(((LoadingActivity)LoadingActivity.loadingActivity).loc);
        user_regions=new ArrayList<String>(tmp);
        Log.d(TAG,"user_regions의 크기: "+user_regions.size());
        for(int i=0;i<((LoadingActivity)LoadingActivity.loadingActivity).loc.size();++i){
            Log.d(TAG,"loc의 원소: "+((LoadingActivity)LoadingActivity.loadingActivity).loc.get(i));
        }
        for(int i=0;i<user_regions.size();++i){
            Log.d(TAG,"user_regions의 원소: "+user_regions.get(i));
        }
        setTexts();
        }else{
            Toast.makeText(context,"삭제할 수 없습니다.",Toast.LENGTH_LONG).show();
        }
    }

   /* private void checkRegions(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String key=snapshot.getKey();
                    HashMap<String,String> info=(HashMap<String,String>) snapshot.getValue();
                    for(int i=0;i<3;++i){
                       // user_regions[i]=info.get("Region"+String.valueOf(i+1));
                       // Log.d("user_regions","user_regions["+String.valueOf(i)+"]의 값: "+user_regions[i]);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("에러","userRegions 정보를 가져오는데 실패하였습니다.");
            }
        });
    }
    */

    private void setTexts(){
        int size=((LoadingActivity)LoadingActivity.loadingActivity).loc.size();
        Log.d(TAG,"loc 배열 크기: "+size);
        for(int i=0;i<3;++i){
            region[i].setText("지역 "+(i+1));
            Log.d(TAG,"region["+i+"]의 값: "+region[i].getText().toString());
        }
        for(int i=0;i<size;++i){
            region[i].setText(((LoadingActivity)LoadingActivity.loadingActivity).loc.get(i));
            Log.d(TAG,"region["+i+"]의 값: "+region[i].getText().toString());
        }
    }

}
