package com.kwokstudio.fuselage.bean;

/**
 * Created by 郭垒 on 2016/11/17.
 */

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 *  */
public class ChannelItem implements Serializable {
    /**
     *指定了serialVersionUID，就可以在序列化后，去添加一个字段
     * 或者方法，而不会影响到后期的还原，还原后的对象照样可以使用
     */
    private static final long serialVersionUID = -6465237897027410019L;
    /**
     * 栏目对应ID
     *  */
    public Integer id;
    /**
     * 栏目对应NAME
     *  */
    public String name;
    /**
     * 栏目在整体中的排序顺序  rank
     *  */
    private Integer orderId;
    /**
     * 栏目是否选中
     *  */
    private Integer selected;
    /**
     *栏目背景
     *
     */
    private String background;

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public ChannelItem() {
    }

    public ChannelItem(int id, String name, int orderId,int selected,String background) {
        this.id = id;
        this.name = name;
        this.orderId = orderId;
        this.selected = selected;
        this.background=background;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getOrderId() {
        return this.orderId;
    }

    public Integer getSelected() {
        return this.selected;
    }

    public void setId(int paramInt) {
        this.id = paramInt;
    }

    public void setName(String paramString) {
        this.name = paramString;
    }

    public void setOrderId(int paramInt) {
        this.orderId = paramInt;
    }

    public void setSelected(Integer paramInteger) {
        this.selected = paramInteger;
    }

    public String toString() {
        return "ChannelItem [id=" + this.id + ", name=" + this.name
                + ", selected=" + this.selected + "]";
    }
}