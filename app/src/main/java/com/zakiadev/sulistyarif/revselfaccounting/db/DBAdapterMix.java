package com.zakiadev.sulistyarif.revselfaccounting.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zakiadev.sulistyarif.revselfaccounting.data.DataAkun;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataJurnal;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataSaldo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sulistyarif on 05/02/2018.
 */

public class DBAdapterMix extends SQLiteOpenHelper {

    private static final String DB_NAME = "revaccounting";
    private static final int DB_VERSION = 1;

    private static final String TABLE_AKUN = "akun";
    private static final String TABLE_DATA_PERUSAHAAN = "data_perusahaan";
    private static final String TABLE_JURNAL = "jurnal";
    private static final String TABLE_RIWAYAT_NOMINAL = "riwayat_nominal";

//    digunakan untuk akun
    private static final String KODE_AKUN = "kode_akun";
    private static final String NAMA_AKUN = "nama_akun";
    private static final String JENIS_AKUN = "jenis";

//    digunakan untuk tabel jurnal
    private static final String ID_JURNAL = "id";
    private static final String TGL_TRANS = "tgl";
    private static final String KETERANGAN = "keterangan";
    private static final String AKUN_DEBET = "akun_debet";
    private static final String NAMA_DEBET = "nama_debet";
    private static final String AKUN_KREDIT = "akun_kredit";
    private static final String NAMA_KREDIT = "nama_kredit";
    private static final String NOMINAL_DEBET = "nominal_debet";
    private static final String NOMINAL_KREDIT = "nominal_kredit";

//    digunakan untuk riwayat nominal
    private static final String ID_RIWAYAT = "id";
    private static final String KODE_AKUN_RYT = "kode_akun";
    private static final String NOMINAL = "nominal";

//    digunakan untuk data perusahaan
    private static final String NAMA_PERUSAHAAN = "nama_perusahaan";
    private static final String NAMA_PEMILIK = "nama_pemilik";
    private static final String ALAMAT_PEMILIK = "alamat";
    private static final String TELP_PEMILIK = "telp";
    private static final String EMAIL_PEMILIK = "email";

    private static final String DROP_TABLE = "DROP TABLE IF EXIST " + TABLE_AKUN;
    private static final String DROP_TABLE_PEMILIK = "DROP TABLE IF EXIST " + TABLE_DATA_PERUSAHAAN;
    private static final String DROP_TABLE_JURNAL = "DROP TABLE IF EXIST " + TABLE_JURNAL;
    private static final String DROP_TABLE_RIWAYAT_NOMINAL = "DROP TABLE IF EXIST" + TABLE_RIWAYAT_NOMINAL;


    public DBAdapterMix(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_AKUN = "CREATE TABLE " + TABLE_AKUN + "(" + KODE_AKUN + " INTEGER PRIMARY KEY," + NAMA_AKUN + " TEXT," + JENIS_AKUN + " INTEGER)";
        String CREATE_DATA_PERUSAHAAN = "CREATE TABLE data_perusahaan(id INTEGER PRIMARY KEY, nama_perusahaan TEXT, nama_pemilik TEXT, alamat TEXT, telp TEXT, email TEXT)";
        String CREATE_TABEL_JURNAL = "CREATE TABLE jurnal(id INTEGER PRIMARY KEY, tgl INTEGER, keterangan TEXT, akun_debet INTEGER, nama_debet TEXT, akun_kredit INTEGER, nama_kredit TEXT, nominal_debet INTEGER, nominal_kredit INTEGER)";
        String CREATE_RIWAYAT_NOMINAL = "CREATE TABLE riwayat_nominal(id INTEGER PRIMARY KEY, kode_akun INTEGER, nominal INTEGER)";

        db.execSQL(CREATE_TABLE_AKUN);
        db.execSQL(CREATE_DATA_PERUSAHAAN);
        db.execSQL(CREATE_TABEL_JURNAL);
        db.execSQL(CREATE_RIWAYAT_NOMINAL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        db.execSQL(DROP_TABLE_PEMILIK);
        db.execSQL(DROP_TABLE_JURNAL);
        db.execSQL(DROP_TABLE_RIWAYAT_NOMINAL);

        onCreate(db);
    }

    public void insertAkun(DataAkun dataAkun, int jenisAkun){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(KODE_AKUN, dataAkun.getKodeAkun());
        cv.put(NAMA_AKUN, dataAkun.getNamaAkun());
        cv.put(JENIS_AKUN, jenisAkun);

        db.insert(TABLE_AKUN, null, cv);
        db.close();

    }

    public ArrayList<DataAkun> selectAkun(int i){
        ArrayList<DataAkun> dataAkuns = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_AKUN + " WHERE jenis = " + i;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        DataAkun dataAkun;

        if (cursor != null){
            while (cursor.moveToNext()){
                String kodeAkun = cursor.getString(0);
                String namaAkun = cursor.getString(1);
                String jenisAkun = cursor.getString(2);

                dataAkun = new DataAkun();
                dataAkun.setKodeAkun(kodeAkun);
                dataAkun.setNamaAkun(namaAkun);
                dataAkun.setJenis(jenisAkun);

                dataAkuns.add(dataAkun);

            }
        }
        return dataAkuns;
    }

    public ArrayList<DataAkun> selectAssetLancar() {
        ArrayList<DataAkun> dataAkuns = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_AKUN + "  WHERE jenis = 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        DataAkun dataAkun;

        if (cursor != null){
            while (cursor.moveToNext()){
                String kodeAkun = cursor.getString(1);
                String namaAkun = cursor.getString(2);

                dataAkun = new DataAkun();
                dataAkun.setKodeAkun(kodeAkun);
                dataAkun.setNamaAkun(namaAkun);

                dataAkuns.add(dataAkun);

            }
        }
        return dataAkuns;
    }



    public ArrayList<DataAkun> selectAkunAll() {
        ArrayList<DataAkun> dataAkuns = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_AKUN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        DataAkun dataAkun;

        if (cursor != null){
            while (cursor.moveToNext()){
                String kodeAkun = cursor.getString(1);
                String namaAkun = cursor.getString(2);

                dataAkun = new DataAkun();
                dataAkun.setKodeAkun(kodeAkun);
                dataAkun.setNamaAkun(namaAkun);

                dataAkuns.add(dataAkun);

            }
        }
        return dataAkuns;
    }

    public void insertDataPerusahaan(String namaPers, String namaPemilik, String alamat, String telp, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(NAMA_PERUSAHAAN, namaPers);
        cv.put(NAMA_PEMILIK, namaPemilik);
        cv.put(ALAMAT_PEMILIK, alamat);
        cv.put(TELP_PEMILIK, telp);
        cv.put(EMAIL_PEMILIK, email);

        db.insert(TABLE_DATA_PERUSAHAAN, null, cv);
        db.close();
    }

//    dengan 2 parameter
    public ArrayList<DataAkun> selectAkun(int i, int i1) {
        ArrayList<DataAkun> dataAkuns = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_AKUN + " WHERE jenis = " + i + " OR jenis =" + i1;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        DataAkun dataAkun;

        if (cursor != null){
            while (cursor.moveToNext()){
                String kodeAkun = cursor.getString(0);
                String namaAkun = cursor.getString(1);
                String jenisAkun = cursor.getString(2);


                dataAkun = new DataAkun();
                dataAkun.setKodeAkun(kodeAkun);
                dataAkun.setNamaAkun(namaAkun);
                dataAkun.setJenis(jenisAkun);

                dataAkuns.add(dataAkun);

            }
        }
        return dataAkuns;
    }

    //    dengan 3 parameter
    public ArrayList<DataAkun> selectAkun(int i, int i1, int i2) {
        ArrayList<DataAkun> dataAkuns = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_AKUN + " WHERE jenis = " + i + " OR jenis =" + i1 + " OR jenis =" + i2;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        DataAkun dataAkun;

        if (cursor != null){
            while (cursor.moveToNext()){
                String kodeAkun = cursor.getString(0);
                String namaAkun = cursor.getString(1);
                String jenisAkun = cursor.getString(2);


                dataAkun = new DataAkun();
                dataAkun.setKodeAkun(kodeAkun);
                dataAkun.setNamaAkun(namaAkun);
                dataAkun.setJenis(jenisAkun);

                dataAkuns.add(dataAkun);

            }
        }
        return dataAkuns;
    }

    //    dengan 4 parameter
    public ArrayList<DataAkun> selectAkun(int i, int i1, int i2, int i3) {
        ArrayList<DataAkun> dataAkuns = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_AKUN + " WHERE jenis = " + i + " OR jenis =" + i1 + " OR jenis =" + i2 + " OR jenis =" + i3;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        DataAkun dataAkun;

        if (cursor != null){
            while (cursor.moveToNext()){
                String kodeAkun = cursor.getString(0);
                String namaAkun = cursor.getString(1);
                String jenisAkun = cursor.getString(2);


                dataAkun = new DataAkun();
                dataAkun.setKodeAkun(kodeAkun);
                dataAkun.setNamaAkun(namaAkun);
                dataAkun.setJenis(jenisAkun);

                dataAkuns.add(dataAkun);

            }
        }
        return dataAkuns;
    }

    //    dengan 5 parameter
    public ArrayList<DataAkun> selectAkun(int i, int i1, int i2, int i3, int i4) {
        ArrayList<DataAkun> dataAkuns = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_AKUN + " WHERE jenis = " + i + " OR jenis =" + i1 + " OR jenis =" + i2 + " OR jenis =" + i3 + " OR jenis =" + i4;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        DataAkun dataAkun;

        if (cursor != null){
            while (cursor.moveToNext()){
                String kodeAkun = cursor.getString(0);
                String namaAkun = cursor.getString(1);
                String jenisAkun = cursor.getString(2);


                dataAkun = new DataAkun();
                dataAkun.setKodeAkun(kodeAkun);
                dataAkun.setNamaAkun(namaAkun);
                dataAkun.setJenis(jenisAkun);

                dataAkuns.add(dataAkun);

            }
        }
        return dataAkuns;
    }

    //    dengan 7 parameter
    public ArrayList<DataAkun> selectAkun(int i, int i1, int i2, int i3, int i4, int i5, int i6) {
        ArrayList<DataAkun> dataAkuns = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_AKUN + " WHERE jenis = " + i + " OR jenis =" + i1 + " OR jenis =" + i2 + " OR jenis =" + i3 + " OR jenis =" + i4 + " OR jenis =" + i5 + " OR jenis =" + i6;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        DataAkun dataAkun;

        if (cursor != null){
            while (cursor.moveToNext()){
                String kodeAkun = cursor.getString(0);
                String namaAkun = cursor.getString(1);
                String jenisAkun = cursor.getString(2);


                dataAkun = new DataAkun();
                dataAkun.setKodeAkun(kodeAkun);
                dataAkun.setNamaAkun(namaAkun);
                dataAkun.setJenis(jenisAkun);

                dataAkuns.add(dataAkun);

            }
        }
        return dataAkuns;
    }

//    dengan 6 parameter
    public ArrayList<DataAkun> selectAkun(int i, int i1, int i2, int i3, int i4, int i5) {
        ArrayList<DataAkun> dataAkuns = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_AKUN + " WHERE jenis = " + i + " OR jenis =" + i1 + " OR jenis =" + i2 + " OR jenis =" + i3 + " OR jenis =" + i4 + " OR jenis =" + i5;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        DataAkun dataAkun;

        if (cursor != null){
            while (cursor.moveToNext()){
                String kodeAkun = cursor.getString(0);
                String namaAkun = cursor.getString(1);
                String jenisAkun = cursor.getString(2);


                dataAkun = new DataAkun();
                dataAkun.setKodeAkun(kodeAkun);
                dataAkun.setNamaAkun(namaAkun);
                dataAkun.setJenis(jenisAkun);

                dataAkuns.add(dataAkun);

            }
        }
        return dataAkuns;
    }




    private long strToLongDate(String tgl) {
        String tglStr = tgl;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");
        Date date = null;
        try {
            date = sdf.parse(tglStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long milis = date.getTime();

        return milis;
    }

    private String longToStr(long aLong) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(aLong);
        return sdf.format(calendar.getTime());
    }

//    melakukan save data jurnal
    public void insertJurnal(DataJurnal dataJurnal){
        SQLiteDatabase db = this.getWritableDatabase();

        long dateInMilis = strToLongDate(dataJurnal.getTgl());

        ContentValues cv = new ContentValues();
        cv.put(TGL_TRANS,dateInMilis);
        cv.put(KETERANGAN, dataJurnal.getKeterangan());
        cv.put(AKUN_DEBET, dataJurnal.getAkunDebet());
        cv.put(NAMA_DEBET, dataJurnal.getNamaDebet());
        cv.put(AKUN_KREDIT, dataJurnal.getAkunKredit());
        cv.put(NAMA_KREDIT, dataJurnal.getNamaKredit());
        cv.put(NOMINAL_DEBET, dataJurnal.getNominalDebet());
        cv.put(NOMINAL_KREDIT, dataJurnal.getNominalKredit());

        db.insert(TABLE_JURNAL, null, cv);
        db.close();
    }

    public ArrayList<DataJurnal> selectJurnal(){
        ArrayList<DataJurnal> dataJurnals = new ArrayList<>();

        String querySelect = "SELECT * FROM " + TABLE_JURNAL;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        DataJurnal dataJurnal;

        if (cursor != null){
            while (cursor.moveToNext()){

                String tgl = longToStr(cursor.getLong(1));
                String keterangan = cursor.getString(2);
                int akun_debet = cursor.getInt(3);
                String nama_debet = cursor.getString(4);
                int akun_kredit = cursor.getInt(5);
                String nama_kredit = cursor.getString(6);
                long nominal_debet = cursor.getLong(7);
                long nominal_kredit = cursor.getLong(8);

                dataJurnal = new DataJurnal();
                dataJurnal.setTgl(tgl);
                dataJurnal.setKeterangan(keterangan);
                dataJurnal.setAkunDebet(akun_debet);
                dataJurnal.setNamaDebet(nama_debet);
                dataJurnal.setAkunKredit(akun_kredit);
                dataJurnal.setNamaKredit(nama_kredit);
                dataJurnal.setNominalDebet(nominal_debet);
                dataJurnal.setNominalKredit(nominal_kredit);

                dataJurnals.add(dataJurnal);

            }
        }
        return dataJurnals;

    }

//    digunakan untuk melakukan pengambilan data neraca saldo
    public ArrayList<DataSaldo> selectRiwayat() {
        ArrayList<DataSaldo> dataSaldos = new ArrayList<>();

        String querySaldo = "SELECT riwayat_nominal.kode_akun, akun.nama_akun,sum(nominal), akun.jenis\n" +
                "FROM `riwayat_nominal` \n" +
                "INNER JOIN akun ON riwayat_nominal.kode_akun=akun.kode_akun\n" +
                "GROUP BY riwayat_nominal.kode_akun";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySaldo, null);

        DataSaldo dataSaldo;

        if (cursor != null){
            while (cursor.moveToNext()){
                String kodeAkun = String.valueOf(cursor.getInt(0));
                String namaAkun = cursor.getString(1);
                long nominal = cursor.getLong(2);
                int jenis = cursor.getInt(3);

//                untuk pengecekan
                System.out.println("Data yang diambil : " + kodeAkun);

                dataSaldo = new DataSaldo();
                dataSaldo.setKodeAkun(kodeAkun);
                dataSaldo.setNamaAkun(namaAkun);
                dataSaldo.setNominal(nominal);
                dataSaldo.setJenis(jenis);

                dataSaldos.add(dataSaldo);
            }
        }
        return dataSaldos;
    }

//    digunakan untuk memasukkan data riwayat jurnal
    public void insertRiwayatSaldo(DataSaldo dataSaldo){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(KODE_AKUN_RYT, dataSaldo.getKodeAkun());
        cv.put(NOMINAL, dataSaldo.getNominal());

        db.insert(TABLE_RIWAYAT_NOMINAL, null, cv);
        db.close();
    }

    public ArrayList<DataJurnal> selectJurnalUmum(){
        ArrayList<DataJurnal> dataJurnals = new ArrayList<>();

        String querySelect = "SELECT * FROM " + TABLE_JURNAL;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        DataJurnal dataJurnal;

        if (cursor != null){
            while (cursor.moveToNext()){

                String tgl = longToStr(cursor.getLong(1));
                String keterangan = cursor.getString(2);
                int akun_debet = cursor.getInt(3);
                int akun_kredit = cursor.getInt(4);
                long nominal_debet = cursor.getLong(5);
                long nominal_kredit = cursor.getLong(6);

                dataJurnal = new DataJurnal();
                dataJurnal.setTgl(tgl);
                dataJurnal.setKeterangan(keterangan);
                dataJurnal.setAkunDebet(akun_debet);
                dataJurnal.setAkunKredit(akun_kredit);
                dataJurnal.setNominalDebet(nominal_debet);
                dataJurnal.setNominalKredit(nominal_kredit);

                dataJurnals.add(dataJurnal);

            }
        }
        return dataJurnals;

    }

    public ArrayList<DataSaldo> selectRiwayatJenis(int jenisAkun) {
        ArrayList<DataSaldo> dataSaldos = new ArrayList<>();

        String querySaldo = "SELECT riwayat_nominal.kode_akun, akun.nama_akun,sum(nominal), akun.jenis\n" +
                "FROM `riwayat_nominal` \n" +
                "INNER JOIN akun ON riwayat_nominal.kode_akun=akun.kode_akun\n" +
                "WHERE akun.jenis = " + jenisAkun + "\n" +
                "GROUP BY riwayat_nominal.kode_akun";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySaldo, null);

        DataSaldo dataSaldo;

        if (cursor != null){
            while (cursor.moveToNext()){
                String kodeAkun = String.valueOf(cursor.getInt(0));
                String namaAkun = cursor.getString(1);
                long nominal = cursor.getLong(2);
                int jenis = cursor.getInt(3);

//                untuk pengecekan
                System.out.println("Data yang diambil : " + kodeAkun);

                dataSaldo = new DataSaldo();
                dataSaldo.setKodeAkun(kodeAkun);
                dataSaldo.setNamaAkun(namaAkun);
                dataSaldo.setNominal(nominal);
                dataSaldo.setJenis(jenis);

                dataSaldos.add(dataSaldo);
            }
        }
        return dataSaldos;
    }

    public ArrayList<DataSaldo> selectRiwayatJenis(int i, int i1) {
        ArrayList<DataSaldo> dataSaldos = new ArrayList<>();

        String querySaldo = "SELECT riwayat_nominal.kode_akun, akun.nama_akun,sum(nominal), akun.jenis\n" +
                "FROM `riwayat_nominal` \n" +
                "INNER JOIN akun ON riwayat_nominal.kode_akun=akun.kode_akun\n" +
                "WHERE akun.jenis = " + i + " OR akun.jenis = " + i1 + "\n" +
                "GROUP BY riwayat_nominal.kode_akun";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySaldo, null);

        DataSaldo dataSaldo;

        if (cursor != null){
            while (cursor.moveToNext()){
                String kodeAkun = String.valueOf(cursor.getInt(0));
                String namaAkun = cursor.getString(1);
                long nominal = cursor.getLong(2);
                int jenis = cursor.getInt(3);

//                untuk pengecekan
                System.out.println("Data yang diambil : " + kodeAkun);

                dataSaldo = new DataSaldo();
                dataSaldo.setKodeAkun(kodeAkun);
                dataSaldo.setNamaAkun(namaAkun);
                dataSaldo.setNominal(nominal);
                dataSaldo.setJenis(jenis);

                dataSaldos.add(dataSaldo);
            }
        }
        return dataSaldos;
    }
}