package com.kwokstudio.fuselage.ui.bottomNav;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kwokstudio.fuselage.R;
import com.kwokstudio.fuselage.bean.Hito;
import com.kwokstudio.fuselage.http.RetrofitCall;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChildHitoFragment extends Fragment {

    private int loadSize=40;

    private List<Hito> hitos=new ArrayList<>();
    private RecyclerView recyclerView;


    private SwipeRefreshLayout swipe;
    private RecyclerView.LayoutManager layoutManager;
    private HitoAdapter hitoAdapter;

    public ChildHitoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_child_hito, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SimpleDraweeView logo = (SimpleDraweeView) view.findViewById(R.id.hito_logo);
        logo.setImageURI("http://image.coolapk.com/feed/2016/1119/570117_1479517364_689.png.m.jpg");

        loadData();

        Toolbar toolbar= (Toolbar) view.findViewById(R.id.hito_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        });

        //recyclerView设置
        recyclerView = (RecyclerView) view.findViewById(R.id.hito_recycler);
        layoutManager = new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setPadding(15,15,15,15);


        //swipeefresh设置
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.hito_swipe);
        swipe.setRefreshing(true);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                hitos.clear();
                loadSize=40;
                loadData();
            }
        });

        hitoAdapter = new HitoAdapter();
        recyclerView.setAdapter(hitoAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastState=0;
            private boolean isBottom=false;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isBottom=false;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                if (lastState==2&&newState==0){
//                    isBottom=true;
//                }
//                if (isBottom&&newState==1){
//                    loadSize+=40;
//                    loadData();
//                    swipe.setRefreshing(true);
//                }
                lastState=newState;
            }
        });


    }

    /**
     * Description 加载数据
     *
     */
    private void loadData() {
        RetrofitCall.getHitoStore().getMoreHito().enqueue(new Callback<Hito>() {
            @Override
            public void onResponse(Call<Hito> call, Response<Hito> response) {
                hitos.add(response.body());
                loadSize--;
                if (loadSize<0){
                    swipe.setRefreshing(false);
                    hitoAdapter.setData(hitos);
                }else{
                    loadData();
                }
            }

            @Override
            public void onFailure(Call<Hito> call, Throwable t) {
                Log.i("Tag",t.getMessage()+"----------"+this.getClass().getSimpleName());
            }
        });
    }
}
