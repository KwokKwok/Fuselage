package com.kwokstudio.fuselage;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;

import com.kwokstudio.fuselage.ui.bottomNav.BottomNavFragment;
import com.kwokstudio.fuselage.ui.index.IndexFragment;
import com.kwokstudio.fuselage.ui.rss.RssFragment;
import com.kwokstudio.fuselage.ui.zhihu.ZeroFragment;
import com.kwokstudio.fuselage.utils.FuselageHelper;
import com.kwokstudio.fuselage.utils.NetworkUtils;

import static com.kwokstudio.fuselage.R.id.fl_container;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int currentFragment = -1;
    private long preTime=0;
    private SparseArray<Fragment> fragments =new SparseArray<>();
    private boolean isNetConnect;

    //一些控件
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();
        isNetConnect=NetworkUtils.isConnected(this);
        switchFragment(0);

    }

    /*
         *设置抽屉
         *
         */
    private void initView() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    /*
     *切换Fragment，参数为int index
     *
     */
    private void switchFragment(int index) {
        if (index==currentFragment|| !isNetConnect){
            return;     //重复会白屏
        }
        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(String.valueOf(index));
        if (fragment ==null) { //说明没有这个fragment
            switch (index) {
                case 0:
                    fragment =new ZeroFragment();
                    break;
                case 1:
                    fragment=new RssFragment();
                    break;
                case 2:
                    fragment =new IndexFragment();
                    break;
                case 3:
                    fragment=new BottomNavFragment();
                    break;
            }
            transaction.add(fl_container, fragment,String.valueOf(index));
            fragments.put(index,fragment);
        }else {
            transaction.show(fragment);
        }

        if (currentFragment!=-1){   //隐藏之前的
            transaction.hide(fragments.get(currentFragment));
        }
        transaction.commit();       //提交

        //设置Toolbar用
        currentFragment = index;
    }

    /*
     *抽屉打开时，返回事件为关闭
     *
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (System.currentTimeMillis()-preTime>1500){
            preTime=System.currentTimeMillis();
            Snackbar.make(findViewById(fl_container), "退出？", Snackbar.LENGTH_SHORT).setAction("嗯", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    overridePendingTransition(0, 0);
                }
            }).show();
        }else {
            //转入后台
            moveTaskToBack(true);
        }
    }

    /*
     *抽屉导航栏菜单
     *
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        isNetConnect=NetworkUtils.isConnected(this);
        if (!isNetConnect){
            drawer.closeDrawer(GravityCompat.START);
            FuselageHelper.showToast("没有网络呢");
            return true;
        }
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_read:
                switchFragment(0);
                break;
            case R.id.nav_rss:
                switchFragment(1);
                break;
            case R.id.nav_index:
                switchFragment(2);
                break;
            case R.id.nav_btm_nav:
                switchFragment(3);
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
