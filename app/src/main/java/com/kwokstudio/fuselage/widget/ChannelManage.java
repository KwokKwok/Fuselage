package com.kwokstudio.fuselage.widget;

import android.database.SQLException;
import android.util.Log;

import com.kwokstudio.fuselage.bean.ChannelItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChannelManage {
	private static ChannelManage channelManage;
	/**
	 * 默认的用户选择频道列表
	 * */
	public static List<ChannelItem> defaultUserChannels;
	/**
	 * 默认的其他频道列表
	 * */
	public static List<ChannelItem> defaultOtherChannels;
	private ChannelDao channelDao;
	/** 判断数据库中是否存在用户数据 */
	private boolean userExist = false;
	static {
		defaultUserChannels = new ArrayList<>();
		defaultOtherChannels = new ArrayList<>();
		defaultUserChannels.add(new ChannelItem(2, "开始游戏", 1, 1,"http://p2.zhimg.com/55/e0/55e06f8fe322fd87b3261b204bae4786.jpg"));
		defaultUserChannels.add(new ChannelItem(3, "电影日报", 2, 1,"http://p1.zhimg.com/80/0b/800b79a4821a535de31b349ffdc9eabb.jpg"));
		defaultUserChannels.add(new ChannelItem(4, "设计日报", 3, 1,"http://p3.zhimg.com/ff/15/ff150eef63a48f0d1dafb77e62610a9f.jpg"));
		defaultUserChannels.add(new ChannelItem(5, "大公司日报", 4, 1,"http://p1.zhimg.com/46/cb/46cb63bdd2bbcb8e5e4c70719c566c69.jpg"));
		defaultUserChannels.add(new ChannelItem(6, "财经日报", 5, 0,"http://p2.zhimg.com/17/d9/17d9ac6447732de9ed566926b197eb6b.jpg"));
		defaultUserChannels.add(new ChannelItem(7, "音乐日报", 6, 0,"http://p1.zhimg.com/02/17/02176dbeefe7f0a54c0563f5533fa4da.jpg"));
		defaultUserChannels.add(new ChannelItem(8, "体育日报", 7, 0,"http://pic1.zhimg.com/6bbd96bfcbe6f407227f9db36cbbaac0.jpg"));
		defaultOtherChannels.add(new ChannelItem(9, "动漫日报", 1, 0,"http://p3.zhimg.com/f0/c2/f0c253357d99fb72fdb16543ad93ca0c.jpg"));
		defaultOtherChannels.add(new ChannelItem(10, "互联网安全", 2, 0,"http://p4.zhimg.com/32/55/32557676e84fcfda4d82d9b8042464e1.jpg"));
		defaultOtherChannels.add(new ChannelItem(11, "不许无聊", 3, 0,"http://pic1.zhimg.com/a5128188ed788005ad50840a42079c41.jpg"));
		defaultOtherChannels.add(new ChannelItem(12, "用户推荐日报", 4, 0,"http://pic1.zhimg.com/0a7885148afab302b948698b0fb1a7bc.jpg"));
		defaultOtherChannels.add(new ChannelItem(13, "日常心理学", 5, 0,"http://pic2.zhimg.com/71c8bcd3d99958de45ed87b8fc213224.jpg"));
	}

	private ChannelManage(SQLHelper paramDBHelper) throws SQLException {
		if (channelDao == null)
			channelDao = new ChannelDao(paramDBHelper.getContext());
		// NavigateItemDao(paramDBHelper.getDao(NavigateItem.class));
		return;
	}

	/**
	 * 初始化频道管理类
	 * @throws SQLException
	 */
	public static ChannelManage getManage(SQLHelper dbHelper)throws SQLException {
		if (channelManage == null)
			channelManage = new ChannelManage(dbHelper);
		return channelManage;
	}

	/**
	 * 清除所有的频道
	 */
	public void deleteAllChannel() {
		channelDao.clearFeedTable();
	}
	/**
	 * 获取其他的频道
	 * @return 数据库存在用户配置 ? 数据库内的用户选择频道 : 默认用户选择频道 ;
	 */
	public List<ChannelItem> getUserChannel() {
		Object cacheList = channelDao.listCache(SQLHelper.SELECTED + "= ?",new String[] { "1" });
		if (cacheList != null && !((List) cacheList).isEmpty()) {
			userExist = true;
			List<Map<String, String>> maplist = (List) cacheList;
			int count = maplist.size();
			List<ChannelItem> list = new ArrayList<ChannelItem>();
			for (int i = 0; i < count; i++) {
				ChannelItem navigate = new ChannelItem();
				navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
				navigate.setName(maplist.get(i).get(SQLHelper.NAME));
				navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDERID)));
				navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
				navigate.setBackground(maplist.get(i).get(SQLHelper.BACKGROUND));
				list.add(navigate);
			}
			return list;
		}
		initDefaultChannel();
		return defaultUserChannels;
	}

	/**
	 * 获取其他的频道
	 * @return 数据库存在用户配置 ? 数据库内的其它频道 : 默认其它频道 ;
	 */
	public List<ChannelItem> getOtherChannel() {
		Object cacheList = channelDao.listCache(SQLHelper.SELECTED + "= ?" ,new String[] { "0" });
		List<ChannelItem> list = new ArrayList<ChannelItem>();
		if (cacheList != null && !((List) cacheList).isEmpty()){
			List<Map<String, String>> maplist = (List) cacheList;
			int count = maplist.size();
			for (int i = 0; i < count; i++) {
				ChannelItem navigate= new ChannelItem();
				navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
				navigate.setName(maplist.get(i).get(SQLHelper.NAME));
				navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDERID)));
				navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
				navigate.setBackground(maplist.get(i).get(SQLHelper.BACKGROUND));
				list.add(navigate);
			}
			return list;
		}
		if(userExist){
			return list;
		}
		cacheList = defaultOtherChannels;
		return (List<ChannelItem>) cacheList;
	}

	/**
	 * 保存用户频道到数据库
	 * @param userList
	 */
	public void saveUserChannel(List<ChannelItem> userList) {
		for (int i = 0; i < userList.size(); i++) {
			ChannelItem channelItem = userList.get(i);
			channelItem.setOrderId(i);
			channelItem.setSelected(1);
			channelDao.addCache(channelItem);
		}
	}

	/**
	 * 保存其他频道到数据库
	 * @param otherList
	 */
	public void saveOtherChannel(List<ChannelItem> otherList) {
		for (int i = 0; i < otherList.size(); i++) {
			ChannelItem channelItem = otherList.get(i);
			channelItem.setOrderId(i);
			channelItem.setSelected(0);
			channelDao.addCache(channelItem);
		}
	}

	/**
	 * 初始化数据库内的频道数据
	 */
	private void initDefaultChannel(){
		Log.d("deleteAll", "deleteAll");
		deleteAllChannel();
		saveUserChannel(defaultUserChannels);
		saveOtherChannel(defaultOtherChannels);
	}
}
