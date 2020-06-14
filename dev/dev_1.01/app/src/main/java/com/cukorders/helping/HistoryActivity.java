package com.cukorders.helping;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.cukorders.Adapter.PageAdapter;
import com.cukorders.Fragment.HelpingFragment;
import com.cukorders.Fragment.HelpingHistoryFragment;
import com.cukorders.Fragment.MyCallingFragment;
import com.cukorders.Fragment.RecentMissionFragment;
import com.cukorders.Fragment.RequestingHistoryFragment;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class HistoryActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tab1,tab2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        tabLayout = (TabLayout) findViewById(R.id.tab_history_main);
        tab1 = (TabItem) findViewById(R.id.tab_now_helping_mission_history);
        tab2 = (TabItem) findViewById(R.id.tab_onRequest_mission_history);
        viewPager = findViewById(R.id.view_pager_history);

        setUpViewPager(viewPager);
        tabLayout.setTabTextColors(Color.parseColor("#e1e1e1"),Color.parseColor("#FFFFFF"));
        tabLayout.setupWithViewPager(viewPager);
    }

    //tab 이동 설정하는 부분
    private void setUpViewPager(ViewPager viewPager) {
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
        adapter.addFragment(new HelpingHistoryFragment(),"내 수행 미션");
        adapter.addFragment(new RequestingHistoryFragment(),"내 의뢰 미션");
        viewPager.setAdapter(adapter);
    }
}
