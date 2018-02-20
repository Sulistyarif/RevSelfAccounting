package com.zakiadev.sulistyarif.revselfaccounting;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zakiadev.sulistyarif.revselfaccounting.data.DataJurnal;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataSaldo;
import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapterMix;
import com.zakiadev.sulistyarif.revselfaccounting.tablehelper.TableHelperDataAkun;
import com.zakiadev.sulistyarif.revselfaccounting.tablehelper.TableHelperDataSaldo;
import com.zakiadev.sulistyarif.revselfaccounting.tasting.JavaScriptInterface;
import com.zakiadev.sulistyarif.revselfaccounting.tasting.WebviewActivity;

import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * Created by sulistyarif on 13/02/18.
 */

public class LaporanNeracaSaldoActivity extends AppCompatActivity {

//    TableView<String[]> tableView;
//    TableHelperDataSaldo tableHelperDataSaldo;
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_neraca_saldo);

//        tableHelperDataSaldo = new TableHelperDataSaldo(this);
//        tableView = (TableView<String[]>)findViewById(R.id.tvNeracaSaldo);
//        tableView.setColumnCount(4);
//        tableView.setHeaderBackgroundColor(Color.parseColor("#3498db"));
//        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, tableHelperDataSaldo.getColHeader()));
//        tableView.setDataAdapter(new SimpleTableDataAdapter(this, tableHelperDataSaldo.getDataSaldo()));
//
//        TableColumnDpWidthModel tableColumnWeightModel = new TableColumnDpWidthModel(this, 4, 200);
//        tableView.setColumnModel(tableColumnWeightModel);

        webView = (WebView)findViewById(R.id.wvNeracaSaldo);
        webView.loadUrl("file:///android_asset/neraca_saldo.html");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setBuiltInZoomControls(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                ArrayList<DataSaldo> dataSaldos = new DBAdapterMix(LaporanNeracaSaldoActivity.this).selectRiwayat();
                DataSaldo dataSaldo;

                for (int i = 0; i< dataSaldos.size(); i++){
                    dataSaldo = dataSaldos.get(i);

                    String kodeAkun = dataSaldo.getKodeAkun();
                    String namaAkun = dataSaldo.getNamaAkun();
                    String nominal = String.valueOf(dataSaldo.getNominal());
                    int jenis = dataSaldo.getJenis();

                    webView.loadUrl("javascript:addRow('" + kodeAkun + "', '" + namaAkun + "', '" + nominal + "', '" + jenis +"');");

                }
            }
        });


    }
}
