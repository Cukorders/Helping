package com.cukorders.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cukorders.Adapter.PageAdapter_Mycalling;
import com.cukorders.helping.AuthActivity;
import com.cukorders.helping.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MyCallingFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tab1,tab2;
    public PageAdapter_Mycalling pagerAdapter;

    public MyCallingFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_mycalling,container,false);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_my_calling_main);
        tab1 = (TabItem) view.findViewById(R.id.tab_now_helping_mission);
        tab2 = (TabItem) view.findViewById(R.id.tab_onRequest_mission);
        viewPager = view.findViewById(R.id.view_pager_mycalling);

        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
/*
        pagerAdapter = new PageAdapter_Mycalling(getChildFragmentManager(),tabLayout.getTabCount());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(pagerAdapter);
*/



        setUpViewPager(viewPager);
        tabLayout.setTabTextColors(Color.parseColor("#e1e1e1"),Color.parseColor("#FFFFFF"));
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager){
        PageAdapter_Mycalling adapter = new PageAdapter_Mycalling(getChildFragmentManager());
        adapter.addFragment(new HelpingFragment(),"수행중인 미션");
        adapter.addFragment(new RequestingFragment(),"의뢰중인 미션");

        viewPager.setAdapter(adapter);
    }
    private void caution(){
        AlertDialog.Builder builder=new AlertDialog.Builder(MyCallingFragment.this.getActivity());
        builder.setTitle("로그인이 필요한 작업입니다.");
        builder.setMessage("이 작업을 수행하시려면 로그인이 필요합니다.");
        builder.setPositiveButton("로그인/회원가입 하기",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(MyCallingFragment.this.getActivity(), AuthActivity.class));
                    }
                }).setNegativeButton("취소",null);
        builder.show();
    }
}