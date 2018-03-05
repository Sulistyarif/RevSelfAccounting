package com.zakiadev.sulistyarif.revselfaccounting;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by sulistyarif on 04/03/18.
 */

public class SettingNeracaAwalActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Button btTgl,btAddAkun,btSaveData, pilihAkun;
    LinearLayout llAkun;
    EditText etNomAkun;
    Calendar calendar;
    String tglStor;
    int jumAkun,etJumAkun, index, kodeAkun, jenisAkun;
    String namaAkun;
    List<Button> dataBtAkun = new ArrayList<Button>();
    List<EditText> dataEtAkun = new ArrayList<EditText>();
    List<Integer> kodeAkunAl = new ArrayList<Integer>();
    List<Integer> jenisAkunAl = new ArrayList<Integer>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_neraca_awal_activity);

        llAkun = (LinearLayout)findViewById(R.id.llWadahNeraca);
        btTgl = (Button)findViewById(R.id.btTglNeracaAwal);
        btAddAkun = (Button)findViewById(R.id.btAddAkunNeracaAwal);
        btSaveData = (Button)findViewById(R.id.btSaveNeracaAwal);

//        set tanggal sekarang pada button pemilihan tanggal
        Date tgl = new Date();
        tgl.setTime(System.currentTimeMillis());
        SimpleDateFormat formatTgl = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatTglStor = new SimpleDateFormat("yyyy-MM-dd");
        tglStor = formatTglStor.format(tgl);
        btTgl.setText(formatTgl.format(tgl));

        btTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance(TimeZone.getDefault());
                DatePickerDialog dialog = new DatePickerDialog(SettingNeracaAwalActivity.this,SettingNeracaAwalActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });


        jumAkun = 1;
        etJumAkun = 101;

        tambahAkun(jumAkun,etJumAkun);
        jumAkun++;
        etJumAkun++;


    }

    private void tambahAkun(int jumAkun, int etJumAkun) {
        pilihAkun = new Button(this);
        pilihAkun.setId(jumAkun);
        pilihAkun.setText("Pilih Debet");
        pilihAkun.setOnClickListener(viewOnclick);

        etNomAkun = new EditText(this);
        etNomAkun.setId(etJumAkun);
        etNomAkun.setInputType(InputType.TYPE_CLASS_NUMBER);
        etNomAkun.setHint("Masukkan Nominal");

        llAkun.addView(pilihAkun);
        dataBtAkun.add(pilihAkun);

        llAkun.addView(etNomAkun);
        dataEtAkun.add(etNomAkun);
    }

    View.OnClickListener viewOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int idClick = view.getId();
            if (idClick < 200){
//                Toast.makeText(TambahDataJurnalActivity.this, "id : " + idClick, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SettingNeracaAwalActivity.this, PilihAkunNeracaAwal.class);
//                intent.putExtra("pilihan", pilihanTransaksi);
//                intent.putExtra("sumber", idClick);
//                Log.i("noid","" + idClick);
                startActivityForResult(intent,idClick);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode < 200){
            if (resultCode == RESULT_OK){
                index = (data.getIntExtra("index",0)) - 1;
                kodeAkun = Integer.parseInt(data.getStringExtra("kodeDebet"));
                namaAkun = data.getStringExtra("namaDebet");
                jenisAkun = Integer.parseInt(data.getStringExtra("jenisDebet"));
                setAkunButton();
                Log.i("account","succeed " + index + " " + resultCode + " " + data.getStringExtra("kodeDebet"));
            }
        }
    }

    private void setAkunButton() {
        try{
            kodeAkunAl.set(index,kodeAkun);
        }catch (Exception e){
            kodeAkunAl.add(kodeAkun);
        }
        try{
            jenisAkunAl.set(index,jenisAkun);
        }catch (Exception e){
            jenisAkunAl.add(jenisAkun);
        }
        dataBtAkun.get(index).setText(namaAkun);
        Toast.makeText(SettingNeracaAwalActivity.this, kodeAkun + namaAkun + jenisAkun,Toast.LENGTH_SHORT).show();
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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
