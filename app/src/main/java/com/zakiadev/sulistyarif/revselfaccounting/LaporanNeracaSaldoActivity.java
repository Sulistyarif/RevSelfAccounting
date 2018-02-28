package com.zakiadev.sulistyarif.revselfaccounting;

import android.graphics.Color;
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

import com.zakiadev.sulistyarif.revselfaccounting.data.DataJurnal;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataSaldo;
import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapterMix;
import com.zakiadev.sulistyarif.revselfaccounting.tablehelper.TableHelperDataAkun;
import com.zakiadev.sulistyarif.revselfaccounting.tablehelper.TableHelperDataSaldo;
import com.zakiadev.sulistyarif.revselfaccounting.tasting.JavaScriptInterface;
import com.zakiadev.sulistyarif.revselfaccounting.tasting.WebviewActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * Created by sulistyarif on 13/02/18.
 */

public class LaporanNeracaSaldoActivity extends AppCompatActivity {

    Spinner spBulan,spTahun;
    private String[] listBulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
    private String[] listTahun = new String[50];
    int bulanDipilih, tahunDipilih;
    String strBulan, strTahun;
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_neraca_saldo);

        webView = (WebView)findViewById(R.id.wvNeracaSaldo);

        spBulan = (Spinner)findViewById(R.id.spNSBln);
        spTahun = (Spinner)findViewById(R.id.spNSThn);

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
                webView.loadUrl("file:///android_asset/neraca_saldo.html");
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
                webView.loadUrl("file:///android_asset/neraca_saldo.html");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        setting web view
//        webView.loadUrl("file:///android_asset/neraca_saldo.html");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setBuiltInZoomControls(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                ArrayList<DataSaldo> dataSaldos = new DBAdapterMix(LaporanNeracaSaldoActivity.this).selectRiwayat(bulanDipilih,tahunDipilih);
                DataSaldo dataSaldo;
                int saldoDebet = 0 ,saldoKredit = 0;

                for (int i = 0; i< dataSaldos.size(); i++){
                    dataSaldo = dataSaldos.get(i);

                    String kodeAkun = dataSaldo.getKodeAkun();
                    String namaAkun = dataSaldo.getNamaAkun();
                    String nominal = String.valueOf(dataSaldo.getNominal());
                    int jenis = dataSaldo.getJenis();

                    if (jenis == 0 || jenis == 1 || jenis == 7 || jenis == 8 || jenis == 9){
                        saldoDebet += dataSaldo.getNominal();
                    }else {
                        saldoKredit += dataSaldo.getNominal();
                    }

                    webView.loadUrl("javascript:addRow('" + kodeAkun + "', '" + namaAkun + "', '" + nominal + "', '" + jenis +"');");

                }
                webView.loadUrl("javascript:addRowTotal('" + saldoDebet + "', '" + saldoKredit + "');");

            }
        });


    }
}
