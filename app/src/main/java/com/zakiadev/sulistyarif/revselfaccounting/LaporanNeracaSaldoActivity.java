package com.zakiadev.sulistyarif.revselfaccounting;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zakiadev.sulistyarif.revselfaccounting.data.DataSaldo;
import com.zakiadev.sulistyarif.revselfaccounting.tablehelper.TableHelperDataAkun;
import com.zakiadev.sulistyarif.revselfaccounting.tablehelper.TableHelperDataSaldo;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * Created by sulistyarif on 13/02/18.
 */

public class LaporanNeracaSaldoActivity extends AppCompatActivity {

    TableView<String[]> tableView;
    TableHelperDataSaldo tableHelperDataSaldo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_neraca_saldo);

        tableHelperDataSaldo = new TableHelperDataSaldo(this);
        tableView = (TableView<String[]>)findViewById(R.id.tvNeracaSaldo);
        tableView.setColumnCount(4);
        tableView.setHeaderBackgroundColor(Color.parseColor("#3498db"));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, tableHelperDataSaldo.getColHeader()));
        tableView.setDataAdapter(new SimpleTableDataAdapter(this, tableHelperDataSaldo.getDataSaldo()));

        TableColumnDpWidthModel tableColumnWeightModel = new TableColumnDpWidthModel(this, 4, 200);
        tableView.setColumnModel(tableColumnWeightModel);

    }
}
