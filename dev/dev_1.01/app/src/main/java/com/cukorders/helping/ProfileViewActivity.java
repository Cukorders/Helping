package com.cukorders.helping;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileViewActivity extends AppCompatActivity {
    private static final String TAG = "ProfileViewActivity";
    private static Context context;

    private CircleImageView profileImg;
    private TextView yournick;
    private Button yourscorebtn,back_btn;
    private String userUid;

    private ProgressBar pb;
    private TextView textView;
    private ImageView imageView;

    private DatabaseReference yourRef;

    //Todo 매너점수 노출

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_others_profile);

        pb=(ProgressBar) findViewById(R.id.pb);
        textView=(TextView) findViewById(R.id.score);
        imageView=(ImageView) findViewById(R.id.face);


        yournick= findViewById(R.id.others_nickname);
        profileImg = findViewById(R.id.others_proview_view);

        context=this;
        findViewById(R.id.bt_back).setOnClickListener(onClickListener);

        yourRef= FirebaseDatabase.getInstance().getReference().child("Users");
        Bundle bundleObject = getIntent().getExtras();
        //
        if(!bundleObject.getString("clientUid").isEmpty()){
            userUid=bundleObject.getString("clientUid");
            Log.d(TAG,"getIncomingIntent : check for incoming intents."+userUid);
        }
        yourRef.child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nick = dataSnapshot.child("Nickname").getValue().toString();
                String score = dataSnapshot.child("Score").getValue().toString();
                String postuserimg = dataSnapshot.child("Image").getValue().toString();

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
                yournick.setText(nick);
                Picasso.get().load(postuserimg).into(profileImg);
                //Todo ProgressBar
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    View.OnClickListener onClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_back:
                    //뒤로가기 버튼
                    break;

            }
        }
    };

}