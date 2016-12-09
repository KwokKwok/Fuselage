package com.kwokstudio.fuselage.ui.bottomNav;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kwokstudio.fuselage.R;
import com.kwokstudio.fuselage.bean.Hito;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 郭垒 on 2016/11/19.
 */

public class HitoAdapter extends RecyclerView.Adapter<HitoAdapter.HitoHolder> {

    private final static String[] colors=new String[]{"#FFFF8D","#80D8FF","#CFD8DC","#A7FFEB","#FF8A80","#FFFFFF"};
    private List<Hito> hitos=new ArrayList<>();

    public void setData(List<Hito> list){
        this.hitos=list;
        notifyDataSetChanged();
    }

    @Override
    public HitoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.hito_item, parent, false);
        return new HitoHolder(inflate);
    }

    @Override
    public void onBindViewHolder(HitoHolder holder, int position) {
        final Hito hito = hitos.get(position);
        holder.text.setText(hito.getText());
        if ("".equals(hito.getSource())){
                holder.source.setVisibility(View.GONE);
            }else{
                holder.source.setText(hito.getSource());
                holder.source.setVisibility(View.VISIBLE);
            }

            holder.itemView.setBackgroundColor(Color.parseColor(colors[new Random().nextInt(6)]));

        }

    @Override
    public int getItemCount() {
        return hitos.size();
    }

    static class HitoHolder extends RecyclerView.ViewHolder{

        private final TextView text;
        private final TextView source;

        HitoHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.hito_item_text);
            source = (TextView) itemView.findViewById(R.id.hito_item_source);
        }
    }
}
