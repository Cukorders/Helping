package com.cukorders.helping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseTheRegionActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regional_certification1);

        findViewById(R.id.bt_back).setOnClickListener(OnClickListener);
        findViewById(R.id.currentLocation).setOnClickListener(OnClickListener);
        findViewById(R.id.SearchLocationButton).setOnClickListener(OnClickListener);

    }

    // 버튼이 클릭됐을 때 설정
    View.OnClickListener OnClickListener=new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO : 기능 구현하기
            switch(v.getId()){
                case R.id.bt_back:
                    // 뒤로 가기 버튼

                    break;

                case R.id.currentLocation:

                    break;

                case R.id.SearchLocationButton:

                    break;
            }
        }
    };

    private void goBack(){
        Intent intent=new Intent(this,RegionActivity.class);

    }
}