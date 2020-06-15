package com.cukorders.helping;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.Calendar;

public class CustomPicker extends Dialog {


    public static String selectedTime;
    private TextView cancel_time,end_time;
    private Context context;
    private Calendar calendar;
    private static DatePicker datePicker;
    private static TimePicker timePicker;
    private final String TAG="CustomPicker";
    private String user_date,user_time;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public CustomPicker(@NonNull Context context) { // CustomPicker(context,R.id.endTime)
        super(context);
        setContentView(R.layout.time_select);

        context=getContext();
        cancel_time=(TextView) findViewById(R.id.cancelTime);
        end_time=(TextView) findViewById(R.id.endTime);
        calendar=Calendar.getInstance();

        final int hour=calendar.get(Calendar.HOUR_OF_DAY),min=calendar.get(Calendar.MINUTE);
        int year=calendar.get(Calendar.YEAR),month=calendar.get(Calendar.MONTH),day=calendar.get(Calendar.DAY_OF_MONTH);

        datePicker=(DatePicker) findViewById(R.id.date_picker);
        timePicker=(TimePicker) findViewById(R.id.time_picker);
        user_date = year+"년 "+ month+"월 "+day+"일 ";
        user_time = hour+"시"+min+"분";

        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                user_date = year+"년 "+(monthOfYear+1)+"월 "+dayOfMonth+"일 ";
                Log.d("user_date","user_date의 값: "+user_date);
                Log.d("user_time","user_time의 값: "+user_time);
                selectedTime=user_date+user_time;
                Log.d("selectedTime","selectedTime의 값: "+selectedTime);
            }
        });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                user_time=hourOfDay+"시 "+minute+"분";
                Log.d("user_date","user_date의 값: "+user_date);
                Log.d("user_time","user_time의 값: "+user_time);
                selectedTime=user_date+user_time;
                Log.d("selectedTime","selectedTime의 값: "+selectedTime);
            }
        });

        /*datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                user_date=(year)+"년"+ (monthOfYear+1)+"월 "+(dayOfMonth)+"일 ";
                //selectedTime=user_date+user_time;
                Log.e("selectedTime의 값","selectedTime의 값: "+selectedTime);
            }
        });*/
        findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("dismiss",TAG+" dismiss");
                dismiss();
            }
        });
        findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}