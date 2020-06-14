package com.cukorders.helping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MyPlaceActivity extends AppCompatActivity {

    private static Context context;
    private TextView region[]=new TextView[3];
    private Button change[]=new Button[3];
    private Button delete[]=new Button[3];
    private String changedLocation;
    public static Context myPlaceActivity;
    public static boolean fromMyPlaceActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myplace_setting);
        context=this;
        myPlaceActivity=this;
        fromMyPlaceActivity=false;

        change[0]=(Button) findViewById(R.id.change1);
        change[1]=(Button) findViewById(R.id.change2);
        change[2]=(Button) findViewById(R.id.change3);
        delete[0]=(Button) findViewById(R.id.delete1);
        delete[1]=(Button) findViewById(R.id.delete2);
        delete[2]=(Button) findViewById(R.id.delete3);
        region[0]=(TextView) findViewById(R.id.region1);
        region[1]=(TextView) findViewById(R.id.region2);
        region[2]=(TextView) findViewById(R.id.region3);

        for(int i=0;i<3;++i){
            change[i].setOnClickListener(onClickListener);
            delete[i].setOnClickListener(onClickListener);
        }

        region[0].setText(((ChooseTheRegionActivity)ChooseTheRegionActivity.regional_certification1).userLocation);
    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_back:
                    startActivity(new Intent(context,MyPageActivity.class));
                    break;

                case R.id.change1:
                    changeRegion(region[0]);
                    break;

                case R.id.change2:
                    changeRegion(region[1]);
                    break;

                case R.id.change3:
                    changeRegion(region[2]);
                    break;

                case R.id.delete1:
                    delete(0);
                    break;

                case R.id.delete2:
                    delete(1);
                    break;

                case R.id.delete3:
                    delete(2);
                    break;
            }
        }
    };

    private void changeRegion(TextView txt){
        //TODO DB에 지역 교체 업데이트
        fromMyPlaceActivity=true;
        startActivity(new Intent(context,ChooseTheRegionActivity.class));
        changedLocation=((ChooseTheRegionActivity)ChooseTheRegionActivity.regional_certification1).userLocation;
        txt.setText(changedLocation);
        fromMyPlaceActivity=false;
    }

    private void delete(int index){
        //TODO DB에 지역 삭제 업데이트
        region[index].setText("지역 "+String.valueOf(index+1));
    }
}
