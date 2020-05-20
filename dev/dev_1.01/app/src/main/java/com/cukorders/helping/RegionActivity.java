package com.cukorders.helping;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
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


public class RegionActivity  extends FragmentActivity implements OnMapReadyCallback{

   private static final int PERMISSIONS_REQUEST_CODE=1000;
    private static final int REQUEST_CODE = 101;
   private static final int GPS_ENABLE_REQUEST_CODE=2001;
   private static final LatLng DEFAULT_LOCATION=new LatLng(37.555744, 126.970431);
   private static final Float DEFAULT_ZOOM=17.0f;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초
   Context context=this;
   //위치 정보 얻는 객체
   private FusedLocationProviderClient mFusedLocationClient;

   private Double latittude,longitude; // 사용자의 현 위치(경도, 위도)
   private GoogleMap mMap;
   private Marker marker=null;
    private Geocoder geocoder;
    private LocationManager locationManager;
    private boolean mLocationPermissionGranted=false;
    Location currentLocation;

    public String dong="";
    private TextView getDong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regional_certification1);

        //위치 관리자 객체 참조
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);

        SupportMapFragment supportMapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        // 버튼들이 클릭됐을 때=> OnClickListener 실행 : 확장성을 위해 OnClickListener 함수를 switch-case문으로 작성하였다.
        findViewById(R.id.currentLocation).setOnClickListener(OnClickListener);
        findViewById(R.id.SearchLocationButton).setOnClickListener(OnClickListener);
        findViewById(R.id.bt_back).setOnClickListener(OnClickListener);

        // 위치 권한 요청을 하기 위한 FusedLocationClient 불러옴
        mFusedLocationClient= LocationServices.getFusedLocationProviderClient(this);
        geocoder=new Geocoder(this);

        getDong=(TextView) findViewById(R.id.editTextQuery);
    }

    View.OnClickListener OnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){ // 후에 다른 버튼이 추가됐을 시 case만 추가하면 되므로 확장성 때문에 switch 구문을 사용하였다.
                case R.id.currentLocation:
                    fetchLocation(); // 현위치 주변 동들을 불러와 result(xml id: search_result)에 나타낸다.
                    break;
                case R.id.SearchLocationButton:
                    getRegionInfo(); // 현재 위치로 찾기 버튼이 클릭됐을 시 주변 지역 정보가 나오는 화면(regional_certification2)으로 이동한다.
                    break;
                case R.id.bt_back:
                    goBack(); //이전 페이지로 가기(뒤로 가기 버튼이 눌렸을 때)
                    break;

            }
        }
    };

    private void getRegionInfo(){
        List<Address> list=null;
        String str=getDong.getText().toString();
        Toast.makeText(this,str,Toast.LENGTH_LONG).show();
        try{
            list = geocoder.getFromLocationName
                    (str, // 지역 이름
                            10); // 읽을 개수
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list==null) Toast.makeText(this,"위치 정보를 불러올 수 없습니다.",Toast.LENGTH_LONG).show();
        else{
            if(list.size()>0){
                Address address=list.get(0);
               // Location location=new
            }else{
                Toast.makeText(this,"해당되는 주소 정보를 찾을 수 없습니다.",Toast.LENGTH_LONG);
            }
        }
    }


    private void goBack(){
        Intent intent=new Intent(this,LoadingActivity.class); // 뒤로 가기 버튼 누름 => 첫 화면으로 다시 돌아감.
        startActivity(intent);
    }

   @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(DEFAULT_LOCATION));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }


    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = mFusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
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
                    MarkerOptions markerOptions = new MarkerOptions().position(now).title(dong);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(now));
                    mMap.addMarker(markerOptions);
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync((OnMapReadyCallback) context);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }



}