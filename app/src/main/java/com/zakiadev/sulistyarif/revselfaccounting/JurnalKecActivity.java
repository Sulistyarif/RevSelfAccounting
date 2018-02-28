package com.zakiadev.sulistyarif.revselfaccounting;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapterMix;
import com.zakiadev.sulistyarif.revselfaccounting.tablehelper.TableHelperDataJurnalKec;
import com.zakiadev.sulistyarif.revselfaccounting.tasting.WebviewActivity;

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
                Intent intent = new Intent(JurnalKecActivity.this, TambahDataJurnalActivity.class);
                startActivity(intent);
            }
        });

        tableView.addDataClickListener(new TableDataClickListener<String[]>() {
            @Override
            public void onDataClicked(int rowIndex, String[] clickedData) {

//                mendapatkan id dari data tersebut yang berguna untuk menghapus data
//                Toast.makeText(JurnalKecActivity.this, " " + clickedData[2].toString(),Toast.LENGTH_SHORT).show();
                final String id = clickedData[2].toString();
                final String tglTrans = clickedData[0].toString();
                final String ketTrans = clickedData[1].toString();
                final String namaDebet = clickedData[3].toString();
                final String namakredit = clickedData[4].toString();
                final String nominal = clickedData[5].toString();


                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case DialogInterface.BUTTON_POSITIVE:{
                                new DBAdapterMix(JurnalKecActivity.this).deleteJurnal(id);
                                int debetID = (Integer.parseInt(id)*2) - 1;
                                int kreditID = Integer.parseInt(id)*2;
                                new DBAdapterMix(JurnalKecActivity.this).deleteRiwayat(debetID);
                                new DBAdapterMix(JurnalKecActivity.this).deleteRiwayat(kreditID);
                                tableView.setDataAdapter(new SimpleTableDataAdapter(JurnalKecActivity.this, tableHelperDataJurnalKec.getDataJurnal2()));
                                break;
                            }
                            case DialogInterface.BUTTON_NEGATIVE:{
                                Intent intent = new Intent(getBaseContext(), EditDataJurnalActivity.class);
                                intent.putExtra("id",id);
                                intent.putExtra("tgl",tglTrans);
                                intent.putExtra("ket", ketTrans);
                                intent.putExtra("debet",namaDebet);
                                intent.putExtra("kredit",namakredit);
                                intent.putExtra("nominal",nominal);
                                startActivity(intent);
                                break;
                            }
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(JurnalKecActivity.this);
                builder.setMessage("Transaksi " + ketTrans + " pada tanggal " + tglTrans).setPositiveButton("Hapus",dialogClickListener).setNegativeButton("Edit",dialogClickListener).show();

//                Intent intent = new Intent(JurnalKecActivity.this, JurnalActivity.class);
//                startActivity(intent);
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
