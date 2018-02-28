package com.zakiadev.sulistyarif.revselfaccounting.tasting;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.zakiadev.sulistyarif.revselfaccounting.R;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataJurnal;
import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapterMix;

import java.util.ArrayList;

/**
 * Created by sulistyarif on 14/02/18.
 */

public class WebviewActivity extends AppCompatActivity {

    WebView webView;

//    @SuppressLint("JavascriptInterface")
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);

        webView = (WebView)findViewById(R.id.wv);
        webView.loadUrl("file:///android_asset/index.html");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setBuiltInZoomControls(true);
//        webView.addJavascriptInterface(new JavaScriptInterface(WebviewActivity.this), "Android");
//        nggak jadi
        webView.addJavascriptInterface(new Object(){
            public void performClick(){
                Toast.makeText(WebviewActivity.this, "testing javascript",Toast.LENGTH_SHORT).show();
            }
        }, "test");

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                ArrayList<DataJurnal> dataJurnals = new DBAdapterMix(WebviewActivity.this).selectJurnal();
                DataJurnal dataJurnal;

                for (int i = 0; i< dataJurnals.size(); i++){
                    dataJurnal = dataJurnals.get(i);

                    String tgl = dataJurnal.getTgl();
                    String ket = dataJurnal.getKeterangan();
                    String akunDebt = String.valueOf(dataJurnal.getAkunDebet());
                    String namaDebt = dataJurnal.getNamaDebet();
                    String nomDebt = String.valueOf(dataJurnal.getNominalDebet());
                    String akunKred = String.valueOf(dataJurnal.getAkunKredit());
                    String namaKred = dataJurnal.getNamaKredit();
                    String nomKred = String.valueOf(dataJurnal.getNominalKredit());
                    webView.loadUrl("javascript:addRow('" + tgl + "', '" + ket + "', '" + akunDebt + "', '" + namaDebt + "', '" + nomDebt + "', '" + akunKred + "', '" + namaKred + "', '" + nomKred + "');");

                }
            }
        });

    }
}
