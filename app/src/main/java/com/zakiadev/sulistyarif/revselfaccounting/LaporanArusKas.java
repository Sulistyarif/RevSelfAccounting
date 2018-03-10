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

public class LaporanArusKas extends AppCompatActivity {

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

//                setting header
                DataPerusahaan dataPerusahaan = new DBAdapterMix(LaporanArusKas.this).selectDataPerusahaan();
                webView.loadUrl("javascript:setNamaPersArusKas('" + dataPerusahaan.getNamaPers() + "');");
                webView.loadUrl("javascript:setPeriode('" + strBulan + "','" + strTahun + "');");

//                mengambil data arus kas yang didapat dari aset lancar
                ArrayList<DataJurnal> dataJurnals = new DBAdapterMix(LaporanArusKas.this).selectArusKasOnDebetMar(bulanDipilih,tahunDipilih,0);
                DataJurnal dataJurnal ;

                int kasDariAsetLancar = 0;

                for (int i = 0; i< dataJurnals.size(); i++){
                    dataJurnal = dataJurnals.get(i);

                    kasDariAsetLancar += dataJurnal.getNominalKredit();
                    Log.i("arusKasAsetLncr : ", String.valueOf(kasDariAsetLancar));

                }

//                menampilkan data aset lancar ke webview
                if (kasDariAsetLancar != 0){
                    webView.loadUrl("javascript:separator('" + "Kas didapat dari aset lancar" + "', '" + kasDariAsetLancar +"', '" + "kasOp" +"');");
                }

//                mengambil data arus kas yang didapat dari hutang jangka pendek
                ArrayList<DataJurnal> dataJurnals1 = new DBAdapterMix(LaporanArusKas.this).selectArusKasOnDebetMar(bulanDipilih,tahunDipilih,2);
                DataJurnal dataJurnal1 ;

                int kasDariHutangJangkaPendek = 0;

                for (int i = 0; i< dataJurnals1.size(); i++){
                    dataJurnal1 = dataJurnals1.get(i);

                    kasDariHutangJangkaPendek += dataJurnal1.getNominalKredit();
                    Log.i("arusKasHtangJngkPnd : ", String.valueOf(kasDariHutangJangkaPendek));

                }

//                menampilkan data hutang jangka pendek ke webview
                if (kasDariHutangJangkaPendek != 0){
                    webView.loadUrl("javascript:separator('" + "Kas didapat dari hutang jangka pendek" + "', '" + kasDariHutangJangkaPendek +"', '" + "kasOp" +"');");
                }

//                mengambil data arus kas yang didapat dari pendapatan operasional
                ArrayList<DataJurnal> dataJurnals2 = new DBAdapterMix(LaporanArusKas.this).selectArusKasOnDebetMar(bulanDipilih,tahunDipilih,5);
                DataJurnal dataJurnal2 ;

                int kasDariPendapatanOp = 0;

                for (int i = 0; i< dataJurnals2.size(); i++){
                    dataJurnal2 = dataJurnals2.get(i);

                    kasDariPendapatanOp += dataJurnal2.getNominalKredit();
                    Log.i("arusKasPendapatanOp : ", String.valueOf(kasDariPendapatanOp));

                }

//                menampilkan datanya ke webview
                if (kasDariPendapatanOp != 0){
                    webView.loadUrl("javascript:separator('" + "Kas didapat dari pendapatan operasional" + "', '" + kasDariPendapatanOp +"', '" + "kasOp" +"');");
                }

//                mengambil data arus kas yang didapat dari pendapatan non operasional
                ArrayList<DataJurnal> dataJurnals3 = new DBAdapterMix(LaporanArusKas.this).selectArusKasOnDebetMar(bulanDipilih,tahunDipilih,6);
                DataJurnal dataJurnal3 ;

                int kasDariPendapatanNonOp = 0;

                for (int i = 0; i< dataJurnals3.size(); i++){
                    dataJurnal3 = dataJurnals3.get(i);

                    kasDariPendapatanNonOp += dataJurnal3.getNominalKredit();
                    Log.i("arusKasPndptanNonOp : ", String.valueOf(kasDariPendapatanNonOp));

                }

//                menampilkan datanya ke webview
                if (kasDariPendapatanNonOp != 0){
                    webView.loadUrl("javascript:separator('" + "Kas didapat dari pendapatan non operasional" + "', '" + kasDariPendapatanNonOp +"', '" + "kasOp" +"');");
                }

//                mengambil data arus kas yang didapat dari biaya Operasional
                ArrayList<DataJurnal> dataJurnals4 = new DBAdapterMix(LaporanArusKas.this).selectArusKasOnDebetMar(bulanDipilih,tahunDipilih,7);
                DataJurnal dataJurnal4 ;

                int kasDariBiayaOp = 0;

                for (int i = 0; i< dataJurnals4.size(); i++){
                    dataJurnal4 = dataJurnals4.get(i);

                    kasDariBiayaOp += dataJurnal4.getNominalKredit();
                    Log.i("arusKasBiayaOp : ", String.valueOf(kasDariBiayaOp));

                }

//                menampilkan datanya ke webview
                if (kasDariPendapatanNonOp != 0){
                    webView.loadUrl("javascript:separator('" + "Kas didapat dari biaya operasional" + "', '" + kasDariBiayaOp +"', '" + "kasOp" +"');");
                }

//                mengambil data arus kas yang didapat dari biaya Non Operasional
                ArrayList<DataJurnal> dataJurnals5 = new DBAdapterMix(LaporanArusKas.this).selectArusKasOnDebetMar(bulanDipilih,tahunDipilih,8);
                DataJurnal dataJurnal5 ;

                int kasDariBiayaNonOp = 0;

                for (int i = 0; i< dataJurnals5.size(); i++){
                    dataJurnal5 = dataJurnals5.get(i);

                    kasDariBiayaNonOp += dataJurnal5.getNominalKredit();
                    Log.i("arusKasBiayaNonOp : ", String.valueOf(kasDariBiayaNonOp));

                }

//                menampilkan datanya ke webview
                if (kasDariPendapatanNonOp != 0){
                    webView.loadUrl("javascript:separator('" + "Kas didapat dari biaya operasional" + "', '" + kasDariBiayaNonOp +"', '" + "kasOp" +"');");
                }

//                CAUTION !!!!!
//                MULAI YANG BERNILAI MINUS PADA AKTIVITAS OPERASI


//                mengambil data arus kas yang bayar untuk aset lancar
                ArrayList<DataJurnal> dataJurnals01 = new DBAdapterMix(LaporanArusKas.this).selectArusKasOnKreditMar(bulanDipilih,tahunDipilih,0);
                DataJurnal dataJurnal01 ;

                int kasUntukAsetLancar = 0;

                for (int i = 0; i< dataJurnals01.size(); i++){
                    dataJurnal01 = dataJurnals01.get(i);

                    kasUntukAsetLancar += dataJurnal01.getNominalKredit();
                    Log.i("brkrngDrAsetLncr : ", String.valueOf(kasUntukAsetLancar));

                }

//                menampilkan data aset lancar ke webview
                if (kasUntukAsetLancar != 0){
                    webView.loadUrl("javascript:separatorBayar('" + "Kas dibayar untuk aset lancar" + "', '" + kasUntukAsetLancar +"', '" + "kasOp" +"');");
                }

//                mengambil data arus kas yang diabayar untuk hutang jangka pendek
                ArrayList<DataJurnal> dataJurnals11 = new DBAdapterMix(LaporanArusKas.this).selectArusKasOnKreditMar(bulanDipilih,tahunDipilih,2);
                DataJurnal dataJurnal11 ;

                int kasUntukHutangJangkaPendek = 0;

                for (int i = 0; i< dataJurnals11.size(); i++){
                    dataJurnal11 = dataJurnals11.get(i);

                    kasUntukHutangJangkaPendek += dataJurnal11.getNominalKredit();
                    Log.i("kasUtkHtangJngkPnd : ", String.valueOf(kasUntukHutangJangkaPendek));

                }

//                menampilkan data hutang jangka pendek ke webview
                if (kasUntukHutangJangkaPendek != 0){
                    webView.loadUrl("javascript:separatorBayar('" + "Kas dibayar untuk hutang jangka pendek" + "', '" + kasUntukHutangJangkaPendek +"', '" + "kasOp" +"');");
                }

//                mengambil data arus kas yang dibayar untuk pendapatan operasional
                ArrayList<DataJurnal> dataJurnals21 = new DBAdapterMix(LaporanArusKas.this).selectArusKasOnKreditMar(bulanDipilih,tahunDipilih,5);
                DataJurnal dataJurnal21 ;

                int kasUntukPendapatanOp = 0;

                for (int i = 0; i< dataJurnals21.size(); i++){
                    dataJurnal21 = dataJurnals21.get(i);

                    kasUntukPendapatanOp += dataJurnal21.getNominalKredit();
                    Log.i("kasUntukPendapatanOp : ", String.valueOf(kasUntukPendapatanOp));

                }

//                menampilkan datanya ke webview
                if (kasUntukPendapatanOp != 0){
                    webView.loadUrl("javascript:separatorBayar('" + "Kas didapat dari pendapatan operasional" + "', '" + kasUntukPendapatanOp +"', '" + "kasOp" +"');");
                }

//                mengambil data arus kas yang dibayar untuk pendapatan non operasional
                ArrayList<DataJurnal> dataJurnals31 = new DBAdapterMix(LaporanArusKas.this).selectArusKasOnKreditMar(bulanDipilih,tahunDipilih,6);
                DataJurnal dataJurnal31 ;

                int kasUntukPendapatanNonOp = 0;

                for (int i = 0; i< dataJurnals31.size(); i++){
                    dataJurnal31 = dataJurnals31.get(i);

                    kasUntukPendapatanNonOp += dataJurnal31.getNominalKredit();
                    Log.i("kasUtkPndptanNonOp : ", String.valueOf(kasUntukPendapatanNonOp));

                }

//                menampilkan datanya ke webview
                if (kasUntukPendapatanNonOp != 0){
                    webView.loadUrl("javascript:separatorBayar('" + "Kas dibayar untuk pendapatan non operasional" + "', '" + kasUntukPendapatanNonOp +"', '" + "kasOp" +"');");
                }

//                mengambil data arus kas yang dibayar untuk biaya Operasional
                ArrayList<DataJurnal> dataJurnals41 = new DBAdapterMix(LaporanArusKas.this).selectArusKasOnKreditMar(bulanDipilih,tahunDipilih,7);
                DataJurnal dataJurnal41 ;

                int kasUntukBiayaOp = 0;

                for (int i = 0; i< dataJurnals41.size(); i++){
                    dataJurnal41 = dataJurnals41.get(i);

                    kasUntukBiayaOp += dataJurnal41.getNominalKredit();
                    Log.i("kasUtkBiayaOp : ", String.valueOf(kasUntukBiayaOp));

                }

//                menampilkan datanya ke webview
                if (kasUntukBiayaOp != 0){
                    webView.loadUrl("javascript:separatorBayar('" + "Kas dibayar untuk biaya operasional" + "', '" + kasUntukBiayaOp +"', '" + "kasOp" +"');");
                }

//                mengambil data arus kas yang dibayar untuk biaya Non Operasional
                ArrayList<DataJurnal> dataJurnals51 = new DBAdapterMix(LaporanArusKas.this).selectArusKasOnKreditMar(bulanDipilih,tahunDipilih,8);
                DataJurnal dataJurnal51 ;

                int kasUntukBiayaNonOp = 0;

                for (int i = 0; i< dataJurnals51.size(); i++){
                    dataJurnal51 = dataJurnals51.get(i);

                    kasUntukBiayaNonOp += dataJurnal51.getNominalKredit();
                    Log.i("kasUtkBiayaNonOp : ", String.valueOf(kasUntukBiayaNonOp));

                }

//                menampilkan datanya ke webview
                if (kasUntukBiayaNonOp != 0){
                    webView.loadUrl("javascript:separatorBayar('" + "Kas didapat dari biaya operasional" + "', '" + kasUntukBiayaNonOp +"', '" + "kasOp" +"');");
                }

//                CAUTION
//                MENGHITUNG TOTAL ARUS KAS DARI AKTIVITAS OPERASI
                int totalAktivitasKas = kasDariAsetLancar - kasUntukAsetLancar + kasDariHutangJangkaPendek - kasUntukHutangJangkaPendek + kasDariPendapatanOp -
                        kasUntukPendapatanOp + kasDariPendapatanNonOp - kasUntukPendapatanNonOp + kasDariBiayaOp - kasUntukBiayaOp + kasDariBiayaNonOp - kasUntukBiayaNonOp;
                webView.loadUrl("javascript:separator('" + "ARUS KAS DARI AKTIVITAS OPERASI" + "', '" + totalAktivitasKas +"', '" + "kasOp" +"');");

//                CAUTION
//                PINDAH MENUJU KE AKTIVITAS INVESTASI

//                mengambil data arus kas yang didapat dari aset tetap
                ArrayList<DataJurnal> dataJurnals02 = new DBAdapterMix(LaporanArusKas.this).selectArusKasOnDebetMar(bulanDipilih,tahunDipilih,1);
                DataJurnal dataJurnal02 ;

                int kasDariAsetTetap = 0;

                for (int i = 0; i< dataJurnals02.size(); i++){
                    dataJurnal02 = dataJurnals02.get(i);

                    kasDariAsetTetap += dataJurnal02.getNominalKredit();
                    Log.i("arusKasAsetTtp : ", String.valueOf(kasDariAsetTetap));

                }

//                menampilkan data aset tetap ke webview
                if (kasDariAsetTetap != 0){
                    webView.loadUrl("javascript:separator('" + "Kas didapat dari penjualan aset tetap" + "', '" + kasDariAsetTetap +"', '" + "kasInvest" +"');");
                }

//                CAUTION
//                PINDAH KE KAS DIBAYAR UNTUK ASET TETAP

//                mengambil data arus kas yang dibayar untuk aset tetap
                ArrayList<DataJurnal> dataJurnals12 = new DBAdapterMix(LaporanArusKas.this).selectArusKasOnKreditMar(bulanDipilih,tahunDipilih,1);
                DataJurnal dataJurnal12 ;

                int kasUntukAsetTetap = 0;

                for (int i = 0; i< dataJurnals12.size(); i++){
                    dataJurnal12 = dataJurnals12.get(i);

                    kasUntukAsetTetap += dataJurnal12.getNominalKredit();
                    Log.i("kasUtkAsetTtp : ", String.valueOf(kasUntukAsetTetap));

                }

//                menampilkan datanya ke webview
                if (kasUntukAsetTetap != 0){
                    webView.loadUrl("javascript:separatorBayar('" + "Kas dibayar untuk pembelian aset tetap" + "', '" + kasUntukAsetTetap +"', '" + "kasInvest" +"');");
                }

//                PENGHITUNGAN TOTAL AKTIVITAS INVESTASI
                int totalAktivitasInvestasi = kasDariAsetTetap - kasUntukAsetTetap;
                webView.loadUrl("javascript:separator('" + "ARUS KAS DARI AKTIVITAS INVESTASI" + "', '" + totalAktivitasInvestasi +"', '" + "kasInvest" +"');");

//                CAUTION
//                PINDAH KE KAS DARI AKTIVITAS PENDANAAN

//                mengambil data arus kas yang didapat dari HUTANG JANGKA PANJANG
                ArrayList<DataJurnal> dataJurnals03 = new DBAdapterMix(LaporanArusKas.this).selectArusKasOnDebetMar(bulanDipilih,tahunDipilih,3);
                DataJurnal dataJurnal03 ;

                int kasDariHutangJangkaPanjang = 0;

                for (int i = 0; i< dataJurnals03.size(); i++){
                    dataJurnal03 = dataJurnals03.get(i);

                    kasDariHutangJangkaPanjang += dataJurnal03.getNominalKredit();
                    Log.i("arusKasHtngJngkPjg : ", String.valueOf(kasDariHutangJangkaPanjang));

                }

//                menampilkan data hutang jangka panjang ke webview
                if (kasDariHutangJangkaPanjang != 0){
                    webView.loadUrl("javascript:separator('" + "Kas didapat dari hutang jangka panjang" + "', '" + kasDariHutangJangkaPanjang +"', '" + "kasDana" +"');");
                }

//                mengambil data arus kas yang didapat dari ekuitas
                ArrayList<DataJurnal> dataJurnals13 = new DBAdapterMix(LaporanArusKas.this).selectArusKasOnDebetMar(bulanDipilih,tahunDipilih,4);
                DataJurnal dataJurnal13 ;

                int kasDariEkuitas = 0;

                for (int i = 0; i< dataJurnals13.size(); i++){
                    dataJurnal13 = dataJurnals13.get(i);

                    kasDariEkuitas += dataJurnal13.getNominalKredit();
                    Log.i("arusKasDrEkuitas : ", String.valueOf(kasDariEkuitas));

                }

//                menampilkan data hutang jangka pendek ke webview
                if (kasDariEkuitas != 0){
                    webView.loadUrl("javascript:separator('" + "Kas didapat dari ekuitas" + "', '" + kasDariEkuitas +"', '" + "kasDana" +"');");
                }

//                mengambil data arus kas yang didapat dari pengembalian ekuitas
                ArrayList<DataJurnal> dataJurnals23 = new DBAdapterMix(LaporanArusKas.this).selectArusKasOnDebetMar(bulanDipilih,tahunDipilih,9);
                DataJurnal dataJurnal23 ;

                int kasDariPengembalianEkuitas = 0;

                for (int i = 0; i< dataJurnals23.size(); i++){
                    dataJurnal23 = dataJurnals23.get(i);

                    kasDariPengembalianEkuitas += dataJurnal23.getNominalKredit();
                    Log.i("arusKasDrPngmblnEkui : ", String.valueOf(kasDariPengembalianEkuitas));

                }

//                menampilkan datanya ke webview
                if (kasDariPengembalianEkuitas != 0){
                    webView.loadUrl("javascript:separator('" + "Kas didapat dari pengembalian Ekuitas" + "', '" + kasDariPengembalianEkuitas +"', '" + "kasDana" +"');");
                }

//                CAUTION
//                MULAI MENYEBERANG KE DIBAYAR UNTUK AKTIVITAS PENDANAAN

//                mengambil data arus kas yang bayar untuk HUTANG JANGKA PANJANG
                ArrayList<DataJurnal> dataJurnals04 = new DBAdapterMix(LaporanArusKas.this).selectArusKasOnKreditMar(bulanDipilih,tahunDipilih,3);
                DataJurnal dataJurnal04 ;

                int kasUntukHutangJangkaPanjang = 0;

                for (int i = 0; i< dataJurnals04.size(); i++){
                    dataJurnal04 = dataJurnals04.get(i);

                    kasUntukHutangJangkaPanjang += dataJurnal04.getNominalKredit();
                    Log.i("brkrngDrHtngPjg : ", String.valueOf(kasUntukHutangJangkaPanjang));

                }

//                menampilkan data hutang jangka panjang ke webview
                if (kasUntukHutangJangkaPanjang != 0){
                    webView.loadUrl("javascript:separatorBayar('" + "Kas dibayar untuk hutang jangka panjang" + "', '" + kasUntukHutangJangkaPanjang +"', '" + "kasDana" +"');");
                }

//                mengambil data arus kas yang diabayar untuk ekuitas
                ArrayList<DataJurnal> dataJurnals14 = new DBAdapterMix(LaporanArusKas.this).selectArusKasOnKreditMar(bulanDipilih,tahunDipilih,4);
                DataJurnal dataJurnal14 ;

                int kasUntukEkuitas = 0;

                for (int i = 0; i< dataJurnals14.size(); i++){
                    dataJurnal14 = dataJurnals14.get(i);

                    kasUntukEkuitas += dataJurnal14.getNominalKredit();
                    Log.i("kasUtkEkuit : ", String.valueOf(kasUntukEkuitas));

                }

//                menampilkan data hutang jangka pendek ke webview
                if (kasUntukEkuitas != 0){
                    webView.loadUrl("javascript:separatorBayar('" + "Kas dibayar untuk ekuitas" + "', '" + kasUntukEkuitas +"', '" + "kasDana" +"');");
                }

//                mengambil data arus kas yang dibayar untuk Pengembalian Ekuitas
                ArrayList<DataJurnal> dataJurnals24 = new DBAdapterMix(LaporanArusKas.this).selectArusKasOnKreditMar(bulanDipilih,tahunDipilih,9);
                DataJurnal dataJurnal24 ;

                int kasUntukPengembalianEkui = 0;

                for (int i = 0; i< dataJurnals24.size(); i++){
                    dataJurnal24 = dataJurnals24.get(i);

                    kasUntukPengembalianEkui += dataJurnal24.getNominalKredit();
                    Log.i("kasUntukPngmlnEkuit : ", String.valueOf(kasUntukPengembalianEkui));

                }

//                menampilkan datanya ke webview
                if (kasUntukPendapatanOp != 0){
                    webView.loadUrl("javascript:separatorBayar('" + "Kas didapat dari pengembalian ekuitas" + "', '" + kasUntukPengembalianEkui +"', '" + "kasDana" +"');");
                }

//                CAUTION
//                MENGHITUNG SALDO PENDANAAN
                int totalAktivitasPendanaan = kasDariHutangJangkaPanjang - kasUntukHutangJangkaPanjang + kasDariEkuitas - kasUntukEkuitas + kasDariPengembalianEkuitas - kasUntukPengembalianEkui;
                webView.loadUrl("javascript:separator('" + "ARUS KAS DARI AKTIVITAS PENDANAAN" + "', '" + totalAktivitasPendanaan +"', '" + "kasDana" +"');");

//                MENAMPILKAN DATA SALDO KAS AWAL BULAN INI YANG MERUPAKAN HASIL SUM KAS BULAN LALU
                ArrayList<DataJurnal> dataJurnalsaldo = new DBAdapterMix(LaporanArusKas.this).selectKasAwalMar(bulanDipilih,tahunDipilih);
                DataJurnal dataJurnalSaldo ;

                int saldoKasAwal = 0;

                for (int i = 0; i< dataJurnalsaldo.size(); i++){
                    dataJurnalSaldo = dataJurnalsaldo.get(i);

                    saldoKasAwal += dataJurnalSaldo.getNominalDebet();
                    Log.i("saldoKasAwal : ", String.valueOf(saldoKasAwal));

                }

                webView.loadUrl("javascript:separator('" + "SALDO KAS AWAL PERIODE" + "', '" + saldoKasAwal +"', '" + "kasDana" +"');");

//                SUPER CAUTION
//                MENGHITUNG SALDO KAS AKHIR SEMUANYA
                int saldoAkhir = totalAktivitasInvestasi + totalAktivitasKas + totalAktivitasPendanaan;
                webView.loadUrl("javascript:separator('" + "SALDO KAS AKHIR PERIODE" + "', '" + saldoAkhir +"', '" + "kasDana" +"');");

                fabPrint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                            createWebPrintJob(webView);
                        } else {
                            Toast.makeText(LaporanArusKas.this, "Versi Android Anda Tidak Mendukung Export PDF",Toast.LENGTH_SHORT).show();
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
