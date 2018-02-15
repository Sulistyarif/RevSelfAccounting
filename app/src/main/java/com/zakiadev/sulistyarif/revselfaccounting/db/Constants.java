package com.zakiadev.sulistyarif.revselfaccounting.db;

/**
 * Created by Sulistyarif on 01/02/2018.
 */

public class Constants {

    static final String DB_NAME = "revaccounting";
    static final int DB_VERSION = 1;

//    untuk data jurnal
    static final String TB_NAME = "jurnal";

    static final String ROW_ID = "id";
    static final String TGL_JUR = "tgl";
    static final String KETERANGAN = "keterangan";
    static final String DEBET = "debet";
    static final String KREDIT = "kredit";

//    sql query
    static final String CREATE_TB_JUR = "CREATE TABLE jurnal(id INTEGER PRIMARY KEY AUTOINCREMENT, tgl INTEGER NOT NULL, keterangan TEXT NOT NULL, debet INTEGER, kredit INTEGER)";
    static final String DROP_TB = "DROP TABLE IF EXIST " + TB_NAME;

}
