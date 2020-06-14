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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.cukorders.Fragment.RecentMissionFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PostActivity extends AppCompatActivity {

    //userlikes
    private DatabaseReference Userlikes;


    public final Context regional_certification2=this;
    private final Context context=this;
    private boolean sameGender;
    private boolean ageChecked[]=new boolean[5]; // 연령이 체크 됐는지 안 됐는지를 확인 여부
    private Button bt_same,bt_dontMind;
    private Button age[]=new Button[5]; // 연령 버튼
    private TextView title, description, pay,due,price,endTime,cancelTime,place;
    private String Title,Description,EndTime,CancelTime,Place,Category;
    private DatabaseReference databaseReference;
    private HashMap<String,Object> childUpdate=null;
    private HashMap<String,Object> postValue=null;
    private Map<String,Object> userValue=null;
    private int Pay,Due,Price,Age;
    private final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser(); // 현재 위저를 불러온다.
    private Post post;
    private String uid=user.getUid();
    private Spinner category;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private ImageButton photo[]=new ImageButton[3];
    private static RelativeLayout[] pic =new RelativeLayout[2];
    private boolean photoCheck[]=new boolean[2];
    private static final int GALLERY_CODE = 10;
    private StorageReference storageReference;
    private String imagePath;
    private String postKey;
    private String nowLocation;

    @SuppressLint("LongLogTag")
    private static void init_ageChecked(boolean ageChecked[]){
        Log.d("init the array ageChecked","init the array ageChecked");
        for(int i=0;i<5;++i)
            ageChecked[i]=false;
    }

    private static void ageValue(boolean[] ageChecked){
        boolean flag=false;
        for(int i=0;i<5;++i){

        }
    }

    private static void initPhotoCheck(boolean[] photoCheck){
        for(int i=0;i<2;++i)
            photoCheck[i]=false;
    }

    private static void setPhotoButton(boolean[] photoCheck){
        for(int i=0;i<2;++i)
            pic[i].setVisibility(photoCheck[i]?View.VISIBLE:View.INVISIBLE);
    }

    private static int checkedAge(boolean[] ageChecked){
        int ret=0;
        for(int i=0;i<5;++i)
            if(ageChecked[i]){
                ret=(i+1)*10;
                break;
            }
        return ret;
    }
    @SuppressLint("LongLogTag")
    private void checkAgeButton(Button age[], boolean ageChecked[]) {
        Log.d("background color of an age button", "set the background color of the age buttons");
        boolean flag = false;
        for (int i = 0; i < 5; ++i) {
            age[i].setBackgroundColor(Color.parseColor(ageChecked[i] ? "#70D398" : "#e1e1e1"));
            Log.d("a button is checked", String.valueOf((i + 1) * 10) + " is " + (ageChecked[i] ? "" : "not ") + "checked");
            if (ageChecked[i]) {
                flag = true;
            }
        }
        if(!flag){
            ageChecked[0]=true;
            age[0].setBackgroundColor(Color.parseColor("#70D398"));
            Toast.makeText(context,"하나 이상의 연령층을 선택해야 합니다.",Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_post);

        uid=user.getUid();
        title=(TextView) findViewById(R.id.title);
        due=(TextView) findViewById(R.id.due);
        pay=(TextView) findViewById(R.id.pay);
        endTime=(TextView) findViewById(R.id.endTime);
        cancelTime=(TextView) findViewById(R.id.cancelTime);
        price=(TextView) findViewById(R.id.price);
        description=(TextView) findViewById(R.id.description);
        place=(TextView) findViewById(R.id.place);
        //카테고리 선택 스피너
        category=(Spinner) findViewById(R.id.category);
        arrayList=new ArrayList<>();
        arrayList.add("구매 대행(음식)");
        arrayList.add("구매 대행(음식 외 상품)");
        arrayList.add("배달");
        arrayList.add("청소");
        arrayList.add("과외");
        arrayList.add("기타");
        arrayAdapter=new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,arrayList);
        category.setAdapter(arrayAdapter);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category=arrayList.get(position);
                Log.d("카테고리","선택된 카테고리 : "+Category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(context,"하나 이상의 카테고리를 설정하세요.",Toast.LENGTH_LONG).show();
            }
        });
        // 사진 추가 버튼
        photo[0]=(ImageButton) findViewById(R.id.camera_album_add1);
        photo[1]=(ImageButton) findViewById(R.id.camera_album_add2);
        photo[2]=(ImageButton) findViewById(R.id.camera_album_add3);
        pic[0]=(RelativeLayout) findViewById(R.id.photo2);
        pic[1]=(RelativeLayout) findViewById(R.id.photo3);

        //기본 기능 버튼
        findViewById(R.id.back_button_write_post).setOnClickListener(onClickListener);
        findViewById(R.id.bt_finish).setOnClickListener(onClickListener);
        findViewById(R.id.bt_post).setOnClickListener(onClickListener);
        // 동성 or 무관
        findViewById(R.id.bt_same).setOnClickListener(checkGender);
        findViewById(R.id.bt_dontMind).setOnClickListener(checkGender);
        // 연령대 버튼
        findViewById(R.id.button10s).setOnClickListener(checkAge);
        findViewById(R.id.button20s).setOnClickListener(checkAge);
        findViewById(R.id.button30s).setOnClickListener(checkAge);
        findViewById(R.id.button40s).setOnClickListener(checkAge);
        findViewById(R.id.button50s).setOnClickListener(checkAge);
        //이미지 삽입
        findViewById(R.id.camera_album_add1).setOnClickListener(addPhoto);
        findViewById(R.id.camera_album_add2).setOnClickListener(addPhoto);
        findViewById(R.id.camera_album_add3).setOnClickListener(addPhoto);

        bt_same=(Button) findViewById(R.id.bt_same);
        bt_dontMind=(Button) findViewById(R.id.bt_dontMind);
        age[0]=(Button) findViewById(R.id.button10s);
        age[1]=(Button) findViewById(R.id.button20s);
        age[2]=(Button) findViewById(R.id.button30s);
        age[3]=(Button) findViewById(R.id.button40s);
        age[4]=(Button) findViewById(R.id.button50s);

        sameGender=true;
        init_ageChecked(ageChecked);
        ageChecked[0]=true; // age의 default값이 10대이므로
        childUpdate=new HashMap<>();
        postValue=new HashMap<>();
        initPhotoCheck(photoCheck);
        storageReference= FirebaseStorage.getInstance().getReference();
        postKey=getRandomString(25);
        nowLocation=RecentMissionFragment.location_now;
    }

    private String getRandomString(int length) {
        final String characters="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890!@#$%";
        StringBuilder stringBuilder=new StringBuilder();
        while(length-- >0){
            Random random=new Random();
            stringBuilder.append(characters.charAt(random.nextInt(characters.length())));
        }
        return stringBuilder.toString();
    }

    final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back_button_write_post:
                    startActivity(new Intent(context, MainActivity.class));
                    break;

                default: //when a button bt_finish and a button bt_post are clicked
                    Title = title.getText().toString();
                    Description = description.getText().toString();
                    Pay = Integer.parseInt(pay.getText().toString());
                    Due = Integer.parseInt(due.getText().toString());
                    Price = Integer.parseInt(price.getText().toString());
                    Place = place.getText().toString();
                    EndTime = endTime.getText().toString();
                    CancelTime = cancelTime.getText().toString();
                    Age = checkedAge(ageChecked);
                    Log.d("title", "title is " + Title);
                    Log.d("Description", "description of the post is " + Description);

                    post = new Post(Title, Description, EndTime, CancelTime, Place, Pay, Due, Price, uid, sameGender, Age,Category,postKey,nowLocation);
                    databaseReference= FirebaseDatabase.getInstance().getReference().child("Posting").child(Title);
                    childUpdate= (HashMap<String, Object>) post.toMap();
                    databaseReference.setValue(childUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context,"포스트가 성공적으로 업로드 되었습니다.",Toast.LENGTH_LONG).show();
                                Log.e("a post is uploaded", "a post is successfully uploaded");
                            } else{
                                Toast.makeText(context,"오류가 발생하여 포스트 업로드에 실패하였습니다. 잠시 후에 다시 시도해주십시오.",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    startActivity(new Intent(context, MainActivity.class));
                    break;
            }
        }
    };

    View.OnClickListener addPhoto=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.camera_album_add1:
                    photoCheck[0]=true;
                    setPhotoButton(photoCheck);

                    break;

                case R.id.camera_album_add2:
                    photoCheck[1]=true;
                    setPhotoButton(photoCheck);

                    break;

                case R.id.camera_album_add3:

                    break;

            }
        }
    };

    View.OnClickListener checkGender=new View.OnClickListener() {
        @SuppressLint("LongLogTag")
        @Override
        public void onClick(View v) {
            switch(v.getId()){
            case R.id.bt_same:
                Log.d("a same gender button is clicked","a same gender button is clicked");
                sameGender=true;
                Log.e("a value of sameGender","a value of sameGender is "+sameGender);
                bt_same.setBackgroundColor(Color.parseColor("#70D398"));
                bt_dontMind.setBackgroundColor(Color.parseColor("#e1e1e1"));
                break;

                case R.id.bt_dontMind:
                    Log.d("a don't mind button is clicked","a don't mind button is clicked");
                    sameGender=false;
                    Log.e("a value of sameGender","a value of sameGender is "+sameGender);
                    bt_dontMind.setBackgroundColor(Color.parseColor("#70D398"));
                    bt_same.setBackgroundColor(Color.parseColor("#e1e1e1"));
                    break;
        }
        }
    };

    View.OnClickListener checkAge=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button10s:
                    Log.d("a button 10s is clicked","a button 10s is clicked");
                    ageChecked[0]=!ageChecked[0];
                    checkAgeButton(age,ageChecked);
                    break;

                case R.id.button20s:
                    Log.d("a button 20s is clicked","a button 20s is clicked");
                    ageChecked[1]=!ageChecked[1];
                    checkAgeButton(age,ageChecked);
                    break;

                case R.id.button30s:
                    ageChecked[2]=!ageChecked[2];
                    Log.e("an age value","an age30s button is clicked. and its value is "+ageChecked[2]);
                    checkAgeButton(age,ageChecked);
                    break;

                case R.id.button40s:
                    ageChecked[3]=!ageChecked[3];
                    Log.e("an age value","an age40s button is clicked. and its value is "+ageChecked[3]);
                    checkAgeButton(age,ageChecked);
                    break;

                default:
                    ageChecked[4]=!ageChecked[4];
                    checkAgeButton(age,ageChecked);
                    break;
            }
        }
    };

    public String getPath(Uri uri){
        String [] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);
        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(index);
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
        if(requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode ==RESULT_OK){
                Uri resultUri = result.getUri();
                String currentUser_Uid = uid;
                //set image Name to random
                //TODO currentUser UID가 아닌 게시판 고유키값을 테이블 프라이머리 키로 지정
                final StorageReference filepath = storageReference.child("post_images").child(currentUser_Uid+".jpg");
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
                                            Toast.makeText(context,"이미지 파일을 성공적으로 업로드하였습니다.",Toast.LENGTH_LONG).show();
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

    class Post{
        String title,description,endTime,cancelTime,place,uid,category,postKey,location;
        int pay,due,price,age;
        boolean sameGender,isMatched,isFinished;
        public Post(String title,String description,String endTime,String cancelTime,String place,int pay,int due,int price,String uid,boolean sameGender,int age,String category,String postKey,String location){
            this.title=title;
            this.description=description;
            this.endTime=endTime;
            this.cancelTime=cancelTime;
            this.location=location;
            this.place=place;
            this.pay=pay;
            this.due=due;
            this.price=price;
            this.uid=uid;
            this.category=category;
            this.sameGender=sameGender;
            this.age=age;
            this.postKey=postKey;
            this.isMatched=false;
            this.isFinished=false;
        }

        public String getTitle(){return title;}
        public String getDescription(){return description;}
        public String getEndTime(){return endTime;}
        public String getCancelTime(){return cancelTime;}
        public String getPlace(){return place;}
        public String getUid(){return uid;}
        public int getPay(){return pay;}
        public int getDue(){return due;}
        public int getPrice(){return price;}
        public boolean isSameGender(){return sameGender;}

        public Map<String,Object> toMap(){
            HashMap<String,Object> ret=new HashMap<>();
            ret.put("title",title);
            ret.put("description",description);
            ret.put("endTime",endTime);
            ret.put("cancelTime",cancelTime);
            ret.put("place",place);
            ret.put("pay",pay);
            ret.put("due",due);
            ret.put("price",price);
            ret.put("uid",uid);
            ret.put("image1","default");
            ret.put("image2","default");
            ret.put("image3","default");
            String tmp="";
            for(int i=0;i<5;++i)
                if(ageChecked[i])
                   tmp+=Integer.toString(i+1); // 선택한 연령대를 모두 age column에 삽입한다.
            ret.put("age",tmp);
            ret.put("gender",sameGender?"동성":"무관");
            ret.put("category",category);
            ret.put("post key",postKey);
            ret.put("location",location);
            ret.put("매칭 여부",isMatched);
            ret.put("미션 완료 여부",isFinished);
            return ret;
        }
    }

}