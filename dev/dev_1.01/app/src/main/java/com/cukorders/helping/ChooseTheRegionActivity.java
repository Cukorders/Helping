package com.cukorders.helping;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ChooseTheRegionActivity  extends AppCompatActivity {

    public static Context regional_certification1;
    private static final int REQUEST_CODE = 101;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager locationManager;
    private Geocoder geocoder;
    private TextView editTextQuery;
    public String userLocation;
    private Location nowLocation;
    private Context context=this;
    private ListView search_result;
    private String errorMsg="위치 정보를 가져오는데 실패하였습니다.";
    private static String defaultURL="https://maps.googleapis.com/maps/api/geocode/json?address=";
    private String lats[],lngs[];
    private int select=0;
    public Location userInputLocation;
    Handler handler=new Handler();

    @SuppressLint("WrongViewCast")
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
        search_result=(ListView) findViewById(R.id.search_result);
        regional_certification1=this;

        // 엔터키를 누를 시 줄바꿈이 아니라 검색 버튼이 눌렸을 때랑 같은 이벤트가 발생하기 위한 코드
        editTextQuery.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&keyCode==KeyEvent.KEYCODE_ENTER){
                    performSearch();
                    return true;
                }
                return false;
            }
        });
    }


    // 버튼이 클릭됐을 때 설정
    View.OnClickListener OnClickListener=new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.bt_back:
                    goBack(); // 뒤로 가기-> 첫 화면
                    break;

                case R.id.SearchLocationButton:
                    performSearch();
                    break;

                case R.id.currentLocation: //TODO : 역지오코딩하기 -> 주변 동들도 검색 결과에 띄운다.
                    fetchLocation();
                    break;
            }
        }
    };

    private void performSearch(){
        userLocation=editTextQuery.getText().toString();
        String urlStr=defaultURL+userLocation+"&key=AIzaSyAXGjPQDzJf_eeqHJM-Xxqc7KDKcysB488&language=ko";
        ConnectThread thread=new ConnectThread(urlStr);
        thread.start();
    }

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

                    // TODO 주변 동들 불러오기 -> 공공 API 찾아볼것

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
                }else{
                    Toast.makeText(this,errorMsg,Toast.LENGTH_LONG).show();
                }
                break;
            default:
                Toast.makeText(this,errorMsg,Toast.LENGTH_LONG).show();
                break;
        }
    }

    class ConnectThread extends Thread{
        String urlStr;
        public ConnectThread (String input){
            this.urlStr=input;
        }
        public void run(){
            try{
                final String print=request(urlStr);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        findLatLng(print);
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private String request(String urlStr){
        StringBuilder output=new StringBuilder();
        try{
            URL url=new URL(urlStr);
            HttpURLConnection conn=(HttpURLConnection) url.openConnection();
            if(conn!=null){
                conn.setConnectTimeout(5000); // 5초 이상 지연될 시 타임 아웃됐다고 판단
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Accept-Charset","UTF-8");

                int resCode=conn.getResponseCode();

                Log.d("resCode",String.valueOf(resCode));
                if(resCode==HttpURLConnection.HTTP_OK){
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                    String line=null;

                    while(true){
                        line=br.readLine();
                        if(line==null) break;
                        output.append(line+"\n");
                    }
                    br.close();
                    conn.disconnect();
                    Log.e("Http","Exception in processing response.");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return output.toString();
    }

    private void findLatLng(String output){
        Log.d("output",output);
        try{
            JSONObject jsonObject=new JSONObject(output);
            String status=jsonObject.getString("status");
            String condition=status.trim();

            if(condition.equals("OK")){
                JSONArray jsonResultsArray=new JSONArray(jsonObject.getString("results"));
                int jsonResultsLength=jsonResultsArray.length();

                if(jsonResultsLength>=1){ // 검색한 장소에 대한 결과가 존재할 때
                    String[] addresses=new String[jsonResultsLength];
                    lats=new String[jsonResultsLength];
                    lngs=new String[jsonResultsLength];

                    for(int i=0;i<jsonResultsLength;++i){
                        String address=jsonResultsArray.getJSONObject(i).getString("formatted_address");
                        JSONObject geoObject=new JSONObject(jsonResultsArray.getJSONObject(i).getString("geometry"));
                        JSONObject locObject=new JSONObject(geoObject.getString("location"));
                        String lat=locObject.getString("lat");
                        String lng=locObject.getString("lng");

                        addresses[i]=address;
                        lats[i]=lat;
                        lngs[i]=lng;
                }

                    // TODO 검색 결과를 ListView에 추가하기
                    AlertDialog.Builder builder=new AlertDialog.Builder(ChooseTheRegionActivity.this);
                    builder.setTitle(userLocation+"에 관한 검색 결과입니다.");
                    builder.setSingleChoiceItems(addresses, select, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            select=which;
                        }
                    }).setPositiveButton("선택", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            List<Address> addr=null;
                            try {
                                addr=geocoder.getFromLocation(Double.parseDouble(lats[select]),Double.parseDouble(lngs[select]),5);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            userLocation=addr.get(0).getAddressLine(0)+addr.get(0).getLocality();
                            startActivity(new Intent(context,RegionActivity.class));
                        }
                    }).setNegativeButton("취소",null);

                    builder.show();
                } else{
                    Toast.makeText(this,"검색 결과가 존재하지 않습니다.",Toast.LENGTH_LONG).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}