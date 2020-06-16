package com.cukorders.helping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cukorders.Adapter.PostAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class MissionActivity extends AppCompatActivity {
    private final Context context=this;
    private static final String TAG ="MissionActivity" ;

    private final ArrayList<InitPost> mPost = new ArrayList<>();
    private ArrayList<String> postKeyList = new ArrayList<>();
    final ArrayList<InitPost> newPost = new ArrayList<>();
    final HashSet<InitPost> temp = new HashSet<>();
    private PostAdapter mAdapter;

    private RecyclerView mylikesListView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout linearLayout;

    private DatabaseReference mRef;
    private DatabaseReference likesList;

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private String mUid;

    private boolean flag=false;
    private boolean chkflag=false;
    //관심목록

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_interesting_mission);
        findViewById(R.id.bt_back).setOnClickListener(onClickListener);

        mylikesListView = (RecyclerView) findViewById(R.id.my_interest_recyclerview);
        mylikesListView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mylikesListView.setLayoutManager(mLayoutManager);
        //맨 밑으로 내려갔을때, 파란색 그라데이션 안뜨도록
        mylikesListView.scrollToPosition(0);

        linearLayout=(LinearLayout) findViewById(R.id.linearLayout_likes);
        mAdapter = new PostAdapter(this,mPost);
        mylikesListView.setAdapter(mAdapter);
        mylikesListView.setItemAnimator(new DefaultItemAnimator());

        /*
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUid = mUser.getUid();

         */
        mUid = "TIhMFvxLG9awVpVPN931vwXDUXz2";
        likesList = FirebaseDatabase.getInstance().getReference().child("Postlikes");
        mRef = FirebaseDatabase.getInstance().getReference().child("Posting");

    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_back:
                    Intent intent=new Intent(context,MainActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Query chklikes = likesList;
        chklikes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();
                        Log.d(TAG, "Check what is key" + key);
                        Log.d(TAG, "Check what is mUid" + mUid);
                        if (mUid.equals(key)) {
                            Log.d(TAG, "I found it !!!");
                            flag=true;
                            break;
                        }
                    }
                    if(flag){
                        //현재 사용자가 있다면
                        Query query = likesList.child(mUid);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                mPost.clear();
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Log.d(TAG, "Check what is datasnapshot" + snapshot);
                                        InitPost object;
                                        postKeyList.add(snapshot.getValue().toString());//InitPost 객체에 담기
                                        // 담은 데이터들을 배열 리스트에 넣고 리사이클러 뷰로 보낼 준비하기
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                                boolean chkanotherflag=false;
                                final ArrayList<InitPost> finalPost = new ArrayList<>();
                                finalPost.clear();
                                for (int i = 0; i < postKeyList.size(); i++) {
                                    Query newquery = mRef.child(postKeyList.get(i));
                                    Log.d(TAG, "Check now pointing key" + postKeyList.get(i));
                                    newquery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                Log.d(TAG, "Check dataSnapshot.getValue" + dataSnapshot.getValue());
                                                InitPost post = dataSnapshot.getValue(InitPost.class);//InitPost 객체에 담기
                                                newPost.add(post); //담은 데이터들을 배열 리스트에 넣고 리사이클러 뷰로 보낼 준비하기
                                                //temp.add(newPost);
                                                chkflag = true;
                                                Log.d(TAG, "Check now pointing newPost1" + newPost);
                                                Log.d(TAG, "Check now pointing temp1" + temp);
                                            }
                                            //ArrayList<InitPost> nowPost = new ArrayList<>(temp);
                                            //finalPost.addAll(temp);
                                            Log.d(TAG, "Check now pointing newPost2" + newPost);
                                            Log.d(TAG, "Check now pointing temp2" + temp);
                                            mAdapter = new PostAdapter(context, newPost);
                                            mylikesListView.setAdapter(mAdapter);
                                            mAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });

                        //Log.d(TAG, "Check now pointing newPost" + newPost);
                    }else{
                        Toast.makeText(context, "현재 사용자의 관심 목록에 있는 게시글이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}