package com.zakiadev.sulistyarif.revselfaccounting;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.zakiadev.sulistyarif.revselfaccounting.data.DataJurnal;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataModal;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataPerusahaan;
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

    String strBulan, strTahun;
    WebView webView;
    Spinner spinnerMonth, spinnerYear;
    private String[] listBulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
    private String[] listTahun = new String[50];
    int bulanDipilih, tahunDipilih;
    FloatingActionButton fabPrint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_perubahan_ekuitas_activity);

        fabPrint = (FloatingActionButton)findViewById(R.id.fabPErubahanEkui);

//        dikasi di awal biar ga null pointer
        webView = (WebView)findViewById(R.id.wvEkuitas);

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
                strBulan = parent.getItemAtPosition(position).toString();
                Log.i("Bulan yang dipilih : ", String.valueOf(position));
                webView.loadUrl("file:///android_asset/perubahan_ekuitas.html");
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
                strTahun = parent.getItemAtPosition(position).toString();
                Log.i("Tahun yang dipilih : ", String.valueOf(position));
                webView.loadUrl("file:///android_asset/perubahan_ekuitas.html");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//        setting webView
//        webView.loadUrl("file:///android_asset/perubahan_ekuitas.html");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setBuiltInZoomControls(true);

//        ngisi data di webview
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

//                setting header
                DataPerusahaan dataPerusahaan = new DBAdapterMix(LaporanPerubahanEkuitas.this).selectDataPerusahaan();
                webView.loadUrl("javascript:setNamaPersPerubahanEkuitas('" + dataPerusahaan.getNamaPers() + "');");
                webView.loadUrl("javascript:setPeriode('" + strBulan + "','" + strTahun + "');");

//                mencari modal awal sesuai tangggal yang diinputkan
                ArrayList<DataJurnal> dataJurnals = new DBAdapterMix(LaporanPerubahanEkuitas.this).selectModalAwalMar(bulanDipilih,tahunDipilih);
                DataJurnal dataJurnal;

                int modalAwal = 0;

                for (int i = 0; i< dataJurnals.size(); i++){
                    dataJurnal = dataJurnals.get(i);

                    String nominal = String.valueOf(dataJurnal.getNominalKredit());
                    Log.i("DataModalAwal : ", nominal);

                    modalAwal += dataJurnal.getNominalKredit();

                }

                webView.loadUrl("javascript:separator('" + "Modal Awal Per Tanggal 1 "  + strBulan + "', '" + modalAwal + "');");

//                  mencari modal tambahan, atau modal yang selain tanggal 1
                ArrayList<DataJurnal> dataJurnals1 = new DBAdapterMix(LaporanPerubahanEkuitas.this).selectModalTambahanMar(bulanDipilih,tahunDipilih);
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
                ArrayList<DataSaldo> dataSaldos = new DBAdapterMix(LaporanPerubahanEkuitas.this).selectLabaRugiMar(bulanDipilih,tahunDipilih);
                DataSaldo dataSaldo ;

                int labaRugi = 0;

                for (int i = 0; i< dataSaldos.size(); i++){
                    dataSaldo = dataSaldos.get(i);

                    String nominal = String.valueOf(dataSaldo.getNominal());
                    Log.i("DataModalTambahan : ", nominal);

                    labaRugi += dataSaldo.getNominal();

                }

                webView.loadUrl("javascript:tambahData('" + "Laba atau Rugi Periode Berjalan " + "', '" + labaRugi + "');");

//                mengambil data prive di bulan dan tahun pada input
                ArrayList<DataSaldo> dataSaldos1 = new DBAdapterMix(LaporanPerubahanEkuitas.this).selectPriveBlnThnMar(bulanDipilih,tahunDipilih);
                DataSaldo dataSaldo1 ;

                int prive = 0;

                for (int i = 0; i< dataSaldos1.size(); i++){
                    dataSaldo1 = dataSaldos1.get(i);

                    String nominal = String.valueOf(dataSaldo1.getNominal());
                    Log.i("DataModalTambahan : ", nominal);

                    prive += dataSaldo1.getNominal();

                }

                webView.loadUrl("javascript:tambahData('" + "Prive " + "', '" + prive + "');");

//                menampilkan total penambahan atau pengurangan modal
                int penambahanAtauPengurangnModal = modalTambahan + labaRugi - prive;
                webView.loadUrl("javascript:separator('" + "Penambahan atau Pengurangan Modal " + "', '" + penambahanAtauPengurangnModal + "');");

//                menampilkan modal di akhir bulan tersebut
                int modalAkhirBulan = modalAwal + penambahanAtauPengurangnModal;
                webView.loadUrl("javascript:separator('" + "Modal per 31 " + strBulan + " " + tahunDipilih + "', '" + modalAkhirBulan + "');");

//                ngambil kas buat nanti dimasukin ke table modal
                int kas = new DBAdapterMix(LaporanPerubahanEkuitas.this).selectKas(bulanDipilih,tahunDipilih);

//                masukin data modal akhir bulan ini biar jadi modal bulan depan
                DataModal dataModal = new DataModal();
                String bulanFormatted = String.format("%02d", bulanDipilih + 1);
//                dataModal.setTgl("01/" + bulanFormatted + "/" + strTahun);
                dataModal.setTgl(strTahun + "-" + bulanFormatted + "-01");
                dataModal.setNominal(modalAkhirBulan);
                dataModal.setNominalKas(kas);
                new DBAdapterMix(LaporanPerubahanEkuitas.this).insertModal(dataModal);

                Boolean isFinish = getIntent().getBooleanExtra("finish", false);
                if (isFinish){
                    finish();
                }

                fabPrint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                            createWebPrintJob(webView);
                        } else {
                            Toast.makeText(LaporanPerubahanEkuitas.this, "Versi Android Anda Tidak Mendukung Export PDF",Toast.LENGTH_SHORT).show();
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
