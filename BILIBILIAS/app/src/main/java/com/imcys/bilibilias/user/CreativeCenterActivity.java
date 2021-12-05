package com.imcys.bilibilias.user;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.google.android.material.tabs.TabLayout;
import com.imcys.bilibilias.R;
import com.imcys.bilibilias.as.video.AsPagerAdapter;
import com.imcys.bilibilias.as.video.VideoComment;
import com.imcys.bilibilias.as.video.VideoRecommend;
import com.imcys.bilibilias.user.Fragment.CreativeData;
import com.imcys.bilibilias.user.Fragment.CreativeHome;
import com.imcys.bilibilias.user.Fragment.CreativeReward;

import java.util.ArrayList;

public class CreativeCenterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creative_center);

        TabLoad();



    }


    private void TabLoad() {
        ViewPager mViewPager = (ViewPager) findViewById(R.id.Creative_ViewPager);
        ArrayList<String> tabList = new ArrayList<>();
        ArrayList<Fragment> fragments = new ArrayList<>();

        CreativeHome mCreativeHome = new CreativeHome();
        CreativeData mCreativeData = new CreativeData();
        CreativeReward mCreativeReward = new CreativeReward();
        fragments.add(mCreativeHome);
        fragments.add(mCreativeData);
        fragments.add(mCreativeReward);
        AsPagerAdapter myAssetPathFetcher = new AsPagerAdapter(getSupportFragmentManager(), fragments, tabList);

        final BubbleNavigationLinearView bubbleNavigationLinearView = findViewById(R.id.Creative_BubbleNavigationLinear);

        //bubbleNavigationLinearView.setBadgeValue(0, "信息");


        mViewPager.setAdapter(myAssetPathFetcher);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                bubbleNavigationLinearView.setCurrentActiveItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                mViewPager.setCurrentItem(position, true);
            }
        });
    }




}
