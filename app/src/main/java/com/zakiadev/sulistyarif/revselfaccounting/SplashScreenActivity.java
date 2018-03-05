package com.zakiadev.sulistyarif.revselfaccounting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.zakiadev.sulistyarif.revselfaccounting.data.DataAkun;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataSaldo;
import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapterMix;

import java.util.ArrayList;

/**
 * Created by Sulistyarif on 07/02/2018.
 */

public class SplashScreenActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sharedPreferences.getBoolean("firstTime", false)){
//            Toast.makeText(SplashScreenActivity.this, "Hanya akan muncul pertama kali install", Toast.LENGTH_LONG).show();
            Log.i("firstInstallation", "Hanya akan berjalan pada saat pertama kali install");
            masukanDataDefault();
//            nanti dipindah ke simpan data yang di simpan neraca awal
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean("firstTime", true);
//            editor.commit();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreenActivity.this, SettingAwalPerusahaanActivity.class));
                    finish();
                }
            }, 2000L);

        }else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreenActivity.this, MenuUtamaActivity.class));
                    finish();
                }
            }, 2000L);
        }

    }

    private void masukanDataDefault() {

        String[][] assetLancar = {
                {"1101", "Kas"},
                {"1102", "Kas di Bank"},
                {"1103", "Piutang Usaha"},
                {"1104", "Perlengkapan"},
                {"1105", "Alat Tulis Kantor"},
                {"1106", "Sewa Dibayar Dimuka"},
                {"1107", "Asuransi Dibayar Dimuka"},
                {"1108", "Iklan Dibayar Dimuka"}
        };

        String[][] assetTetap ={
                {"1201", "Tanah"},
                {"1202", "Bangunan"},
                {"1203", "Peralatan"},
                {"1204", "Kendaraan"},
                {"1205", "Mesin"}
        };

        String [][] utangJangkaPendek = {
                {"2101", "Utang Usaha"},
                {"2102", "Utang Gaji"},
                {"2103", "Utang Pajak"},
                {"2104", "Utang Bunga"},
                {"2105", "Utang Biaya"},
                {"2106", "Utang Wesel"},
                {"2107", "Pendapatan Diterima Dimuka"}
        };

        String[][] utangJangkaPanjang = {
                {"2201", "Utang Bank"},
                {"2202", "Utang Hipotek"}
        };

        String[][] modal = {
                {"3101", "Modal Pemilik"},
                {"3201", "Hibah"},
                {"3301", "Sumbangan"}
        };

        String[][] pendapatanUsaha = {
                {"4101", "Pendapatan Jasa"}
        };

        String[][] pendapatanNonOperasional = {
                {"4201","Pendapatan bunga"},
                {"4202", "Pendapatan Lain-Lain"}
        };

        String[][] biayaUsaha = {
                {"5101", "Biaya Gaji"},
                {"5102", "Biaya Perlengkapan"},
                {"5103", "Biaya Sewa"},
                {"5104", "Biaya Listrik"},
                {"5105", "Biaya Air"},
                {"5106", "Biaya Telepon"},
                {"5107", "Biaya Iklan"},
                {"5108", "Biaya Asuransi"},
                {"5109", "Biaya Pemeliharaan Peralatan"},
                {"5110", "Biaya Pemeliharaan Mesin"},
                {"5111", "Biaya Penyusutan Bangunan"},
                {"5112", "Biaya Penyusutan Peralatan"},
                {"5113", "Biaya Penyusutan Kendaraan"},
                {"5114", "Biaya Penyusutan Mesin"}
        };

        String[][] biayaLuarUsaha = {
                {"5201", "Biaya Bunga"},
                {"5202", "Biaya Administrasi Bank"},
                {"5203", "Biaya Pajak"},
                {"5204", "Kerugian Penjualan Aset"},
                {"5205", "Kerugian Piutang Tak Tertagih"}
        };

        String[][] pengembalianEkuitas = {
                {"6101", "Prive Pemilik"},
                {"6102", "Dividen"}
        };

        String[][] akunLainLain = {
                {"7101", "Akumulasi Penyusutan Bangunan"},
                {"7102", "Akumulasi Penyusutan Peralatan"},
                {"7103", "Akumulasi Penyusutan Kendaraan"},
                {"7104", "Akumulasi Penyusutan Mesin"},
                {"7105", "Cadangan Kerugian Piutang"}
        };

//        memasukkan data asset lancar
        for (int i = 0; i<assetLancar.length; i++){
            DataAkun dataAkun = new DataAkun();
            dataAkun.setKodeAkun(assetLancar[i][0]);
            dataAkun.setNamaAkun(assetLancar[i][1]);

            new DBAdapterMix(SplashScreenActivity.this).insertAkun(dataAkun,0);
        }

//        memasukkan data asset tetap
        for (int i = 0; i<assetTetap.length; i++){
            DataAkun dataAkun = new DataAkun();
            dataAkun.setKodeAkun(assetTetap[i][0]);
            dataAkun.setNamaAkun(assetTetap[i][1]);

            new DBAdapterMix(SplashScreenActivity.this).insertAkun(dataAkun,1);
        }

//        memasukkan data utang jangka pendek
        for (int i = 0; i<utangJangkaPendek.length; i++){
            DataAkun dataAkun = new DataAkun();
            dataAkun.setKodeAkun(utangJangkaPendek[i][0]);
            dataAkun.setNamaAkun(utangJangkaPendek[i][1]);

            new DBAdapterMix(SplashScreenActivity.this).insertAkun(dataAkun,2);
        }

//        memasukkan data utang jangka panjang
        for (int i = 0; i<utangJangkaPanjang.length; i++){
            DataAkun dataAkun = new DataAkun();
            dataAkun.setKodeAkun(utangJangkaPanjang[i][0]);
            dataAkun.setNamaAkun(utangJangkaPanjang[i][1]);

            new DBAdapterMix(SplashScreenActivity.this).insertAkun(dataAkun,3);
        }

//        memasukkan data modal utawa ekuitas
        for (int i = 0; i<modal.length; i++){
            DataAkun dataAkun = new DataAkun();
            dataAkun.setKodeAkun(modal[i][0]);
            dataAkun.setNamaAkun(modal[i][1]);

            new DBAdapterMix(SplashScreenActivity.this).insertAkun(dataAkun,4);
        }

//        memasukkan data pendapatan usaha
        for (int i = 0; i<pendapatanUsaha.length; i++){
            DataAkun dataAkun = new DataAkun();
            dataAkun.setKodeAkun(pendapatanUsaha[i][0]);
            dataAkun.setNamaAkun(pendapatanUsaha[i][1]);

            new DBAdapterMix(SplashScreenActivity.this).insertAkun(dataAkun,5);
        }

//        memasukkan data pendapatan luar usaha (update tgl 22-2-18)
        for (int i = 0; i<pendapatanNonOperasional.length; i++){
            DataAkun dataAkun = new DataAkun();
            dataAkun.setKodeAkun(pendapatanNonOperasional[i][0]);
            dataAkun.setNamaAkun(pendapatanNonOperasional[i][1]);

            new DBAdapterMix(SplashScreenActivity.this).insertAkun(dataAkun,6);
        }


//        memasukkan data biaya usaha
        for (int i = 0; i<biayaUsaha.length; i++){
            DataAkun dataAkun = new DataAkun();
            dataAkun.setKodeAkun(biayaUsaha[i][0]);
            dataAkun.setNamaAkun(biayaUsaha[i][1]);

            new DBAdapterMix(SplashScreenActivity.this).insertAkun(dataAkun,7);
        }

//        memasukkan data biaya luar usaha
        for (int i = 0; i<biayaLuarUsaha.length; i++){
            DataAkun dataAkun = new DataAkun();
            dataAkun.setKodeAkun(biayaLuarUsaha[i][0]);
            dataAkun.setNamaAkun(biayaLuarUsaha[i][1]);

            new DBAdapterMix(SplashScreenActivity.this).insertAkun(dataAkun,8);
        }

//        memasukkan data pengembalian ekuitas
        for (int i = 0; i<pengembalianEkuitas.length; i++){
            DataAkun dataAkun = new DataAkun();
            dataAkun.setKodeAkun(pengembalianEkuitas[i][0]);
            dataAkun.setNamaAkun(pengembalianEkuitas[i][1]);

            new DBAdapterMix(SplashScreenActivity.this).insertAkun(dataAkun,9);
        }

//        memasukkan data akun lain lain
        for (int i = 0; i<akunLainLain.length; i++){
            DataAkun dataAkun = new DataAkun();
            dataAkun.setKodeAkun(akunLainLain[i][0]);
            dataAkun.setNamaAkun(akunLainLain[i][1]);

            new DBAdapterMix(SplashScreenActivity.this).insertAkun(dataAkun,10);
        }

    }

}
