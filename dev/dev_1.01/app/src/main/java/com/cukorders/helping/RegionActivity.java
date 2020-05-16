package com.cukorders.helping;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class RegionActivity  extends AppCompatActivity {

   private TextView result;
   private final int PERMISSIONS_REQUEST_RESULT=1;
   Context context=this;
   private FusedLocationProviderClient mFusedLocationClient;
   private Double latittude,longitude; // 사용자의 현 위치(경도, 위도)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regional_certification1);

        // 현 위치를 입력하고 검색을 눌렀을 때
        result=(TextView) findViewById(R.id.search_result);

        // 버튼들이 클릭됐을 때=> OnClickListener 실행 : 확장성을 위해 OnClickListener 함수를 switch-case문으로 작성하였다.
        findViewById(R.id.currentLocation).setOnClickListener(OnClickListener);
        findViewById(R.id.SearchLocationButton).setOnClickListener(OnClickListener);
        findViewById(R.id.bt_back).setOnClickListener(OnClickListener);

        // 위치 권한 요청을 하기 위한 FusedLocationClient 불러옴
        mFusedLocationClient= LocationServices.getFusedLocationProviderClient(this);

    }

    View.OnClickListener OnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){ // 후에 다른 버튼이 추가됐을 시 case만 추가하면 되므로 확장성 때문에 switch 구문을 사용하였다.
                case R.id.currentLocation:
                    searchRegionalInfo(); // 현위치 주변 동들을 불러와 result(xml id: search_result)에 나타낸다.
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
        Intent intent=new Intent(this,ChooseTheRegionActivity.class); // 주변 지역 정보가 나오는 화면(regional_certification2)으로 이동
        startActivity(intent);
    }


    private void goBack(){
        Intent intent=new Intent(this,RegisterActivity.class); // 뒤로 가기 버튼 누름 => 회원가입 화면으로 다시 돌아감.
        startActivity(intent);
    }

    private void searchRegionalInfo(){
        /* TODO
        주변 지역 정보 불러와서 표시될 수 있도록 한다.*/

        //사용자에게 GPS 위치 정보 권한 요청

        if (ContextCompat.checkSelfPermission((Activity)context,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) { //위치 권한이 없을 때
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                new AlertDialog.Builder(this) // 다음 기능을 위해 위치 권한 설정을 묻는 창
                        .setTitle("위치 권한 설정")
                        .setMessage("현 위치 정보를 불러오기 위해서는 위치 권한 설정을 해야합니다.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions((Activity)context,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        PERMISSIONS_REQUEST_RESULT);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                result.setText("위치 정보를 불러올 수 없습니다.");
                            }
                        })
                        .create()
                        .show();

            } else {
                // 위치 권한을 요청
                ActivityCompat.requestPermissions((Activity)context,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_REQUEST_RESULT);

            }
        } else { // 위치 권한을 가지고 있을 때
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // 위치를 불러온다
                            if (location != null) {
                                latittude = location.getLatitude();
                                longitude = location.getLongitude(); // 현재 사용자의 경도와 위도를 불러온다.
                                result.setText(""); // 위치 정보를 불러올 수 없다는 텍스트가 남아있을 수 있으므로 지운다.
                            }
                        }
                    });

        }
    }

}
