package com.example;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.Data.MainViewModel;
import com.example.fragment.HomeFragment;
import com.example.fragment.MeFragment;
import com.example.fragment.OrderFragment;
import com.example.uidesign.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity   implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    MainViewModel mainViewModel;
    ViewPager viewPager;
    BottomNavigationView mNavigationView;
    HomeFragment HomeFragments = new HomeFragment();
    OrderFragment OrderFragments = new OrderFragment();
    MeFragment MeFragments =new MeFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        Intent intent = getIntent();
        String tokenn = intent.getStringExtra("token");
        String uName = intent.getStringExtra("userName");

        mainViewModel.setToken(tokenn);
        mainViewModel.setUserName(uName);
        //页面初始化导航栏
        init();


    }

    private void init() {

        //获取页面标签对象
        viewPager = findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(this);
        mNavigationView = findViewById(R.id.navigation);
        mNavigationView.setOnNavigationItemSelectedListener(this);

        //页面切换
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return  HomeFragments;
                    case 1:
                        return  OrderFragments;
                    case 2:
                        return  MeFragments;
                }

                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
    }

    //实现接口的相关方法  implements上面两个方法后 alt+enter就会弹出这些接口，直接回车实现他们
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mNavigationView.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        viewPager.setCurrentItem(menuItem.getOrder());
        return true;
    }
}

