package com.zakiadev.sulistyarif.revselfaccounting.tablehelper;

import android.content.Context;

import com.zakiadev.sulistyarif.revselfaccounting.data.DataSaldo;
import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapterMix;

import java.util.ArrayList;

/**
 * Created by sulistyarif on 13/02/18.
 */

public class TableHelperDataSaldo {
    Context context;

    public String[] colHeader = {"Kode Akun", "Nama Akun", "Debet", "Kredit"};
    public String[][] dataSaldo;

    public TableHelperDataSaldo(Context context) {
        this.context = context;
    }

    public String[] getColHeader() {
        return colHeader;
    }

    public String[][] getDataSaldo() {
        ArrayList<DataSaldo> dataSaldos = new DBAdapterMix(context).selectRiwayat();
        DataSaldo dataSaldo;

        this.dataSaldo = new String[dataSaldos.size()][3];
        for (int i = 0; i < dataSaldos.size(); i++){
            dataSaldo = dataSaldos.get(i);

            this.dataSaldo[i][0] = dataSaldo.getKodeAkun();
            this.dataSaldo[i][1] = dataSaldo.getNamaAkun();
            this.dataSaldo[i][2] = String.valueOf(dataSaldo.getNominal());

        }
        return this.dataSaldo;
    }
}
