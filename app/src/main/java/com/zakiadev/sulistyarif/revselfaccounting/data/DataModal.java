package com.zakiadev.sulistyarif.revselfaccounting.data;

/**
 * Created by sulistyarif on 23/02/18.
 */

public class DataModal {
    String tgl;

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    int nominal;

    public int getNominalKas() {
        return nominalKas;
    }

    public void setNominalKas(int nominalKas) {
        this.nominalKas = nominalKas;
    }

    int nominalKas;
}
