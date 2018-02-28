package com.zakiadev.sulistyarif.revselfaccounting;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class EditDataJurnalActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    Spinner spinner;
    EditText etNominalTrans, etKeterangan;
    Button btAddJurnal, btTgl, btDebet, btKredit;
    Calendar calendar;
    int pilihanTransaksi;
    String kodeDebet, namaDebet, jenisDebet;
    String kodeKredit, namaKredit, jenisKredit;
    String tglStor;
    String idInt,tglInt,ketInt,debtint,kredInt,nomInt;
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

//        set tanggal sekarang pada button pemilihan tanggal
        Date tgl = new Date();
        tgl.setTime(System.currentTimeMillis());
        SimpleDateFormat formatTgl = new SimpleDateFormat("dd/MM/YYYY");
        SimpleDateFormat formatTglStor = new SimpleDateFormat("YYYY-MM-dd");
        tglStor = formatTglStor.format(tgl);
        btTgl.setText(formatTgl.format(tgl));

        btTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance(TimeZone.getDefault());
                DatePickerDialog dialog = new
                        DatePickerDialog(EditDataJurnalActivity.this, EditDataJurnalActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
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
                dataJurnal.setId(idInt);
//                dataJurnal.setTgl(tglStor);
                dataJurnal.setKeterangan(etKeterangan.getText().toString());
//                dataJurnal.setAkunDebet(Integer.parseInt(kodeDebet));
//                dataJurnal.setNamaDebet(namaDebet);
//                dataJurnal.setAkunKredit(Integer.parseInt(kodeKredit));
//                dataJurnal.setNamaKredit(namaKredit);
                dataJurnal.setNominalDebet(nominalTransaksi);
                dataJurnal.setNominalKredit(nominalTransaksi);

                new DBAdapterMix(EditDataJurnalActivity.this).updateJurnal(dataJurnal);
                Toast.makeText(EditDataJurnalActivity.this, "Data Jurnal Telah Tersimpan",Toast.LENGTH_LONG).show();

//                kodeDebetint = Integer.parseInt(jenisDebet);
//                kodeKreditInt = Integer.parseInt(jenisKredit);

                int nominalTransaksiDebet = nominalTransaksi;
                int nominalTransaksiKredit = nominalTransaksi;

                if (kodeDebetint == 2 || kodeDebetint == 3 || kodeDebetint == 4 || kodeDebetint == 5  || kodeDebetint == 6){
                    nominalTransaksiDebet = -1 * nominalTransaksi;
                }else if (kodeDebetint == 0 || kodeDebetint == 1 || kodeDebetint == 7 || kodeDebetint == 8 || kodeDebetint == 9){
                    nominalTransaksiDebet = 1 * nominalTransaksi;
                }

                if (kodeKreditInt == 2 || kodeKreditInt == 3 || kodeKreditInt == 4 || kodeKreditInt == 5  || kodeKreditInt == 6){
                    nominalTransaksiKredit = 1 * nominalTransaksi;
                }else if (kodeKreditInt == 0 || kodeKreditInt == 1 || kodeKreditInt == 7 || kodeKreditInt == 8 || kodeKreditInt == 9){
                    nominalTransaksiKredit = -1 * nominalTransaksi;
                }

                Log.i("TambahJurnalActivity","jenis debet : " + kodeDebetint + ", besar transaksi : " + nominalTransaksiDebet + ", jenis kredit : " + kodeKreditInt + ", besar transaksi : "+ nominalTransaksiKredit);

                DataSaldo dataSaldo = new DataSaldo();
                dataSaldo.setKodeAkun(kodeDebet);
                dataSaldo.setTgl(tglStor);
                dataSaldo.setNominal(nominalTransaksiDebet);
                int idDebet = (Integer.parseInt(idInt)*2) - 1;
                new DBAdapterMix(EditDataJurnalActivity.this).updateRiwayatSaldo(dataSaldo,idDebet);
//                Toast.makeText(TambahJurnalActivity.this, "Data riwayat debet telah tersimpan",Toast.LENGTH_LONG).show();

                DataSaldo dataSaldo1 = new DataSaldo();
                dataSaldo1.setKodeAkun(kodeKredit);
                dataSaldo1.setTgl(tglStor);
                dataSaldo1.setNominal(nominalTransaksiKredit);
                int idKredit = (Integer.parseInt(idInt)*2);
                new DBAdapterMix(EditDataJurnalActivity.this).updateRiwayatSaldo(dataSaldo1,idKredit);
//                Toast.makeText(TambahJurnalActivity.this, "Data riwayat kredit telah tersimpan",Toast.LENGTH_LONG).show();

                finish();

            }
        });

        btDebet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditDataJurnalActivity.this, PilihDebetActivity.class);
                i.putExtra("pilihan", pilihanTransaksi);
                startActivityForResult(i,1);
            }
        });

        btKredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditDataJurnalActivity.this, PilihKreditActivity.class);
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

//        disetting sesuai data yang diklik
        idInt = getIntent().getStringExtra("id");
        tglInt = getIntent().getStringExtra("tgl");
        ketInt = getIntent().getStringExtra("ket");
        debtint = getIntent().getStringExtra("debet");
        kredInt = getIntent().getStringExtra("kredit");
        nomInt = getIntent().getStringExtra("nominal");

        btTgl.setText(tglInt);
        btTgl.setClickable(false);

        spinner.setVisibility(View.GONE);
        btDebet.setText(debtint);
        btKredit.setText(kredInt);
        btDebet.setClickable(false);
        btKredit.setClickable(false);

        etKeterangan.setText(ketInt);
        etNominalTrans.setText(nomInt);

        btAddJurnal.setText("UBAH DATA");

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
        String formatDateStor = "yyy-MM-dd";

        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.US);
        SimpleDateFormat sdfStor = new SimpleDateFormat(formatDateStor, Locale.US);

        btTgl.setText(sdf.format(calendar.getTime()));
        tglStor = sdfStor.format(calendar.getTime());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                kodeDebet = data.getStringExtra("kodeDebet");
                namaDebet = data.getStringExtra("namaDebet");
                jenisDebet = data.getStringExtra("jenisDebet");
                Toast.makeText(EditDataJurnalActivity.this, kodeDebet + " " + namaDebet + " " + jenisDebet,Toast.LENGTH_SHORT).show();
                btDebet.setText(namaDebet);
            }
        } else if (requestCode == 2){
            if (resultCode == RESULT_OK){
                kodeKredit = data.getStringExtra("kodeKredit");
                namaKredit = data.getStringExtra("namaKredit");
                jenisKredit = data.getStringExtra("jenisKredit");
                Toast.makeText(EditDataJurnalActivity.this, kodeKredit + " " + namaKredit + " " + jenisKredit,Toast.LENGTH_SHORT).show();
                btKredit.setText(namaKredit);
            }
        }
    }
}
