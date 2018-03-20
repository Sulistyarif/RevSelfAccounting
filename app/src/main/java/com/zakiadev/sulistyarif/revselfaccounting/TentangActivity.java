package com.zakiadev.sulistyarif.revselfaccounting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by sulistyarif on 08/03/18.
 */

public class TentangActivity extends AppCompatActivity {

    WebView webView;
    int pilihanMenu;
    String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.materi_webview_activity);

        webView = (WebView)findViewById(R.id.wvMateri);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/tentang.html");

    }
}