package com.cukorders.helping;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CustomDialog extends Dialog{

    private TextView txt;
    private String loc;
    public CustomDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.ask_main);

        loc=((ChooseTheRegionActivity)ChooseTheRegionActivity.regional_certification1).userLocation;
        txt=(TextView) findViewById(R.id.txt);
        txt.setText("가입하고 "+loc);
        findViewById(R.id.bt_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.bt_loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(),AuthActivity.class));
            }
        });
    }
}
