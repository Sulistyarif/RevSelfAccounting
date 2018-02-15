package com.zakiadev.sulistyarif.revselfaccounting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.zakiadev.sulistyarif.revselfaccounting.tasting.WebviewActivity;

/**
 * Created by Sulistyarif on 31/01/2018.
 */

public class MenuLaporanAcvtivity extends AppCompatActivity {

    ListView lvMenuLaporan;
    String[] menu = {"Jurnal", "Neraca Saldo", "Laba Rugi", "Perubahan Ekuitas", "Neraca", "Arus Kas"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pengaturan_activity);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.simple_listview, R.id.label, menu);

        lvMenuLaporan = (ListView)findViewById(R.id.lvPengaturan);
        lvMenuLaporan.setAdapter(arrayAdapter);

        lvMenuLaporan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:{
//                        setting pengaturan perusahaan
                        Intent i = new Intent(MenuLaporanAcvtivity.this, JurnalActivity.class);
                        startActivity(i);
                        break;
                    }
                    case 1:{
//                        neraca saldo
                        Intent i = new Intent(MenuLaporanAcvtivity.this, LaporanNeracaSaldoActivity.class);
                        startActivity(i);
                        break;
                    }
                    case 2:{
//                        Laba Rugi
                        break;
                    }
                    case 3:{
//                        Perubahan Ekuitas
                        break;
                    }
                    case 4:{
//                        Neraca
                        break;
                    }
                    case 5:{
//                        Arus Kas
                        Intent i = new Intent(MenuLaporanAcvtivity.this, WebviewActivity.class);
                        startActivity(i);
                        break;
                    }
                }
            }
        });


    }
}
