package com.kwokstudio.fuselage.ui.rss;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.kwokstudio.fuselage.R;
import com.kwokstudio.fuselage.bean.Sspai;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RssFragment extends Fragment {

    private final Object mLock=new Object();


    private SspaiRecyclerAdapter adapter;
    private ProgressBar progressBar;
    private RssHandler handler=new RssHandler(this);
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView rv;

    public RssFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rss, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Toolbar设置
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.rss_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        });
        toolbar.setTitle("RSS");

        //ProgressBar和Fab设置
        progressBar = (ProgressBar) view.findViewById(R.id.rss_progressBar);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.rss_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!progressBar.isActivated()){
                    progressBar.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.INVISIBLE);
                    getDataFromNet();
                }
            }
        });

        //RecyclerView设置
        rv = (RecyclerView) view.findViewById(R.id.rss_recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        adapter = new SspaiRecyclerAdapter((AppCompatActivity) getActivity());
        rv.setAdapter(adapter);

        getDataFromNet();
    }

    /**
     * Description 使用Okhttp获取网络数据，并在获取到数据后进行解析
     *
     */
    private void getDataFromNet(){
        synchronized (mLock){
            OkHttpClient okHttpClient=new OkHttpClient();
            Request request=new Request.Builder().url("http://sspai.com/feed").build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String string = response.body().string();
                    final List<Sspai> sspaiList =   parseData(string);
                    Message msg=Message.obtain();
                    msg.what=1;
                    msg.obj=sspaiList;
                    handler.sendMessage(msg);
                }
            });
        }
    }

    /**
     * Description 对获取到的XML数据进行解析
     *
     */
    private List<Sspai> parseData(String xml) {
        List<Sspai> sspaiList = null;
        Sspai sspai = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            factory.setNamespaceAware(true);   //设置为true，则factory创建的XmlPrser提供对xml 命名空间的支持
            XmlPullParser parser = Xml.newPullParser();

            parser.setInput(new StringReader(xml));

//            parser.nextTag();  //此时是rss标签
//            parser.nextTag();  //此时是channel标签

            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        sspaiList = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name1 = parser.getName();
                        if ("item".equals(name1)) {    //item节点
                            sspai = new Sspai();

                            while (parser.nextTag() == XmlPullParser.START_TAG) {   //读item节点内的内容
                                String name2 = parser.getName();
                                if ("title".equals(name2)) {
                                    sspai.setTitle(parser.nextText());
                                } else if ("link".equals(name2)) {
                                    sspai.setLink(parser.nextText());
                                } else if ("pubDate".equals(name2)) {
                                    sspai.setTime(parser.nextText());
                                } else if ("dc:creator".equals(name2)) {
                                    sspai.setCreator(parser.nextText());
                                } else if ("description".equals(name2)) {
                                    int token = parser.nextToken();
                                    while(token!=XmlPullParser.CDSECT){
                                        token = parser.nextToken();
                                    }
                                    final String text = parser.getText();
                                    sspai.setImg(text.substring(text.indexOf("img src=")+9,text.indexOf("alt=")-2));
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String end = parser.getName();
                        if ("item".equals(end)) {
                            sspaiList.add(sspai);
                            sspai = null;
                        }
                        break;
                }
                event = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return sspaiList;

    }


    static class RssHandler extends Handler{
        private WeakReference<RssFragment> reference;

        RssHandler(RssFragment fragment){
            this.reference=new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final RssFragment fragment = reference.get();
            if (fragment!=null){
                switch (msg.what){
                    case 1:
                        fragment.adapter.setData((List<Sspai>) msg.obj);
                        fragment.progressBar.setVisibility(View.GONE);
                        fragment.layoutManager.smoothScrollToPosition(fragment.rv,null,0);
                        fragment.rv.setVisibility(View.VISIBLE);
//                        fragment.rv.setAdapter(new SspaiAnimateAdapter((AppCompatActivity) fragment.getActivity(),(List<Sspai>)msg.obj));
                        break;
                }
            }
        }
    }

}
