package com.zakiadev.sulistyarif.revselfaccounting.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.zakiadev.sulistyarif.revselfaccounting.data.DataJurnal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sulistyarif on 02/02/2018.
 */

public class DBAdapter {
    Context c;
    SQLiteDatabase database;
    DBHelper dbHelper;

    public DBAdapter(Context c){
        this.c = c;
        dbHelper = new DBHelper(c);
    }

    public boolean saveDataJurnal(DataJurnal dataJurnal){

        try {

            long dateMilis = 0;
            try {
                dateMilis = strToLongDate(dataJurnal.getTgl());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            database = dbHelper.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(Constants.TGL_JUR, dateMilis);
            cv.put(Constants.KETERANGAN, dataJurnal.getKeterangan());
//            cv.put(Constants.DEBET, dataJurnal.getAkun_debet());
//            cv.put(Constants.KREDIT, dataJurnal.getNominal_kredit());

            long result = database.insert(Constants.TB_NAME, Constants.ROW_ID, cv);
            if (result > 0){
                return true;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            dbHelper.close();
        }

        return false;
    }

    private long strToLongDate(String tgl) throws ParseException {

        String tglStr = tgl;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");
        Date date = sdf.parse(tglStr);
        long milis = date.getTime();

        return milis;
    }


    public ArrayList<DataJurnal> ambilData(){
        ArrayList<DataJurnal> dataJurnals = new ArrayList<>();
        String[] col = {Constants.ROW_ID, Constants.TGL_JUR, Constants.KETERANGAN, Constants.DEBET, Constants.KREDIT};

        try {
            database = dbHelper.getWritableDatabase();
            Cursor cursor = database.query(Constants.TB_NAME, col, null, null, null, null, Constants.TGL_JUR);

            DataJurnal dataJurnal;

            if (cursor != null){
                while (cursor.moveToNext()){

                    String tglMilis = getDateStr(cursor.getLong(1));

//                    long tanggal = Long.parseLong(cursor.getString(1));
                    String keterangan = cursor.getString(2);
                    long debet = Long.parseLong(cursor.getString(3));
                    long kredit = Long.parseLong(cursor.getString(4));

                    dataJurnal = new DataJurnal();
                    dataJurnal.setTgl(tglMilis);
                    dataJurnal.setKeterangan(keterangan);
//                    dataJurnal.setNominal_debet(debet);
//                    dataJurnal.setNominal_debet(kredit);

                    dataJurnals.add(dataJurnal);

                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return dataJurnals;

    }

    private String getDateStr(long dateMilis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateMilis);
        return sdf.format(calendar.getTime());

    }


}
