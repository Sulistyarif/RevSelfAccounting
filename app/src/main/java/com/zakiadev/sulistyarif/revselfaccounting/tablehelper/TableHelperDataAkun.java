package com.zakiadev.sulistyarif.revselfaccounting.tablehelper;

import android.content.Context;

import com.zakiadev.sulistyarif.revselfaccounting.data.DataAkun;
import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapterMix;

import java.util.ArrayList;

/**
 * Created by Sulistyarif on 05/02/2018.
 */

public class TableHelperDataAkun {
    Context context;

    public String[] colHeader = {"Kode Akun", "Nama Akun"};
    private String[][] dataAkun;

    public TableHelperDataAkun(Context context) {
        this.context = context;
    }

    public String[] getColHeader() {
        return colHeader;
    }

    public String[][] getDataAkun(int jenisAkun) {

        ArrayList<DataAkun> dataAkuns = new DBAdapterMix(context).selectAkun(jenisAkun);
        DataAkun dataAkun;

        this.dataAkun = new String[dataAkuns.size()][3];
        for (int j = 0; j < dataAkuns.size(); j++){
            dataAkun = dataAkuns.get(j);

            this.dataAkun[j][0] = dataAkun.getKodeAkun();
            this.dataAkun[j][1] = dataAkun.getNamaAkun();
            this.dataAkun[j][2] = dataAkun.getJenis();

        }

        return this.dataAkun;
    }

    public String[][] getDataAssetLancar(){
        ArrayList<DataAkun> dataAkuns = new DBAdapterMix(context).selectAssetLancar();
        DataAkun dataAkun;

        this.dataAkun = new String[dataAkuns.size()][3];
        for (int j = 0; j < dataAkuns.size(); j++){
            dataAkun = dataAkuns.get(j);

            this.dataAkun[j][0] = dataAkun.getKodeAkun();
            this.dataAkun[j][1] = dataAkun.getNamaAkun();
            this.dataAkun[j][2] = dataAkun.getJenis();

        }

        return this.dataAkun;
    }

    public String[][] getDataAll() {
        ArrayList<DataAkun> dataAkuns = new DBAdapterMix(context).selectAkunAll();
        DataAkun dataAkun;

        this.dataAkun = new String[dataAkuns.size()][3];
        for (int j = 0; j < dataAkuns.size(); j++){
            dataAkun = dataAkuns.get(j);

            this.dataAkun[j][0] = dataAkun.getKodeAkun();
            this.dataAkun[j][1] = dataAkun.getNamaAkun();
            this.dataAkun[j][2] = dataAkun.getJenis();

        }

        return this.dataAkun;
    }

    public String[][] getDataPil(int i, int i1, int i2, int i3) {
        ArrayList<DataAkun> dataAkuns = new DBAdapterMix(context).selectAkun(i, i1, i2, i3);
        DataAkun dataAkun;

//        input 1
        this.dataAkun = new String[dataAkuns.size()][3];
        for (int j = 0; j < dataAkuns.size(); j++){
            dataAkun = dataAkuns.get(j);

            this.dataAkun[j][0] = dataAkun.getKodeAkun();
            this.dataAkun[j][1] = dataAkun.getNamaAkun();
            this.dataAkun[j][2] = dataAkun.getJenis();

        }

        return this.dataAkun;
    }

    public String[][] getDataPil(int i, int i1, int i2, int i3, int i4) {
        ArrayList<DataAkun> dataAkuns = new DBAdapterMix(context).selectAkun(i, i1, i2, i3, i4);
        DataAkun dataAkun;

//        input 1
        this.dataAkun = new String[dataAkuns.size()][3];
        for (int j = 0; j < dataAkuns.size(); j++){
            dataAkun = dataAkuns.get(j);

            this.dataAkun[j][0] = dataAkun.getKodeAkun();
            this.dataAkun[j][1] = dataAkun.getNamaAkun();
            this.dataAkun[j][2] = dataAkun.getJenis();

        }

        return this.dataAkun;
    }


    public String[][] getDataPil(int i, int i1) {
        ArrayList<DataAkun> dataAkuns = new DBAdapterMix(context).selectAkun(i, i1);
        DataAkun dataAkun;

//        input 1
        this.dataAkun = new String[dataAkuns.size()][3];
        for (int j = 0; j < dataAkuns.size(); j++){
            dataAkun = dataAkuns.get(j);

            this.dataAkun[j][0] = dataAkun.getKodeAkun();
            this.dataAkun[j][1] = dataAkun.getNamaAkun();
            this.dataAkun[j][2] = dataAkun.getJenis();

        }

        return this.dataAkun;
    }

    public String[][] getDataPil(int i, int i1, int i2, int i3, int i4, int i5, int i6) {
        ArrayList<DataAkun> dataAkuns = new DBAdapterMix(context).selectAkun(i, i1, i2, i3 ,i4, i5, i6);
        DataAkun dataAkun;

//        input 1
        this.dataAkun = new String[dataAkuns.size()][3];
        for (int j = 0; j < dataAkuns.size(); j++){
            dataAkun = dataAkuns.get(j);

            this.dataAkun[j][0] = dataAkun.getKodeAkun();
            this.dataAkun[j][1] = dataAkun.getNamaAkun();
            this.dataAkun[j][2] = dataAkun.getJenis();

        }

        return this.dataAkun;
    }

    public String[][] getDataPil(int i, int i1, int i2) {
        ArrayList<DataAkun> dataAkuns = new DBAdapterMix(context).selectAkun(i, i1, i2);
        DataAkun dataAkun;

//        input 1
        this.dataAkun = new String[dataAkuns.size()][3];
        for (int j = 0; j < dataAkuns.size(); j++){
            dataAkun = dataAkuns.get(j);

            this.dataAkun[j][0] = dataAkun.getKodeAkun();
            this.dataAkun[j][1] = dataAkun.getNamaAkun();
            this.dataAkun[j][2] = dataAkun.getNamaAkun();

        }

        return this.dataAkun;
    }

    public String[][] getDataPil(int i) {
        ArrayList<DataAkun> dataAkuns = new DBAdapterMix(context).selectAkun(i);
        DataAkun dataAkun;

        this.dataAkun = new String[dataAkuns.size()][3];
        for (int j = 0; j < dataAkuns.size(); j++){
            dataAkun = dataAkuns.get(j);

            this.dataAkun[j][0] = dataAkun.getKodeAkun();
            this.dataAkun[j][1] = dataAkun.getNamaAkun();
            this.dataAkun[j][2] = dataAkun.getJenis();

        }

        return this.dataAkun;
    }

    public String[][] getDataPil(int i, int i1, int i2, int i3, int i4, int i5) {
        ArrayList<DataAkun> dataAkuns = new DBAdapterMix(context).selectAkun(i,i1,i2,i3,i4,i5);
        DataAkun dataAkun;

        this.dataAkun = new String[dataAkuns.size()][3];
        for (int j = 0; j < dataAkuns.size(); j++){
            dataAkun = dataAkuns.get(j);

            this.dataAkun[j][0] = dataAkun.getKodeAkun();
            this.dataAkun[j][1] = dataAkun.getNamaAkun();
            this.dataAkun[j][2] = dataAkun.getJenis();

        }

        return this.dataAkun;
    }
}
