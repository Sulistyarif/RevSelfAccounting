package com.zakiadev.sulistyarif.revselfaccounting;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.zakiadev.sulistyarif.revselfaccounting.data.DataJurnal;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataPerusahaan;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataSaldo;
import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapterMix;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sulistyarif on 19/02/18.
 */

public class LaporanArusKas2 extends AppCompatActivity {

    WebView webView;
    Spinner spBulan,spTahun;
    private String[] listBulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
    private String[] listTahun = new String[50];
    int bulanDipilih, tahunDipilih;
    String strBulan, strTahun;
    FloatingActionButton fabPrint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_arus_kas_activity);

        fabPrint = (FloatingActionButton)findViewById(R.id.fabArusKas);

        webView = (WebView)findViewById(R.id.wvArusKas);

        spBulan = (Spinner)findViewById(R.id.spArusKasBln);
        spTahun = (Spinner)findViewById(R.id.spArusKasThn);

        //        ambil waktu sekarang
        Date currentDate = Calendar.getInstance().getTime();

//        setting spinner bulan
        final ArrayAdapter<String> adapterSpinnerMonth = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, listBulan);
        spBulan.setAdapter(adapterSpinnerMonth);
        spBulan.setSelection(currentDate.getMonth());
        bulanDipilih = currentDate.getMonth();
        spBulan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bulanDipilih = position+1;
                strBulan = parent.getItemAtPosition(position).toString();
                Log.i("Bulan yang dipilih : ", String.valueOf(position));
                webView.loadUrl("file:///android_asset/arus_kas.html");
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
        spTahun.setAdapter(adapterSpinnerYear);
        spTahun.setSelection(currentDate.getYear()-90);
        tahunDipilih = currentDate.getYear()-90;
        spTahun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tahunDipilih = position+1990;
                strTahun = parent.getItemAtPosition(position).toString();
                Log.i("Tahun yang dipilih : ", String.valueOf(position));
                webView.loadUrl("file:///android_asset/arus_kas.html");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//        setting webview
//        laod url disini di comment karena jika diaktifkan akan melakukan pemanggilan method onPageFinished 2 kali
//        webView.loadUrl("file:///android_asset/arus_kas.html");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setBuiltInZoomControls(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                int totalArusKasAktifitasOperasi = 0;
                int totalArusKasAktifitasInvestasi = 0;
                int totalArusKasAktifitasPendanaan = 0;

//                setting header
                DataPerusahaan dataPerusahaan = new DBAdapterMix(LaporanArusKas2.this).selectDataPerusahaan();
                webView.loadUrl("javascript:setNamaPersArusKas('" + dataPerusahaan.getNamaPers() + "');");
                webView.loadUrl("javascript:setPeriode('" + strBulan + "','" + strTahun + "');");


                Log.i("ArusKas2", "--mulai Laba");
                int laba = new DBAdapterMix(LaporanArusKas2.this).selectSumLabaRugiMar(bulanDipilih,tahunDipilih);

                totalArusKasAktifitasOperasi += laba;

                if (laba < 0){
                    webView.loadUrl("javascript:separatorMinus('Ikhtisar L/R (Saldo Laba)', '" + Math.abs(laba) + "', 'kasOp');");
                }else {
                    webView.loadUrl("javascript:separator('Ikhtisar L/R (Saldo Laba)', '" + Math.abs(laba) + "', 'kasOp');");
                }

//                menampilkan data aktifitas operasi
                Log.i("ArusKas2", "--mulai aktifitas operasi");
                ArrayList<DataSaldo> dataSaldos = new DBAdapterMix(LaporanArusKas2.this).selectArusKasMar2(bulanDipilih,tahunDipilih,0);
                DataSaldo dataSaldo;


                for (int i = 0; i< dataSaldos.size(); i++){
                    dataSaldo = dataSaldos.get(i);

                    String namaAkun = dataSaldo.getNamaAkun();
                    int jenisAkun = dataSaldo.getJenis();
                    long nominal = dataSaldo.getNominal();


                    if (!namaAkun.equals("Kas")){
                        if (jenisAkun == 0){
                            if (nominal > 0){
                                webView.loadUrl("javascript:separatorMinus('Kenaikan " + namaAkun + "', '" + Math.abs(nominal) + "', 'kasOp');");
                            }else {
                                webView.loadUrl("javascript:separator('Penurunan  " + namaAkun + "', '" + Math.abs(nominal) + "', 'kasOp');");
                            }
                            totalArusKasAktifitasOperasi -= nominal;
                        }else {
                            if (nominal > 0){
                                webView.loadUrl("javascript:separator('Kenaikan  " + namaAkun + "', '" + Math.abs(nominal) + "', 'kasOp');");
                            }else {
                                webView.loadUrl("javascript:separatorMinus('Penurunan " + namaAkun + "', '" + Math.abs(nominal) + "', 'kasOp');");
                            }
                            totalArusKasAktifitasOperasi += nominal;
                        }
                    }

                }

                webView.loadUrl("javascript:separator('ARUS KAS DARI AKTIVITAS OPERASI', '" + totalArusKasAktifitasOperasi + "', 'kasOp');");

//                menampilkan data aktifitas investasi
                Log.i("ArusKas2", "--mulai aktifitas investasi");
                ArrayList<DataSaldo> dataSaldos1 = new DBAdapterMix(LaporanArusKas2.this).selectArusKasMar2(bulanDipilih,tahunDipilih,1);

                for (int i = 0; i< dataSaldos1.size(); i++){
                    dataSaldo = dataSaldos1.get(i);

                    String namaAkun = dataSaldo.getNamaAkun();
                    int jenisAkun = dataSaldo.getJenis();
                    long nominal = dataSaldo.getNominal();


                    if (jenisAkun == 1){
                        if (nominal > 0){
                            webView.loadUrl("javascript:separatorMinus('Kenaikan  " + namaAkun + "', '" + Math.abs(nominal) + "', 'kasInvest');");
                        }else {
                            webView.loadUrl("javascript:separator('Penurunan " + namaAkun + "', '" + Math.abs(nominal) + "', 'kasInvest');");
                        }
                        totalArusKasAktifitasInvestasi -= nominal;
                    }else {
                        if (nominal > 0){
                            webView.loadUrl("javascript:separator('Penurunan " + namaAkun + "', '" + Math.abs(nominal) + "', 'kasInvest');");
                        }else {
                            webView.loadUrl("javascript:separatorMinus('Kenaikan  " + namaAkun + "', '" + Math.abs(nominal) + "', 'kasInvest');");
                        }
                        totalArusKasAktifitasInvestasi += nominal;
                    }
                }

                webView.loadUrl("javascript:separator('ARUS KAS DARI AKTIVITAS INVESTASI', '" + totalArusKasAktifitasInvestasi + "', 'kasInvest');");

//                menampilkan data arus kas aktifitas pendanaan
                Log.i("ArusKas2", "--mulai aktifitas pendanaan");
                ArrayList<DataSaldo> dataSaldos2 = new DBAdapterMix(LaporanArusKas2.this).selectArusKasMar2(bulanDipilih,tahunDipilih,2);

                for (int i = 0; i< dataSaldos2.size(); i++){
                    dataSaldo = dataSaldos2.get(i);

                    String namaAkun = dataSaldo.getNamaAkun();
                    long nominal = dataSaldo.getNominal();

                    totalArusKasAktifitasPendanaan += nominal;

                        if (nominal > 0){
                            webView.loadUrl("javascript:separator('Kenaikan  " + namaAkun + "', '" + Math.abs(nominal) + "', 'kasDana');");
                        }else {
                            webView.loadUrl("javascript:separatorMinus('Penurunan " + namaAkun + "', '" + Math.abs(nominal) + "', 'kasDana');");
                        }

                }

//                menampilkan data modal pemilik
                int modalPemilik = 0;
//                modalPemilik = new DBAdapterMix(LaporanArusKas2.this).selectModalPemilikArusKasMar(bulanDipilih,tahunDipilih);
                modalPemilik = new DBAdapterMix(LaporanArusKas2.this).selectTambahanModalBulanIniMar(bulanDipilih,tahunDipilih);

                if (modalPemilik != 0){
                    if (modalPemilik < 0){
                        webView.loadUrl("javascript:separatorMinus('Penurunan Modal', '" + Math.abs(modalPemilik) + "', 'kasDana');");
                    }else {
                        webView.loadUrl("javascript:separator('Kenaikan Modal', '" + Math.abs(modalPemilik) + "', 'kasDana');");
                    }
                    totalArusKasAktifitasPendanaan += modalPemilik;
                }

                int priveBulanIni = 0;
                priveBulanIni = new DBAdapterMix(LaporanArusKas2.this).selectPriveMar(bulanDipilih,tahunDipilih);
                if (priveBulanIni != 0){
                    if (priveBulanIni < 0){
                        webView.loadUrl("javascript:separatorMinus('Pengurangan Prive', '" + Math.abs(priveBulanIni) + "', 'kasDana');");
                    }else {
                        webView.loadUrl("javascript:separator('Penambahan Prive', '" + Math.abs(priveBulanIni) + "', 'kasDana');");
                    }
                    totalArusKasAktifitasPendanaan -= priveBulanIni;
                }

                if (totalArusKasAktifitasPendanaan < 0){
                    webView.loadUrl("javascript:separatorMinus('ARUS KAS DARI AKTIVITAS PENDANAAN', '" + totalArusKasAktifitasPendanaan + "', 'kasDana');");
                }else {
                    webView.loadUrl("javascript:separator('ARUS KAS DARI AKTIVITAS PENDANAAN', '" + totalArusKasAktifitasPendanaan + "', 'kasDana');");
                }

                boolean isFirst = false;
                isFirst = new DBAdapterMix(LaporanArusKas2.this).isFirstMonth(bulanDipilih,tahunDipilih);
                Log.i("nilaiBoolean", "Nilainya: " + String.valueOf(isFirst));

                if (isFirst){
//                    kalo ini merupakan bulan pertama maka tidak menambahkan saldo kas awal
                    //                menampilkan total arus kas
                    int totalArusKas = totalArusKasAktifitasOperasi + totalArusKasAktifitasPendanaan + totalArusKasAktifitasInvestasi;
                    if (totalArusKas < 0){
                        webView.loadUrl("javascript:separatorMinus('TOTAL ARUS KAS', '" + totalArusKas + "', 'kasDana');");
                    }else {
                        webView.loadUrl("javascript:separator('TOTAL ARUS KAS', '" + totalArusKas + "', 'kasDana');");
                    }

                }else {
//                MENAMPILKAN DATA SALDO KAS AWAL BULAN INI YANG MERUPAKAN HASIL SUM KAS BULAN LALU
                    ArrayList<DataJurnal> dataJurnalsaldo = new DBAdapterMix(LaporanArusKas2.this).selectKasAwalMar(bulanDipilih,tahunDipilih);
                    DataJurnal dataJurnalSaldoKasAwal;

                    int saldoKasAwal = 0;

                    for (int i = 0; i< dataJurnalsaldo.size(); i++){
                        dataJurnalSaldoKasAwal = dataJurnalsaldo.get(i);

                        saldoKasAwal += dataJurnalSaldoKasAwal.getNominalDebet();
                        Log.i("saldoKasAwal : ", String.valueOf(saldoKasAwal));

                    }

                    webView.loadUrl("javascript:separator('" + "SALDO KAS AWAL PERIODE" + "', '" + saldoKasAwal +"', '" + "kasDana" +"');");

//                menampilkan total arus kas
                    int totalArusKas = totalArusKasAktifitasOperasi + totalArusKasAktifitasPendanaan + totalArusKasAktifitasInvestasi + saldoKasAwal;
                    if (totalArusKas < 0){
                        webView.loadUrl("javascript:separatorMinus('TOTAL ARUS KAS', '" + totalArusKas + "', 'kasDana');");
                    }else {
                        webView.loadUrl("javascript:separator('TOTAL ARUS KAS', '" + totalArusKas + "', 'kasDana');");
                    }

                }


                fabPrint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                            createWebPrintJob(webView);
                        } else {
                            Toast.makeText(LaporanArusKas2.this, "Versi Android Anda Tidak Mendukung Export PDF",Toast.LENGTH_SHORT).show();
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