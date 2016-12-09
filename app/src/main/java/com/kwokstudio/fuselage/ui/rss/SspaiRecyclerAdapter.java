package com.kwokstudio.fuselage.ui.rss;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kwokstudio.fuselage.R;
import com.kwokstudio.fuselage.TextContentActivity;
import com.kwokstudio.fuselage.bean.Sspai;

import java.util.ArrayList;
import java.util.List;

/**
 * 郭垒
 *
 */

public class SspaiRecyclerAdapter extends RecyclerView.Adapter<SspaiRecyclerAdapter.SspaiHolder> {

    private List<Sspai> mList=new ArrayList<>();
    private AppCompatActivity mActivity;
    private int lastPosition=-1;

    public SspaiRecyclerAdapter(AppCompatActivity activity){
        this.mActivity=activity;
    }

    public void setData(List<Sspai> list){
        this.mList=list;
        notifyDataSetChanged();
    }

    @Override
    public SspaiHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View inflate = inflater.inflate(R.layout.sspai_item, parent, false);
        return new SspaiHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final SspaiHolder holder, int position) {
        final Sspai sspai = mList.get(position);
        holder.title.setText(sspai.getTitle());
        holder.img.setImageURI(sspai.getImg());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.startActivity(new Intent(mActivity, TextContentActivity.class)
                        .putExtra("title",sspai.getTitle()).putExtra("url",sspai.getLink()));
                mActivity.overridePendingTransition(R.anim.activity_enter,R.anim.activity_noanim);
            }
        });

        if (position>lastPosition&&position>2){
            final Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.recycler_item);
            holder.itemView.startAnimation(animation);
            lastPosition=position;

            //以下另外一种做法
//            holder.itemView.setTranslationY(MetricUtils.getScrHeight(mActivity));
//            holder.itemView.animate()
//                    .translationY(0)
//                    .setInterpolator(new DecelerateInterpolator(3.f))
//                    .setDuration(duration)
//                    .start();
        }
    }


    @Override
    public void onViewDetachedFromWindow(SspaiHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();  //View不可见时去掉动画
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class SspaiHolder extends RecyclerView.ViewHolder{

        private SimpleDraweeView img;
        private TextView title;

        SspaiHolder(View itemView) {
            super(itemView);
            img= (SimpleDraweeView) itemView.findViewById(R.id.sspai_item_img);
            title= (TextView) itemView.findViewById(R.id.sspai_item_title);
        }
    }
}
