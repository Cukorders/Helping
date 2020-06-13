package com.cukorders.helping;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class PostActivity extends AppCompatActivity {

    public Context postActivity;
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
    private static RelativeLayout[] pic =new RelativeLayout[3];
    private boolean photoCheck[]=new boolean[2];
    private static final int GALLERY_CODE = 10;
    private StorageReference storageReference;
    private static StorageReference firebaseStorage;
    private static int count=0;
    private static Calendar calendar;
    private static DatePickerDialog.OnDateSetListener datePicker=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        }
    };

    private String imagePath;
    private String images[]=new String[3];
    private String postKey;
    private static final String TAG="PostActivity";
    private String nowLocation;
    private static final int PICK_IMAGE=1;
    private ArrayList<Uri> ImageList=new ArrayList<Uri>();
    private Uri ImageUri;
    private int index;
    private final int GALLERY_REQUEST=1;
    private final int GALLERY_REQUEST2=2;
    private final int GALLERY_REQUEST3=3;
    private final int GET_GALLERY_IMAGE=200;
    private Uri mImageUri[]=new Uri[3];
    private static StorageReference mStorage;
    private static String img[]=new String[3];

    @SuppressLint("LongLogTag")
    private static void init_ageChecked(boolean ageChecked[]){
        Log.d("init the array ageChecked","init the array ageChecked");
        for(int i=0;i<5;++i)
            ageChecked[i]=false;
    }

    private static void initPhotoCheck(boolean[] photoCheck){
        for(int i=0;i<2;++i)
            photoCheck[i]=false;
    }

    /*private static void setPhotoButton(boolean[] photoCheck){
        for(int i=1;i<3;++i)
            pic[i].setVisibility(photoCheck[i]?View.VISIBLE:View.INVISIBLE);
    }*/

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

        postActivity=this;
        firebaseStorage=FirebaseStorage.getInstance().getReference();
        uid=user.getUid();
        calendar=Calendar.getInstance();

        title=(TextView) findViewById(R.id.title);
        due=(TextView) findViewById(R.id.due);
        pay=(TextView) findViewById(R.id.pay);
        endTime=(TextView) findViewById(R.id.endTime);
        cancelTime=(TextView) findViewById(R.id.cancelTime);
        price=(TextView) findViewById(R.id.price);
        description=(TextView) findViewById(R.id.description);
        place=(TextView) findViewById(R.id.place);

        for(int i=0;i<3;++i){
            mImageUri[i]=null;
        }
        mStorage=FirebaseStorage.getInstance().getReference();

        //카테고리 선택 스피너
        category=(Spinner) findViewById(R.id.category);
        arrayList=new ArrayList<>();
        arrayList.add("구매 대행(음식)");
        arrayList.add("구매 대행(음식 외 물품)");
        arrayList.add("배달(음식)");
        arrayList.add("배달(음식 외 물품)");
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
                if(Category.equals("구매 대행(음식)")||Category.equals("배달(음식)")){
                    cancelTime.setEnabled(false);
                }else{
                    cancelTime.setEnabled(true);
                }
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
        pic[0]=(RelativeLayout) findViewById(R.id.photo1);
        pic[1]=(RelativeLayout) findViewById(R.id.photo2);
        pic[2]=(RelativeLayout) findViewById(R.id.photo3);

        for(int i=1;i<=2;++i) photo[i].setEnabled(false);

       // imageRef=FirebaseStorage.getInstance().getReference().child("PostImage");

        //기본 기능 버튼
        findViewById(R.id.back_button_write_post).setOnClickListener(onClickListener);
        findViewById(R.id.bt_filter).setOnClickListener(onClickListener);
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

        // 날짜 선택
        findViewById(R.id.endTime).setOnClickListener(setTime);
        findViewById(R.id.cancelTime).setOnClickListener(setTime);

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
        postKey=getRandomString(50);
        nowLocation= RecentMissionFragment.location_now;
        for(int i=0;i<3;++i){
            images[i]="default";
            img[i]="default";
        }
    }

    private void updateLabel(TextView txt){
        String format="yyyy/MM/dd";
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format, Locale.KOREA);
        txt.setText(simpleDateFormat.format(calendar.getTime()));
    }

    private String getRandomString(int length) {
        final String characters="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890!@%_&*^+-=";
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

                    post = new Post(Title, Description, EndTime, CancelTime, Place, Pay, Due, Price, uid, sameGender, Age,Category,postKey,nowLocation,false,false);
                    databaseReference= FirebaseDatabase.getInstance().getReference().child("Posting").child(postKey);
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

    View.OnClickListener setTime=new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.endTime:
                    select_times();
                    Log.e("selectedTime의 값","selectedTime= "+CustomPicker.selectedTime);
                    endTime.setText(CustomPicker.selectedTime);
                    Log.e("endTime","endTime의 값 = "+endTime.getText());
                    break;

                case R.id.cancelTime:
                    select_times();
                    Log.e("selectedTime의 값","selectedTime= "+CustomPicker.selectedTime);
                    cancelTime.setText(CustomPicker.selectedTime);
                    Log.e("cancelTime","cancelTime의 값: "+cancelTime.getText());
                    break;
            }

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void select_times(){
        CustomPicker customPicker=new CustomPicker(context);
        customPicker.show();
    }

    View.OnClickListener addPhoto=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.camera_album_add1:
                    photoCheck[0]=true;
                  //  setPhotoButton(photoCheck);
                  photo[1].setEnabled(true);
                    Log.d(TAG,"사진 추가 버튼이 클릭되었습니다.");
                    Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                    startActivityForResult(intent,GALLERY_REQUEST);
                    break;

                case R.id.camera_album_add2:
                    Log.d(TAG,"사진 추가 버튼이 클릭되었습니다.");
                    Intent intent2=new Intent(Intent.ACTION_GET_CONTENT);
                    photo[2].setEnabled(true);
                    intent2.setType("image/*");
                    intent2.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                    startActivityForResult(intent2,GALLERY_REQUEST2);
                    break;

                case R.id.camera_album_add3:
                    Log.d(TAG,"사진 추가 버튼이 클릭되었습니다.");
                    Intent intent3=new Intent(Intent.ACTION_GET_CONTENT);
                    intent3.setType("image/*");
                    intent3.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                    startActivityForResult(intent3,GALLERY_REQUEST3);
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

    public class Post{
        String title,description,endTime,cancelTime,place,uid,category,postKey,location;
        int pay,due,price,age;
        boolean sameGender,isMatched,isFinished;
        String image[]=new String[3];

        public Post(String title,String description,String endTime,String cancelTime,String place,int pay,int due,int price,String uid,boolean sameGender,int age,String category,String postKey,String location,boolean isMatched,boolean isFinished){
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
            this.isMatched=isMatched;
            this.isFinished=isFinished;
        }

        public String getTitle(){return title;}
        public String getDescription(){return description;}
        public String getEndTime(){return endTime;}
        public String getCancelTime(){return cancelTime;}
        public String getPlace(){return place;}
        public String getUid(){return uid;}
        public String getPostKey(){return postKey;}
        public String getCategory(){return category;}
        public String getLocation(){return location;}
        public int getPay(){return pay;}
        public int getDue(){return due;}
        public int getPrice(){return price;}
        public boolean isMatched(){return isMatched;}
        public boolean isFinished(){return isFinished;}
        public boolean isSameGender(){return sameGender;}

        public Map<String,Object> toMap(){
            final HashMap<String,Object> ret=new HashMap<>();
            ret.put("title",title);
            ret.put("description",description);
            ret.put("endTime",endTime);
            ret.put("cancelTime",cancelTime);
            ret.put("place",place);
            ret.put("pay",pay);
            ret.put("due",due);
            ret.put("price",price);
            ret.put("uid",uid);
            String tmp="";
            for(int i=0;i<5;++i)
                if(ageChecked[i])
                   tmp+=Integer.toString(i+1); // 선택한 연령대를 모두 age column에 삽입한다.
            ret.put("age",tmp);
            ret.put("gender",sameGender?"동성":"무관");
            ret.put("category",category);
            ret.put("postKey",postKey);
            ret.put("location",location);
            ret.put("isMatched",isMatched?"1":"0");
            ret.put("isFinished",isFinished?"1":"0");
            ret.put("isSended","0");
            for(int i=0;i<3;++i)
                ret.put("image"+(i+1),img[i]);
            return ret;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            switch (requestCode){
                case GALLERY_REQUEST:
                    mImageUri[0]=data.getData();
                    photo[0].setImageURI(mImageUri[0]);
                    break;

                case GALLERY_REQUEST2:
                    mImageUri[1]=data.getData();
                    photo[1].setImageURI(mImageUri[1]);
                    break;

                case GALLERY_REQUEST3:
                    mImageUri[2]=data.getData();
                    photo[2].setImageURI(mImageUri[2]);
                    break;
            }
        }
        StorageReference filepath=mStorage.child("post_images").child(postKey+"1.jpg");
        StorageReference filepath2=mStorage.child("post_images").child(postKey+"2.jpg");
        StorageReference filepath3=mStorage.child("post_images").child(postKey+"3.jpg");
        if(mImageUri[0]!=null){
        filepath.putFile(mImageUri[0]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                            firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadUrl = uri.toString();
                                    Log.d(TAG,"downloadUrl의 값: "+downloadUrl);
                                    img[0]=downloadUrl;
                                    Log.d(TAG,"img[1]의 값: "+img[0]);
                                }
                            });
                        }
                    });
        }
        if(mImageUri[1]!=null){
        filepath2.putFile(mImageUri[1]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final String downloadUrl = uri.toString();
                        Log.d(TAG,"downloadUrl의 값: "+downloadUrl);
                        img[1]=downloadUrl;
                        Log.d(TAG,"img[1]의 값: "+img[1]);
                    }
                });
            }
        });
        }
        if(mImageUri[2]!=null){
        filepath3.putFile(mImageUri[2]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final String downloadUrl = uri.toString();
                        Log.d(TAG,"downloadUrl의 값: "+downloadUrl);
                        img[2]=downloadUrl;
                        Log.d(TAG,"img[1]의 값: "+img[2]);
                    }
                });
            }
        });
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
}