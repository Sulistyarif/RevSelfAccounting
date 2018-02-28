package com.zakiadev.sulistyarif.revselfaccounting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.zakiadev.sulistyarif.revselfaccounting.data.DataModal;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataSaldo;
import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapterMix;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sulistyarif on 19/02/18.
 */

public class LaporanNeracaActivity extends AppCompatActivity {

    WebView webView;
    Spinner spBulan,spTahun;
    private String[] listBulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
    private String[] listTahun = new String[50];
    int bulanDipilih, tahunDipilih;
    String strBulan, strTahun;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_neraca_activity);

        webView = (WebView)findViewById(R.id.wvNeraca);

        spBulan = (Spinner)findViewById(R.id.spNeracaBulan);
        spTahun = (Spinner)findViewById(R.id.spNeracaTahun);

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
                webView.loadUrl("file:///android_asset/neraca.html");
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
                webView.loadUrl("file:///android_asset/neraca.html");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//        setting webview
        webView.loadUrl("file:///android_asset/neraca.html");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setBuiltInZoomControls(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                int labaBersih = 0;

//                pengambilan data untuk aktiva lancar
                ArrayList<DataSaldo> dataSaldos = new DBAdapterMix(LaporanNeracaActivity.this).selectRiwayatJenisBlnThn(0, bulanDipilih, tahunDipilih);
                DataSaldo dataSaldo;

                int aktivaLancar = 0;

                for (int i = 0; i< dataSaldos.size(); i++){
                    dataSaldo = dataSaldos.get(i);

                    String kodeAkun = dataSaldo.getKodeAkun();
                    String namaAkun = dataSaldo.getNamaAkun();
                    String nominal = String.valueOf(dataSaldo.getNominal());

                    aktivaLancar += dataSaldo.getNominal();

                    webView.loadUrl("javascript:tambahDataAktiva('" + kodeAkun + "', '" + namaAkun + "', '" + nominal + "');");

                }
                    webView.loadUrl("javascript:separatorAktiva('" + "Total Aset Lancar " + "', '" + aktivaLancar + "');");

//                pengambilan data untuk aktiva tetap
                ArrayList<DataSaldo> dataSaldos1 = new DBAdapterMix(LaporanNeracaActivity.this).selectRiwayatJenisBlnThn(1, bulanDipilih, tahunDipilih);
                DataSaldo dataSaldo1;

                int aktivaTetap = 0;

                for (int i = 0; i< dataSaldos1.size(); i++){
                    dataSaldo1 = dataSaldos1.get(i);

                    String kodeAkun = dataSaldo1.getKodeAkun();
                    String namaAkun = dataSaldo1.getNamaAkun();
                    String nominal = String.valueOf(dataSaldo1.getNominal());

                    aktivaTetap += dataSaldo1.getNominal();

                    webView.loadUrl("javascript:tambahDataAktiva('" + kodeAkun + "', '" + namaAkun + "', '" + nominal + "');");

                }

                webView.loadUrl("javascript:separatorAktiva('" + "Total Aset Tetap" + "', '" + aktivaTetap + "');");

//                footer total aktiva
                int totalAktiva = aktivaLancar + aktivaTetap;
                webView.loadUrl("javascript:bigSeparatorAktiva('" + "TOTAL ASET" + "', '" + totalAktiva + "');");

//                pengambilan data untuk hutang lancar
                ArrayList<DataSaldo> dataSaldos2 = new DBAdapterMix(LaporanNeracaActivity.this).selectRiwayatJenisBlnThn(2, bulanDipilih, tahunDipilih);
                DataSaldo dataSaldo2;

                int totalHutangLancar = 0;

                for (int i = 0; i< dataSaldos2.size(); i++){
                    dataSaldo2 = dataSaldos2.get(i);

                    String kodeAkun = dataSaldo2.getKodeAkun();
                    String namaAkun = dataSaldo2.getNamaAkun();
                    String nominal = String.valueOf(dataSaldo2.getNominal());

                    totalHutangLancar += dataSaldo2.getNominal();

                    webView.loadUrl("javascript:tambahDataPasiva('" + kodeAkun + "', '" + namaAkun + "', '" + nominal + "');");

                }

                webView.loadUrl("javascript:separatorPasiva('" + "Total Hutang Lancar" + "', '" + totalHutangLancar + "');");

//                pengambilan data untuk biaya luar usaha
                ArrayList<DataSaldo> dataSaldos3 = new DBAdapterMix(LaporanNeracaActivity.this).selectRiwayatJenisBlnThn(3, bulanDipilih, tahunDipilih);
                DataSaldo dataSaldo3;

                int totalHutangJangkaPanjang = 0;

                for (int i = 0; i< dataSaldos3.size(); i++){
                    dataSaldo3 = dataSaldos3.get(i);

                    String kodeAkun = dataSaldo3.getKodeAkun();
                    String namaAkun = dataSaldo3.getNamaAkun();
                    String nominal = String.valueOf(dataSaldo3.getNominal());

                    totalHutangJangkaPanjang += dataSaldo3.getNominal();

                    webView.loadUrl("javascript:tambahDataPasiva('" + kodeAkun + "', '" + namaAkun + "', '" + nominal + "');");

                }

                webView.loadUrl("javascript:separatorPasiva('" + "Total Hutang Jangka Panjang" + "', '" + totalHutangJangkaPanjang + "');");

//                menghitung total laba tanggal tersebut
                ArrayList<DataSaldo> dataSaldos4 = new DBAdapterMix(LaporanNeracaActivity.this).selectModalNeraca(bulanDipilih,tahunDipilih);
                DataSaldo dataSaldo4;

                int modalPemilik = 0;
                String sModalPemilik = "";

                for (int i = 0; i< dataSaldos4.size(); i++){
                    dataSaldo4 = dataSaldos4.get(i);

                    sModalPemilik = String.valueOf(dataSaldo4.getNominal());

                    modalPemilik += dataSaldo4.getNominal();

                    webView.loadUrl("javascript:tambahDataPasiva('" + "3101" + "', '" + "Modal Pemilik" + "', '" + sModalPemilik + "');");

                }

                webView.loadUrl("javascript:separatorPasiva('" + "Total Ekuitas" + "', '" + modalPemilik + "');");

//                total pasiva
                int totalPasiva = totalHutangJangkaPanjang + totalHutangLancar + modalPemilik;
                webView.loadUrl("javascript:bigSeparatorPasiva('" + "TOTAL UTANG DAN EKUITAS" + "', '" + totalPasiva + "');");

            }
        });

    }
}
