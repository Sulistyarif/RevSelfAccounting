package com.zakiadev.sulistyarif.revselfaccounting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.zakiadev.sulistyarif.revselfaccounting.tablehelper.TableHelperDataAkun;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * Created by Sulistyarif on 06/02/2018.
 */

public class PilihAkunNeracaAwal extends AppCompatActivity {

    FloatingActionButton fab;
    TableView<String[]> tableView;
    TableHelperDataAkun tableHelperDataAkun;
    int pilihanTrans, indexSumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jurnal_kecil_activity);

        fab = (FloatingActionButton)findViewById(R.id.btnAddJurnal);
        fab.setVisibility(View.GONE);

        pilihanTrans = getIntent().getIntExtra("pilihan", 99);
        indexSumber = getIntent().getIntExtra("sumber",0);
        Log.i("indexDebt", String.valueOf(indexSumber));

        tableHelperDataAkun = new TableHelperDataAkun(this);
        tableView = (TableView<String[]>)findViewById(R.id.tvKecJurnal);
        tableView.setColumnCount(2);
        tableView.setHeaderBackgroundColor(Color.parseColor("#3498db"));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, tableHelperDataAkun.getColHeader()));
        tableView.setDataAdapter(new SimpleTableDataAdapter(this, tableHelperDataAkun.getDataAll()));

//        TableColumnDpWidthModel tableColumnWeightModel = new TableColumnDpWidthModel(PilihAkunNeracaAwal.this, 2, 100);
//        tableView.setColumnModel(tableColumnWeightModel);

        TableColumnWeightModel columnModel = new TableColumnWeightModel(4);
        columnModel.setColumnWeight(1,1);
        columnModel.setColumnWeight(2,3);
        tableView.setColumnModel(columnModel);

//        ketika dipilih itemnya, lalu akan kembali ke activity sebelumnya dengan melakukan passing data akun yang dipilih
        tableView.addDataClickListener(new TableDataClickListener<String[]>() {
            @Override
            public void onDataClicked(int rowIndex, String[] clickedData) {
                Intent intent = new Intent();
                intent.putExtra("index", indexSumber);
                intent.putExtra("kodeDebet", clickedData[0]);
                intent.putExtra("namaDebet", clickedData[1]);
                intent.putExtra("jenisDebet", clickedData[2]);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}