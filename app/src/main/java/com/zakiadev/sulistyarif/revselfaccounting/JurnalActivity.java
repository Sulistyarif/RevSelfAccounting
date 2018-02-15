package com.zakiadev.sulistyarif.revselfaccounting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zakiadev.sulistyarif.revselfaccounting.tablehelper.TableHelperDataJurnal;
import com.zakiadev.sulistyarif.revselfaccounting.tablehelper.TableHelperDataJurnalKec;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * Created by Sulistyarif on 02/02/2018.
 */

public class JurnalActivity extends AppCompatActivity {
    TableView<String[]> tableView;
    TableHelperDataJurnal tableHelperDataJurnal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jurnal_activity);

        tableHelperDataJurnal = new TableHelperDataJurnal(this);
        tableView = (TableView<String[]>)findViewById(R.id.tvJurnal);
        tableView.setColumnCount(6);
        tableView.setHeaderBackgroundColor(Color.parseColor("#3498db"));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this,tableHelperDataJurnal.getColHeader()));
        tableView.setDataAdapter(new SimpleTableDataAdapter(this, tableHelperDataJurnal.getDataJurnal2()));

        TableColumnDpWidthModel tableColumnWeightModel = new TableColumnDpWidthModel(this, 6, 200);
        tableView.setColumnModel(tableColumnWeightModel);


    }
}
