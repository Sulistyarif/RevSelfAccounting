package com.zakiadev.sulistyarif.revselfaccounting;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.zakiadev.sulistyarif.revselfaccounting.data.DataJurnal;
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
    int bulanDipilih, tahunDipilih;

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
        bulanDipilih = currentDate.getMonth();
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bulanDipilih = position+1;
                Log.i("Bulan yang dipilih : ", String.valueOf(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        setting spinner tahun
        int c = 1990;
        for (int i=0; i<listTahun.length; i++){
            listTahun[i] = String.valueOf(c);
            c++;
        }
        final ArrayAdapter<String> adapterSpinnerYear = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, listTahun);
        spinnerYear.setAdapter(adapterSpinnerYear);
        spinnerYear.setSelection(currentDate.getYear()-90);
        tahunDipilih = currentDate.getYear()-90;
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tahunDipilih = position+1990;
                Log.i("Tahun yang dipilih : ", String.valueOf(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

//                mencari modal awal sesuai tangggal yang diinputkan
                ArrayList<DataJurnal> dataJurnals = new DBAdapterMix(LaporanPerubahanEkuitas.this).selectModalAwal(bulanDipilih,tahunDipilih);
                DataJurnal dataJurnal;

                int modalAwal = 0;

                for (int i = 0; i< dataJurnals.size(); i++){
                    dataJurnal = dataJurnals.get(i);

                    String nominal = String.valueOf(dataJurnal.getNominalKredit());
                    Log.i("DataModalAwal : ", nominal);

                    modalAwal += dataJurnal.getNominalKredit();

                }

                webView.loadUrl("javascript:separator('" + "Modal Awal Per Tanggal 1" + "', '" + modalAwal + "');");

//                  mencari modal tambahan, atau modal yang selain tanggal 1
                ArrayList<DataJurnal> dataJurnals1 = new DBAdapterMix(LaporanPerubahanEkuitas.this).selectModalTambahan(bulanDipilih,tahunDipilih);
                DataJurnal dataJurnal1;

                int modalTambahan = 0;

                for (int i = 0; i< dataJurnals1.size(); i++){
                    dataJurnal1 = dataJurnals1.get(i);

                    String nominal = String.valueOf(dataJurnal1.getNominalKredit());
                    Log.i("DataModalTambahan : ", nominal);

                    modalTambahan += dataJurnal1.getNominalKredit();

                }

                webView.loadUrl("javascript:tambahData('" + "Tambahan Modal " + "', '" + modalTambahan + "');");

//                Laba Rugi pada periode yang diinputkan
                ArrayList<DataSaldo> dataSaldos = new DBAdapterMix(LaporanPerubahanEkuitas.this).selectLabaRugi(bulanDipilih,tahunDipilih);
                DataSaldo dataSaldo ;

                int labaRugi = 0;

                for (int i = 0; i< dataSaldos.size(); i++){
                    dataSaldo = dataSaldos.get(i);

                    String nominal = String.valueOf(dataSaldo.getNominal());
                    Log.i("DataModalTambahan : ", nominal);

                    labaRugi += dataSaldo.getNominal();

                }

                webView.loadUrl("javascript:tambahData('" + "Tambahan Modal " + "', '" + labaRugi + "');");

            }

        });



    }

}
