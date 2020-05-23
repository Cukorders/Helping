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

import java.io.IOException;
import java.util.List;

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
   Context context=this;
   //위치 정보 얻는 객체
   private FusedLocationProviderClient mFusedLocationClient;

   private Double latittude,longitude; // 사용자의 현 위치(경도, 위도)
   private GoogleMap mMap;
   private Marker marker=null;
    private Geocoder geocoder;
    private LocationManager locationManager;
    private boolean mLocationPermissionGranted=false;
    private Location currentLocation;
    private String compare;
    public String dong="";
    private TextView result_gps;
    private String errorMSG="인증을 하려면 위치 정보를 불러와야 합니다.";

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
                    compare=((ChooseTheRegionActivity)ChooseTheRegionActivity.regional_certification1).userLocation;
                    Log.e("compare value","compare value is "+compare);
                    Log.e("the user's location is ","the user's location is "+dong);

                    if(compare.equals(dong)){
                        //TODO : DB에서 위치 인증이 완료된 유저라고 체크되어야 함.
                    goPhoneAuth();
                    } else{
                        // 인증 실패 에러 메시지 띄움
                        Toast.makeText(context,errorMsg,Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.bt_back:
                    goBack(); //이전 페이지로 가기(뒤로 가기 버튼이 눌렸을 때)
                    break;

                case R.id.bt_skip:
                    //TODO : DB에서 인증 안 된 유저라고 체크해야 함.
                    goPhoneAuth(); // 바로 전화 인증으로 건너 뜀.
                    break;
            }
        }
    };

    private void goPhoneAuth(){
        Intent intent=new Intent(this,AuthActivity.class);
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
                    //Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + " " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
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