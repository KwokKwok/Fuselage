package com.kwokstudio.fuselage.ui.zhihu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kwokstudio.fuselage.R;
import com.kwokstudio.fuselage.TextContentActivity;
import com.kwokstudio.fuselage.bean.ZhihuThemeItem;

import java.util.List;

/**
 * Created by 郭垒 on 2016/11/14.
 */

public class ZhihuItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int IMG=0;
    private static final int NO_IMG=1;
    private List<ZhihuThemeItem.StoriesBean> contents;
    private AppCompatActivity mActivity;

    public ZhihuItemAdapter(AppCompatActivity context){
        this.mActivity=context;
    }

    public void setData(List<ZhihuThemeItem.StoriesBean> list){
        list.remove(0);
        this.contents=list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (contents.get(position).getImages()==null){
            return NO_IMG;
        }
        return IMG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case IMG:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zhihu_item_img, parent, false);
                return new ImgHolder(view);
            case NO_IMG:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zhihu_item_text, parent, false);
                return new TextHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ZhihuThemeItem.StoriesBean story = contents.get(position);
        switch (getItemViewType(position)){
            case IMG:
                final ImgHolder holder1 = (ImgHolder) holder;
                holder1.img.setImageURI(story.getImages().get(0));
                holder1.text.setText(story.getTitle());
                holder1.ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toContentActivity(story);
                    }
                });
                break;
            case NO_IMG:
                final TextHolder holder2 = (TextHolder) holder;
                holder2.text.setText(story.getTitle());
                holder2.text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toContentActivity(story);
                    }
                });
                break;
        }
    }

    //跳转到内容页
    private void toContentActivity(ZhihuThemeItem.StoriesBean story) {
        mActivity.startActivity(new Intent(mActivity, TextContentActivity.class).putExtra("id",story.getId()).putExtra("title",story.getTitle()));
        mActivity.overridePendingTransition(R.anim.activity_enter,R.anim.activity_noanim);
    }

    @Override
    public int getItemCount() {
        return contents!=null?contents.size():0;
    }



    /*
     *两个ViewHolder类
     *
     */
    static class TextHolder extends RecyclerView.ViewHolder{

        private TextView text;
        public TextHolder(View itemView) {
            super(itemView);
            text= (TextView) itemView.findViewById(R.id.zhihu_item_text_text);
        }
    }
    static class ImgHolder extends RecyclerView.ViewHolder{

        private TextView text;
        private SimpleDraweeView img;
        private LinearLayout ll;
        public ImgHolder(View itemView) {
            super(itemView);
            text= (TextView) itemView.findViewById(R.id.zhihu_item_img_text);
            img= (SimpleDraweeView) itemView.findViewById(R.id.zhihu_item_img_img);
            ll= (LinearLayout) itemView.findViewById(R.id.zhihu_item_img_ll);
        }
    }
}
