package com.zakiadev.sulistyarif.revselfaccounting.tablehelper;

import android.content.Context;
import android.util.Log;

import com.zakiadev.sulistyarif.revselfaccounting.data.DataAkun;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataJurnal;
import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapter;
import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapterMix;

import java.util.ArrayList;

/**
 * Created by Sulistyarif on 02/02/2018.
 */

public class TableHelperDataJurnalKec {
    Context context;

    private String[] colHeader = {"Tanggal", "Keterangan"};
    private String[][] dataJurnal;

    public TableHelperDataJurnalKec(Context context) {
        this.context = context;
    }

    public String[] getColHeader() {
        return colHeader;
    }

    public String[][] getDataJurnal() {

        ArrayList<DataJurnal> dataJurnals = new DBAdapter(context).ambilData();
        DataJurnal dataJurnal;

        this.dataJurnal = new String[dataJurnals.size()][2];
        for (int i = 0; i< dataJurnals.size(); i++){
            dataJurnal = dataJurnals.get(i);

            this.dataJurnal[i][0] = String.valueOf(dataJurnal.getTgl());
            this.dataJurnal[i][1] = dataJurnal.getKeterangan();

        }

        return this.dataJurnal;
    }

    public String[][] getDataJurnal2() {

        ArrayList<DataJurnal> dataJurnals = new DBAdapterMix(context).selectJurnal();
        DataJurnal dataJurnal;

        this.dataJurnal = new String[dataJurnals.size()][2];
        for (int i = 0; i< dataJurnals.size(); i++){
            dataJurnal = dataJurnals.get(i);

            this.dataJurnal[i][0] = dataJurnal.getTgl();
            String[] pisahTgl = dataJurnal.getTgl().split("/");
            Log.i("returnDate",i + "Format pisah,tanggal :" + pisahTgl[0] + ", bulan : " + pisahTgl[1] + ", tahun : " + pisahTgl[2]);
            this.dataJurnal[i][1] = dataJurnal.getKeterangan();

        }

        return this.dataJurnal;
    }

}
