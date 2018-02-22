package com.zakiadev.sulistyarif.revselfaccounting;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zakiadev.sulistyarif.revselfaccounting.data.DataAkun;
import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapterMix;
import com.zakiadev.sulistyarif.revselfaccounting.tablehelper.TableHelperDataAkun;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * Created by Sulistyarif on 04/02/2018.
 */

public class DaftarAkunActivity extends AppCompatActivity {

    TableView<String[]> tableView;
    FloatingActionButton fab;
    TableHelperDataAkun tableHelperDataAkun;
    EditText etKodeAkun, etNamaAkun;
    TextView tvJudul;
    Button btAddData;
    int jenisAkun;
    String judulAkun;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jurnal_kecil_activity);

        jenisAkun = getIntent().getIntExtra("jenisAkun", 99);
        judulAkun = getIntent().getStringExtra("judulAkun");

        fab = (FloatingActionButton)findViewById(R.id.btnAddJurnal);


        tvJudul = (TextView)findViewById(R.id.tvJudul);
        tvJudul.setText(judulAkun);

        tableHelperDataAkun = new TableHelperDataAkun(this);
        tableView = (TableView<String[]>)findViewById(R.id.tvKecJurnal);
        tableView.setColumnCount(2);
        tableView.setHeaderBackgroundColor(Color.parseColor("#3498db"));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, tableHelperDataAkun.getColHeader()));
        if (jenisAkun == 11){
            tableView.setDataAdapter(new SimpleTableDataAdapter(this, tableHelperDataAkun.getDataAll()));
            fab.setVisibility(View.INVISIBLE);
        }else {
            tableView.setDataAdapter(new SimpleTableDataAdapter(this, tableHelperDataAkun.getDataAkun(jenisAkun)));
            fab.setVisibility(View.VISIBLE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(DaftarAkunActivity.this);
                dialog.setTitle("Tambah Data Akun");
                dialog.setContentView(R.layout.card_tambah_data);

                etKodeAkun = (EditText)dialog.findViewById(R.id.etKodeAkun);
                etNamaAkun = (EditText)dialog.findViewById(R.id.etNamaAkun);

                btAddData = (Button)dialog.findViewById(R.id.btAddAkun);

                btAddData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String kodeBaru = etKodeAkun.getText().toString();
                        String namaBaru = etNamaAkun.getText().toString();

                        DataAkun dataAkun = new DataAkun();
                        dataAkun.setKodeAkun(kodeBaru);
                        dataAkun.setNamaAkun(namaBaru);

                        new DBAdapterMix(DaftarAkunActivity.this).insertAkun(dataAkun,jenisAkun);

                        tableView.setDataAdapter(new SimpleTableDataAdapter(DaftarAkunActivity.this, tableHelperDataAkun.getDataAkun(jenisAkun)));

                    }
                });
                dialog.show();


            }
        });

    }
}
