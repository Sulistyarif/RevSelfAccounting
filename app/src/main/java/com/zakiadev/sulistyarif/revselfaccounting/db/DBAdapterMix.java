package com.zakiadev.sulistyarif.revselfaccounting.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zakiadev.sulistyarif.revselfaccounting.data.DataAkun;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataJurnal;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataJurnalMar;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataModal;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataPerusahaan;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataSaldo;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataTransMar;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataTransaksiMar;
import com.zakiadev.sulistyarif.revselfaccounting.data.EditDataTransMar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private static final String TABLE_MODAL = "modal";

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
    private static final String TGL = "tgl";

//    digunakan untuk data perusahaan
    private static final String NAMA_PERUSAHAAN = "nama_perusahaan";
    private static final String NAMA_PEMILIK = "nama_pemilik";
    private static final String ALAMAT_PEMILIK = "alamat";
    private static final String TELP_PEMILIK = "telp";
    private static final String EMAIL_PEMILIK = "email";

//    digunakan untuk data modal
    private static final String TGL_MODAL = "tgl";
    private static final String NOMINAL_MODAL = "nominal";
    private static final String NOMINAL_KAS = "nominalKas";

    private static final String DROP_TABLE = "DROP TABLE IF EXIST " + TABLE_AKUN;
    private static final String DROP_TABLE_PEMILIK = "DROP TABLE IF EXIST " + TABLE_DATA_PERUSAHAAN;
//    private static final String DROP_TABLE_JURNAL = "DROP TABLE IF EXIST " + TABLE_JURNAL;
//    private static final String DROP_TABLE_RIWAYAT_NOMINAL = "DROP TABLE IF EXIST" + TABLE_RIWAYAT_NOMINAL;
    private static final String DROP_TABLE_JURNAL = "DROP TABLE IF EXIST jurnal";
    private static final String DROP_TABLE_TRANS = "DROP TABLE IF EXIST trans";
    private static final String DROP_TABLE_MODAL = "DROP TABLE IF EXIST " + TABLE_MODAL;



    public DBAdapterMix(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_AKUN = "CREATE TABLE " + TABLE_AKUN + "(" + KODE_AKUN + " INTEGER PRIMARY KEY," + NAMA_AKUN + " TEXT," + JENIS_AKUN + " INTEGER)";
        String CREATE_DATA_PERUSAHAAN = "CREATE TABLE data_perusahaan(id INTEGER PRIMARY KEY, nama_perusahaan TEXT, nama_pemilik TEXT, alamat TEXT, telp TEXT, email TEXT)";
//        String CREATE_TABEL_JURNAL = "CREATE TABLE jurnal(id INTEGER PRIMARY KEY, tgl INTEGER, keterangan TEXT, akun_debet INTEGER, nama_debet TEXT, akun_kredit INTEGER, nama_kredit TEXT, nominal_debet INTEGER, nominal_kredit INTEGER)";
//        String CREATE_RIWAYAT_NOMINAL = "CREATE TABLE riwayat_nominal(id INTEGER PRIMARY KEY, kode_akun INTEGER, nominal INTEGER, tgl TEXT)";
        String CREATE_TABLE_JURNAL = "CREATE TABLE jurnal(id INTEGER PRIMARY KEY AUTOINCREMENT, pid TEXT, tgl TEXT, ket TEXT, kode_trans int);";
        String CREATE_TABLE_TRANS = "CREATE TABLE trans(id INTEGER PRIMARY KEY AUTOINCREMENT, pid TEXT, kode_akun TEXT, nominal INTEGER, pos INTEGER);";
        String CREATE_TABLE_MODAL = "CREATE TABLE modal(tgl TEXT PRIMARY KEY, nominal INTEGER, nominalKas INTEGER)";

        db.execSQL(CREATE_TABLE_AKUN);
        db.execSQL(CREATE_DATA_PERUSAHAAN);
        db.execSQL(CREATE_TABLE_JURNAL);
        db.execSQL(CREATE_TABLE_TRANS);
        db.execSQL(CREATE_TABLE_MODAL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        db.execSQL(DROP_TABLE_PEMILIK);
        db.execSQL(DROP_TABLE_JURNAL);
        db.execSQL(DROP_TABLE_TRANS);
        db.execSQL(DROP_TABLE_MODAL);

        onCreate(db);
    }

//    digunakan untuk menentukan pid
    public int selectLastId() {
        String querySaldo = "SELECT MAX(id) FROM jurnal";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySaldo, null);

        int id = 0;

        if (cursor != null){
            while (cursor.moveToNext()){
                id = cursor.getInt(0);
                Log.i("idSeharusnya", ""+id);
            }
        }
        Log.i("idDireturn", ""+id);
        return id;
    }

//    insert data jurnal dengan model bulan maret
    public void insertJurnalMar(DataJurnalMar dataJurnalMar) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("pid", dataJurnalMar.getPid());
        cv.put("tgl", dataJurnalMar.getTgl());
        cv.put("ket", dataJurnalMar.getKet());
        cv.put("kode_trans", dataJurnalMar.getKode_trans());

        db.insert("jurnal", null, cv);
        db.close();
    }

//    digunakan untuk melakukan select semua data pada tabel jurnal yang dilakukan oleh jurnal kecil dan laporan jurnal umum
    public ArrayList<DataJurnalMar> selectJurnalMar() {
        ArrayList<DataJurnalMar> dataJurnalMars = new ArrayList<>();

        String selectQuery = "SELECT * FROM jurnal";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        DataJurnalMar dataJurnalMar;

        if (cursor != null){
            while (cursor.moveToNext()){

//                karena col 0 itu id
                String pid = cursor.getString(1);
                String tgl = formatter(cursor.getString(2));
                String ket = cursor.getString(3);
                int kodeTrans = cursor.getInt(4);

                dataJurnalMar = new DataJurnalMar();
                dataJurnalMar.setPid(pid);
                dataJurnalMar.setTgl(tgl);
                dataJurnalMar.setKet(ket);
                dataJurnalMar.setKode_trans(kodeTrans);

                Log.i("dataJurnal", "pid: " + pid + ", tgl: " + tgl + ", ket:" + ket + ", kodeTrans: " + kodeTrans);

                dataJurnalMars.add(dataJurnalMar);

            }
        }
        return dataJurnalMars;
    }

    public void updateJurnalMar(DataJurnalMar dataJurnalMar) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("tgl", dataJurnalMar.getTgl());
        cv.put("ket", dataJurnalMar.getKet());
        cv.put("kode_trans", dataJurnalMar.getKode_trans());

        db.update("jurnal",cv,"pid=?", new String[]{dataJurnalMar.getPid()});
        db.close();
    }

    public void deleteJurnalMar(String pid) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("jurnal", "pid=?", new String[]{pid});
        db.delete("trans","pid=?", new String[]{pid});
        db.close();
    }

//    digunakan untuk memasukkan data
    public void insertTrans(DataTransMar dataTransMar) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("pid", dataTransMar.getPid());
        cv.put("kode_akun", dataTransMar.getKode_akun());
        cv.put("nominal", dataTransMar.getNominal());
        cv.put("pos", dataTransMar.getPos());

        db.insert("trans", null, cv);
        db.close();
    }

    public void updateTrans(DataTransMar dataTransMar, Integer integer) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("kode_akun", dataTransMar.getKode_akun());
        cv.put("nominal", dataTransMar.getNominal());
        cv.put("pos", dataTransMar.getPos());

        db.update("trans", cv, "id=?", new String[]{String.valueOf(integer)});
        db.close();
    }

//    digunakan untuk melakukan select data transaksi yang mau di edit
    public ArrayList<EditDataTransMar> selectTransToEdit(String editPid) {
        ArrayList<EditDataTransMar> editDataTransMarArrayList = new ArrayList<>();

        String selectQuery = "SELECT trans.id, trans.pid, trans.kode_akun, akun.nama_akun, trans.nominal, akun.jenis, trans.pos \n" +
                "FROM trans \n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun \n" +
                "WHERE trans.pid = '" + editPid + "';";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        EditDataTransMar editDataTransMar;

        if (cursor != null){
            while (cursor.moveToNext()){

                String id = cursor.getString(0);
                String pid = cursor.getString(1);
                String kodeAkun = cursor.getString(2);
                String namaAkun = cursor.getString(3);
                int nominal = cursor.getInt(4);
                int jenis = cursor.getInt(5);
                int transPos = cursor.getInt(6);

                editDataTransMar = new EditDataTransMar();

                editDataTransMar.setId(id);
                editDataTransMar.setPid(pid);
                editDataTransMar.setKodeAkun(kodeAkun);
                editDataTransMar.setNamaAkun(namaAkun);
                editDataTransMar.setNominal(nominal);
                editDataTransMar.setJenisAkun(jenis);
                editDataTransMar.setPos(transPos);

                Log.i("dataDiEdit", "id: " + id + ", pid:" + pid + ", kodeAkun:" + kodeAkun + ", namaAkun:" + namaAkun + ", nominal:" + nominal + ", jenis:" + jenis + ", pos:" + transPos);

                editDataTransMarArrayList.add(editDataTransMar);

            }
        }
        return editDataTransMarArrayList;
    }

//    digunakan untuk melihat jurnak umum
    public ArrayList<DataTransaksiMar> selectTransMar() {
        ArrayList<DataTransaksiMar> dataTransaksiMarArrayList = new ArrayList<DataTransaksiMar>();

        String selectQuery = "SELECT jurnal.pid, jurnal.tgl, jurnal.ket, trans.kode_akun, akun.nama_akun, trans.nominal, trans.pos, akun.jenis\n" +
                "FROM jurnal\n" +
                "LEFT JOIN trans ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "ORDER BY jurnal.tgl ASC,jurnal.pid ASC, trans.pos ASC;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        DataTransaksiMar dataTransaksiMar;

        if (cursor != null){
            while (cursor.moveToNext()){

                String pid = cursor.getString(0);
                String tgl = formatter(cursor.getString(1));
                String ket = cursor.getString(2);
                String kodeAkun = cursor.getString(3);
                String namaAkun = cursor.getString(4);
                int nominal = cursor.getInt(5);
                int pos = cursor.getInt(6);
                int jenis = cursor.getInt(7);

                dataTransaksiMar = new DataTransaksiMar();

                dataTransaksiMar.setPid(pid);
                dataTransaksiMar.setTgl(tgl);
                dataTransaksiMar.setKet(ket);
                dataTransaksiMar.setKodeAkun(kodeAkun);
                dataTransaksiMar.setNamaAkun(namaAkun);
                dataTransaksiMar.setNominal(nominal);
                dataTransaksiMar.setPos(pos);
                dataTransaksiMar.setJenis(jenis);

                Log.i("dataJurnal", "pid:" + pid + ", tgl:" + tgl + ", ket:" + ket + ", kodeAkun:" + kodeAkun + ", namaAkun:" + namaAkun + ", nominal:" + nominal + ", pos:" + pos + ", jenis:" + jenis);

                dataTransaksiMarArrayList.add(dataTransaksiMar);

            }
        }
        return dataTransaksiMarArrayList;
    }

//    digunakan untuk melihat neraca saldo
    public ArrayList<DataSaldo> selectNeracaSaldoAkumMar(int bulanDipilih, int tahunDipilih) {
        ArrayList<DataSaldo> dataSaldos = new ArrayList<DataSaldo>();
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);

        String querySaldo = "SELECT trans.kode_akun, akun.nama_akun, sum(trans.nominal), akun.jenis\n" +
                "FROM trans\n" +
                "INNER JOIN jurnal ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE (akun.jenis = 0 OR akun.jenis = 1 OR akun.jenis = 2 OR akun.jenis = 3 OR akun.jenis = 10) AND " +
                "strftime('%m',jurnal.tgl) <= '" + bulan + "' AND strftime('%Y',jurnal.tgl) <= '" + tahun + "'\n" +
                "GROUP BY trans.kode_akun;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySaldo, null);

        DataSaldo dataSaldo;

        if (cursor != null){
            while (cursor.moveToNext()){
                String kodeAkun = String.valueOf(cursor.getInt(0));
                String namaAkun = cursor.getString(1);
                long nominal = cursor.getLong(2);
                int jenis = cursor.getInt(3);

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

//    digunakn untuk laporan labarugi
    public ArrayList<DataSaldo> selectRiwayatJenisBlnThnMar(int i, int bulanDipilih, int tahunDipilih) {
        ArrayList<DataSaldo> dataSaldos = new ArrayList<DataSaldo>();
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);

        String querySaldo = "SELECT trans.kode_akun, akun.nama_akun, sum(trans.nominal), akun.jenis\n" +
                "FROM trans\n" +
                "INNER JOIN jurnal ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE strftime('%m',jurnal.tgl) <= '" + bulan +"' AND strftime('%Y',jurnal.tgl) <= '" + tahun + "' AND akun.jenis = '" + i + "'\n" +
                "GROUP BY trans.kode_akun;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySaldo, null);

        DataSaldo dataSaldo;

        if (cursor != null){
            while (cursor.moveToNext()){

                    String kodeAkun = String.valueOf(cursor.getInt(0));
                    String namaAkun = cursor.getString(1);
                    long nominal = cursor.getLong(2);
                    int jenis = cursor.getInt(3);

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

//    digunakan untuk mengambil data modal yang dimasukkan di tanggal 1 bulan dan tahun custom
    public ArrayList<DataJurnal> selectModalAwalMar(int bulanDipilih, int tahunDipilih) {
        ArrayList<DataJurnal> dataJurnals = new ArrayList<>();

        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);
        String querySelectModalBulanIni = "SELECT tgl, nominal\n" +
                "FROM modal\n" +
                "WHERE strftime('%m',tgl) = '" + bulan + "' AND strftime('%Y',tgl) = '" + tahun + "';";

        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor1 = db1.rawQuery(querySelectModalBulanIni, null);

        DataJurnal dataJurnal;

        if (cursor1 != null){
            while (cursor1.moveToNext()){

                String tgl = formatter(cursor1.getString(0));
                long nominal_kredit = cursor1.getLong(1);

                Log.i("Isi modal", "Datane : tanggal : " + tgl + ", Nominal : " + nominal_kredit );

                dataJurnal = new DataJurnal();

                    dataJurnal.setTgl(tgl);
                    dataJurnal.setNominalKredit(nominal_kredit);
                    dataJurnals.add(dataJurnal);

            }
        }

        return dataJurnals;

    }

//    digunakan untuk mencari modal tambahan (modal yang didapatkan jika tidak pada tanggal 1)
    public ArrayList<DataJurnal> selectModalTambahanMar(int bulanDipilih, int tahunDipilih) {
        ArrayList<DataJurnal> dataJurnals = new ArrayList<>();
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);

        String querySelect = "SELECT jurnal.tgl, trans.nominal\n" +
                "FROM trans\n" +
                "INNER JOIN jurnal ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE strftime('%m',jurnal.tgl) = '" + bulan + "' AND strftime('%Y',jurnal.tgl) = '" + tahun + "' AND akun.jenis = '4';";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        DataJurnal dataJurnal;

        if (cursor != null){
            while (cursor.moveToNext()){

                String tgl = formatter(cursor.getString(0));
                long nominal_kredit = cursor.getLong(1);

                Log.i("DBAdapterMix", "Datane : tanggal : " + tgl + ", Nominal : " + nominal_kredit );

                dataJurnal = new DataJurnal();

//                    Log.i("DBAdapterMix", "Masuk di hasil true");
                    dataJurnal.setTgl(tgl);
                    dataJurnal.setNominalKredit(nominal_kredit);
                    dataJurnals.add(dataJurnal);


            }
        }
        return dataJurnals;
    }

//    digunakan untuk mencari laba/rugi disuatu bulan
    public ArrayList<DataSaldo> selectLabaRugiMar(int bulanDipilih, int tahunDipilih) {

        int saldoPendapatan = 0;
        int saldoBeban = 0;
        int labaRugi = 0;
        ArrayList<DataSaldo> dataSaldos = new ArrayList<>();
        DataSaldo dataSaldo;
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);

    //        mencari data pendapatan
        String querySaldo = "SELECT trans.kode_akun, trans.nominal, jurnal.tgl\n" +
                "FROM trans\n" +
                "INNER JOIN jurnal ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE strftime('%m',jurnal.tgl) = '" + bulan + "' \n" +
                "AND strftime('%Y',jurnal.tgl) = '" + tahun + "' \n" +
                "AND (akun.jenis = '5' OR akun.jenis = '6');";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySaldo, null);

        if (cursor != null){
            while (cursor.moveToNext()){

    //                mengambil data tanggal biar diparse
                String tgl = formatter(cursor.getString(2));

    //                ngeparse tanggal
                String splitTgl[] = tgl.split("/");

    //                ngecek, cuma data yang bertanggal seperti input yang boleh dimasukkan
                    saldoPendapatan += cursor.getInt(1);
                    Log.i("SaldoPendapatan", "akun : " + cursor.getString(0) + ", dengan nominal : " + cursor.getString(1) + ", ditambahkan pada " + cursor.getString(2));
            }
        }
        Log.i("Pendapatane : ", String.valueOf(saldoPendapatan));
        db.close();

    //        mencari data beban biaya
        String queryBeban = "SELECT trans.kode_akun, trans.nominal, jurnal.tgl\n" +
                "FROM trans\n" +
                "INNER JOIN jurnal ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE strftime('%m',jurnal.tgl) = '" + bulan + "' \n" +
                "AND strftime('%Y',jurnal.tgl) = '" + tahun + "' \n" +
                "AND (akun.jenis = '7' OR akun.jenis = '8');";
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor1 = db1.rawQuery(queryBeban, null);

        if (cursor1 != null){
            while (cursor1.moveToNext()){
                    saldoBeban += cursor1.getInt(1);
                    Log.i("BebanBiaya", "akun : " + cursor1.getString(0) + ", dengan nominal : " + cursor1.getString(1) + ", ditambahkan pada " + cursor1.getString(2));
            }
        }
        Log.i("Bebane : ", String.valueOf(saldoBeban));

    //        Menghitung saldo laba
        labaRugi = saldoPendapatan - saldoBeban;
        Log.i("Labane :", String.valueOf(labaRugi));

        dataSaldo = new DataSaldo();
        dataSaldo.setNominal(labaRugi);
        dataSaldos.add(dataSaldo);

        return dataSaldos;
    }

//    mencari prive suatu waktu
    public ArrayList<DataSaldo> selectPriveBlnThnMar(int bulanDipilih, int tahunDipilih) {
        ArrayList<DataSaldo> dataSaldos = new ArrayList<>();
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);
        int prive = 0;

        String querySaldo = "SELECT trans.kode_akun, trans.nominal, jurnal.tgl\n" +
                "FROM trans\n" +
                "INNER JOIN jurnal ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE strftime('%m',jurnal.tgl) = '" + bulan + "' \n" +
                "AND strftime('%Y',jurnal.tgl) = '" + tahun + "' \n" +
                "AND akun.kode_akun = '6101';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySaldo, null);

        DataSaldo dataSaldo;

        if (cursor != null){
            while (cursor.moveToNext()){

                Log.i("priveSelect","data : " + cursor.getString(0) + ", " + cursor.getString(1) + ", " + cursor.getString(2));

                String tgl = formatter(cursor.getString(2));

                    prive += cursor.getInt(1);
                    Log.i("Prive", String.valueOf(prive));

            }
        }

    //                kirim ke laporan perubahan dengan lewat datasaldo
        dataSaldo = new DataSaldo();
        dataSaldo.setNominal(prive);

        dataSaldos.add(dataSaldo);

        return dataSaldos;
    }

//    digunakan untuk mencari modal neraca
    public ArrayList<DataSaldo> selectModalNeracaMar(int bulanDipilih, int tahunDipilih) {
        ArrayList<DataSaldo> dataSaldos = new ArrayList<>();

        String bulan = String.format("%02d", bulanDipilih + 1);
        String tahun = String.valueOf(tahunDipilih);
        String querySelect = "SELECT * FROM modal WHERE strftime('%m', tgl) = '" + bulan + "' AND strftime('%Y', tgl) = '" + tahun + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        DataSaldo dataSaldo;

        if (cursor != null){
            while (cursor.moveToNext()){

                String tgl = formatter(cursor.getString(0));
                long nominal_kredit = cursor.getLong(1);

                Log.i("NeracaModalPemilik", "Datane : tanggal : " + tgl + ", Nominal : " + nominal_kredit + " ," + cursor.getInt(2));

    //                String[] splitTgl = tgl.split("/");

    //                dibikin begini karena inputnya modal buat bulan depan
    //                int custBulan = Integer.parseInt(splitTgl[1]) - 1;
    //                String custBulanStr = String.format("%02d", custBulan);
    //                Log.i("hasilCustBulan ", "custBulanInt : " + custBulan + "custBulanStr0 : " + custBulanStr);

                dataSaldo = new DataSaldo();
    //                if (custBulanStr.equals(bulan) && splitTgl[2].equals(tahun)){

                dataSaldo.setTgl(tgl);
                dataSaldo.setNominal(nominal_kredit);
                dataSaldos.add(dataSaldo);

    //                }
            }
        }

        return dataSaldos;
    }

//    digunakan untuk mencari kas yang berhubungan dengan akun lain pada bagian debet
    public ArrayList<DataJurnal> selectArusKasOnDebetMar(int bulanDipilih, int tahunDipilih, int kodeAkun) {
        ArrayList<DataJurnal> dataJurnals = new ArrayList<>();
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);



        String querySelect = "SELECT jurnal.tgl, trans.kode_akun, akun.nama_akun, trans.nominal\n" +
                "FROM trans\n" +
                "LEFT JOIN jurnal ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE jurnal.pid IN (SELECT trans.pid FROM trans WHERE trans.kode_akun = '1101' AND trans.pos = '0') AND akun.jenis = '" + kodeAkun + "' AND jurnal.ket != 'Neraca Awal' AND strftime('%m', jurnal.tgl) = '" + bulan + "' AND strftime('%Y', jurnal.tgl) = '" + tahun + "'\n" +
                "EXCEPT \n" +
                "SELECT jurnal.tgl, trans.kode_akun, akun.nama_akun, trans.nominal\n" +
                "FROM trans\n" +
                "LEFT JOIN jurnal ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE trans.kode_akun = '1101';";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        DataJurnal dataJurnal;

        if (cursor != null){
            while (cursor.moveToNext()){

                String tgl = formatter(cursor.getString(0));
                int sKodeAkun = cursor.getInt(1);
                String namaAkun = cursor.getString(2);
                int nominalkredit = Math.abs(cursor.getInt(3));

                Log.i("hasilArusKas : ", tgl + ", " + sKodeAkun + ", " + namaAkun + ", " + nominalkredit);

//                String[] splitTgl = tgl.split("/");

//                if (splitTgl[1].equals(bulan) && splitTgl[2].equals(tahun)){

                    dataJurnal = new DataJurnal();
                    dataJurnal.setTgl(tgl);
                    dataJurnal.setAkunKredit(sKodeAkun);
                    dataJurnal.setNamaKredit(namaAkun);
                    dataJurnal.setNominalKredit(nominalkredit);

                    dataJurnals.add(dataJurnal);

//                }

            }
        }
        return dataJurnals;
    }

//    mencari data arus kas yang posisi kas berada pada kredit
    public ArrayList<DataJurnal> selectArusKasOnKreditMar(int bulanDipilih, int tahunDipilih, int kodeAkun) {
        ArrayList<DataJurnal> dataJurnals = new ArrayList<>();
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);



        String querySelect = "SELECT jurnal.tgl, trans.kode_akun, akun.nama_akun, trans.nominal\n" +
                "FROM trans\n" +
                "LEFT JOIN jurnal ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE jurnal.pid IN (SELECT trans.pid FROM trans WHERE trans.kode_akun = '1101' AND trans.pos = '1') AND akun.jenis = '" + kodeAkun + "' AND jurnal.ket != 'Neraca Awal' AND strftime('%m', jurnal.tgl) = '" + bulan + "' AND strftime('%Y', jurnal.tgl) = '" + tahun + "'\n" +
                "EXCEPT \n" +
                "SELECT jurnal.tgl, trans.kode_akun, akun.nama_akun, trans.nominal\n" +
                "FROM trans\n" +
                "LEFT JOIN jurnal ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE trans.kode_akun = '1101';";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        DataJurnal dataJurnal;

        if (cursor != null){
            while (cursor.moveToNext()){

                String tgl = formatter(cursor.getString(0));
                int sKodeAkun = cursor.getInt(1);
                String namaAkun = cursor.getString(2);
                int nominalkredit = Math.abs(cursor.getInt(3));

                Log.i("hasilArusKas : ", tgl + ", " + sKodeAkun + ", " + namaAkun + ", " + nominalkredit);

//                String[] splitTgl = tgl.split("/");

//                if (splitTgl[1].equals(bulan) && splitTgl[2].equals(tahun)){

                dataJurnal = new DataJurnal();
                dataJurnal.setTgl(tgl);
                dataJurnal.setAkunKredit(sKodeAkun);
                dataJurnal.setNamaKredit(namaAkun);
                dataJurnal.setNominalKredit(nominalkredit);

                dataJurnals.add(dataJurnal);

//                }

            }
        }
        return dataJurnals;
    }

//    buat cari data kas awal sebelum adanya transaksi
    public ArrayList<DataJurnal> selectKasAwalMar(int bulanDipilih, int tahunDipilih) {
        ArrayList<DataJurnal> dataJurnals = new ArrayList<>();
        int totalkas = 0;

        String tahun = String.valueOf(tahunDipilih);
        String bulanIni = String.format("%02d", bulanDipilih);
        String bulan = String.format("%02d", bulanDipilih - 1);

        String querySelectNeraca = "SELECT jurnal.tgl, trans.nominal\n" +
                "FROM jurnal\n" +
                "INNER JOIN trans ON jurnal.pid = trans.pid\n" +
                "WHERE kode_akun = 1101 AND jurnal.ket = 'Neraca Awal' AND strftime('%m', jurnal.tgl) = '" + bulanIni + "' AND strftime('%Y', jurnal.tgl) = '" + tahun + "';";

        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor1 = db1.rawQuery(querySelectNeraca, null);

        if (cursor1 != null){
            while (cursor1.moveToNext()){

                String tgl = formatter(cursor1.getString(0));
                int kasNeracaAwal = cursor1.getInt(1);

                totalkas += kasNeracaAwal;

            }
        }

        String querySelect = "SELECT jurnal.tgl, trans.nominal\n" +
                "FROM jurnal\n" +
                "INNER JOIN trans ON jurnal.pid = trans.pid\n" +
                "WHERE kode_akun = 1101 AND strftime('%m', jurnal.tgl) = '" + bulan + "' AND strftime('%Y', jurnal.tgl) = '" + tahun + "';";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        DataJurnal dataJurnal;

        if (cursor != null){
            while (cursor.moveToNext()){

                String tgl = formatter(cursor.getString(0));
                int saldoKas = cursor.getInt(1);

                String[] splitTgl = tgl.split("/");

//                yang dicari itu bulan kemarin
//                int bulanKmrn = bulanDipilih - 1;
//                String bulan = String.format("%02d", bulanKmrn);

//                if (splitTgl[1].equals(bulan) && splitTgl[2].equals(tahun)){
                    totalkas += saldoKas;
//                }
            }
        }
        dataJurnal = new DataJurnal();
        dataJurnal.setNominalDebet(totalkas);
        dataJurnals.add(dataJurnal);
        Log.i("totalkas ", " " + totalkas);
        return dataJurnals;
    }

//    melakukan select pada data perusahaan
    public DataPerusahaan selectDataPerusahaan() {

        DataPerusahaan dataPerusahaan = new DataPerusahaan();
        String querySelect = "SELECT * FROM data_perusahaan";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        if (cursor != null){
            while (cursor.moveToNext()){

                dataPerusahaan.setNamaPers(cursor.getString(1));
                dataPerusahaan.setNamaPemilik(cursor.getString(2));
                dataPerusahaan.setAlamat(cursor.getString(3));
                dataPerusahaan.setTelp(cursor.getString(4));
                dataPerusahaan.setEmail(cursor.getString(5));

            }
        }

        return dataPerusahaan;

    }

//    digunakan untuk insert data akun, dipanggil di splashscreen dan juga di setting
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

    public void insertDataPerusahaan(DataPerusahaan dataPerusahaan) {
        SQLiteDatabase db = this.getWritableDatabase();

        String namaPers = dataPerusahaan.getNamaPers();
        String namaPemilik = dataPerusahaan.getNamaPemilik();
        String alamat = dataPerusahaan.getAlamat();
        String telp = dataPerusahaan.getTelp();
        String email = dataPerusahaan.getEmail();

        String query = "INSERT OR REPLACE INTO data_perusahaan('id','nama_perusahaan','nama_pemilik','alamat','telp','email') VALUES ('1', '" + namaPers +"', '" + namaPemilik +"', '" + alamat + "', '" + telp + "', '" + email +"');";
        db.execSQL(query);

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

//    melakukan save data jurnal
    public void insertJurnal(DataJurnal dataJurnal){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TGL_TRANS,dataJurnal.getTgl());
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

                String id = cursor.getString(0);
                String tgl = formatter(cursor.getString(1));
                String keterangan = cursor.getString(2);
                int akun_debet = cursor.getInt(3);
                String nama_debet = cursor.getString(4);
                int akun_kredit = cursor.getInt(5);
                String nama_kredit = cursor.getString(6);
                long nominal_debet = cursor.getLong(7);
                long nominal_kredit = cursor.getLong(8);

                dataJurnal = new DataJurnal();
                dataJurnal.setId(id);
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

//    membuat method untuk mengambil data sebuah jenis akun di tanggal tertentu
    public ArrayList<DataJurnal> selectJurnalBlnThnJenis(int bulan, int tahun, int jenis){
        ArrayList<DataJurnal> dataJurnals = new ArrayList<>();

        String querySelect = "SELECT * FROM " + TABLE_JURNAL;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        DataJurnal dataJurnal;

        if (cursor != null){
            while (cursor.moveToNext()){

                String tgl = formatter(cursor.getString(1));
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
//                System.out.println("Data yang diambil : " + kodeAkun);

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
        cv.put(TGL, dataSaldo.getTgl());

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

                String tgl = formatter(cursor.getString(1));
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
//                System.out.println("Data yang diambil : " + kodeAkun);

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
//                System.out.println("Data yang diambil : " + kodeAkun);

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


//    melakukan testing mengambil date menggunakan function date()
    public void selectDateJurnal() {
        ArrayList<DataJurnal> dataJurnals = new ArrayList<>();

        String querySelect = "SELECT date(tgl) FROM " + TABLE_JURNAL;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        DataJurnal dataJurnal;

        if (cursor != null){
            while (cursor.moveToNext()){

                String tgl = formatter(cursor.getString(0));
                Log.i("returnDate", "Formatnya : " + tgl);

            }
        }
    }


    public ArrayList<DataJurnal> selectModalAwal(int bulanDipilih, int tahunDipilih) {
        ArrayList<DataJurnal> dataJurnals = new ArrayList<>();

        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);
        String querySelect = "SELECT jurnal.tgl, nominal_kredit\n" +
                "FROM jurnal\n" +
                "INNER JOIN akun ON jurnal.akun_kredit = akun.kode_akun\n" +
                "WHERE akun.jenis = 4";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        DataJurnal dataJurnal;

        if (cursor != null){
            while (cursor.moveToNext()){

                String tgl = formatter(cursor.getString(0));
                long nominal_kredit = cursor.getLong(1);

                Log.i("ModalBlnLalu", "Datane : tanggal : " + tgl + ", Nominal : " + nominal_kredit );

                String[] splitTgl = tgl.split("/");

                dataJurnal = new DataJurnal();
                if (splitTgl[0].equals("01") && splitTgl[1].equals(bulan) && splitTgl[2].equals(tahun)){

                    dataJurnal.setTgl(tgl);
                    dataJurnal.setNominalKredit(nominal_kredit);
                    dataJurnals.add(dataJurnal);

                }
            }
        }

        String querySelectModalBulanKemarin = "SELECT tgl, nominal\n" +
                "FROM modal";

        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor1 = db1.rawQuery(querySelectModalBulanKemarin, null);

//        DataJurnal dataJurnal;

        if (cursor != null){
            while (cursor1.moveToNext()){

                String tgl = formatter(cursor1.getString(0));
                long nominal_kredit = cursor1.getLong(1);

                Log.i("Isi modal", "Datane : tanggal : " + tgl + ", Nominal : " + nominal_kredit );

                String[] splitTgl = tgl.split("/");

                dataJurnal = new DataJurnal();
                if (splitTgl[0].equals("01") && splitTgl[1].equals(bulan) && splitTgl[2].equals(tahun)){

                    dataJurnal.setTgl(tgl);
                    dataJurnal.setNominalKredit(nominal_kredit);
                    dataJurnals.add(dataJurnal);

                }
            }
        }

        return dataJurnals;
    }

    public ArrayList<DataJurnal> selectModalTambahan(int bulanDipilih, int tahunDipilih) {
        ArrayList<DataJurnal> dataJurnals = new ArrayList<>();
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);

        String querySelect = "SELECT jurnal.tgl, nominal_kredit\n" +
                "FROM jurnal\n" +
                "INNER JOIN akun ON jurnal.akun_kredit = akun.kode_akun\n" +
                "WHERE akun.jenis = 4";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        DataJurnal dataJurnal;

        if (cursor != null){
            while (cursor.moveToNext()){

                String tgl = formatter(cursor.getString(0));
                long nominal_kredit = cursor.getLong(1);

                Log.i("DBAdapterMix", "Datane : tanggal : " + tgl + ", Nominal : " + nominal_kredit );

                String[] splitTgl = tgl.split("/");


                dataJurnal = new DataJurnal();
                if (!splitTgl[0].equals("1") && splitTgl[1].equals(bulan) && splitTgl[2].equals(tahun)){

//                    Log.i("DBAdapterMix", "Masuk di hasil true");
                    dataJurnal.setTgl(tgl);
                    dataJurnal.setNominalKredit(nominal_kredit);
                    dataJurnals.add(dataJurnal);

                }
            }
        }
        return dataJurnals;
    }

    public ArrayList<DataSaldo> selectLabaRugi(int bulanDipilih, int tahunDipilih) {

        int saldoPendapatan = 0;
        int saldoBeban = 0;
        int labaRugi = 0;
        ArrayList<DataSaldo> dataSaldos = new ArrayList<>();
        DataSaldo dataSaldo;
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);

//        mencari data pendapatan
        String querySaldo = "SELECT riwayat_nominal.kode_akun, riwayat_nominal.nominal, riwayat_nominal.tgl\n" +
                "FROM riwayat_nominal\n" +
                "INNER JOIN akun ON riwayat_nominal.kode_akun = akun.kode_akun\n" +
                "WHERE akun.jenis = 5 OR akun.jenis = 6";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySaldo, null);

        if (cursor != null){
            while (cursor.moveToNext()){

//                mengambil data tanggal biar diparse
                String tgl = formatter(cursor.getString(2));

//                ngeparse tanggal
                String splitTgl[] = tgl.split("/");

//                ngecek, cuma data yang bertanggal seperti input yang boleh dimasukkan
                if (splitTgl[1].equals(bulan) && splitTgl[2].equals(tahun)){
                       saldoPendapatan += cursor.getInt(1);
                       Log.i("SaldoPendapatan", "akun : " + cursor.getString(0) + ", dengan nominal : " + cursor.getString(1) + ", ditambahkan pada " + cursor.getString(2));
                }
            }
        }
        Log.i("Pendapatane : ", String.valueOf(saldoPendapatan));
        db.close();

//        mencari data beban biaya
        String queryBeban = "SELECT riwayat_nominal.kode_akun, riwayat_nominal.nominal, riwayat_nominal.tgl\n" +
                "FROM riwayat_nominal\n" +
                "INNER JOIN akun ON riwayat_nominal.kode_akun = akun.kode_akun\n" +
                "WHERE akun.jenis = 7 OR akun.jenis = 8";
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor1 = db1.rawQuery(queryBeban, null);

        if (cursor1 != null){
            while (cursor1.moveToNext()){

//                mengambil data tanggal biar diparse
                String tgl = formatter(cursor1.getString(2));

//                ngeparse tanggal
                String splitTgl[] = tgl.split("/");

//                ngecek, cuma data yang bertanggal seperti input yang boleh dimasukkan
                if (splitTgl[1].equals(bulan) && splitTgl[2].equals(tahun)){
                    saldoBeban += cursor1.getInt(1);
                    Log.i("BebanBiaya", "akun : " + cursor1.getString(0) + ", dengan nominal : " + cursor1.getString(1) + ", ditambahkan pada " + cursor1.getString(2));
                }
            }
        }
        Log.i("Bebane : ", String.valueOf(saldoBeban));

//        Menghitung saldo laba
        labaRugi = saldoPendapatan - saldoBeban;
        Log.i("Labane :", String.valueOf(labaRugi));

        dataSaldo = new DataSaldo();
        dataSaldo.setNominal(labaRugi);
        dataSaldos.add(dataSaldo);

        return dataSaldos;
    }

    public ArrayList<DataSaldo> selectPriveBlnThn(int bulanDipilih, int tahunDipilih) {
        ArrayList<DataSaldo> dataSaldos = new ArrayList<>();
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);
        int prive = 0;

        String querySaldo = "SELECT riwayat_nominal.kode_akun, riwayat_nominal.nominal, riwayat_nominal.tgl\n" +
                "FROM riwayat_nominal\n" +
                "WHERE riwayat_nominal.kode_akun = 6101";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySaldo, null);

        DataSaldo dataSaldo;

        if (cursor != null){
            while (cursor.moveToNext()){

                Log.i("priveSelect","data : " + cursor.getString(0) + ", " + cursor.getString(1) + ", " + cursor.getString(2));

                String tgl = formatter(cursor.getString(2));
                String[] splitTgl = tgl.split("/");

                if (splitTgl[1].equals(bulan) && splitTgl[2].equals(tahun)){
                    prive += cursor.getInt(1);
                    Log.i("Prive", String.valueOf(prive));
                }

            }
        }

//                kirim ke laporan perubahan dengan lewat datasaldo
        dataSaldo = new DataSaldo();
        dataSaldo.setNominal(prive);

        dataSaldos.add(dataSaldo);

        return dataSaldos;
    }

    public ArrayList<DataSaldo> selectRiwayatJenisBlnThn(int i, int bulanDipilih, int tahunDipilih) {
        ArrayList<DataSaldo> dataSaldos = new ArrayList<>();
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);

        String querySaldo = "SELECT riwayat_nominal.kode_akun, akun.nama_akun,sum(nominal), akun.jenis, riwayat_nominal.tgl\n" +
                "FROM `riwayat_nominal` \n" +
                "INNER JOIN akun ON riwayat_nominal.kode_akun=akun.kode_akun\n" +
                "WHERE akun.jenis = " + i + "\n" +
                "GROUP BY riwayat_nominal.kode_akun";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySaldo, null);

        DataSaldo dataSaldo;

        if (cursor != null){
            while (cursor.moveToNext()){

                String tgl = formatter(cursor.getString(4));
                String[] splitTgl = tgl.split("/");

                if (splitTgl[1].equals(bulan) && splitTgl[2].equals(tahun)){
                    String kodeAkun = String.valueOf(cursor.getInt(0));
                    String namaAkun = cursor.getString(1);
                    long nominal = cursor.getLong(2);
                    int jenis = cursor.getInt(3);

                    dataSaldo = new DataSaldo();
                    dataSaldo.setKodeAkun(kodeAkun);
                    dataSaldo.setNamaAkun(namaAkun);
                    dataSaldo.setNominal(nominal);
                    dataSaldo.setJenis(jenis);

                    dataSaldos.add(dataSaldo);
                }


            }
        }
        return dataSaldos;
    }

//    digunakan untuk memasukkan data modal bulan depan
    public void insertModal(DataModal dataModal){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "INSERT OR REPLACE INTO modal('tgl','nominal','nominalKas') VALUES ('" + dataModal.getTgl() + "','" + dataModal.getNominal() + "','" + dataModal.getNominalKas() + "' )";
        db.execSQL(query);

        db.close();

    }

//    digunakan untuk memasukkan data kas bulan depan
    public void insertKas(DataModal dataModal){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "INSERT OR REPLACE INTO modal('tgl','nominalKas') VALUES ('" + dataModal.getTgl() + "'," + dataModal.getNominalKas() + " )";
        db.execSQL(query);

        db.close();

    }

    public ArrayList<DataSaldo> selectModalNeraca(int bulanDipilih, int tahunDipilih) {
        ArrayList<DataSaldo> dataSaldos = new ArrayList<>();

        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);
        String querySelect = "SELECT tgl, nominal FROM modal";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        DataSaldo dataSaldo;

        if (cursor != null){
            while (cursor.moveToNext()){

                String tgl = formatter(cursor.getString(0));
                long nominal_kredit = cursor.getLong(1);

                Log.i("NeracaModalPemilik", "Datane : tanggal : " + tgl + ", Nominal : " + nominal_kredit );

                String[] splitTgl = tgl.split("/");

//                dibikin begini karena inputnya modal buat bulan depan
                int custBulan = Integer.parseInt(splitTgl[1]) - 1;
                String custBulanStr = String.format("%02d", custBulan);
                Log.i("hasilCustBulan ", "custBulanInt : " + custBulan + "custBulanStr0 : " + custBulanStr);

                dataSaldo = new DataSaldo();
                if (custBulanStr.equals(bulan) && splitTgl[2].equals(tahun)){

                    dataSaldo.setTgl(tgl);
                    dataSaldo.setNominal(nominal_kredit);
                    dataSaldos.add(dataSaldo);

                }
            }
        }

        return dataSaldos;
    }

    public ArrayList<DataJurnal> selectArusKasOnDebet(int bulanDipilih, int tahunDipilih, int kodeAkun) {
        ArrayList<DataJurnal> dataJurnals = new ArrayList<>();
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);



        String querySelect = "SELECT jurnal.tgl, jurnal.akun_kredit, jurnal.nama_kredit, jurnal.nominal_kredit " +
                "FROM jurnal " +
                "INNER JOIN akun ON jurnal.akun_kredit = akun.kode_akun " +
                "WHERE jurnal.akun_debet = 1101 AND akun.jenis = " + kodeAkun;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        DataJurnal dataJurnal;

        if (cursor != null){
            while (cursor.moveToNext()){

                String tgl = formatter(cursor.getString(0));
                int sKodeAkun = cursor.getInt(1);
                String namaAkun = cursor.getString(2);
                int nominalkredit = cursor.getInt(3);

                Log.i("hasilArusKas : ", tgl + ", " + sKodeAkun + ", " + namaAkun + ", " + nominalkredit);

                String[] splitTgl = tgl.split("/");

                if (splitTgl[1].equals(bulan) && splitTgl[2].equals(tahun)){

                    dataJurnal = new DataJurnal();
                    dataJurnal.setTgl(tgl);
                    dataJurnal.setAkunKredit(sKodeAkun);
                    dataJurnal.setNamaKredit(namaAkun);
                    dataJurnal.setNominalKredit(nominalkredit);

                    dataJurnals.add(dataJurnal);

                }


            }
        }
        return dataJurnals;

    }

    public ArrayList<DataJurnal> selectArusKasOnKredit(int bulanDipilih, int tahunDipilih, int kodeAkun) {
        ArrayList<DataJurnal> dataJurnals = new ArrayList<>();
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);



        String querySelect = "SELECT jurnal.tgl, jurnal.akun_debet, jurnal.nama_debet, jurnal.nominal_debet " +
                "FROM jurnal " +
                "INNER JOIN akun ON jurnal.akun_debet = akun.kode_akun " +
                "WHERE jurnal.akun_kredit = 1101 AND akun.jenis = " + kodeAkun;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        DataJurnal dataJurnal;

        if (cursor != null){
            while (cursor.moveToNext()){

                String tgl = formatter(cursor.getString(0));
                int sKodeAkun = cursor.getInt(1);
                String namaAkun = cursor.getString(2);
                int nominalkredit = cursor.getInt(3);

                Log.i("untukArusKas : ", tgl + ", " + sKodeAkun + ", " + namaAkun + ", " + nominalkredit);

                String[] splitTgl = tgl.split("/");

                if (splitTgl[1].equals(bulan) && splitTgl[2].equals(tahun)){

                    dataJurnal = new DataJurnal();
                    dataJurnal.setTgl(tgl);
                    dataJurnal.setAkunKredit(sKodeAkun);
                    dataJurnal.setNamaKredit(namaAkun);
                    dataJurnal.setNominalKredit(nominalkredit);

                    dataJurnals.add(dataJurnal);

                }


            }
        }
        return dataJurnals;

    }

    public ArrayList<DataJurnal> selectKasAwal(int bulanDipilih, int tahunDipilih) {
        ArrayList<DataJurnal> dataJurnals = new ArrayList<>();

        String tahun = String.valueOf(tahunDipilih);

        String querySelect = "SELECT tgl,nominal\n" +
                "FROM riwayat_nominal\n" +
                "WHERE kode_akun = 1101";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        DataJurnal dataJurnal;
        int totalkas = 0;
        int i = 0;

        if (cursor != null){
            while (cursor.moveToNext()){

                String tgl = formatter(cursor.getString(0));
                int saldoKas = cursor.getInt(1);

                String[] splitTgl = tgl.split("/");

//                yang dicari itu bulan kemarin
                int bulanKmrn = bulanDipilih - 1;
                String bulan = String.format("%02d", bulanKmrn);

                if (splitTgl[1].equals(bulan) && splitTgl[2].equals(tahun)){
                    totalkas += saldoKas;
                }
            }
        }
        dataJurnal = new DataJurnal();
        dataJurnal.setNominalDebet(totalkas);
        dataJurnals.add(dataJurnal);
        Log.i("totalkas ", " " + totalkas);
        return dataJurnals;
    }

    public ArrayList<DataSaldo> selectRiwayat(int bulanDipilih, int tahunDipilih) {
        ArrayList<DataSaldo> dataSaldos = new ArrayList<>();
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);

        String querySaldo = "SELECT riwayat_nominal.kode_akun, akun.nama_akun, sum(riwayat_nominal.nominal), akun.jenis " +
                "FROM riwayat_nominal " +
                "INNER JOIN akun ON riwayat_nominal.kode_akun = akun.kode_akun " +
                "WHERE strftime('%m',riwayat_nominal.tgl) IN ('" + bulan + "') " +
                "AND strftime('%Y',riwayat_nominal.tgl) IN ('" + tahun +"') " +
                "GROUP BY riwayat_nominal.kode_akun;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySaldo, null);

        DataSaldo dataSaldo;

        if (cursor != null){
            while (cursor.moveToNext()){
                String kodeAkun = String.valueOf(cursor.getInt(0));
                String namaAkun = cursor.getString(1);
                long nominal = cursor.getLong(2);
                int jenis = cursor.getInt(3);

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

    public String formatter(String tanggalDbase){
//        Log.i("formatTglLama", tanggalDbase);
        Date oldFormatTgl = new Date();
        try {
            oldFormatTgl = new SimpleDateFormat("yyyy-MM-dd").parse(tanggalDbase);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatTglStor = new SimpleDateFormat("dd/MM/yyyy");
        String newFormatTgl = formatTglStor.format(oldFormatTgl);

//        Log.i("formatTglBaru", newFormatTgl);
        return newFormatTgl;
    }

    public void deleteJurnal(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("jurnal", "id=" + id, null);
        db.close();
    }

    public void updateJurnal(DataJurnal dataJurnal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ID_JURNAL,dataJurnal.getId());
//        cv.put(TGL_TRANS,dataJurnal.getTgl());
        cv.put(KETERANGAN, dataJurnal.getKeterangan());
//        cv.put(AKUN_DEBET, dataJurnal.getAkunDebet());
//        cv.put(NAMA_DEBET, dataJurnal.getNamaDebet());
//        cv.put(AKUN_KREDIT, dataJurnal.getAkunKredit());
//        cv.put(NAMA_KREDIT, dataJurnal.getNamaKredit());
        cv.put(NOMINAL_DEBET, dataJurnal.getNominalDebet());
        cv.put(NOMINAL_KREDIT, dataJurnal.getNominalKredit());

        db.update(TABLE_JURNAL,cv,"id=" + dataJurnal.getId(), null);
        db.close();
    }

    public void updateRiwayatSaldo(DataSaldo dataSaldo, int idDebet) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(NOMINAL,dataSaldo.getNominal());

        db.update(TABLE_RIWAYAT_NOMINAL, cv, "id = " + idDebet, null);
    }

    public void deleteRiwayat(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_RIWAYAT_NOMINAL, "id=" + id, null);
        db.close();
    }

    public ArrayList<DataSaldo> selectArusKasMar2(int bulanDipilih, int tahunDipilih, int pilihan) {
        ArrayList<DataSaldo> dataSaldos = new ArrayList<>();

        String bulan = String.format("%02d", bulanDipilih);
        String bulanLalu = String.format("%02d", bulanDipilih -1);
        String tahun = String.valueOf(tahunDipilih);
        String tahunLalu = String.valueOf(tahunDipilih - 1);

        String querySelect;
        if (pilihan == 0){
//            querySelect = "SELECT trans.kode_akun, akun.nama_akun, sum(trans.nominal) as nominal, akun.jenis, strftime(\"%m-%Y\", jurnal.tgl) as 'month'\n" +
//                    "FROM trans\n" +
//                    "LEFT JOIN jurnal ON trans.pid = jurnal.pid\n" +
//                    "LEFT JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
//                    "WHERE (strftime('%m',jurnal.tgl) = '" + bulanLalu + "' OR strftime('%m',jurnal.tgl) = '" + bulan + "')\n" +
//                    "AND (strftime('%Y',jurnal.tgl) = '" + tahunLalu + "' OR strftime('%Y',jurnal.tgl) = '" + tahun + "')\n" +
//                    "AND jurnal.ket != 'Neraca Awal'\n" +
//                    "AND (akun.jenis = 0 OR akun.jenis = 2)\n" +
//                    "GROUP BY akun.kode_akun, strftime(\"%m-%Y\", jurnal.tgl)" +
//                    "ORDER BY trans.kode_akun, month ASC ;";
            querySelect = "SELECT trans.kode_akun, akun.nama_akun, sum(trans.nominal) as nominal, akun.jenis\n" +
                    "FROM trans\n" +
                    "LEFT JOIN jurnal ON trans.pid = jurnal.pid\n" +
                    "LEFT JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                    "WHERE strftime('%m',jurnal.tgl) <= '" + bulanLalu + "'\n" +
                    "AND (akun.jenis = 0 OR akun.jenis = 2)\n" +
                    "AND strftime('%Y',jurnal.tgl) <= '" + tahun + "'\n" +
                    "GROUP BY akun.kode_akun\n" +
                    "UNION ALL\n" +
                    "SELECT trans.kode_akun, akun.nama_akun, sum(trans.nominal) as nominal, akun.jenis\n" +
                    "FROM trans\n" +
                    "LEFT JOIN jurnal ON trans.pid = jurnal.pid\n" +
                    "LEFT JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                    "WHERE strftime('%m',jurnal.tgl) <= '" + bulan + "'\n" +
                    "AND (akun.jenis = 0 OR akun.jenis = 2)\n" +
                    "AND strftime('%Y',jurnal.tgl) <= '" + tahun + "'\n" +
                    "GROUP BY akun.kode_akun\n" +
                    "ORDER BY trans.kode_akun;";
        }else if (pilihan == 1){
//            querySelect = "SELECT trans.kode_akun, akun.nama_akun, sum(trans.nominal) as nominal, akun.jenis, strftime(\"%m-%Y\", jurnal.tgl) as 'month'\n" +
//                    "FROM trans\n" +
//                    "LEFT JOIN jurnal ON trans.pid = jurnal.pid\n" +
//                    "LEFT JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
//                    "WHERE (strftime('%m',jurnal.tgl) = '" + bulanLalu + "' OR strftime('%m',jurnal.tgl) = '" + bulan + "')\n" +
//                    "AND (strftime('%Y',jurnal.tgl) = '" + tahunLalu + "' OR strftime('%Y',jurnal.tgl) = '" + tahun + "')\n" +
//                    "AND jurnal.ket != 'Neraca Awal'\n" +
//                    "AND (akun.jenis = 1 OR akun.jenis = 10)\n" +
//                    "GROUP BY akun.kode_akun, strftime(\"%m-%Y\", jurnal.tgl)" +
//                    "ORDER BY trans.kode_akun, month ASC ;";
            querySelect = "SELECT trans.kode_akun, akun.nama_akun, sum(trans.nominal) as nominal, akun.jenis\n" +
                    "FROM trans\n" +
                    "LEFT JOIN jurnal ON trans.pid = jurnal.pid\n" +
                    "LEFT JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                    "WHERE strftime('%m',jurnal.tgl) <= '" + bulanLalu + "'\n" +
                    "AND (akun.jenis = 1 OR akun.jenis = 10)\n" +
                    "AND strftime('%Y',jurnal.tgl) <= '" + tahun + "'\n" +
                    "GROUP BY akun.kode_akun\n" +
                    "UNION ALL\n" +
                    "SELECT trans.kode_akun, akun.nama_akun, sum(trans.nominal) as nominal, akun.jenis\n" +
                    "FROM trans\n" +
                    "LEFT JOIN jurnal ON trans.pid = jurnal.pid\n" +
                    "LEFT JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                    "WHERE strftime('%m',jurnal.tgl) <= '" + bulan + "'\n" +
                    "AND (akun.jenis = 1 OR akun.jenis = 10)\n" +
                    "AND strftime('%Y',jurnal.tgl) <= '" + tahun + "'\n" +
                    "GROUP BY akun.kode_akun\n" +
                    "ORDER BY trans.kode_akun;";
        }else {
//            querySelect = "SELECT trans.kode_akun, akun.nama_akun, sum(trans.nominal) as nominal, akun.jenis, strftime(\"%m-%Y\", jurnal.tgl) as 'month'\n" +
//                    "FROM trans\n" +
//                    "LEFT JOIN jurnal ON trans.pid = jurnal.pid\n" +
//                    "LEFT JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
//                    "WHERE (strftime('%m',jurnal.tgl) = '" + bulanLalu + "' OR strftime('%m',jurnal.tgl) = '" + bulan + "')\n" +
//                    "AND (strftime('%Y',jurnal.tgl) = '" + tahunLalu + "' OR strftime('%Y',jurnal.tgl) = '" + tahun + "')\n" +
//                    "AND jurnal.ket != 'Neraca Awal'\n" +
//                    "AND (akun.jenis = 3)\n" +
//                    "GROUP BY akun.kode_akun, strftime(\"%m-%Y\", jurnal.tgl)" +
//                    "ORDER BY trans.kode_akun, month ASC ;";
            querySelect = "SELECT trans.kode_akun, akun.nama_akun, sum(trans.nominal) as nominal, akun.jenis\n" +
                    "FROM trans\n" +
                    "LEFT JOIN jurnal ON trans.pid = jurnal.pid\n" +
                    "LEFT JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                    "WHERE strftime('%m',jurnal.tgl) <= '" + bulanLalu + "'\n" +
                    "AND (akun.jenis = 3)\n" +
                    "AND strftime('%Y',jurnal.tgl) <= '" + tahun + "'\n" +
                    "GROUP BY akun.kode_akun\n" +
                    "UNION ALL\n" +
                    "SELECT trans.kode_akun, akun.nama_akun, sum(trans.nominal) as nominal, akun.jenis\n" +
                    "FROM trans\n" +
                    "LEFT JOIN jurnal ON trans.pid = jurnal.pid\n" +
                    "LEFT JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                    "WHERE strftime('%m',jurnal.tgl) <= '" + bulan + "'\n" +
                    "AND (akun.jenis = 3)\n" +
                    "AND strftime('%Y',jurnal.tgl) <= '" + tahun + "'\n" +
                    "GROUP BY akun.kode_akun\n" +
                    "ORDER BY trans.kode_akun;";
        }



        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);
        Cursor cursorNext = db.rawQuery(querySelect, null);

        DataSaldo dataSaldo;

        if (cursor != null){
            while (cursor.moveToNext()){


                dataSaldo = new DataSaldo();

//                mengintip next data
                if (cursor.getInt(2) != 0){
                    if (!cursor.isLast()){
                        cursorNext.moveToPosition(cursor.getPosition() + 1);
                    }else {
                        int nominal = Math.abs(cursor.getInt(2)) - 0;
                        Log.i("ArusKas2", cursor.getString(1) + ",nominalnya: " + nominal);
                        dataSaldo.setNamaAkun(cursor.getString(1));
                        dataSaldo.setNominal(nominal);
                        dataSaldo.setJenis(cursor.getInt(3));
                        dataSaldos.add(dataSaldo);
                        continue;
                    }

                    if (cursor.getInt(0) == cursorNext.getInt(0)){
                        int nominal = Math.abs(cursorNext.getInt(2)) - Math.abs(cursor.getInt(2));
                        Log.i("ArusKas2", cursor.getString(1) + ",nominalnya: " + nominal);
                        dataSaldo.setNamaAkun(cursor.getString(1));
                        dataSaldo.setNominal(nominal);
                        dataSaldo.setJenis(cursor.getInt(3));
                        dataSaldos.add(dataSaldo);
                        cursor.moveToNext();
                    }else if (cursor.getInt(0) != cursorNext.getInt(0)){
                        int nominal = Math.abs(cursor.getInt(2)) - 0;
                        Log.i("ArusKas2", cursor.getString(1) + ",nominalnya: " + nominal);
                        dataSaldo.setNamaAkun(cursor.getString(1));
                        dataSaldo.setJenis(cursor.getInt(3));
                        dataSaldo.setNominal(nominal);
                        dataSaldos.add(dataSaldo);
                    }else {
                        int nominal = Math.abs(cursor.getInt(2)) - 0;
                        Log.i("ArusKas2", cursor.getString(1) + ",nominalnya: " + nominal);
                        dataSaldo.setNamaAkun(cursor.getString(1));
                        dataSaldo.setNominal(nominal);
                        dataSaldos.add(dataSaldo);
                    }
                }
            }
        }
        return dataSaldos;
    }

    public int selectSumLabaRugiMar(int bulanDipilih, int tahunDipilih) {

        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);

        String querySelect = "SELECT * FROM \n" +
                "(SELECT sum(trans.nominal)\n" +
                "FROM trans\n" +
                "LEFT JOIN jurnal ON trans.pid = jurnal.pid\n" +
                "LEFT JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE strftime('%m',jurnal.tgl) = '" + bulan + "'\n" +
                "AND strftime('%Y',jurnal.tgl) = '" + tahun + "'\n" +
                "AND (akun.jenis = 5 OR akun.jenis = 6)),\n" +
                "(SELECT sum(trans.nominal)\n" +
                "FROM trans\n" +
                "LEFT JOIN jurnal ON trans.pid = jurnal.pid\n" +
                "LEFT JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE strftime('%m',jurnal.tgl) = '" + bulan + "'\n" +
                "AND strftime('%Y',jurnal.tgl) = '" + tahun + "'\n" +
                "AND (akun.jenis = 7 OR akun.jenis = 8));";


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        int laba = 0;

        if (cursor != null){
            while (cursor.moveToNext()){

                laba = cursor.getInt(0) - cursor.getInt(1);
                Log.i("ArusKas2", "labanya: " + laba);

            }
        }

        return laba;
    }

    public int selectModalPemilikArusKasMar(int bulanDipilih, int tahunDipilih) {
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);
        String bulanDepan = String.format("%02d", bulanDipilih + 1);

        int modalPemilik = 0;

//        select modal pemilik bulan lalu (yang merupakan modal bulan depan)
        String querySelectModalBulanIni = "SELECT tgl, nominal FROM modal WHERE strftime('%m', tgl) = '" + bulanDepan + "' AND strftime('%Y', tgl) = '" + tahun + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelectModalBulanIni, null);

        int modalBulanLalu = 0;

        if (cursor != null){
            while (cursor.moveToNext()){
                modalBulanLalu += cursor.getInt(1);
//                Log.i("ArusKas23", "Modal bulan lalu pada:" + cursor.getString(0) + ", sebesar:" + cursor.getInt(1));
            }
        }

        db.close();

        int modalBulanIni = 0;

        String querySelectModalAwal = "SELECT tgl, nominal FROM modal WHERE strftime('%m', tgl) = '" + bulan + "' AND strftime('%Y', tgl) = '" + tahun + "'";

        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor1 = db1.rawQuery(querySelectModalAwal, null);

        if (cursor1 != null){
            while (cursor1.moveToNext()){
                modalBulanIni += cursor1.getInt(1);
//                Log.i("ArusKas23", "Modal bulan ini pada:" + cursor.getString(0) + ", sebesar:" + cursor.getInt(1));
            }
        }

        db1.close();

        String querySelectTambahanModal = "SELECT jurnal.tgl, trans.nominal\n" +
                "FROM trans\n" +
                "INNER JOIN jurnal ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE strftime('%d', jurnal.tgl) != '01' AND strftime('%m',jurnal.tgl) = '" + bulan + "' AND strftime('%Y',jurnal.tgl) = '" + tahun + "' AND akun.jenis = '4';";

        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor2 = db2.rawQuery(querySelectTambahanModal, null);

        if (cursor2 != null){
            while (cursor2.moveToNext()){
                modalBulanIni += cursor2.getInt(1);
//                Log.i("ArusKas23", "Modal tambahan pada:" + cursor.getString(0) + ", sebesar:" + cursor.getInt(1));
            }
        }

        db2.close();

        modalPemilik = modalBulanLalu - modalBulanIni;
        Log.i("ArusKas23", "hasil modal Pemilik: " + modalPemilik);
        return modalPemilik;
    }

    public int selectPriveMar(int bulanDipilih, int tahunDipilih) {
        ArrayList<DataSaldo> dataSaldos = new ArrayList<>();
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);
        int prive = 0;

        String querySaldo = "SELECT trans.kode_akun, trans.nominal, jurnal.tgl\n" +
                "FROM trans\n" +
                "INNER JOIN jurnal ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE strftime('%m',jurnal.tgl) = '" + bulan + "' \n" +
                "AND strftime('%Y',jurnal.tgl) = '" + tahun + "' \n" +
                "AND akun.kode_akun = '6101';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySaldo, null);

        DataSaldo dataSaldo;

        if (cursor != null){
            while (cursor.moveToNext()){
                prive += cursor.getInt(1);
                Log.i("Prive", String.valueOf(prive));

            }
        }
        return prive;
    }

    public ArrayList<DataSaldo> selectRiwayatJenisBlnThnNoKasMar(int i, int bulanDipilih, int tahunDipilih) {
        ArrayList<DataSaldo> dataSaldos = new ArrayList<DataSaldo>();
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);

        String querySaldo = "SELECT trans.kode_akun, akun.nama_akun, sum(trans.nominal), akun.jenis\n" +
                "FROM trans\n" +
                "INNER JOIN jurnal ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE strftime('%m',jurnal.tgl) <= '" + bulan +"' AND trans.kode_akun != '1101' AND strftime('%Y',jurnal.tgl) <= '" + tahun + "' AND akun.jenis = '" + i + "'\n" +
                "GROUP BY trans.kode_akun;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySaldo, null);

        DataSaldo dataSaldo;

        if (cursor != null){
            while (cursor.moveToNext()){

                String kodeAkun = String.valueOf(cursor.getInt(0));
                String namaAkun = cursor.getString(1);
                long nominal = cursor.getLong(2);
                int jenis = cursor.getInt(3);

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

    public int selecModalKasNeraca(int bulanDipilih, int tahunDipilih) {
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);
        String querySelect = "SELECT * FROM modal WHERE strftime('%m', tgl) = '" + bulan + "' AND strftime('%Y', tgl) = '" + tahun + "'";

        int modalkas = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        if (cursor != null){
            while (cursor.moveToNext()){
                modalkas += cursor.getInt(1);
                Log.i("kasAkumulasi", "nilainya : " + String.valueOf(cursor.getInt(2)) + ", tambah:" + String.valueOf(cursor.getInt(1)));
            }
        }
        db.close();
        cursor.close();

        String querySelectKas = "SELECT sum(trans.nominal)\n" +
                "FROM trans\n" +
                "INNER JOIN jurnal ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE strftime('%m',jurnal.tgl) = '" + bulan + "' AND strftime('%Y',jurnal.tgl) = '" + tahun + "' AND akun.kode_akun = '1101'\n" +
                "GROUP BY trans.kode_akun;";

        db = this.getReadableDatabase();
        cursor = db.rawQuery(querySelectKas, null);

        if (cursor != null){
            while (cursor.moveToNext()){
                modalkas += cursor.getInt(0);
                Log.i("kasAkumulasi", "nilainya: " + String.valueOf(cursor.getInt(0)));
            }
        }

        return modalkas;
    }

    public int selectKas(int bulanDipilih, int tahunDipilih) {
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);
        String querySelect = "SELECT sum(trans.nominal)\n" +
                "FROM trans\n" +
                "INNER JOIN jurnal ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE strftime('%m',jurnal.tgl) = '" + bulan + "' AND strftime('%Y',jurnal.tgl) = '" + tahun + "' AND akun.kode_akun = '1101'\n" +
                "GROUP BY trans.kode_akun;";

        int kas = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        if (cursor != null){
            while (cursor.moveToNext()){
                kas += cursor.getInt(0);
                Log.i("kasAkumulasi", "nilainya: " + String.valueOf(cursor.getInt(0)));
            }
        }
        return kas;
    }

    public boolean isFirstMonth(int bulanDipilih, int tahunDipilih) {
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);
        String querySelect = "SELECT strftime('%m', tgl) as bln, strftime('%Y', tgl) as thn " +
                "FROM jurnal " +
                "ORDER BY bln ASC, thn ASC;";

        boolean first = false;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        if (cursor != null){
            while (cursor.moveToNext()){
                if (cursor.getString(1).equals(tahun) && cursor.getString(0).equals(bulan)){
                    first = true;
                }
                break;
            }
        }
        return first;
    }

    public ArrayList<DataSaldo> selectNeracaSaldoMar(int bulanDipilih, int tahunDipilih) {
        ArrayList<DataSaldo> dataSaldos = new ArrayList<DataSaldo>();
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);

        String querySaldo = "SELECT trans.kode_akun, akun.nama_akun, sum(trans.nominal), akun.jenis\n" +
                "FROM trans\n" +
                "INNER JOIN jurnal ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE (akun.jenis = 4 OR akun.jenis = 5 OR akun.jenis = 6 OR akun.jenis = 7 OR akun.jenis = 8 OR akun.jenis = 9) AND " +
                "strftime('%m',jurnal.tgl) = '" + bulan + "' AND strftime('%Y',jurnal.tgl) = '" + tahun + "'\n" +
                "GROUP BY trans.kode_akun;";

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
//                System.out.println("Data yang diambil : " + kodeAkun);

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

    public int selectModalBulanIni(int bulanDipilih, int tahunDipilih) {
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);
        String querySelect = "SELECT * FROM modal WHERE strftime('%m', tgl) = '" + bulan + "' AND strftime('%Y', tgl) = '" + tahun + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        int modal = 0;

        if (cursor != null){
            while (cursor.moveToNext()){
                modal = cursor.getInt(1);
            }
        }
        return modal;
    }

    public int selectTambahanModalBulanIniMar(int bulanDipilih, int tahunDipilih) {
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);

        String querySaldo = "SELECT trans.kode_akun, akun.nama_akun, sum(trans.nominal), akun.jenis\n" +
                "FROM trans\n" +
                "INNER JOIN jurnal ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE (akun.jenis = 4) AND " +
                "strftime('%m',jurnal.tgl) = '" + bulan + "' AND strftime('%Y',jurnal.tgl) = '" + tahun + "'\n" +
                "GROUP BY trans.kode_akun;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySaldo, null);

        int modalTambahan = 0;

        if (cursor != null){
            while (cursor.moveToNext()){
                modalTambahan += cursor.getInt(2);
            }
        }
        return modalTambahan;

    }

//    digunakan untuk melakukan update modal agar tidak perlu membuka laporan ekuitas
    public void updateModal(int bulanDipilih, int tahunDipilih) {
        String bulan = String.format("%02d", bulanDipilih);
        String bulanDepan = String.format("%02d", bulanDipilih + 1);
        String tahun = String.valueOf(tahunDipilih);
//        modal bulan depan = modal awal bulan ini  + tambahan modal + labarugi - prive
//        modal awal = modal bulan kemarin
//        tambahan modal = transaksi pemasukan modal bulan ini
//        laba rugi = pendapatan bulan ini - biaya bulan ini
//        prive = prive bulan ini
//        input dari parameter, ketika pilih bulan maret, outputnya keluar 2, tahun tetep 2018
//        kalo bulannya april, keluarnya angka 3
        int modalAkhirBulanIni = 0;
        int modalAwal = 0;
        int tambahanModal = 0;
        int pendapatan = 0;
        int biaya = 0;
        int prive = 0;
        int kas = 0;

//        mengambil data modal awal
        String querySelectModalBulanIni = "SELECT *\n" +
                "FROM modal\n" +
                "WHERE strftime('%m',tgl) = '" + bulan + "' AND strftime('%Y',tgl) = '" + tahun + "';";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySelectModalBulanIni, null);

        if (cursor != null){
            while (cursor.moveToNext()){
                modalAwal += cursor.getInt(1);
            }
        }
        db.close();

//        mengambil data modal tambahan
        String querySelectModalTambahan = "SELECT jurnal.tgl, trans.nominal\n" +
                "FROM trans\n" +
                "INNER JOIN jurnal ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE strftime('%m',jurnal.tgl) = '" + bulan + "' AND strftime('%Y',jurnal.tgl) = '" + tahun + "' AND akun.jenis = '4';";

        db = this.getReadableDatabase();
        cursor = db.rawQuery(querySelectModalTambahan, null);

        if (cursor != null){
            while (cursor.moveToNext()){
                tambahanModal += cursor.getInt(1);
            }
        }
        db.close();

//        mengambil data  pendapatan
        String querySelectPendapatan = "SELECT trans.kode_akun, trans.nominal, jurnal.tgl\n" +
                "FROM trans\n" +
                "INNER JOIN jurnal ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE strftime('%m',jurnal.tgl) = '" + bulan + "' \n" +
                "AND strftime('%Y',jurnal.tgl) = '" + tahun + "' \n" +
                "AND (akun.jenis = '5' OR akun.jenis = '6');";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(querySelectPendapatan, null);

        if (cursor != null){
            while (cursor.moveToNext()){
                pendapatan += cursor.getInt(1);
            }
        }
        db.close();

//        mengambil data beban biaya
        String querySelectBiaya = "SELECT trans.kode_akun, trans.nominal, jurnal.tgl\n" +
                "FROM trans\n" +
                "INNER JOIN jurnal ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE strftime('%m',jurnal.tgl) = '" + bulan + "' \n" +
                "AND strftime('%Y',jurnal.tgl) = '" + tahun + "' \n" +
                "AND (akun.jenis = '7' OR akun.jenis = '8');";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(querySelectBiaya, null);

        if (cursor != null){
            while (cursor.moveToNext()){
                biaya += cursor.getInt(1);
            }
        }
        db.close();

//        mengambil data prive
        String querySelectPrive = "SELECT trans.kode_akun, trans.nominal, jurnal.tgl\n" +
                "FROM trans\n" +
                "INNER JOIN jurnal ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE strftime('%m',jurnal.tgl) = '" + bulan + "' \n" +
                "AND strftime('%Y',jurnal.tgl) = '" + tahun + "' \n" +
                "AND akun.kode_akun = '6101';";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(querySelectPrive, null);

        if (cursor != null){
            while (cursor.moveToNext()){
                prive += cursor.getInt(1);
            }
        }
        db.close();

//        mengambil data modal kas sekarang
        String querySelectkas = "SELECT sum(trans.nominal)\n" +
                "FROM trans\n" +
                "INNER JOIN jurnal ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE strftime('%m',jurnal.tgl) = '" + bulan + "' AND strftime('%Y',jurnal.tgl) = '" + tahun + "' AND akun.kode_akun = '1101'\n" +
                "GROUP BY trans.kode_akun;";


        db = this.getReadableDatabase();
        cursor = db.rawQuery(querySelectkas, null);

        if (cursor != null){
            while (cursor.moveToNext()){
                kas += cursor.getInt(0);
            }
        }
        db.close();

        modalAkhirBulanIni = modalAwal + tambahanModal + (pendapatan - biaya) - prive;

//        memasukkan data modal akhir bulan ini untuk modal awal bulan depan
        String tgl = tahun + "-" + bulanDepan + "-01";
        SQLiteDatabase db1 = this.getWritableDatabase();

        String query = "INSERT OR REPLACE INTO modal('tgl','nominal','nominalKas') VALUES ('" + tgl + "','" + modalAkhirBulanIni + "','" + kas + "' )";
        db1.execSQL(query);
        db1.close();

        Log.i("updateModal", "Bulan input:" + bulanDipilih + ",Tahun input:" + tahunDipilih);
        Log.i("updateModal", "Bulan:" + bulan + " ,Tahun:" + tahun + " ,Modal Akhirnya:" + modalAkhirBulanIni);
        Log.i("updateModal", "Modal Sudah diupdate");

    }

//    mengambil semua data bulan
    public ArrayList<DataModal> selectDistinctBulan() {
        ArrayList<DataModal> dataBulanModals = new ArrayList<DataModal>();

        String selectQuery = "SELECT DISTINCT strftime('%m',tgl) FROM jurnal;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        DataModal dataModal;

        if (cursor != null){
            while (cursor.moveToNext()){

                dataModal = new DataModal();
                String bulan = cursor.getString(0);
                dataModal.setTgl(bulan);
                dataBulanModals.add(dataModal);
                Log.i("updateModal", "Hasil Distinct:" + cursor.getString(0));

            }
        }
        return dataBulanModals;
    }

    public ArrayList<DataSaldo> selectRiwayatJenisBlnThnMarLabaRugi(int i, int bulanDipilih, int tahunDipilih) {
        ArrayList<DataSaldo> dataSaldos = new ArrayList<DataSaldo>();
        String bulan = String.format("%02d", bulanDipilih);
        String tahun = String.valueOf(tahunDipilih);

        String querySaldo = "SELECT trans.kode_akun, akun.nama_akun, sum(trans.nominal), akun.jenis\n" +
                "FROM trans\n" +
                "INNER JOIN jurnal ON jurnal.pid = trans.pid\n" +
                "INNER JOIN akun ON trans.kode_akun = akun.kode_akun\n" +
                "WHERE strftime('%m',jurnal.tgl) = '" + bulan +"' AND strftime('%Y',jurnal.tgl) = '" + tahun + "' AND akun.jenis = '" + i + "'\n" +
                "GROUP BY trans.kode_akun;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querySaldo, null);

        DataSaldo dataSaldo;

        if (cursor != null){
            while (cursor.moveToNext()){

                String kodeAkun = String.valueOf(cursor.getInt(0));
                String namaAkun = cursor.getString(1);
                long nominal = cursor.getLong(2);
                int jenis = cursor.getInt(3);

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