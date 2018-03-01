package com.zakiadev.sulistyarif.revselfaccounting.data;

/**
 * Created by sulistyarif on 01/03/18.
 */

public class EditDataTransMar {
    String id, pid, kodeAkun, namaAkun;
    int nominal, pos, jenisAkun;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    public int getJenisAkun() {
        return jenisAkun;
    }

    public void setJenisAkun(int jenisAkun) {
        this.jenisAkun = jenisAkun;
    }
}
