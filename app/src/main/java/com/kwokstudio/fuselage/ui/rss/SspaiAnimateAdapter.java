package com.kwokstudio.fuselage.ui.rss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kwokstudio.fuselage.R;
import com.kwokstudio.fuselage.TextContentActivity;
import com.kwokstudio.fuselage.bean.Sspai;

import java.util.ArrayList;
import java.util.List;

/**
 * kwokg
 * 2016/11/27
 */

public class SspaiAnimateAdapter extends BaseRecyclerAdapter<Sspai> {

    private List<Sspai> mList=new ArrayList<>();
    private AppCompatActivity mActivity;

    public SspaiAnimateAdapter(AppCompatActivity activity, List<Sspai> list) {
        super(activity, list);
        this.mList=list;
        this.mActivity=activity;
    }

    @Override
    public void bottomEnterAnim(RecyclerView.ViewHolder viewHolder) {
        AnimHelper.BottomInDelayAnim(viewHolder,mActivity,500);
    }

    @Override
    public void topEnterAnim(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.sspai_item;
    }

    @Override
    public void onBind(BaseRecyclerAdapter<Sspai>.BaseViewHolder viewHolder, int position) {
        final SimpleDraweeView img = viewHolder.getViewById(R.id.sspai_item_img);
        final TextView title = viewHolder.getViewById(R.id.sspai_item_title);
        final Sspai sspai = mList.get(position);
        title.setText(sspai.getTitle());
        img.setImageURI(sspai.getImg());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.startActivity(new Intent(mActivity, TextContentActivity.class)
                        .putExtra("title",sspai.getTitle()).putExtra("url",sspai.getLink()));
                mActivity.overridePendingTransition(R.anim.activity_enter,R.anim.activity_noanim);
            }
        });
    }

}
