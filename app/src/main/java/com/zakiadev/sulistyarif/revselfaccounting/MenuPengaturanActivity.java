package com.zakiadev.sulistyarif.revselfaccounting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Sulistyarif on 31/01/2018.
 */

public class MenuPengaturanActivity extends AppCompatActivity{

    ListView lvMenuPengaturan;
    String[] menu = {"Pengaturan Perusahaan", "Pengaturan Modal Awal",
    "Pengaturan Kode Akun", "Hapus Semua Data", "Backup Data"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pengaturan_activity);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.simple_listview, R.id.label, menu);

        lvMenuPengaturan = (ListView)findViewById(R.id.lvPengaturan);
        lvMenuPengaturan.setAdapter(arrayAdapter);

        lvMenuPengaturan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MenuPengaturanActivity.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                Log.i("YangDiklik", "yang dipilih adalah : " + parent.getItemAtPosition(position).toString());
                switch (position){
                    case 2:{
//                        pindah ke aktiviti pengaturan kode akun
                        Intent intent = new Intent(MenuPengaturanActivity.this, DaftarJenisAkun.class);
                        startActivity(intent);
                        break;
                    }

                }

            }
        });

    }
}
