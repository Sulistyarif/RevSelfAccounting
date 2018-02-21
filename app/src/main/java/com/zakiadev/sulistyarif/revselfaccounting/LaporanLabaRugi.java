package com.zakiadev.sulistyarif.revselfaccounting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zakiadev.sulistyarif.revselfaccounting.data.DataSaldo;
import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapterMix;

import java.util.ArrayList;

/**
 * Created by sulistyarif on 19/02/18.
 */

public class LaporanLabaRugi extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_laba_rugi_activity);

        webView = (WebView)findViewById(R.id.wvLabaRugi);
        webView.loadUrl("file:///android_asset/laba_rugi.html");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setBuiltInZoomControls(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                int labaBersih = 0;

//                pengambilan data untuk pendapatan

                ArrayList<DataSaldo> dataSaldos = new DBAdapterMix(LaporanLabaRugi.this).selectRiwayatJenis(5);
                DataSaldo dataSaldo;

                int totalPendapatan = 0;

                for (int i = 0; i< dataSaldos.size(); i++){
                    dataSaldo = dataSaldos.get(i);

                    String kodeAkun = dataSaldo.getKodeAkun();
                    String namaAkun = dataSaldo.getNamaAkun();
                    String nominal = String.valueOf(dataSaldo.getNominal());

                    totalPendapatan += dataSaldo.getNominal();

                    webView.loadUrl("javascript:tambahData('" + kodeAkun + "', '" + namaAkun + "', '" + nominal + "');");

                }
                    webView.loadUrl("javascript:separator('" + "Total Pendapatan Bersih" + "', '" + totalPendapatan + "');");

//                pengambilan data untuk beban biaya operasional

                ArrayList<DataSaldo> dataSaldos1 = new DBAdapterMix(LaporanLabaRugi.this).selectRiwayatJenis(6);
                DataSaldo dataSaldo1;

                int totalBeban = 0;

                for (int i = 0; i< dataSaldos1.size(); i++){
                    dataSaldo1 = dataSaldos1.get(i);

                    String kodeAkun = dataSaldo1.getKodeAkun();
                    String namaAkun = dataSaldo1.getNamaAkun();
                    String nominal = String.valueOf(dataSaldo1.getNominal());

                    totalBeban += dataSaldo1.getNominal();

                    webView.loadUrl("javascript:tambahData('" + kodeAkun + "', '" + namaAkun + "', '" + nominal + "');");

                }

                webView.loadUrl("javascript:separator('" + "Total Biaya" + "', '" + totalBeban + "');");

//                pengambilan data untuk pendapatan luar usaha
//                sedang dalam konfirmasi

//                pengambilan data untuk biaya luar usaha
                ArrayList<DataSaldo> dataSaldos3 = new DBAdapterMix(LaporanLabaRugi.this).selectRiwayatJenis(7);
                DataSaldo dataSaldo3;

                int totalBebanNonOp = 0;

                for (int i = 0; i< dataSaldos3.size(); i++){
                    dataSaldo3 = dataSaldos3.get(i);

                    String kodeAkun = dataSaldo3.getKodeAkun();
                    String namaAkun = dataSaldo3.getNamaAkun();
                    String nominal = String.valueOf(dataSaldo3.getNominal());

                    totalBebanNonOp += dataSaldo3.getNominal();

                    webView.loadUrl("javascript:tableDataPendapatan('" + kodeAkun + "', '" + namaAkun + "', '" + nominal + "');");

                }

                webView.loadUrl("javascript:separator('" + "Total Biaya Diluar Usaha" + "', '" + totalBebanNonOp + "');");


//                menghitung total laba
                labaBersih = totalPendapatan - totalBeban - totalBebanNonOp;

                webView.loadUrl("javascript:separator('" + "Laba Bersih" + "', '" + labaBersih + "');");

            }
        });

    }
}
