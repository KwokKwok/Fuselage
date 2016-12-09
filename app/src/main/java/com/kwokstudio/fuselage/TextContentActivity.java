package com.kwokstudio.fuselage;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.kwokstudio.fuselage.bean.TextContent;
import com.kwokstudio.fuselage.http.RetrofitCall;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TextContentActivity extends AppCompatActivity {

    private WebView web;
    private android.support.v7.app.ActionBar actionBar;
    private ProgressBar progressBar;
    private MyHandler handler = new MyHandler(this);
    private int id;
    private String title;
    private boolean rss = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_content);

        //webView设置
        web = (WebView) findViewById(R.id.web_content);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setBlockNetworkImage(true);//先不加载图片，
        web.setWebViewClient(new MyWebClient());

        web.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");


        //布局设置
        progressBar = (ProgressBar) findViewById(R.id.content_progressBar);

        final Intent intent = getIntent();
        actionBar = getSupportActionBar();
        title = intent.getStringExtra("title");
        actionBar.setTitle(title);

        //sspai是直接发送链接
        String url = intent.getStringExtra("url");
        if (url != null) {
            actionBar.setTitle("少数派");
            rss = true;
            web.loadUrl(url);
            return;
        }


        id = intent.getIntExtra("id", 0);
        RetrofitCall.getApiStore().getTextContent(id).enqueue(new Callback<TextContent>() {
            @Override
            public void onResponse(Call<TextContent> call, Response<TextContent> response) {
                final TextContent body = response.body();
                web.loadUrl(body.getShare_url());
                actionBar.setSubtitle(body.getTheme().getName() == null ? "" : body.getTheme().getName());
            }

            @Override
            public void onFailure(Call<TextContent> call, Throwable t) {

            }
        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.content,menu);
//        MenuItem item=menu.findItem(R.id.content_share);
//        ShareActionProvider shareActionProvider= (ShareActionProvider) MenuItemCompat.getActionProvider(item);
//        Intent intent=new Intent();
//        intent.setType("text/*");
//        intent.setAction(Intent.ACTION_SEND);
//        intent.putExtra(Intent.EXTRA_TEXT, title+"\n"+UrlConfigs.BASE_URL+"news/"+id);
//        shareActionProvider.setShareIntent(intent);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.activity_noanim, R.anim.activity_exit);
    }


    class MyWebClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            String javascript;
            view.loadUrl("javascript:window.local_obj.showSource('<head>'+"
                    + "document.getElementsByClassName('meta')[0].innerHTML+'</head>');");
            if (!rss) {
                javascript = "javascript:function hideOther() {" +
                        "document.getElementsByClassName('header-for-mobile')[0].remove();" +
                        "document.getElementsByClassName('headline')[0].remove();" +
                        "document.getElementsByClassName('bottom-wrap')[0].remove();}";
            } else {
                javascript = "javascript:function hideOther() {" +
                        "document.getElementsByClassName('login-to-comment')[0].remove();" +
                        "document.getElementsByClassName('box widget widget-list widget-app-info')[0].remove();" +
                        "document.getElementsByClassName('js-widget-app-info')[0].remove();" +
                        "document.getElementsByClassName('copyright-box')[0].remove();" +
                        "document.getElementsByClassName('clearfix tag-box')[0].remove();" +
                        "document.getElementsByClassName('clearfix post-user-action')[0].remove();" +
                        "document.getElementsByClassName('liked-user')[0].remove();" +
                        "document.getElementsByClassName('clearfix relate-box')[0].remove();" +
                        "document.getElementsByClassName('clearfix copyright')[0].remove();" +
                        "document.getElementsByClassName('clearfix saya')[0].remove();" +
                        "document.getElementsByClassName('clearfix saya')[0].remove();" +
                        "document.getElementsByClassName('clearfix saya-header headroom headroom--pinned headroom-header-top-mobile')[0].remove();" +
                        "document.getElementsByClassName('clearfix saya-header headroom headroom-header-not-top-mobile headroom--pinned')[0].remove();}";
            }


//            "document.getElementsByClassName('clearfix saya-header headroom headroom--pinned headroom-header-top-mobile')[0].remove();" +
//                    "document.getElementsByClassName('login-to-comment')[0].remove();" +
//
//                    "document.getElementsByClassName('js-widget-app-info')[0].remove();" +
//                    "document.getElementsByClassName('comments-count')[0].remove();" +
//                    "document.getElementsByClassName('clearfix relate-box')[0].remove();" +
//                    "document.getElementsByClassName('clearfix tag-box')[0].remove();" +
            view.loadUrl(javascript);
            view.loadUrl("javascript:hideOther();");
            super.onPageFinished(view, url);
            web.getSettings().setBlockNetworkImage(false);//可以加载图片了
        }
    }

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            if (html.contains("来自 mp.weixin.qq.com")) {
                //解决微信图片不显示问题
                final String substring = html.substring(html.indexOf("herf=") + 17, html.indexOf(">原文链接") - 1).replace("amp;", "");
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = substring;
                handler.sendMessage(msg);
            } else {
                handler.sendEmptyMessage(2);
            }
        }
    }

    static class MyHandler extends Handler {
        private WeakReference<TextContentActivity> mActivity;

        MyHandler(TextContentActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final TextContentActivity textContentActivity = mActivity.get();
            if (textContentActivity != null) {
                switch (msg.what) {
                    case 1:
                        textContentActivity.web.loadUrl((String) msg.obj);
                        new CountDownTimer(300, 300) {

                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                textContentActivity.progressBar.setVisibility(View.INVISIBLE);
                                textContentActivity.web.setVisibility(View.VISIBLE);
                            }
                        }.start();
                        break;
                    case 2:
                        textContentActivity.progressBar.setVisibility(View.INVISIBLE);
                        textContentActivity.web.setVisibility(View.VISIBLE);
                        break;

                }
            }
        }
    }
}
