package com.zakiadev.sulistyarif.revselfaccounting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zakiadev.sulistyarif.revselfaccounting.data.DataPerusahaan;
import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapterMix;

/**
 * Created by sulistyarif on 05/03/18.
 */

public class SettingDataPerusahaan extends AppCompatActivity {

    Button btEdit, btSimpan;
    EditText etNamaPers, etNamaPemilik, etAlamat, etTelp, etEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_data_perusahaan);

        btEdit = (Button)findViewById(R.id.btSettEdit);
        btSimpan = (Button)findViewById(R.id.btSettSimpan);

        etNamaPers = (EditText)findViewById(R.id.etSettNamaUsaha);
        etNamaPemilik = (EditText)findViewById(R.id.etSettPemilik);
        etAlamat = (EditText)findViewById(R.id.etSettAlamat);
        etTelp = (EditText)findViewById(R.id.etSettTelepon);
        etEmail = (EditText)findViewById(R.id.etSettEmail);

        DataPerusahaan dataPerusahaan = new DBAdapterMix(SettingDataPerusahaan.this).selectDataPerusahaan();

        etNamaPers.setText(dataPerusahaan.getNamaPers());
        etNamaPemilik.setText(dataPerusahaan.getNamaPemilik());
        etAlamat.setText(dataPerusahaan.getAlamat());
        etTelp.setText(dataPerusahaan.getTelp());
        etEmail.setText(dataPerusahaan.getEmail());

        btSimpan.setEnabled(false);
        etNamaPers.setEnabled(false);
        etNamaPemilik.setEnabled(false);
        etAlamat.setEnabled(false);
        etTelp.setEnabled(false);
        etEmail.setEnabled(false);

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btSimpan.setEnabled(true);
                btEdit.setEnabled(false);
                etNamaPers.setEnabled(true);
                etNamaPemilik.setEnabled(true);
                etAlamat.setEnabled(true);
                etTelp.setEnabled(true);
                etEmail.setEnabled(true);
            }
        });

        btSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etNamaPers.getText().toString().equals("") || etNamaPemilik.getText().toString().equals("")){
                    Toast.makeText(SettingDataPerusahaan.this, "Nama perusahaan atau Nama pemilik tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else {
                    DataPerusahaan dataPerusahaan1 = new DataPerusahaan();

                    dataPerusahaan1.setNamaPers(etNamaPers.getText().toString());
                    dataPerusahaan1.setNamaPemilik(etNamaPemilik.getText().toString());
                    dataPerusahaan1.setAlamat(etAlamat.getText().toString());
                    dataPerusahaan1.setTelp(etTelp.getText().toString());
                    dataPerusahaan1.setEmail(etEmail.getText().toString());

                    new DBAdapterMix(SettingDataPerusahaan.this).insertDataPerusahaan(dataPerusahaan1);
                }
                btSimpan.setEnabled(false);
                btEdit.setEnabled(true);
                etNamaPers.setEnabled(false);
                etNamaPemilik.setEnabled(false);
                etAlamat.setEnabled(false);
                etTelp.setEnabled(false);
                etEmail.setEnabled(false);
            }
        });

    }
}
