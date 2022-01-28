package com.example.scavengerhunt.Misc;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.scavengerhunt.Entities.Scavenger;

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

        String tab1 = "";
        String tab2 = "Team";
        String tab3 = "Log";
        String tab4 = "";
        String tab5 = "";


        switch (Scavenger.getInstance().getRole()) {
            case "Navigator" : tab1 = "Navigate"; break;
            case "Discoverer" :
                tab1 = "Radar";
                tab4 = "Collect"; break;
            case "Story Teller" : tab1 = "Story";
        }

        switch (position){
            case 0: return tab1;
            case 1: return tab2;
            case 2: return tab3;
            case 3: return tab4;
            case 4: return tab5;
        }

        return "";
    }
}
