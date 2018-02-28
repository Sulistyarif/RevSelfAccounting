package com.zakiadev.sulistyarif.revselfaccounting;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by sulistyarif on 28/02/18.
 */

public class TambahDataJurnalActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    LinearLayout llDebet, llKredit;
    Button btAddDebet, btAddKredit, btAddJurnal, btTgl;
    Button pilihDebet, pilihKredit;
    EditText etNomDebet, etNomKredit, etKeterangan;
    int jumDebet, etJumDebet, jumKredit, etJumKredit;
    List<Button> dataBtDebet = new ArrayList<Button>();
    List<Button> dataBtKredit = new ArrayList<Button>();
    List<EditText> dataEtDebet = new ArrayList<EditText>();
    List<EditText> dataEtKredit = new ArrayList<EditText>();
    List<Integer> kodeDebetAl = new ArrayList<Integer>();
    List<Integer> kodeKreditAl = new ArrayList<Integer>();
    List<Integer> jenisDebetAl = new ArrayList<Integer>();
    List<Integer> jenisKreditAl = new ArrayList<Integer>();
    int pilihanTransaksi, index;
    String namaDebet;
    int kodeDebet, jenisDebet;
    String namaKredit;
    int kodeKredit, jenisKredit;
    Calendar calendar;
    String tglStor;
    Spinner spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_data_activity);

        llDebet = (LinearLayout)findViewById(R.id.llDebet);
        llKredit = (LinearLayout)findViewById(R.id.llKredit);

        btAddDebet = (Button)findViewById(R.id.btAddDebet);
        btAddKredit = (Button)findViewById(R.id.btAddKredit);
        btAddJurnal = (Button)findViewById(R.id.btAddData);
        btTgl = (Button)findViewById(R.id.btTgl2);

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
                DatePickerDialog dialog = new DatePickerDialog(TambahDataJurnalActivity.this,TambahDataJurnalActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        etKeterangan = (EditText)findViewById(R.id.etKeterangan2);

//        setting spinner
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

//        spinner on data change listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pilihanTransaksi = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        jumDebet = 1;
        etJumDebet = 101;
        jumKredit = 201;
        etJumKredit = 301;

        tambahDebet(jumDebet,etJumDebet);
        jumDebet++;
        etJumDebet++;

        tambahKredit(jumKredit,etJumKredit);
        jumKredit++;
        etJumKredit++;

        btAddDebet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahDebet(jumDebet, etJumDebet);
                jumDebet++;
                etJumDebet++;
            }
        });

        btAddKredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahKredit(jumKredit, etJumKredit);
                jumKredit++;
                etJumKredit++;
            }
        });

        btAddJurnal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < dataEtDebet.size() ; i++){
                    Log.i("data debet", i + " kode akun : " + kodeDebetAl.get(i).toString() + ", nominalnya : " + dataEtDebet.get(i).getText().toString() + ", jenis : " + jenisDebetAl.get(i).toString());
                }

                for (int i = 0 ; i < dataEtKredit.size() ; i++){
//                    Log.i("data kredit", dataEtKredit.get(i).getText().toString());
                    Log.i("data debet", i + " kode akun : " + kodeKreditAl.get(i).toString() + ", nominalnya : " + dataEtKredit.get(i).getText().toString() + ", jenis : " + jenisKreditAl.get(i).toString());
                }
            }
        });

    }

    private void tambahKredit(int jumKredit, int etJumKredit) {
        pilihKredit = new Button(this);
        pilihKredit.setId(jumKredit);
        pilihKredit.setText("Pilih Kredit");
        pilihKredit.setOnClickListener(viewOnclick);

        etNomKredit = new EditText(this);
        etNomKredit.setId(etJumKredit);
        etNomKredit.setInputType(InputType.TYPE_CLASS_NUMBER);
        etNomKredit.setHint("Masukkan Nominal Kredit");

        llKredit.addView(pilihKredit);
        dataBtKredit.add(pilihKredit);

        llKredit.addView(etNomKredit);
        dataEtKredit.add(etNomKredit);
    }

    private void tambahDebet(int jumDebet, int etJumDebet) {
        pilihDebet = new Button(this);
        pilihDebet.setId(jumDebet);
        pilihDebet.setText("Pilih Debet");
        pilihDebet.setOnClickListener(viewOnclick);

        etNomDebet = new EditText(this);
        etNomDebet.setId(etJumDebet);
        etNomDebet.setInputType(InputType.TYPE_CLASS_NUMBER);
        etNomDebet.setHint("Masukkan Nominal Debet");

        llDebet.addView(pilihDebet);
        dataBtDebet.add(pilihDebet);

        llDebet.addView(etNomDebet);
        dataEtDebet.add(etNomDebet);
    }

    View.OnClickListener viewOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int idClick = view.getId();
            if (idClick < 200){
//                Toast.makeText(TambahDataJurnalActivity.this, "id : " + idClick, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TambahDataJurnalActivity.this, PilihDebetActivity.class);
                intent.putExtra("pilihan", pilihanTransaksi);
                intent.putExtra("sumber", idClick);
                Log.i("noid","" + idClick);
                startActivityForResult(intent,idClick);
            }else {
//                Toast.makeText(TambahDataJurnalActivity.this, "id : " + idClick, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TambahDataJurnalActivity.this, PilihKreditActivity.class);
                intent.putExtra("pilihan", pilihanTransaksi);
                intent.putExtra("sumber", idClick);
                Log.i("noid","" + idClick);
                startActivityForResult(intent,idClick);
            }
        }
    };

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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode < 200){
            if (resultCode == RESULT_OK){
                index = (data.getIntExtra("index",0)) - 1;
                kodeDebet = Integer.parseInt(data.getStringExtra("kodeDebet"));
                namaDebet = data.getStringExtra("namaDebet");
                jenisDebet = Integer.parseInt(data.getStringExtra("jenisDebet"));
                setDebetButton();
                Log.i("account","succeed " + index + " " + resultCode + " " + data.getStringExtra("kodeDebet"));
            }
        }else {
            if (resultCode == RESULT_OK){
                index = (data.getIntExtra("index",0)) - 201;
                Log.i("account","succeed " + index + " " + resultCode + " " + data.getStringExtra("kodeKredit"));
                kodeKredit = Integer.parseInt(data.getStringExtra("kodeKredit"));
                namaKredit = data.getStringExtra("namaKredit");
                jenisKredit = Integer.parseInt(data.getStringExtra("jenisKredit"));
                setKreditButton();
                dataBtKredit.get(index).setText(namaKredit);
            }
        }
    }

    private void setKreditButton() {
        try{
            kodeKreditAl.set(index,kodeDebet);
        }catch (Exception e){
            kodeKreditAl.add(kodeDebet);
        }
        try{
            jenisKreditAl.set(index,jenisKredit);
        }catch (Exception e){
            jenisKreditAl.add(jenisKredit);
        }
        dataBtKredit.get(index).setText(namaKredit);
    }

    private void setDebetButton() {
        try{
            kodeDebetAl.set(index,kodeDebet);
        }catch (Exception e){
            kodeDebetAl.add(kodeDebet);
        }
        try{
            jenisDebetAl.set(index,jenisDebet);
        }catch (Exception e){
            jenisDebetAl.add(jenisDebet);
        }
        dataBtDebet.get(index).setText(namaDebet);
    }
}
