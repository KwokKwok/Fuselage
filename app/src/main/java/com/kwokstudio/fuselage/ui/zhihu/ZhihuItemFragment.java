package com.kwokstudio.fuselage.ui.zhihu;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.kwokstudio.fuselage.R;
import com.kwokstudio.fuselage.bean.ChannelItem;
import com.kwokstudio.fuselage.bean.ZhihuThemeItem;
import com.kwokstudio.fuselage.http.RetrofitCall;
import com.kwokstudio.fuselage.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ZhihuItemFragment extends Fragment {


    private RecyclerView mRecyclerView;

    private List<ZhihuThemeItem.StoriesBean> list=new ArrayList<>();
    private ZhihuItemAdapter zhihuItemAdapter;
    private int index;
    private SwipeRefreshLayout swipe;

    public ZhihuItemFragment() {
        // Required empty public constructor
    }

    /*
     *构造方法，传递一个Item对象过来
     *
     */
    public static Fragment newInstance(ChannelItem item) {
        Bundle bundle=new Bundle();
        bundle.putInt("index",item.getId());
        ZhihuItemFragment zhihuItemFragment = new ZhihuItemFragment();
        zhihuItemFragment.setArguments(bundle);
        return zhihuItemFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView(view);
        //获取Item对象，填充数据
        final Bundle arguments = getArguments();
        if (arguments!=null){
            index=arguments.getInt("index");
            getDataFromNet();
        }
    }

    /*
     *初始化View
     *
     */
    private void initRecyclerView(View view) {
        mRecyclerView= (RecyclerView) view.findViewById(R.id.zhihu_item_recycler);
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.zhihu_item_swipe);

        //布局管理器
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());

        //适配器
        zhihuItemAdapter = new ZhihuItemAdapter((AppCompatActivity) getActivity());
        mRecyclerView.setAdapter(zhihuItemAdapter);

        //滑动监听
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromNet();
            }
        });
        swipe.setProgressViewOffset(false,DensityUtil.dip2px(getContext(), 200),DensityUtil.dip2px(getContext(), 260));
    }

    private void getDataFromNet() {
        swipe.setRefreshing(true);
        RetrofitCall.getApiStore().getZhihuThemeItems(index).enqueue(new Callback<ZhihuThemeItem>() {
            @Override
            public void onResponse(Call<ZhihuThemeItem> call, Response<ZhihuThemeItem> response) {
                list=response.body().getStories();
                zhihuItemAdapter.setData(list);
                if (swipe.isRefreshing()){
                    swipe.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<ZhihuThemeItem> call, Throwable t) {
                Log.i("Tag",t.getMessage()+"-----from Retrofit-----"+this.getClass().getSimpleName());
            }
        });
    }
}
