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

import com.zakiadev.sulistyarif.revselfaccounting.data.DataPerusahaan;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataSaldo;
import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapterMix;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sulistyarif on 19/02/18.
 */

public class LaporanLabaRugi extends AppCompatActivity {

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
        setContentView(R.layout.laporan_laba_rugi_activity);

        fabPrint = (FloatingActionButton)findViewById(R.id.fabLabaRugi);

        webView = (WebView)findViewById(R.id.wvLabaRugi);

        spBulan = (Spinner)findViewById(R.id.spNeracaSaldobulan);
        spTahun = (Spinner)findViewById(R.id.spNeracaSaldoTahun);

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
                webView.loadUrl("file:///android_asset/laba_rugi.html");
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
                webView.loadUrl("file:///android_asset/laba_rugi.html");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//        setting webview
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

//                setting header
                DataPerusahaan dataPerusahaan = new DBAdapterMix(LaporanLabaRugi.this).selectDataPerusahaan();
                webView.loadUrl("javascript:setNamaPres('" + dataPerusahaan.getNamaPers() + "');");
                webView.loadUrl("javascript:setPeriode('" + strBulan + "','" + strTahun + "');");

//                pengambilan data untuk pendapatan
                ArrayList<DataSaldo> dataSaldos = new DBAdapterMix(LaporanLabaRugi.this).selectRiwayatJenisBlnThnMar(5, bulanDipilih, tahunDipilih);
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
                    webView.loadUrl("javascript:separator('" + "Total Pendapatan Operasional" + "', '" + totalPendapatan + "');");

//                pengambilan data untuk beban biaya operasional

                ArrayList<DataSaldo> dataSaldos1 = new DBAdapterMix(LaporanLabaRugi.this).selectRiwayatJenisBlnThnMar(7, bulanDipilih, tahunDipilih);
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

                webView.loadUrl("javascript:separator('" + "Total Biaya Operasional" + "', '" + totalBeban + "');");

//                pengambilan data untuk pendapatan luar usaha
                ArrayList<DataSaldo> dataSaldos2 = new DBAdapterMix(LaporanLabaRugi.this).selectRiwayatJenisBlnThnMar(6, bulanDipilih, tahunDipilih);
                DataSaldo dataSaldo2;

                int totalPendapatanNonOP = 0;

                for (int i = 0; i< dataSaldos2.size(); i++){
                    dataSaldo2 = dataSaldos2.get(i);

                    String kodeAkun = dataSaldo2.getKodeAkun();
                    String namaAkun = dataSaldo2.getNamaAkun();
                    String nominal = String.valueOf(dataSaldo2.getNominal());

                    totalPendapatanNonOP += dataSaldo2.getNominal();

                    webView.loadUrl("javascript:tambahData('" + kodeAkun + "', '" + namaAkun + "', '" + nominal + "');");

                }

                webView.loadUrl("javascript:separator('" + "Total Pendapatan Non Operasional" + "', '" + totalPendapatanNonOP + "');");

//                pengambilan data untuk biaya luar usaha
                ArrayList<DataSaldo> dataSaldos3 = new DBAdapterMix(LaporanLabaRugi.this).selectRiwayatJenisBlnThnMar(8, bulanDipilih, tahunDipilih);
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

                webView.loadUrl("javascript:separator('" + "Total Biaya Non Operasional" + "', '" + totalBebanNonOp + "');");


//                menghitung total laba
                labaBersih = totalPendapatan + totalPendapatanNonOP - totalBeban - totalBebanNonOp;

                webView.loadUrl("javascript:separator('" + "Laba Bersih" + "', '" + labaBersih + "');");

                fabPrint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                            createWebPrintJob(webView);
                        } else {
                            Toast.makeText(LaporanLabaRugi.this, "Versi Android Anda Tidak Mendukung Export PDF",Toast.LENGTH_SHORT).show();
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
