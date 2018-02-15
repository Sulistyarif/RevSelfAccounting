package com.zakiadev.sulistyarif.revselfaccounting.tablehelper;

import android.content.Context;

import com.zakiadev.sulistyarif.revselfaccounting.data.DataJurnal;
import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapter;
import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapterMix;

import java.util.ArrayList;

/**
 * Created by Sulistyarif on 02/02/2018.
 */

public class TableHelperDataJurnal {
    Context context;

    private String[] colHeader = {"Tanggal", "Keterangan", "Akun Debet", "Nominal Debet", "Akun Kredit", "Nominal Kredit"};
    private String[][] dataJurnal;

    public TableHelperDataJurnal(Context context) {
        this.context = context;
    }

    public String[] getColHeader() {
        return colHeader;
    }

    public String[][] getDataJurnal() {

        ArrayList<DataJurnal> dataJurnals = new DBAdapter(context).ambilData();
        DataJurnal dataJurnal;

        this.dataJurnal = new String[dataJurnals.size()][4];
        for (int i = 0; i< dataJurnals.size(); i++){
            dataJurnal = dataJurnals.get(i);

            this.dataJurnal[i][0] = String.valueOf(dataJurnal.getTgl());
            this.dataJurnal[i][1] = dataJurnal.getKeterangan();
//            this.dataJurnal[i][2] = String.valueOf(dataJurnal.get());
//            this.dataJurnal[i][3] = String.valueOf(dataJurnal.getKredit());

        }

        return this.dataJurnal;
    }

    public String[][] getDataJurnal2() {

        ArrayList<DataJurnal> dataJurnals = new DBAdapterMix(context).selectJurnal();
        DataJurnal dataJurnal;

        this.dataJurnal = new String[dataJurnals.size()][6];
        for (int i = 0; i< dataJurnals.size(); i++){
            dataJurnal = dataJurnals.get(i);

            this.dataJurnal[i][0] = dataJurnal.getTgl();
            this.dataJurnal[i][1] = dataJurnal.getKeterangan();
            this.dataJurnal[i][2] = String.valueOf(dataJurnal.getAkunDebet());
            this.dataJurnal[i][3] = String.valueOf(dataJurnal.getNominalDebet());
            this.dataJurnal[i][4] = String.valueOf(dataJurnal.getAkunKredit());
            this.dataJurnal[i][5] = String.valueOf(dataJurnal.getNominalKredit());

        }

        return this.dataJurnal;
    }

}
