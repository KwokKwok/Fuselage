package com.kwokstudio.fuselage.ui.zhihu;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.kwokstudio.fuselage.MyApp;
import com.kwokstudio.fuselage.R;
import com.kwokstudio.fuselage.SubjectActivity;
import com.kwokstudio.fuselage.bean.ChannelItem;
import com.kwokstudio.fuselage.bean.ZhihuThemeItem;
import com.kwokstudio.fuselage.widget.ChannelManage;

import java.util.ArrayList;
import java.util.List;


/***
 * 知乎日报页面
 * A simple {@link Fragment} subclass.
 */
public class ZeroFragment extends Fragment {

    private List<ZhihuThemeItem> all=new ArrayList<>();//保存数据

    private int[] colors=new int[]{R.color.c0,R.color.c1,R.color.c2,R.color.c3,R.color.c4
            ,R.color.c5,R.color.c6,R.color.c7};//设置头部主题色必须使用R.color

    private String[] colorString;//设置GradientDrawable使用Color.parseColor才有效果
    private List<Integer> choose=new ArrayList<>();//保存要显示的栏目
    private int index=0;//
    private boolean isDataChange=false;//如果栏目数据改变


    //一些控件
    private MaterialViewPager mMaterialViewPager;
    private ImageView headerLogo;
    private ProgressBar progressBar;
    private FrameLayout headerBack;
    private AppCompatActivity mActivity;
    private List<ChannelItem> userChannel;
    private ViewStub stub;


    public ZeroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //填充菜单，会回调onCreateOpinionMenu()
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_zero, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity= (AppCompatActivity) getActivity();
        mActivity.setTheme(R.style.AppTheme);

        colorString=getResources().getStringArray(R.array.color);
        initView(view);
        getBaseData();  //获取订阅数据
    }


    /**
     *获取订阅数据
     *
     */
    private void getBaseData() {
        progressBar.setVisibility(View.VISIBLE);
        userChannel = ChannelManage.getManage(MyApp.getApp().getSQLHelper()).getUserChannel();

        setMaterialViewPager(userChannel);
    }

    /**
     *初始化View
     *
     */
    private void initView(final View view) {
        //主视图
        mMaterialViewPager = (MaterialViewPager) view.findViewById(R.id.materialViewpager);
        progressBar = (ProgressBar) view.findViewById(R.id.zero_progressBar);
        final Toolbar toolbar = mMaterialViewPager.getToolbar();


        //设置Toolbar
        toolbar.setTitle("知乎日报");
        toolbar.setNavigationIcon(R.mipmap.menu_white);
        toolbar.inflateMenu(R.menu.zero_menu);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId()==R.id.add_subject){
                    startActivityForResult(new Intent(getActivity(),SubjectActivity.class),1);
                    getActivity().overridePendingTransition(R.anim.swipe_in_bottom,R.anim.activity_noanim);
                }
                return true;
            }
        });
        toolbar.setTitleTextColor(Color.WHITE);

        //Header视图
        headerLogo= (ImageView) view.findViewById(R.id.zhihu_topic);
        headerBack= (FrameLayout) view.findViewById(R.id.zhihu_header_background);

        //设置头部
        mMaterialViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                final int index = userChannel.get(page).getId()% colors.length;

                //设置圆圈颜色
                GradientDrawable myGrad = (GradientDrawable)headerBack.getBackground();
                myGrad.setColor(Color.parseColor(colorString[index]));

                //设置主题色彩
                final int drawable = getResources().getIdentifier("p" + userChannel.get(page).getId(), "mipmap", getActivity().getPackageName());
                headerLogo.setImageResource(drawable);

                //设置一个动画
                ObjectAnimator scaleY=ObjectAnimator.ofFloat(headerBack,"scaleY",0.5f,1f);
                ObjectAnimator scaleX=ObjectAnimator.ofFloat(headerBack,"scaleX",0.5f,1f);
                AnimatorSet set=new AnimatorSet();
                set.play(scaleX).with(scaleY);
                set.setDuration(1000);
                set.setInterpolator(new BounceInterpolator());
                set.start();

                //设置背景主色和图片
                return HeaderDesign.fromColorResAndUrl(colors[index],userChannel.get(page).getBackground());
            }
        });
    }

    /**
     *设置MaterialViewPager的适配器、头部、关联
     *
     */
    private void setMaterialViewPager(final List<ChannelItem> list) {
        //一个Fragment适配器
        FragmentStatePagerAdapter statePagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ZhihuItemFragment.newInstance(list.get(position));
            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return list.get(position).getName();
            }
        };

        //设置适配器和底部栏
        mMaterialViewPager.getViewPager().setAdapter(statePagerAdapter);
//        mMaterialViewPager.getPagerTitleStrip().setTextColor(Color.WHITE);
        mMaterialViewPager.getPagerTitleStrip().setTextColorStateListResource(R.color.tab_text);
        mMaterialViewPager.getPagerTitleStrip().setViewPager(mMaterialViewPager.getViewPager());

        mMaterialViewPager.getViewPager().setOffscreenPageLimit(mMaterialViewPager.getViewPager().getAdapter().getCount());
        //改变header,顺便加动画
        mMaterialViewPager.getViewPager().setCurrentItem(1);
        mMaterialViewPager.getViewPager().setCurrentItem(0);
        progressBar.setVisibility(View.GONE);
        mMaterialViewPager.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==1){
            isDataChange=data.getBooleanExtra("change",false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isDataChange){ //订阅数据改变
            getBaseData();
            isDataChange=false;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("Tag","1"+"----------"+this.getClass().getSimpleName());
    }
}
