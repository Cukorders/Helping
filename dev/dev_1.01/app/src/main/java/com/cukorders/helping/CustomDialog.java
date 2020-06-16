package com.cukorders.helping;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class CustomDialog extends Dialog{

    private TextView txt;
    private String loc;
    GoMain goMain; //the callback
    private Context context;

    public CustomDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.ask_main);

        loc=((ChooseTheRegionActivity)ChooseTheRegionActivity.regional_certification1).userLocation;
        txt=(TextView) findViewById(R.id.txt);
        txt.setText("가입하고 "+loc);
        context=getContext();

        findViewById(R.id.bt_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                //caution();
            }
        });
        findViewById(R.id.bt_loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(),AuthActivity.class));
            }
        });
    }

    public void setDialogResult(GoMain go_main){
        goMain=go_main;
    }
    public interface GoMain{
        void finish(boolean see);
    }

    /*
    private void caution(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("로그인이 필요한 작업입니다.");
                builder.setMessage("이 작업을 수행하시려면 로그인이 필요합니다.");
                builder.setPositiveButton("로그인/회원가입 하기",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                context.startActivity(new Intent(context,AuthActivity.class));
                            }
                        }).setNegativeButton("취소",null);
                builder.show();
            }
        }, 5000);
    }

     */
}
