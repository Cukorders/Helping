package com.cukorders.helping;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.VISIBLE;

public class LogInActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity" ;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private DatabaseReference mUserDatabase;

    private FirebaseStorage mStorage;
    private StorageReference mImageStorage;
    private String imagePath;
    private static final int GALLERY_CODE = 10;

    private boolean chkNick=false;
    private TextView mErrorText;
    private boolean chkPhoto=false;
    private CircleImageView profileImage;
    private ImageButton mEditImage;
    private String mNick;
    private EditText mNickname;
    private Button mNickBtn;
    private String mGender;
    private Button F_Btn;
    private Button M_Btn;
    private String mAge;
    private Button Age1;
    private Button Age2;
    private Button Age3;
    private Button Age4;
    private Button Age5;
    private Button mRegisterBtn;
    private Button mLogoutBtn;
    //닉네임, 성별, 나이값도 업데이트 진행하기
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_details);

        profileImage = findViewById(R.id.userProfileImage);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Nickname").getValue().toString();
                String image = dataSnapshot.child("Image").getValue().toString();
                String imageThumbnails = dataSnapshot.child("Thumb_img").getValue().toString();
                String gender = dataSnapshot.child("Gender").getValue().toString();
                String age = dataSnapshot.child("Age").getValue().toString();
                String score = dataSnapshot.child("Score").getValue().toString();
                String money = dataSnapshot.child("Money").getValue().toString();
                //변경사항 업데이트 하는 부분
                Picasso.get().load(image).into(profileImage);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mErrorText = findViewById(R.id.profile_form_feedback);
        //firebase Storage
        mStorage = FirebaseStorage.getInstance();

        //앨범 권한 적용
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //버전 6이상부터 작동
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }
        //버튼 누르면 업로드
        mEditImage = findViewById(R.id.cameraBtn);
        mEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getAlbumIntent = new Intent(Intent.ACTION_PICK);
                getAlbumIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(getAlbumIntent,GALLERY_CODE);
            }
        });
        //firebase Storage의 root 가리킴
        mImageStorage = FirebaseStorage.getInstance().getReference();
        profileImage= findViewById(R.id.userProfileImage);


        //logout
        mLogoutBtn = findViewById(R.id.logoutBtn);
        mLogoutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mAuth.signOut();
                sendUserToLogin();
            }
        });
        //Nickname
        mNick="";
        mNickname = findViewById(R.id.userNickname);
        mNickBtn = findViewById(R.id.nicknameCheck);
        mNickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNick = mNickname.getText().toString();
                if(mNick.equals("")){
                    mErrorText.setText("닉네임을 입력해주세요");
                    mErrorText.setVisibility(VISIBLE);
                }else {
                    Log.d(TAG, "checkIfUserNicknameExists : checking if " + mNick + "already exists");
                    DatabaseReference chkReference = FirebaseDatabase.getInstance().getReference();
                    Query query = chkReference
                            .child("Users")
                            .orderByChild("Nickname")
                            .equalTo(mNick);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getChildrenCount()>0) {
                                mErrorText.setText("기존에 존재하는 닉네임입니다.\n 다른 닉네임을 입력해주세요");
                                mErrorText.setVisibility(VISIBLE);
                            }else{
                                mErrorText.setText("사용 가능한 닉네임입니다!");
                                chkNick = true;
                                mErrorText.setTextColor(getResources().getColor(R.color.colorAccent));
                                mNickBtn.setEnabled(false);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
        //gender
        mGender="";
        F_Btn = findViewById(R.id.femaleBtn);
        M_Btn = findViewById(R.id.maleBtn);
        M_Btn.setOnClickListener(setGender);
        F_Btn.setOnClickListener(setGender);
        //age
        mAge="";
        Age1 = findViewById(R.id.button10s);
        Age2 = findViewById(R.id.button20s);
        Age3 = findViewById(R.id.button30s);
        Age4 = findViewById(R.id.button40s);
        Age5 = findViewById(R.id.button50s);
        Age1.setOnClickListener(setAge);
        Age2.setOnClickListener(setAge);
        Age3.setOnClickListener(setAge);
        Age4.setOnClickListener(setAge);
        Age5.setOnClickListener(setAge);
        //registerBtn
        mRegisterBtn = findViewById(R.id.profileRegisterBt);
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(mAge.equals("")) && !(mGender.equals("")) && (chkNick != false)) {
                    Log.d(TAG, "Register complete " + chkNick + "is true");
                    mUserDatabase.child("Nickname").setValue(mNick);
                    mUserDatabase.child("Gender").setValue(mGender);
                    mUserDatabase.child("Age").setValue(mAge);
                    mRegisterBtn.setEnabled(false);
                    sendUserToMain();
                } else {
                    if (mAge.equals("")) {
                        Log.d(TAG, "Register error mAge is not set");
                        mErrorText.setTextColor(getResources().getColor(R.color.colorError));
                        mErrorText.setText("연령대를 선택해주세요");
                        mErrorText.setVisibility(VISIBLE);
                    }
                    if (mGender.equals("")) {
                        Log.d(TAG, "Register error mGender is not set");
                        mErrorText.setTextColor(getResources().getColor(R.color.colorError));
                        mErrorText.setText("성별을 선택해주세요");
                        mErrorText.setVisibility(VISIBLE);
                    }
                    if (chkNick == false) {
                        Log.d(TAG, "Register error chkNick is not set");
                        mErrorText.setTextColor(getResources().getColor(R.color.colorError));
                        mErrorText.setText("닉네임 중복확인을 해주세요");
                        mErrorText.setVisibility(VISIBLE);
                    }
                };
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE&& resultCode==RESULT_OK) {
            imagePath = getPath(data.getData());
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        //crop에 성공하면 result에 담고, resultUri에 저장
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode ==RESULT_OK){
                Uri resultUri = result.getUri();
                String currentUser_Uid = mCurrentUser.getUid();
                //set image Name to random
                final StorageReference filepath = mImageStorage.child("profile_images").child(currentUser_Uid+".jpg");
                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                        firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadUrl = uri.toString();
                                mUserDatabase.child("Image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(LogInActivity.this,"Succeed in uploading Profile Img",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            } else if (resultCode ==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
    }


    //사진 경로 가져오는 코드
    public String getPath(Uri uri){
        String [] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);
        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(index);
    }

    View.OnClickListener setGender = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.femaleBtn:
                    mGender = "Female";
                    F_Btn.setBackgroundColor(Color.parseColor("#70D398"));
                    M_Btn.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    break;
                case R.id.maleBtn:
                    mGender="Male";
                    M_Btn.setBackgroundColor(Color.parseColor("#70D398"));
                    F_Btn.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    break;
            }
        }
    };
    View.OnClickListener setAge = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.button10s :
                    mAge="10s";
                    Age1.setBackgroundColor(Color.parseColor("#70D398"));
                    Age2.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    Age3.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    Age4.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    Age5.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    break;
                case R.id.button20s :
                    mAge="20s";
                    Age2.setBackgroundColor(Color.parseColor("#70D398"));
                    Age1.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    Age3.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    Age4.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    Age5.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    break;
                case R.id.button30s :
                    mAge="30s";
                    Age3.setBackgroundColor(Color.parseColor("#70D398"));
                    Age1.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    Age2.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    Age4.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    Age5.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    break;
                case R.id.button40s :
                    mAge="40s";
                    Age4.setBackgroundColor(Color.parseColor("#70D398"));
                    Age1.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    Age2.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    Age3.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    Age5.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    break;
                case R.id.button50s :
                    mAge="50s";
                    Age5.setBackgroundColor(Color.parseColor("#70D398"));
                    Age1.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    Age2.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    Age3.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    Age4.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    break;
            }
        }
    };

    protected void onStart(){
        super.onStart();
        if(mCurrentUser==null){
            sendUserToLogin();
        }
    }
    private void sendUserToLogin(){
        Intent LoginIntent = new Intent(LogInActivity.this,LoadingActivity.class);
        LoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        LoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(LoginIntent);
        finish();
    }
    private void sendUserToMain(){
        Intent profileIntent = new Intent(LogInActivity.this,MainActivity.class);
        profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(profileIntent);
        finish();
    }
}