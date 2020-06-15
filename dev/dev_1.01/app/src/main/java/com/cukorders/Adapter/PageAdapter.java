package com.cukorders.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PageAdapter extends FragmentPagerAdapter {


    private List<Fragment> fragmentMainList = new ArrayList<>();
    private List<String> titleMainList = new ArrayList<>();

    public PageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentMainList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentMainList.size();
    }

    @Nullable
    public CharSequence getPageTitle(int position){
        return titleMainList.get(position);

    }
    public void addFragment(Fragment fragment, String title){
        fragmentMainList.add(fragment);
        titleMainList.add(title);
    }
}