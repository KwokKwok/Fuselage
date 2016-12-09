package com.kwokstudio.fuselage.ui.bottomNav;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.kwokstudio.fuselage.R;
import com.kwokstudio.fuselage.utils.FuselageHelper;


public class BottomNavFragment extends Fragment {

    private BottomNavigationView btmMenu;
    private MyViewPager vp;
    private SparseArray<Fragment> fragments;

    public BottomNavFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Toolbar及home点击事件
        setViewPager(view);

        //BottomNavigationView使用
        initBottomNav(view);
    }

    /**
     * Description 底部导航功能
     *
     */
    private void initBottomNav(View view) {
        btmMenu = (BottomNavigationView) view.findViewById(R.id.btm_nav);
        btmMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.btn_game:
                        vp.setCurrentItem(0);
                        break;
                    case R.id.btm_movie:
                        vp.setCurrentItem(1);
                        break;
                    case R.id.btn_music:
                        vp.setCurrentItem(2);
                        break;
                    case R.id.btm_book:
                        vp.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
    }

    /**
     * Description Toolbar的设置
     *
     */
    private void setViewPager(View view) {
        vp = (MyViewPager) view.findViewById(R.id.second_vp);

        //因为底部导航栏无法设置位置，取消ViewPager的滑动功能，自定义的ViewPager
        vp.setSwipePageEnable(false);

        //数据源
        fragments = new SparseArray<>();
        fragments.put(0,new ChildHitoFragment());
        fragments.put(1,new ChildCalendarFragment());
        fragments.put(2,new ChildHitoFragment());
        fragments.put(3,new ChildHitoFragment());

        //适配器
        vp.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.valueAt(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });

        //预加载全部
        vp.setOffscreenPageLimit(fragments.size());
        final ActivityManager systemService = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        final int memoryClass = systemService.getMemoryClass();
        FuselageHelper.showToast(memoryClass+"");

    }
}
