package com.zakiadev.sulistyarif.revselfaccounting.data;

import java.util.Date;

/**
 * Created by Sulistyarif on 01/02/2018.
 */

public class DataJurnal {
    String keterangan;
    String tgl;
    String namaDebet;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public String getNamaDebet() {
        return namaDebet;
    }

    public void setNamaDebet(String namaDebet) {
        this.namaDebet = namaDebet;
    }

    public String getNamaKredit() {
        return namaKredit;
    }

    public void setNamaKredit(String namaKredit) {
        this.namaKredit = namaKredit;
    }

    String namaKredit;
    long nominalDebet, nominalKredit;
    int akunDebet, akunKredit;

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public long getNominalDebet() {
        return nominalDebet;
    }

    public void setNominalDebet(long nominalDebet) {
        this.nominalDebet = nominalDebet;
    }

    public long getNominalKredit() {
        return nominalKredit;
    }

    public void setNominalKredit(long nominalKredit) {
        this.nominalKredit = nominalKredit;
    }

    public int getAkunDebet() {
        return akunDebet;
    }

    public void setAkunDebet(int akunDebet) {
        this.akunDebet = akunDebet;
    }

    public int getAkunKredit() {
        return akunKredit;
    }

    public void setAkunKredit(int akunKredit) {
        this.akunKredit = akunKredit;
    }



}
