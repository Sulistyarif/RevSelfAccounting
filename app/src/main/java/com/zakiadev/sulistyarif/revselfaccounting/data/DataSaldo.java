package com.zakiadev.sulistyarif.revselfaccounting.data;

/**
 * Created by sulistyarif on 13/02/18.
 */

public class DataSaldo {
    String kodeAkun, namaAkun;
    long nominal;
    int jenis;

    public int getJenis() {
        return jenis;
    }

    public void setJenis(int jenis) {
        this.jenis = jenis;
    }

    public String getKodeAkun() {
        return kodeAkun;
    }

    public void setKodeAkun(String kodeAkun) {
        this.kodeAkun = kodeAkun;
    }

    public String getNamaAkun() {
        return namaAkun;
    }

    public void setNamaAkun(String namaAkun) {
        this.namaAkun = namaAkun;
    }

    public long getNominal() {
        return nominal;
    }

    public void setNominal(long nominal) {
        this.nominal = nominal;
    }
}
