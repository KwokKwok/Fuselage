package com.kwokstudio.fuselage;

import android.test.InstrumentationTestCase;
import android.util.Log;
import android.util.Xml;

import com.kwokstudio.fuselage.bean.Sspai;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 郭垒 on 2016/11/27.
 */

public class textXML extends InstrumentationTestCase {

    public void testGetData() throws Exception {
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder().url("http://sspai.com/feed").build();
        final Response execute = okHttpClient.newCall(request).execute();
        final List<Sspai> sspaiList = parseData(execute.body().string());
        System.out.print(sspaiList.get(1).getImg());
        Log.i("Tag",sspaiList.get(2).getImg()+"----------"+this.getClass().getSimpleName());
    }

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
                                    sspai.setImg(text.substring(text.indexOf("img src=")+9,text.indexOf("alt=")-1));
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
}
