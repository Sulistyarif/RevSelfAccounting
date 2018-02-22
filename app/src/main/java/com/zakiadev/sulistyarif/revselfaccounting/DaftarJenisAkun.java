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
 * Created by Sulistyarif on 05/02/2018.
 */

public class DaftarJenisAkun extends AppCompatActivity {

    ListView lv;
    String[] menu = {"Asset Lancar", "Asset Tetap", "Utang Jangka Pendek", "Utang Jangka Panjang", "Modal"
            , "Pendapatan Operasional", "Pendapatan Non Operasional", "Biaya Biaya Operasional", "Biaya Biaya Non Operasional", "Pengembalian Ekuitas", "Akun Lain Lain", "Lihat Semua Akun"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pengaturan_activity);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.simple_listview,R.id.label, menu);

        lv = (ListView)findViewById(R.id.lvPengaturan);
        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(DaftarJenisAkun.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DaftarJenisAkun.this, DaftarAkunActivity.class);
                intent.putExtra("jenisAkun",position);
                intent.putExtra("judulAkun",parent.getItemAtPosition(position).toString());
                startActivity(intent);
            }
        });

    }
}
