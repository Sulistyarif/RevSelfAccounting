package com.zakiadev.sulistyarif.revselfaccounting;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by sulistyarif on 20/02/18.
 */

public class LaporanPerubahanEkuitas extends AppCompatActivity {

    Button btTgl;
    WebView webView;
    String tglDipilih;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_perubahan_ekuitas_activity);

        btTgl = (Button)findViewById(R.id.btTglEkuitas);

//        setting tanggal sekarang ke dalam button
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyy");
        tglDipilih = simpleDateFormat.format(date);
        btTgl.setText(tglDipilih);

//        bikin datePicker dialog
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateButtonText(calendar);
            }
        };

//        ketika button tanggal diklik, muncul dialog picker
        btTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(LaporanPerubahanEkuitas.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

//        setting webView
        webView = (WebView)findViewById(R.id.wvEkuitas);
        webView.loadUrl("file:///android_asset/perubahan_ekuitas.html");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setBuiltInZoomControls(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });

    }

//    method untuk mengubah button tanggal ketika setelah milih tanggal
    private void updateButtonText(Calendar calendar) {
        String formatTgl = "dd-MM-yyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatTgl, Locale.US);
        tglDipilih = simpleDateFormat.format(calendar.getTime());
        btTgl.setText(tglDipilih);
        Log.i("perubahanEkuitas","bulan yang dipilih : " + calendar.getTime().getMonth());
        Log.i("perubahanEkuitas","tahun yang dipilih : " + calendar.getTime().getYear());
    }
}
