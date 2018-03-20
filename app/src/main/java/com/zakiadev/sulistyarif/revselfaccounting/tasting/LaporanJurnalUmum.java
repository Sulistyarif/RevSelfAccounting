package com.zakiadev.sulistyarif.revselfaccounting.tasting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.zakiadev.sulistyarif.revselfaccounting.LaporanNeracaSaldoActivity;
import com.zakiadev.sulistyarif.revselfaccounting.R;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataPerusahaan;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataTransaksiMar;
import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapterMix;

import java.util.ArrayList;

/**
 * Created by sulistyarif on 14/02/18.
 */

public class LaporanJurnalUmum extends AppCompatActivity {

    WebView webView;
    FloatingActionButton fab;

//    @SuppressLint("JavascriptInterface")
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);

        fab = (FloatingActionButton)findViewById(R.id.fabJurnal);

        webView = (WebView)findViewById(R.id.wv);
        webView.loadUrl("file:///android_asset/index.html");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setBuiltInZoomControls(true);
//        webView.addJavascriptInterface(new JavaScriptInterface(LaporanJurnalUmum.this), "Android");
//        nggak jadi
        webView.addJavascriptInterface(new Object(){
            public void performClick(){
                Toast.makeText(LaporanJurnalUmum.this, "testing javascript",Toast.LENGTH_SHORT).show();
            }
        }, "test");

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

//                set header pake nama pers
                DataPerusahaan dataPerusahaan = new DBAdapterMix(LaporanJurnalUmum.this).selectDataPerusahaan();
                webView.loadUrl("javascript:setNamaPers('" + dataPerusahaan.getNamaPers() + "');");

                ArrayList<DataTransaksiMar> dataTransaksiMarArrayList = new DBAdapterMix(LaporanJurnalUmum.this).selectTransMar();
                DataTransaksiMar dataTransaksiMar;
                DataTransaksiMar dataTransaksiMar0 = dataTransaksiMarArrayList.get(0);
                String pidSebelum = dataTransaksiMar0.getPid();
                int j = 0;
                int saldoDebet, saldoKredit;
                saldoDebet = saldoKredit = 0;
                String[][] data = new String[dataTransaksiMarArrayList.size()][8];

                for (int i = 0; i < dataTransaksiMarArrayList.size(); i++){
                    dataTransaksiMar = dataTransaksiMarArrayList.get(i);

                    if (dataTransaksiMar.getPos() == 0){
                        saldoDebet += Math.abs(dataTransaksiMar.getNominal());
                    }else{
                        saldoKredit += Math.abs(dataTransaksiMar.getNominal());
                    }

                    if (dataTransaksiMar.getPid().equals(pidSebelum)){
                        data[j][0] = dataTransaksiMar.getPid();
                        data[j][1] = dataTransaksiMar.getTgl();
                        data[j][2] = dataTransaksiMar.getKet();
                        data[j][3] = dataTransaksiMar.getKodeAkun();
                        data[j][4] = dataTransaksiMar.getNamaAkun();
                        data[j][5] = String.valueOf(Math.abs(dataTransaksiMar.getNominal()));
                        data[j][6] = String.valueOf(dataTransaksiMar.getJenis());
                        data[j][7] = String.valueOf(dataTransaksiMar.getPos());
                        pidSebelum = dataTransaksiMar.getPid();
                        j++;
                    }else {
                        webView.loadUrl("javascript:addRow('" + data[0][1] + "', '" + data[0][3] +"', '" + data[0][4] +"','" + data[0][5] +"','" + data[0][7] +"');");
                        for (int k = 1; k < j; k++){
                            webView.loadUrl("javascript:addRow('', '" + data[k][3] +"', '" + data[k][4] +"','" + data[k][5] +"','" + data[k][7] +"');");
                        }
                        webView.loadUrl("javascript:addRow('','','(" + data[0][2] + ")','','');");
                        j = 0;
                        data[j][0] = dataTransaksiMar.getPid();
                        data[j][1] = dataTransaksiMar.getTgl();
                        data[j][2] = dataTransaksiMar.getKet();
                        data[j][3] = dataTransaksiMar.getKodeAkun();
                        data[j][4] = dataTransaksiMar.getNamaAkun();
                        data[j][5] = String.valueOf(Math.abs(dataTransaksiMar.getNominal()));
                        data[j][6] = String.valueOf(dataTransaksiMar.getJenis());
                        data[j][7] = String.valueOf(dataTransaksiMar.getPos());
                        pidSebelum = dataTransaksiMar.getPid();
                        j++;
//                        Log.i("bleketek","bleketek " + i);
                    }

                }
                webView.loadUrl("javascript:addRow('" + data[0][1] + "', '" + data[0][3] +"', '" + data[0][4] +"','" + data[0][5] +"','" + data[0][7] +"');");
                for (int k = 1; k < j; k++){
                    webView.loadUrl("javascript:addRow('', '" + data[k][3] +"', '" + data[k][4] +"','" + data[k][5] +"','" + data[k][7] +"');");
                }
                webView.loadUrl("javascript:addRow('','','(" + data[0][2] + ")','','');");

                webView.loadUrl("javascript:total('" + saldoDebet + "','" + saldoKredit + "');");

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                            createWebPrintJob(webView);
                        } else {
                            Toast.makeText(LaporanJurnalUmum.this, "Versi Android Anda Tidak Mendukung Export PDF",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

    }

    private void createWebPrintJob(WebView webView) {
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        PrintDocumentAdapter printAdapter =
                this.webView.createPrintDocumentAdapter();

        String jobName = getString(R.string.app_name) + " Print Test";

        if (printManager != null) {
            printManager.print(jobName, printAdapter,
                    new PrintAttributes.Builder().build());
        }
    }
}
