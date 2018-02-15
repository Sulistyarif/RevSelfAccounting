package com.zakiadev.sulistyarif.revselfaccounting.tasting;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.zakiadev.sulistyarif.revselfaccounting.R;

/**
 * Created by sulistyarif on 14/02/18.
 */

public class WebviewActivity extends AppCompatActivity {

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);

        WebView webView = new WebView(this);
        webView = (WebView)findViewById(R.id.wv);
        webView.loadUrl("file:///android_asset/hello.html");
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");

    }
}
