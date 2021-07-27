package com.imcys.bilibilias.as.video;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import java.util.ArrayList;
import java.util.List;

public class AsPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList;
    private List<String> title;

    public AsPagerAdapter(FragmentManager supportFragmentManager, ArrayList<Fragment> fragments, List<String> titles) {
        super(supportFragmentManager);
        fragmentManager = supportFragmentManager;
        fragmentList = fragments;
        title = titles;

    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        return title.get(position);
    }


}



