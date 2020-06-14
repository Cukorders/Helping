package com.cukorders.helping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cukorders.Fragment.RecentMissionFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FilterActivity extends AppCompatActivity {

    private static final String TAG ="FilterActivity" ;

    private LinearLayout thisactivity;
    public  static boolean[] checked = new boolean[7];
    private static Context context;
    private ImageButton backButton;
    private CheckBox buyFoodButton;
    private CheckBox buyButton;
    private CheckBox delieveryFoodButton;
    private CheckBox deliveryButton;
    private CheckBox cleaningButton;
    private CheckBox teachButton;
    private CheckBox etcButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);

        thisactivity = (LinearLayout) findViewById(R.id.filter_linear);

        backButton = (ImageButton) findViewById(R.id.bt_back);
        buyFoodButton = (CheckBox) findViewById(R.id.cb_buy_food);
        buyButton = (CheckBox) findViewById(R.id.cb_buy_etc);
        delieveryFoodButton = (CheckBox) findViewById(R.id.cb_food_delivery);
        deliveryButton = (CheckBox) findViewById(R.id.cb_delivery);
        cleaningButton = (CheckBox) findViewById(R.id.cb_cleaning);
        teachButton = (CheckBox) findViewById(R.id.cb_teach);
        etcButton = (CheckBox) findViewById(R.id.cb_etc);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(buyFoodButton.isChecked()){
                    checked[0] = true;
                }
                else if(!buyFoodButton.isChecked()){
                    checked[0] =false;
                }
                if(buyButton.isChecked()){
                    checked[1] = true;
                }
                else if(!buyButton.isChecked()){
                    checked[1] =false;
                }
                if(deliveryButton.isChecked()){
                    checked[2] = true;
                }
                else if(!deliveryButton.isChecked()){
                    checked[2] =false;
                }
                if(cleaningButton.isChecked()){
                    checked[3] = true;
                }
                else if(!cleaningButton.isChecked()){
                    checked[3] =false;
                }
                if(teachButton.isChecked()){
                    checked[4] = true;
                }
                else if(!teachButton.isChecked()){
                    checked[4] =false;
                }
                if(etcButton.isChecked()){
                    checked[5] = true;
                }
                else if(!etcButton.isChecked()){
                    checked[5] = false;
                }

                /*
                Bundle bundle1 = new Bundle();
                bundle1.putBooleanArray("checks",checked);
                */

                Log.d(TAG, "I moved to filter Activity :");
                Intent intent = new Intent();
                intent.putExtra("checks",checked);
                Log.d(TAG, "This is checked array 0 :"+checked[0]);
                Log.d(TAG, "This is checked array 1 :"+checked[1]);
                Log.d(TAG, "This is checked array 2 :"+checked[2]);
                Log.d(TAG, "This is checked array 3 :"+checked[3]);
                Log.d(TAG, "This is checked array 4 :"+checked[4]);
                Log.d(TAG, "This is checked array 5 :"+checked[5]);
                Log.d(TAG, "This is checked array 6 :"+checked[6]);

                Log.d(TAG, "I moved to filter Activity. This is intent :"+intent);
                setResult(RESULT_OK,intent);
                finish();

            }
        });
    }

}