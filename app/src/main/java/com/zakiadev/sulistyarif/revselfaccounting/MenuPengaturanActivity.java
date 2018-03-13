package com.zakiadev.sulistyarif.revselfaccounting;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * Created by Sulistyarif on 31/01/2018.
 */

public class MenuPengaturanActivity extends AppCompatActivity{

    ListView lvMenuPengaturan;
    String[] menu = {"Pengaturan Perusahaan", "Pengaturan Kode Akun", "Backup Data", "Restore Data", "Hapus Semua Data"};

    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pengaturan_activity);

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);

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
                        cekPermission("backup");
                        break;
                    }
                    case 3:{
//                        melakukan restore
                        cekPermission("restore");
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

    private void cekPermission(final String menu) {
        Dexter.withActivity(MenuPengaturanActivity.this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()){
                            if (menu.equals("restore")){
                                dialogRestore();
                            }else if (menu.equals("backup")){
                                dialogbackUp();

                            }
                        }
                        
                        if (report.isAnyPermissionPermanentlyDenied()){
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                Toast.makeText(getApplicationContext(), "Error dalam meminta izin!", Toast.LENGTH_LONG).show();
            }
        }).onSameThread().check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuPengaturanActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void dialogRestore() {
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
    }

    private void dialogbackUp() {
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
            Toast.makeText(this, "Berhasil melakukan Restore", Toast.LENGTH_SHORT).show();
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
        }else {
            Toast.makeText(MenuPengaturanActivity.this, "Terjadi kesalahan saat mengakses penyimpanan", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Berhasil Melakukan Backup", Toast.LENGTH_SHORT).show();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(MenuPengaturanActivity.this, "Terjadi Kesalahan saat melakukan penyimpanan", Toast.LENGTH_SHORT).show();
        }
    }

}