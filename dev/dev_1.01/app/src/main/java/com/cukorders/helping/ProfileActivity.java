package com.cukorders.helping;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private static Context context;
    private TextView nickname;
    private CircleImageView userProfileImage;
    private String imagePath;
    private Button bt_male,bt_female;
    private boolean isMale;
    private Button age[]=new Button[5];
    private int userAge=1;
    private String userNickname;

    private static DatabaseReference databaseReference;
    private static String uid;
    private static StorageReference firebaseStorage;
    private static final int GALLERY_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_profile);

        context=this;
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseStorage=FirebaseStorage.getInstance().getReference();
        userProfileImage=(CircleImageView) findViewById(R.id.userProfileImage);

        bt_male=(Button) findViewById(R.id.button_male);
        bt_female=(Button) findViewById(R.id.button_female);
        age[0]=(Button) findViewById(R.id.button);
        age[1]=(Button) findViewById(R.id.button2);
        age[2]=(Button) findViewById(R.id.button3);
        age[3]=(Button) findViewById(R.id.button4);
        age[4]=(Button) findViewById(R.id.button5);

        for(int i=0;i<5;++i)
            age[i].setOnClickListener(onClickListener);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String key=snapshot.getKey();
                    Log.d("key","유저 key: "+key);
                    String image,gender;
                    if(uid.equals(key)){
                        HashMap<String,String> info=(HashMap<String,String>)snapshot.getValue();
                        image=snapshot.child("Image").getValue().toString();
                        gender=snapshot.child("Gender").getValue().toString();
                        (gender.equals("Male")?bt_male:bt_female).setBackgroundColor(Color.parseColor("#70D398"));
                        userAge=Integer.parseInt(String.valueOf(snapshot.child("Age").getValue().toString().charAt(0)))-1;
                        Log.d("사용자 나이대","사용자 나이대: "+String.valueOf(userAge));
                        userNickname=snapshot.child("Nickname").getValue().toString();
                        isMale=gender.equals("Male");
                        Picasso.get().load(image).into(userProfileImage);
                    }
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error in getting the user profile img","프로필 이미지를 불러오는데 실패하였습니다.");
            }
        });

        age[userAge].setBackgroundColor(Color.parseColor("#70D398"));

        findViewById(R.id.profileRegisterBt).setOnClickListener(onClickListener);
        findViewById(R.id.bt_back).setOnClickListener(onClickListener);
        findViewById(R.id.cameraBtn).setOnClickListener(onClickListener);
        findViewById(R.id.button_male).setOnClickListener(onClickListener);
        findViewById(R.id.button_female).setOnClickListener(onClickListener);

        nickname=(TextView) findViewById(R.id.new_nickname);
    }

    View.OnClickListener onClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_back:
                    startActivity(new Intent(context,MyPageActivity.class));
                    break;

                case R.id.profileRegisterBt:
                    // TODO DB에 수정 내용 업데이트
                    HashMap<String,String> information=new HashMap<>();
                    information.put("Age",String.valueOf(userAge)+"0s");
                    information.put("Gender",isMale?"Male":"Female");
                    information.put("Nickname",nickname.toString().equals("")?userNickname:nickname.toString());

                    Toast.makeText(context,"프로필 수정이 완료되었습니다.",Toast.LENGTH_LONG).show();
                    break;

                case R.id.cameraBtn:
                    Intent getAlbumIntent = new Intent(Intent.ACTION_PICK);
                    getAlbumIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(getAlbumIntent,GALLERY_CODE);
                    break;

                case R.id.button_female:
                    isMale=false;
                    (isMale?bt_male:bt_female).setBackgroundColor(Color.parseColor("#70D398"));
                    (!isMale?bt_male:bt_female).setBackgroundColor(Color.parseColor("#e1e1e1"));
                    break;

                case R.id.button_male:
                    isMale=true;
                    (isMale?bt_male:bt_female).setBackgroundColor(Color.parseColor("#70D398"));
                    (!isMale?bt_male:bt_female).setBackgroundColor(Color.parseColor("#e1e1e1"));
                    break;

                case R.id.button:
                    userAge=1;
                    setColors();
                    break;

                case R.id.button2:
                    userAge=2;
                    setColors();
                    break;

                case R.id.button3:
                    userAge=3;
                    setColors();
                    break;

                case R.id.button4:
                    userAge=4;
                    setColors();
                    break;
                case R.id.button5:
                    userAge=5;
                    setColors();
                    break;
            }
        }
    };

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
                String currentUser_Uid = uid;
                //set image Name to random
                final StorageReference filepath = firebaseStorage.child("profile_images").child(currentUser_Uid+".jpg");
                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                        firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadUrl = uri.toString();
                                databaseReference.child("Image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Picasso.get().load(downloadUrl).into(userProfileImage);
                                            Toast.makeText(context,"Succeed in uploading Profile Img",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
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

    private void setColors(){
        for(int i=0;i<5;++i)
            age[i].setBackgroundColor(Color.parseColor(i+1==userAge?"#70D398":"#e1e1e1"));
    }
}
