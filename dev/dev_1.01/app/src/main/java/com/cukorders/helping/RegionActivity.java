package com.cukorders.helping;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.text.Spanned.SPAN_INCLUSIVE_EXCLUSIVE;


public class RegionActivity  extends FragmentActivity implements OnMapReadyCallback{

    private static final int PERMISSIONS_REQUEST_CODE=1000;
    private static final int REQUEST_CODE = 101;
    private static final int GPS_ENABLE_REQUEST_CODE=2001;
    private static final LatLng DEFAULT_LOCATION=new LatLng(37.555744, 126.970431);
    private static final Float DEFAULT_ZOOM=17.0f;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초
    private static final String msg1="현재 위치가 내 지역으로 설정한 ";
    private static final String msg2="에 있습니다.";
    private static final String errorMsg="선택하신 위치와 현 위치가 달라 지역 인증에 실패하였습니다.";
    private static Spinner spinner;
    private ArrayList<String> arrayList;
    private int index;
    private String loc[]=new String[3];
    private ArrayAdapter<String> arrayAdapter;
    Context context=this;

    //위치 정보 얻는 객체
    private FusedLocationProviderClient mFusedLocationClient;

    private Double latittude,longitude; // 사용자의 현 위치(경도, 위도)
    private GoogleMap mMap;
    private Marker marker=null;
    private Geocoder geocoder;
    private LocationManager locationManager;
    private ArrayList<String> now=new ArrayList<>();
    private boolean mLocationPermissionGranted=false;
    private Location currentLocation;
    private String compare;
    public String dong="";
    private TextView result_gps;
    private String errorMSG="인증을 하려면 위치 정보를 불러와야 합니다.";
    public static Context regional_certification2;
    private static FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    private static DatabaseReference databaseReference;
    private static String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regional_certification2);

        //위치 관리자 객체 참조
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);

        SupportMapFragment supportMapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googlemapview);
        supportMapFragment.getMapAsync(this);

        // 버튼들이 클릭됐을 때=> OnClickListener 실행 : 확장성을 위해 OnClickListener 함수를 switch-case문으로 작성하였다.
        findViewById(R.id.currentLocation).setOnClickListener(OnClickListener);
        findViewById(R.id.finish_location).setOnClickListener(OnClickListener);
        findViewById(R.id.bt_back).setOnClickListener(OnClickListener);
        findViewById(R.id.bt_skip).setOnClickListener(OnClickListener);

        // 위치 권한 요청을 하기 위한 FusedLocationClient 불러옴
        mFusedLocationClient= LocationServices.getFusedLocationProviderClient(this);
        geocoder=new Geocoder(this);

        result_gps=(TextView) findViewById(R.id.result_gps);

        regional_certification2=this;

        spinner=(Spinner) findViewById(R.id.location_spinner);
        arrayList=new ArrayList<>();
        arrayAdapter=new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,arrayList);
       /*
        for(int i=0;i<3;++i){

            loc[i]=((LoadingActivity)LoadingActivity.loadingActivity).userLoc[i];
            if(loc[i]==null) loc[i]="default";
            Log.d("loc[i]의 값","loc["+String.valueOf(i)+"] = "+loc[i]);
        }
        for(int i=0;i<3;++i){
            Log.d("loc","loc["+String.valueOf(i)+"]= "+loc[i]);
            if(loc[i].equals("default")){
                continue;
            }
            arrayList.add(loc[i]);
            Log.d("arrayList","새 원소 "+arrayList.get(i)+"가 추가되었습니다.");
        }*/

       Log.d("loc size","연결리스트 loc의 크기: "+((LoadingActivity)LoadingActivity.loadingActivity).loc.size());
       for(int i=0;i<((LoadingActivity)LoadingActivity.loadingActivity).loc.size();++i){
           String str=(((LoadingActivity)LoadingActivity.loadingActivity).loc).get(i);
           if(str.equals("default")) continue;
           arrayList.add(str);
       }

       compare=arrayList.get(0);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                compare=arrayList.get(position);
                index=position;
                Log.d("인증할 지역","인증할 지역 : "+compare);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(context,"하나 이상의 카테고리를 설정하세요.",Toast.LENGTH_LONG).show();
            }
        });

    }

    View.OnClickListener OnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){ // 후에 다른 버튼이 추가됐을 시 case만 추가하면 되므로 확장성 때문에 switch 구문을 사용하였다.
                case R.id.currentLocation:
                    Log.d("button is clicked","button is clicked");
                    fetchLocation(); // 현위치 주변 동들을 불러와 result(xml id: search_result)에 나타낸다.
                    break;
                case R.id.finish_location:
                    // regional_certification1에서 입력한 위치 정보를 가져온다.
                    Log.e("compare value","compare value is "+compare);
                    Log.e("the user's location is ","the user's location is "+dong);

                    if(compare.equals(dong)){
                        ((LoadingActivity)LoadingActivity.loadingActivity).isCertified[index]=true;
                        Log.e("인증 여부","인증 여부 : "+((LoadingActivity)LoadingActivity.loadingActivity).isCertified[index]);
                        if(firebaseUser!=null){
                            uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                            databaseReference= FirebaseDatabase.getInstance().getReference().child("userRegions").child(uid);
                            int index=0;
                            for(;index<3;++index){
                                if(compare.equals(dong)){
                                    Log.d("지역 인증 여부 업데이트",compare+"의 지역 인증여부가 업데이트 되었습니다.");
                                    break;
                                }
                            }
                            Map<String, Object> updates=new HashMap<String, Object>();
                                updates.put("Region"+String.valueOf(index+1)+" state",true);
                                Log.d("지역 인증 업데이트","지역 인증이 업데이트 되었습니다.");
                            databaseReference.updateChildren(updates);
                        }
                        goMain();
                    }
                    else{
                        // 인증 실패 에러 메시지 띄움
                        ((LoadingActivity)LoadingActivity.loadingActivity).isCertified[index]=false;
                        Toast.makeText(context,errorMsg,Toast.LENGTH_LONG).show();
                    }
                    break;

                case R.id.bt_back:
                    goBack(); //이전 페이지로 가기(뒤로 가기 버튼이 눌렸을 때)
                    break;

              case R.id.bt_skip:
                  ((LoadingActivity)LoadingActivity.loadingActivity).isCertified[index]=false;
                    Log.d("a skip button","a skip button is clicked");
                    Toast.makeText(context,"지역 인증을 완료하려면 마이페이지>지역 인증에서 완료하시길 바랍니다.",Toast.LENGTH_LONG).show();
                    goMain();
                    break;
            }
        }
    };

    private void goMain(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void goBack(){
        Intent intent=new Intent(this,ChooseTheRegionActivity.class); // 뒤로 가기 버튼 누름 => 첫 화면으로 다시 돌아감.
        startActivity(intent);
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            Log.e("Permission is denied","Location permission is denied");
            return;
        }
        Task<Location> task = mFusedLocationClient.getLastLocation();
        Log.e("get the location","get the location");
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Log.d("current Location is ",currentLocation.toString());
                    LatLng now=new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                    List<Address> addr = null;
                    String loc="";
                    try {
                        addr=geocoder.getFromLocation(currentLocation.getLatitude(),currentLocation.getLongitude(), 5);
                        if(addr.size()>0){
                            android.location.Address address = addr.get(0);
                            loc = address.getAddressLine(0)+address.getLocality();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address cur=addr.get(0);
                    dong=cur.getThoroughfare();
                    Log.d("current Location",dong);
                    int start=msg1.length(),end=start+dong.length();
                    MarkerOptions markerOptions = new MarkerOptions().position(now).title(dong);
                    result_gps.setText("현재 위치가 내 지역으로 설정한 "+dong+"에 있습니다.");
                    Spannable span=(Spannable) result_gps.getText();
                    span.setSpan(new StyleSpan(Typeface.BOLD),start,end, SPAN_INCLUSIVE_EXCLUSIVE);
                    span.setSpan(new ForegroundColorSpan(Color.RED),start,end, SPAN_INCLUSIVE_EXCLUSIVE);
                    result_gps.setText(span);

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(now));
                    mMap.addMarker(markerOptions);
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googlemapview);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync((OnMapReadyCallback) context);
                } else{
                    //TODO make the location parameter return non-null value
                    Log.e("location is empty","a location parameter returns null");
                    Toast.makeText(context,"위치 정보를 불러오는데 실패하였습니다.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(DEFAULT_LOCATION));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                } else{
                    Toast.makeText(this,errorMSG,Toast.LENGTH_LONG).show();
                }
                break;

            default:
                Toast.makeText(this,errorMSG,Toast.LENGTH_LONG).show();
                break;
        }
    }
}