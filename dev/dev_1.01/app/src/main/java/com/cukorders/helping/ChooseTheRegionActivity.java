package com.cukorders.helping;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class ChooseTheRegionActivity  extends AppCompatActivity {

    private static final int REQUEST_CODE = 101;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager locationManager;
    private Geocoder geocoder;
    private TextView editTextQuery;
    public String userLocation;
    private Location nowLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regional_certification1);

        findViewById(R.id.bt_back).setOnClickListener(OnClickListener);
        findViewById(R.id.currentLocation).setOnClickListener(OnClickListener);
        findViewById(R.id.SearchLocationButton).setOnClickListener(OnClickListener);

        mFusedLocationClient= LocationServices.getFusedLocationProviderClient(this);
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        editTextQuery=(TextView) findViewById(R.id.editTextQuery);
        geocoder=new Geocoder(this);

    }

    // 버튼이 클릭됐을 때 설정
    View.OnClickListener OnClickListener=new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.bt_back:
                    goBack();
                    break;

                case R.id.SearchLocationButton:
                    userLocation=editTextQuery.getText().toString();
                    break;

                case R.id.currentLocation: //TODO : 위치 정보 권한 받아오기/ 역지오코딩하기
                    fetchLocation();
                    break;
            }
        }
    };

    private void goBack(){
        Intent intent=new Intent(this,LoadingActivity.class);
        startActivity(intent);
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
                    nowLocation = location;
                    LatLng now=new LatLng(nowLocation.getLatitude(),nowLocation.getLongitude());

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