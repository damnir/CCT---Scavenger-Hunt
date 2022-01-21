package com.example.scavengerhunt.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.scavengerhunt.Activities.HuntFragments.CompassFragment;
import com.example.scavengerhunt.Activities.HuntFragments.LogFragment;
import com.example.scavengerhunt.Activities.HuntFragments.MapFragment;
import com.example.scavengerhunt.HuntPagerAdapter;
import com.example.scavengerhunt.R;
import com.google.android.material.tabs.TabLayout;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class HuntActivity extends AppCompatActivity {

    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunt);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new CompassFragment());
        fragmentList.add(new MapFragment());
        fragmentList.add(new LogFragment());

        pager = findViewById(R.id.pager);
        pagerAdapter = new HuntPagerAdapter(getSupportFragmentManager(), fragmentList);

        pager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);
    }


}