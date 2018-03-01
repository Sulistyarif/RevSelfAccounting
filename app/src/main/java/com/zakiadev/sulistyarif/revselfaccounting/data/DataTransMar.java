package com.zakiadev.sulistyarif.revselfaccounting.data;

/**
 * Created by sulistyarif on 01/03/18.
 */

public class DataTransMar {
    String pid, kode_akun;
    int nominal, pos;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getKode_akun() {
        return kode_akun;
    }

    public void setKode_akun(String kode_akun) {
        this.kode_akun = kode_akun;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
