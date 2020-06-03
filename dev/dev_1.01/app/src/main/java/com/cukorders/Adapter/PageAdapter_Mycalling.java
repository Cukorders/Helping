package com.cukorders.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cukorders.Fragment.HelpingFragment;
import com.cukorders.Fragment.RequestingFragment;

import java.util.ArrayList;
import java.util.List;

public class PageAdapter_Mycalling extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    public PageAdapter_Mycalling(@NonNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    public CharSequence getPageTitle(int position){
        return titleList.get(position);

    }
    public void addFragment(Fragment fragment, String title){
        fragmentList.add(fragment);
        titleList.add(title);
    }
}
