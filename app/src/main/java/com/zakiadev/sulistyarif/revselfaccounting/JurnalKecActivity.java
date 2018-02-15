package com.zakiadev.sulistyarif.revselfaccounting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.zakiadev.sulistyarif.revselfaccounting.tablehelper.TableHelperDataJurnalKec;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.listeners.TableHeaderClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * Created by Sulistyarif on 02/02/2018.
 */

public class JurnalKecActivity extends AppCompatActivity {

    FloatingActionButton fab;
    TableView<String[]> tableView;
    TableHelperDataJurnalKec tableHelperDataJurnalKec;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jurnal_kecil_activity);

        tableHelperDataJurnalKec = new TableHelperDataJurnalKec(this);
        tableView = (TableView<String[]>)findViewById(R.id.tvKecJurnal);
        tableView.setColumnCount(2);
        tableView.setHeaderBackgroundColor(Color.parseColor("#3498db"));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this,tableHelperDataJurnalKec.getColHeader()));
        tableView.setDataAdapter(new SimpleTableDataAdapter(this, tableHelperDataJurnalKec.getDataJurnal2()));

        fab = (FloatingActionButton)findViewById(R.id.btnAddJurnal);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JurnalKecActivity.this, TambahJurnalActivity.class);
                startActivity(intent);
            }
        });

        tableView.addDataClickListener(new TableDataClickListener<String[]>() {
            @Override
            public void onDataClicked(int rowIndex, String[] clickedData) {
                Intent intent = new Intent(JurnalKecActivity.this, JurnalActivity.class);
                startActivity(intent);
            }
        });

        tableView.addHeaderClickListener(new TableHeaderClickListener() {
            @Override
            public void onHeaderClicked(int columnIndex) {
                Intent intent = new Intent(JurnalKecActivity.this, JurnalActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        tableView.setDataAdapter(new SimpleTableDataAdapter(this, tableHelperDataJurnalKec.getDataJurnal2()));
    }
}
