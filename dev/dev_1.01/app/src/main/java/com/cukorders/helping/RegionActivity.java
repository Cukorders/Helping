package com.cukorders.helping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;

public class RegionActivity  extends AppCompatActivity {

   // private TextView result;
   // GoogleMap map; // 구글 맵 api를 따 옴.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regional_certification1);

        // 현 위치를 입력하고 검색을 눌렀을 때
     //   result=(TextView) findViewById(R.id.search_result);

        // 버튼들이 클릭됐을 때=> OnClickListener 실행 : 확장성을 위해 OnClickListener 함수를 switch-case문으로 작성하였다.
        findViewById(R.id.buttonGpsSearch).setOnClickListener(OnClickListener);
        findViewById(R.id.SearchGpsButton).setOnClickListener(OnClickListener);
        findViewById(R.id.GobackButton).setOnClickListener(OnClickListener);


    }

    View.OnClickListener OnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){ // 후에 다른 버튼이 추가됐을 시 case만 추가하면 되므로 확장성 때문에 switch 구문을 사용하였다.
                case R.id.buttonGpsSearch:
                    searchRegionalInfo(); // 현위치 주변 동들을 불러와 result(xml id: search_result)에 나타낸다.
                    break;
                case R.id.SearchGpsButton:
                    getRegionInfo(); // 현재 위치로 찾기 버튼이 클릭됐을 시 주변 지역 정보가 나오는 화면(regional_certification2)으로 이동한다.
                    break;
                case R.id.GobackButton:
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
    }
}
