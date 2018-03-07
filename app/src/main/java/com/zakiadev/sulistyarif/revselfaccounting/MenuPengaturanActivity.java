package com.zakiadev.sulistyarif.revselfaccounting;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * Created by Sulistyarif on 31/01/2018.
 */

public class MenuPengaturanActivity extends AppCompatActivity{

    ListView lvMenuPengaturan;
    String[] menu = {"Pengaturan Perusahaan", "Pengaturan Kode Akun", "Backup Data", "Restore Data", "Hapus Semua Data"};

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
                    case 0:{
                        Intent intent = new Intent(MenuPengaturanActivity.this, SettingDataPerusahaan.class);
                        startActivity(intent);
                        break;
                    }
                    case 1:{
//                        pindah ke aktiviti pengaturan kode akun
                        Intent intent = new Intent(MenuPengaturanActivity.this, DaftarJenisAkun.class);
                        startActivity(intent);
                        break;
                    }
                    case 2:{
//                        melakukan backup
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i){
                                    case DialogInterface.BUTTON_POSITIVE:{
                                        dialogInterface.dismiss();
                                        break;

                                    }
                                    case DialogInterface.BUTTON_NEGATIVE:{
                                        exportDB("revaccounting");
                                        break;
                                    }
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(MenuPengaturanActivity.this);
                        builder.setMessage("Apakah anda yakin akan melakukan backup data ?").setPositiveButton("Tidak",dialogClickListener).setNegativeButton("Ya", dialogClickListener).show();
                        break;
                    }
                    case 3:{
//                        melakukan restore
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i){
                                    case DialogInterface.BUTTON_POSITIVE:{
                                        dialogInterface.dismiss();
                                        break;

                                    }
                                    case DialogInterface.BUTTON_NEGATIVE:{
                                        importDB("revaccounting");
                                        break;
                                    }
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(MenuPengaturanActivity.this);
                        builder.setMessage("Apakah anda yakin akan melakukan restore data ?").setPositiveButton("Tidak",dialogClickListener).setNegativeButton("Ya", dialogClickListener).show();
                        break;
                    }
                    case 4:{
//                        menghapus semua data yang ada
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i){
                                    case DialogInterface.BUTTON_POSITIVE:{
                                        dialogInterface.dismiss();
                                        break;
                                    }
                                    case DialogInterface.BUTTON_NEGATIVE:{
                                        Intent intent = new Intent(MenuPengaturanActivity.this, ClearDataActivity.class);
                                        startActivity(intent);
                                        break;
                                    }
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(MenuPengaturanActivity.this);
                        builder.setMessage("Apakah anda yakin akan menghapus semua data yang ada ?").setPositiveButton("Tidak",dialogClickListener).setNegativeButton("Ya", dialogClickListener).show();
                        break;
                    }

                }

            }
        });

    }

    private void importDB(String db_name) {
        File sd = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +
                File.separator + "backupAccounting"+
                File.separator );
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String backupDBPath = "/data/"+ MenuPengaturanActivity.this.getPackageName() +"/databases/"+db_name;
        String currentDBPath = db_name;
        File currentDB = new File(sd, currentDBPath);
        File backupDB = new File(data, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void exportDB(String db_name){

        File sd = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +
                File.separator + "backupAccounting"+
                File.separator );

        boolean success = true;
        if (!sd.exists()) {
            success = sd.mkdir();
        }
        if (success) {

            File data = Environment.getDataDirectory();
            FileChannel source=null;
            FileChannel destination=null;
            String currentDBPath = "/data/"+ MenuPengaturanActivity.this.getPackageName() +"/databases/"+db_name;
            String backupDBPath = db_name;
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(sd, backupDBPath);
            try {
                source = new FileInputStream(currentDB).getChannel();
                destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
                Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

}