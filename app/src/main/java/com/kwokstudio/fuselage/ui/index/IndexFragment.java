package com.kwokstudio.fuselage.ui.index;


import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kwokstudio.fuselage.R;
import com.kwokstudio.fuselage.bean.ContactInfo;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends Fragment {
    private RecyclerView recycler;
    private LinearLayoutManager layoutManager;
    private Thread getDataThread;
    private List<ContactInfo> list = new ArrayList<>();
    private IndexAdapter indexAdapter;

    public IndexFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_index, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //先从资源文件里获取信息
//        Collections.addAll(names, getResources().getStringArray(R.array.index_test));

        getContactInfo();

        initView(view);
    }

    /**
     * 初始化布局，ToolBar
     * indexBar
     * RecyclerView
     */
    private void initView(final View view) {

        //Toolbar
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.index_toolbar);
        toolbar.setTitle("Index");
        toolbar.setSubtitle("索引实现");
        toolbar.setNavigationIcon(R.mipmap.menu_black);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        });

        //IndexBar
        IndexBar indexBar = (IndexBar) view.findViewById(R.id.index_bar);

        //RecyclerView
        recycler = (RecyclerView) view.findViewById(R.id.index_recycler);
        layoutManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize(true);  //Item高度固定,不用重新计算
        indexAdapter = new IndexAdapter(getActivity());
        recycler.setAdapter(indexAdapter);

        //监听IndexBar的滚动
        indexBar.setOnTouchLetterListener(new IndexBar.OnTouchLetterListener() {
            @Override
            public void onTouchLetter(String letters) {
                final int selection = indexAdapter.getPositionForSelection(letters);
                if (selection != -1) {
                    moveToPosition(selection);
                }
            }
        });
//
//        //监听RecyclerView的滚动
//        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                final int start = indexAdapter.getSelectionForPosition(layoutManager.findFirstVisibleItemPosition());
//                final int end = indexAdapter.getSelectionForPosition(layoutManager.findLastVisibleItemPosition());
//                indexBar.setZone(start,end);
//            }
//        });
//

    }

    /**
     * Description 分情况滚动RecyclerView
     */
    private void moveToPosition(int n) {
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = layoutManager.findFirstVisibleItemPosition();
        int lastItem = layoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (n <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            recycler.smoothScrollToPosition(n);
        } else if (n <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = recycler.getChildAt(n - firstItem).getTop();
            recycler.smoothScrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            recycler.smoothScrollToPosition(n);
            //这里这个变量是用在RecyclerView滚动监听里面的
//            move = true;
        }

    }


    /**
     * Description 获取联系人信息
     *
     */
    private void getContactInfo() {
        final IndexHandler indexHandler = new IndexHandler(this);
        getDataThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ContentResolver resolver = getActivity().getContentResolver();
                List<ContactInfo> contactInfos = new ArrayList<>();
                final Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                while (cursor.moveToNext()) {
                    ContactInfo contactInfo = new ContactInfo();

                    //名字
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                    contactInfo.setName(name);
                    //先获取_ID
                    String contact_id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                    //根据_ID得到一个Cursor,条件选择_ID
                    Cursor query_id = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contact_id, null, null);
                    while (query_id.moveToNext()) {
                        final String phoneNum = query_id.getString(query_id.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactInfo.setPhoneNum(phoneNum.replace(" ", "").replace("-", ""));
                    }
                    query_id.close();

                    //设置标记
                    String tag = String.valueOf(getFirstChar(name));
                    contactInfo.setFirstChar(tag.matches("[A-Z]") ? tag : "#");

                    //有些是没有号码的
                    if (contactInfo.getPhoneNum()!=null){
                        contactInfos.add(contactInfo);
                    }
                }
                cursor.close();
                //查询完毕对数据进行排序然后返回数据
                Collections.sort(contactInfos, new Comparator<ContactInfo>() {
                    @Override
                    public int compare(ContactInfo contactInfo, ContactInfo t1) {
                        final int c1 = contactInfo.getFirstChar().charAt(0);
                        final int c2 = t1.getFirstChar().charAt(0);
                        if (c1 < 65 || c2 > 90) {
                            return -1;
                        }
                        return c1 - c2;
                    }
                });

                //发送出去
                Message msg=Message.obtain();
                msg.what=1;
                msg.obj=contactInfos;
                indexHandler.sendMessage(msg);
            }
        });
        getDataThread.start();
    }

    /**
     * 获取汉字的拼音
     */
    public static char getFirstChar(String chinese) {
        //用来设置转化的拼音的大小写，或者声调
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);//设置转化的拼音是大写字母
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//设置转化的拼音不带声调

        //1.由于只能对单个汉字转化，所以需要将字符串转化为字符数组，然后对每个字符转化，最后拼接起来
        char[] charArray = chinese.toCharArray();
        String pinyin = "";
        for (char aCharArray : charArray) {
            //2.过滤空格
            if (Character.isWhitespace(aCharArray)) continue;
            //3.需要判断是否是汉字
            //汉字占2个字节，一个字节范围是-128~127，那么汉字肯定大于127
            if (aCharArray > 127) {
                //可能是汉字
                try {
                    //由于多音字的存在，比如单  dan shan,
                    String[] pinyinArr = PinyinHelper.toHanyuPinyinStringArray(aCharArray, format);
                    if (pinyinArr != null) {
                        pinyin += pinyinArr[0];//此处即使有多音字，那么也只能取第一个拼音
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                    //说明转化失败，不是汉字，比如O(∩_∩)O~，那么则忽略
                }
            } else {
                //肯定不是汉字，应该是键盘上能够直接输入的字符，这些字符能够排序，但不能获取拼音
                //所以可以直接拼接
                pinyin += aCharArray;
            }
        }

        return pinyin.toUpperCase().charAt(0);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /**
     * Description 静态内部类加弱引用防止Handler内存泄漏
     *
     */
    private static class IndexHandler extends Handler {
        private WeakReference<IndexFragment> fragment;

        IndexHandler(IndexFragment fragment) {
            this.fragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (fragment.get().isAdded()){
                switch (msg.what) {
                    case 1:
                        fragment.get().indexAdapter.setData((List<ContactInfo>) msg.obj);
                        break;
                }
            }
        }
    }
}
