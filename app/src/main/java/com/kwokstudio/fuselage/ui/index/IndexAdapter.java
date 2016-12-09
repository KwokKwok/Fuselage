package com.kwokstudio.fuselage.ui.index;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kwokstudio.fuselage.R;
import com.kwokstudio.fuselage.bean.ContactInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭垒 on 2016/11/18.
 */

public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.ItemHolder> {

    private List<ContactInfo> mList = new ArrayList<>();
    private Context mContext;

    public IndexAdapter(Context context){
        this.mContext=context;
    }

    public void setData(List<ContactInfo> list) {
        this.mList=list;
        notifyDataSetChanged();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.index_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {
        if (position==0||!mList.get(position).getFirstChar().equals(mList.get(position-1).getFirstChar())) {
            holder.tag.setVisibility(View.VISIBLE);
            holder.tag.setText(mList.get(position).getFirstChar());
        }else{
            holder.tag.setVisibility(View.GONE);
        }
        holder.name.setText(mList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + mList.get(position).getPhoneNum()));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public int getPositionForSelection(String s){
        for ( int i = 0 ; i < mList.size() ; i++){
            if (mList.get(i).getFirstChar().equals(s)){
                return i;
            }
        }
        return -1;
    }

    public int getSelectionForPosition(int position){
        return mList.get(position).getFirstChar().charAt(0)-64;   //1对应A
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView name;
        private TextView tag;

        ItemHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.index_item_icon);
            name = (TextView) itemView.findViewById(R.id.index_item_name);
            tag = (TextView) itemView.findViewById(R.id.index_item_tag);
        }
    }





}
