package com.zakiadev.sulistyarif.revselfaccounting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zakiadev.sulistyarif.revselfaccounting.tablehelper.TableHelperDataAkun;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * Created by Sulistyarif on 06/02/2018.
 */

public class PilihDebetActivity extends AppCompatActivity {

    FloatingActionButton fab;
    TableView<String[]> tableView;
    TableHelperDataAkun tableHelperDataAkun;
    int pilihanTrans;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jurnal_kecil_activity);

        fab = (FloatingActionButton)findViewById(R.id.btnAddJurnal);
        fab.setVisibility(View.GONE);

        pilihanTrans = getIntent().getIntExtra("pilihan", 99);

        tableHelperDataAkun = new TableHelperDataAkun(this);
        tableView = (TableView<String[]>)findViewById(R.id.tvKecJurnal);
        tableView.setColumnCount(2);
        tableView.setHeaderBackgroundColor(Color.parseColor("#3498db"));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, tableHelperDataAkun.getColHeader()));

        switch (pilihanTrans){
            case 1:{
                tableView.setDataAdapter(new SimpleTableDataAdapter(this, tableHelperDataAkun.getDataPil(0,1)));
                break;
            }
            case 2:{
                tableView.setDataAdapter(new SimpleTableDataAdapter(this, tableHelperDataAkun.getDataPil(0,1)));
                break;
            }
            case 3:{
                tableView.setDataAdapter(new SimpleTableDataAdapter(this, tableHelperDataAkun.getDataPil(0,5,6)));
                break;
            }
            case 4:{
                tableView.setDataAdapter(new SimpleTableDataAdapter(this, tableHelperDataAkun.getDataPil(0,2,3)));
                break;
            }
            case 5:{
                tableView.setDataAdapter(new SimpleTableDataAdapter(this, tableHelperDataAkun.getDataPil(7,8)));
                break;
            }
            case 6:{
                tableView.setDataAdapter(new SimpleTableDataAdapter(this, tableHelperDataAkun.getDataPil(4)));
                break;
            }
            case 7:{
                tableView.setDataAdapter(new SimpleTableDataAdapter(this, tableHelperDataAkun.getDataPil(0,1 ,2,3,7,8)));
                break;
            }
            case 8:{
                tableView.setDataAdapter(new SimpleTableDataAdapter(this, tableHelperDataAkun.getDataPil(0,2,7,8)));
                break;
            }
        }

//        ketika dipilih itemnya, lalu akan kembali ke activity sebelumnya dengan melakukan passing data akun yang dipilih
        tableView.addDataClickListener(new TableDataClickListener<String[]>() {
            @Override
            public void onDataClicked(int rowIndex, String[] clickedData) {
                Intent intent = new Intent();
                intent.putExtra("kodeDebet", clickedData[0]);
                intent.putExtra("namaDebet", clickedData[1]);
                intent.putExtra("jenisDebet", clickedData[2]);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}