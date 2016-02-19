package com.qianfeng.webviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qianfeng.webviewdemo.utils.EditTextWithDel;

public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.webviewTest)
    private WebView webView;

    @ViewInject(R.id.id_search)
    private EditTextWithDel etSearch;

    @ViewInject(R.id.id_btn_go)
    private ImageButton go;

    @ViewInject(R.id.id_progress_bar)
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewUtils.inject(this);

        initWebView();

        go.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(MainActivity.this, etSearch.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void initWebView()
    {
//        webView.loadUrl("file:///sdcard/face.png");//加载本地图片
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);//js允许
        settings.setUseWideViewPort(true);//网页或本地图片自适应
        settings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
               etSearch.setText(url);
                return false;
            }
        });//网页在app里加载，不会调用浏览器

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                progressBar.setProgress(newProgress);//显示加载进度
                if(newProgress == 100)
                {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });


        etSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(s.length() == 0)
                {
                    go.setVisibility(View.GONE);
                } else
                {
                    go.setVisibility(View.VISIBLE);
                }
            }
        });
        etSearch.setText("file:///android_asset/test.html");

        //把原生的JAVA代码映射到js对象中
        webView.addJavascriptInterface(new JsTest(), "jsObj");
        webView.loadUrl("file:///android_asset/test.html");//加载网页

    }

    /**
     * 按下所有键时回调
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        /**
         * 如果按下返回键，判断网页能否返回上一页，如果可以就返回上一页，如果不行，执行默认操作
         */
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            if(webView.canGoBack())
            {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onOpenUrl(View view)
    {

    }

    //安卓中提供给js调用的所有方法都写在这，每个方法都要加上这个@JavascriptInterface注解
    class JsTest
    {
        //安卓原生java代码返回一个字符串给js
        @JavascriptInterface
        public String sayHello()
        {
            return "hello from android";
        }

        //在js中点击一个按钮来控制安卓中的imagebutton显示出来
        @JavascriptInterface
        public void showAndroidButton()
        {
            //如果在js中调用了java原生代码中需要操作ui，必需在主线程中执行
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    go.setVisibility(View.VISIBLE);
                }
            });
        }

        //使用安卓中java原生代码调用js中代码，也要在主线程中执行
        @JavascriptInterface
        public void excuteHtmlFun()
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    webView.loadUrl("javascript:showFromHtml('1514')");
                }
            });
        }
    }
}
