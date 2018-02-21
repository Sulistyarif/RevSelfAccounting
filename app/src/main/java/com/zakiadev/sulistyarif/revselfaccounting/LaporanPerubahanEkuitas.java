package com.zakiadev.sulistyarif.revselfaccounting;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.zakiadev.sulistyarif.revselfaccounting.data.DataSaldo;
import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapterMix;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sulistyarif on 20/02/18.
 */

public class LaporanPerubahanEkuitas extends AppCompatActivity {

    Button btTgl;
    WebView webView;
    String tglDipilih;
    Spinner spinnerMonth, spinnerYear;
    private String[] listBulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
    private String[] listTahun = new String[50];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_perubahan_ekuitas_activity);

        spinnerMonth = (Spinner)findViewById(R.id.spinnerEkuiMonth);
        spinnerYear = (Spinner)findViewById(R.id.spinnerEkuiYear);

//        ambil waktu sekarang
        Date currentDate = Calendar.getInstance().getTime();

//        setting spinner bulan
        final ArrayAdapter<String> adapterSpinnerMonth = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, listBulan);
        spinnerMonth.setAdapter(adapterSpinnerMonth);
        spinnerMonth.setSelection(currentDate.getMonth());

//        setting spinner tahun
        int c = 1990;
        for (int i=0; i<listTahun.length; i++){
            listTahun[i] = String.valueOf(c);
            c++;
        }
        final ArrayAdapter<String> adapterSpinnerYear = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, listTahun);
        spinnerYear.setAdapter(adapterSpinnerYear);
        spinnerYear.setSelection(currentDate.getYear()-90);

//        setting webView
        webView = (WebView)findViewById(R.id.wvEkuitas);
        webView.loadUrl("file:///android_asset/perubahan_ekuitas.html");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setBuiltInZoomControls(true);

//        ngisi data di webview
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                ArrayList<DataSaldo> dataSaldos = new DBAdapterMix(LaporanPerubahanEkuitas.this).selectAkunTertentu();
                DataSaldo dataSaldo;

                for (int i = 0; i< dataSaldos.size(); i++){
                    dataSaldo = dataSaldos.get(i);

                    String kodeAkun = dataSaldo.getKodeAkun();
                    String namaAkun = dataSaldo.getNamaAkun();
                    String nominal = String.valueOf(dataSaldo.getNominal());

//                    totalPendapatan += dataSaldo.getNominal();

                    webView.loadUrl("javascript:tambahData('" + kodeAkun + "', '" + namaAkun + "', '" + nominal + "');");

                }
            }
        });

    }

}
