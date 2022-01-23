package com.example.scavengerhunt.Misc;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class HuntPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public HuntPagerAdapter(FragmentManager fm, List<Fragment> mFragments) {
        super(fm);
        this.fragments = mFragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Navigate";
            case 1: return "Map";
            case 2: return "Log";
            case 3: return "Radar";
        }

        return "";
    }
}
