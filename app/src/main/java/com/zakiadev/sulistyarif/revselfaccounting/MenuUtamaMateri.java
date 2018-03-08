package com.zakiadev.sulistyarif.revselfaccounting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by sulistyarif on 08/03/18.
 */

public class MenuUtamaMateri extends AppCompatActivity {

    ListView lvMenuMateri;
    String[] menu = {"Pengenalan Akutansi", "Macam-Macam dan Manfaat Laporan Keuangan", "Elemen dan Akun-Akun dalam Laporan Keuangan", "Kode Akun", "Pencatatan Transaksi Kedalam Jurnal", "Penyesuaian"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pengaturan_activity);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.simple_listview, R.id.label, menu);

        lvMenuMateri = (ListView)findViewById(R.id.lvPengaturan);
        lvMenuMateri.setAdapter(arrayAdapter);

        lvMenuMateri.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MenuUtamaMateri.this, MateriWebView1.class);
                intent.putExtra("pilihanMenu",i);
                startActivity(intent);
            }
        });
    }
}
