package com.programing.anheimoxin.realcode;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class ContentActivity extends AppCompatActivity{

    //控件变量
    private WebView webView;

    public static void actionStart(Context context,String contentUrl){
        Intent intent=new Intent(context,ContentActivity.class);
        intent.putExtra("CONTENT_URL",contentUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        //绑定控件
        webView=(WebView)findViewById(R.id.content_webview);

        //setup webview attributes
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        //webView.loadUrl("file:///android_asset/"+getIntent().getStringExtra("CONTENT_URL"));
        webView.loadUrl(getIntent().getStringExtra("CONTENT_URL"));
    }
}
