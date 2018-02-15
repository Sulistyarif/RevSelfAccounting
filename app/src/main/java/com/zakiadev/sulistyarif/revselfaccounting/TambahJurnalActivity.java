package com.zakiadev.sulistyarif.revselfaccounting;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.zakiadev.sulistyarif.revselfaccounting.data.DataJurnal;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataSaldo;
import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapter;
import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapterMix;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Sulistyarif on 02/02/2018.
 */

public class TambahJurnalActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    Spinner spinner;
    EditText etNominalTrans, etKeterangan;
    Button btAddJurnal, btTgl, btDebet, btKredit;
    Calendar calendar;
    int pilihanTransaksi;
    String kodeDebet, namaDebet, jenisDebet;
    String kodeKredit, namaKredit, jenisKredit;
    int kodeDebetint, kodeKreditInt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_jurnal_activity);

        etKeterangan = (EditText)findViewById(R.id.etKeterangan);
        etNominalTrans = (EditText)findViewById(R.id.etNominalTrans);
        btDebet = (Button)findViewById(R.id.btPildeb);
        btKredit = (Button)findViewById(R.id.btPilKred);

        btTgl = (Button)findViewById(R.id.btTgl);
        btTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance(TimeZone.getDefault());
                DatePickerDialog dialog = new DatePickerDialog(TambahJurnalActivity.this, TambahJurnalActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        spinner = (Spinner)findViewById(R.id.spinner);
        List<String> listSpinner = new ArrayList<String>();
        listSpinner.add("Pilih Jenis Transaksi");
        listSpinner.add("Setoran Modal");
        listSpinner.add("Pembelian");
        listSpinner.add("Penjualan Aset/Jasa");
        listSpinner.add("Pinjaman dari Pihak Luar (Utang)");
        listSpinner.add("Pembayaran Biaya");
        listSpinner.add("Pengambilan untuk Pribadi");
        listSpinner.add("Barter");
        listSpinner.add("Penyesuaian");
        ArrayAdapter adapterSpinner = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, listSpinner);
        adapterSpinner.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        btAddJurnal = (Button)findViewById(R.id.btAddData);
        btAddJurnal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int nominalTransaksi = Integer.parseInt(etNominalTrans.getText().toString());

                DataJurnal dataJurnal = new DataJurnal();
                dataJurnal.setTgl(btTgl.getText().toString());
                dataJurnal.setKeterangan(etKeterangan.getText().toString());
                dataJurnal.setAkunDebet(Integer.parseInt(kodeDebet));
                dataJurnal.setAkunKredit(Integer.parseInt(kodeKredit));
                dataJurnal.setNominalDebet(nominalTransaksi);
                dataJurnal.setNominalKredit(nominalTransaksi);

                new DBAdapterMix(TambahJurnalActivity.this).insertJurnal(dataJurnal);
                Toast.makeText(TambahJurnalActivity.this, "Data Jurnal Telah Tersimpan",Toast.LENGTH_LONG).show();

                kodeDebetint = Integer.parseInt(kodeDebet);
                kodeKreditInt = Integer.parseInt(kodeKredit);

                if (kodeDebetint == 2 || kodeDebetint == 3 || kodeDebetint == 4 || kodeDebetint == 5 || kodeDebetint == 8 || kodeDebetint == 9){
                    nominalTransaksi = -nominalTransaksi;
                }else if (kodeKreditInt == 0 || kodeKreditInt == 1 || kodeKreditInt == 6 || kodeKreditInt == 7){
                    nominalTransaksi = -nominalTransaksi;
                }

                DataSaldo dataSaldo = new DataSaldo();
                dataSaldo.setKodeAkun(kodeDebet);
                dataSaldo.setNominal(nominalTransaksi);
                new DBAdapterMix(TambahJurnalActivity.this).insertRiwayatSaldo(dataSaldo);
                Toast.makeText(TambahJurnalActivity.this, "Data riwayat debet telah tersimpan",Toast.LENGTH_LONG).show();

                DataSaldo dataSaldo1 = new DataSaldo();
                dataSaldo1.setKodeAkun(kodeKredit);
                dataSaldo1.setNominal(nominalTransaksi);
                new DBAdapterMix(TambahJurnalActivity.this).insertRiwayatSaldo(dataSaldo1);
                Toast.makeText(TambahJurnalActivity.this, "Data riwayat kredit telah tersimpan",Toast.LENGTH_LONG).show();

                finish();

            }
        });
//          disegel karena masih ada error index out of bound
        btDebet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TambahJurnalActivity.this, PilihDebetActivity.class);
                i.putExtra("pilihan", pilihanTransaksi);
                startActivityForResult(i,1);
            }
        });

        btKredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TambahJurnalActivity.this, PilihKreditActivity.class);
                i.putExtra("pilihan", pilihanTransaksi);
                startActivityForResult(i,2);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pilihanTransaksi = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateButton();
    }

    private void updateButton() {
        String formatDate = "dd/MM/yyy";
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.US);

        btTgl.setText(sdf.format(calendar.getTime()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                kodeDebet = data.getStringExtra("kodeDebet");
                namaDebet = data.getStringExtra("namaDebet");
                jenisDebet = data.getStringExtra("jenisDebet");
                Toast.makeText(TambahJurnalActivity.this, kodeDebet + " " + namaDebet + " " + jenisDebet,Toast.LENGTH_SHORT).show();
                btDebet.setText(namaDebet);
            }
        } else if (requestCode == 2){
            if (resultCode == RESULT_OK){
                kodeKredit = data.getStringExtra("kodeKredit");
                namaKredit = data.getStringExtra("namaKredit");
                jenisKredit = data.getStringExtra("jenisKredit");
                Toast.makeText(TambahJurnalActivity.this, kodeKredit + " " + namaKredit + " " + jenisKredit,Toast.LENGTH_SHORT).show();
                btKredit.setText(namaKredit);
            }
        }
    }
}
