package com.cukorders.helping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoadingActivity extends AppCompatActivity {

    public static Context loadingActivity;
    public static ArrayList<String> loc=new ArrayList<>();
    public static boolean isCertified[]=new boolean[3];
    public static int loc_cnt=0;
    private static FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    private static String uid;
    private static DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        loadingActivity=this;
        findViewById(R.id.StartButton).setOnClickListener(OnClickListener);

        Log.d("firebaseUser","firebaseUser in the loading activity: "+firebaseUser);
        if(firebaseUser==null){
            for(int i=0;i<3;++i)
                loc.add("default");
        }
        else{
            uid=firebaseUser.getUid();
            databaseReference= FirebaseDatabase.getInstance().getReference().child("userRegions").child(uid);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        String key= snapshot.getKey();
                        Log.d("value","유저 key: "+key);
                        if(!key.contains("state")){
                            Log.d("loc","loc의 원소: "+snapshot.getValue().toString());
                            if(!snapshot.getValue().toString().equals("default")){
                                loc.add(snapshot.getValue().toString());
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }
    }

    View.OnClickListener OnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.StartButton:
                    goNextPage();
                    break;
            }
        }
    };

    private void goNextPage(){
        Intent intent=new Intent(this, ChooseTheRegionActivity.class);
        startActivity(intent);
    }
}