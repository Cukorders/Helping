package com.cukorders.helping;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CustomDialog extends AppCompatActivity {
    private Context context;
    private Context now;
    public CustomDialog(Context context){
        this.context=context;
    }

    public void callFuction(){
        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.ask_main);
        dialog.show();

        now=this;
        final String userLoc=((ChooseTheRegionActivity) ChooseTheRegionActivity.regional_certification1).userLocation;
        final TextView text=(TextView) dialog.findViewById(R.id.txt);
        final Button bt_login=(Button) dialog.findViewById(R.id.bt_login);
        final Button bt_main=(Button) dialog.findViewById(R.id.bt_main);

        text.setText("가입하고 "+userLoc);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                Log.d("bt_login","로그인 버튼이 클릭되었습니다.");
                Intent intent=new Intent(now, AuthActivity.class);
                Log.d("intent in a bt_login button","로그인 버튼 인텐트: "+intent);
                Log.d("start the intent","인텐트를 시작합니다.");
                startActivity(intent);
            }
        });

        bt_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("bt_main","둘러보기 버튼이 클릭되었습니다.");
                dialog.dismiss();
            }
        });
    }
}
