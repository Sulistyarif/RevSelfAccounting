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

import com.wajahatkarim3.easymoneywidgets.EasyMoneyEditText;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataJurnalMar;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataTransMar;
import com.zakiadev.sulistyarif.revselfaccounting.data.EditDataTransMar;
import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapterMix;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by sulistyarif on 28/02/18.
 * id juga dimasukkan ke dalam attribute button karena digunakan insert or update
 */

public class EditDataJurnalActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    LinearLayout llDebet, llKredit;
    Button btAddDebet, btAddKredit, btUpdateJurnal, btTgl;
    Button pilihDebet, pilihKredit;
    EditText etNomDebet, etNomKredit;
    EditText etKeterangan;
    int jumDebet, etJumDebet, jumKredit, etJumKredit;
    List<Button> dataBtDebet = new ArrayList<Button>();
    List<Button> dataBtKredit = new ArrayList<Button>();
    List<EditText> dataEtDebet = new ArrayList<EditText>();
    List<EditText> dataEtKredit = new ArrayList<EditText>();
    List<Integer> kodeDebetAl = new ArrayList<Integer>();
    List<Integer> kodeKreditAl = new ArrayList<Integer>();
    List<Integer> jenisDebetAl = new ArrayList<Integer>();
    List<Integer> jenisKreditAl = new ArrayList<Integer>();
    List<Integer> idDebet = new ArrayList<Integer>();
    List<Integer> idKredit = new ArrayList<Integer>();
    int pilihanTransaksi, index;
    String namaDebet;
    int kodeDebet, jenisDebet;
    String namaKredit;
    int kodeKredit, jenisKredit;
    int kodeId;
    Calendar calendar;
    String tglStor;
    Spinner spinner;
    String editPid, editTgl, editKet, editKodeTrans;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_data_activity);

//        ngambil data dari class jurnal kecil
        Intent intent = getIntent();
        editPid = intent.getStringExtra("pid");
        editTgl = intent.getStringExtra("tgl");
        editKet = intent.getStringExtra("ket");
        editKodeTrans = intent.getStringExtra("kodeTrans");

        llDebet = (LinearLayout)findViewById(R.id.llDebet);
        llKredit = (LinearLayout)findViewById(R.id.llKredit);

        btAddDebet = (Button)findViewById(R.id.btAddDebet);
        btAddKredit = (Button)findViewById(R.id.btAddKredit);
        btUpdateJurnal = (Button)findViewById(R.id.btAddData);
        btTgl = (Button)findViewById(R.id.btTgl2);

//        set tanggal sekarang pada button pemilihan tanggal
        Date tgl = new Date();
        SimpleDateFormat formatTgl = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatTglStor = new SimpleDateFormat("yyyy-MM-dd");

//        ngisi tanggal dari db ke button
        try {
            tgl = formatTglStor.parse(editTgl);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tglStor = formatTglStor.format(tgl);
        btTgl.setText(formatTgl.format(tgl));

        btTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance(TimeZone.getDefault());
                DatePickerDialog dialog = new DatePickerDialog(EditDataJurnalActivity.this,EditDataJurnalActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        etKeterangan = (EditText)findViewById(R.id.etKeterangan2);
        etKeterangan.setText(editKet);

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
        spinner.setSelection(Integer.parseInt(editKodeTrans));

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

        String idTrans, pidTrans, kodeAkunTrans, namaAkunTrans;
        int nominalTrans, jenisTrans, posTrans;

        ArrayList<EditDataTransMar> editDataTransMarArrayList = new DBAdapterMix(EditDataJurnalActivity.this).selectTransToEdit(editPid);
        EditDataTransMar editDataTransMar;
        for (int i = 0; i < editDataTransMarArrayList.size(); i++){
            editDataTransMar = editDataTransMarArrayList.get(i);

            idTrans = editDataTransMar.getId();
            pidTrans = editDataTransMar.getPid();
            kodeAkunTrans = editDataTransMar.getKodeAkun();
            namaAkunTrans = editDataTransMar.getNamaAkun();
            nominalTrans = editDataTransMar.getNominal();
            jenisTrans = editDataTransMar.getJenisAkun();
            posTrans = editDataTransMar.getPos();

//            kalo posTrans bernilai 0 berarti debet
            if (posTrans == 0){
                loadDebet(idTrans,pidTrans,kodeAkunTrans,namaAkunTrans,nominalTrans,jenisTrans,jumDebet,etJumDebet);
                jumDebet++;
                etJumDebet++;
            }else {
                loadKredit(idTrans,pidTrans,kodeAkunTrans,namaAkunTrans,nominalTrans,jenisTrans,jumKredit,etJumKredit);
                jumKredit++;
                etJumKredit++;
            }

        }

//        ini nanti tidak dipakai karena nggak bikin baru, tapi load dari db
//        tambahDebet(jumDebet,etJumDebet);
//        jumDebet++;
//        etJumDebet++;
//
//        tambahKredit(jumKredit,etJumKredit);
//        jumKredit++;
//        etJumKredit++;

//        yang ini tetep karena misal editnya mau nambah data lagi
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

        btUpdateJurnal.setText("UPDATE JURNAL");
        btUpdateJurnal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                debetloop:
                for (int i = 0; i < dataEtDebet.size(); i++){
                    if (NumberTextWatcherForThousand.trimCommaOfString(dataEtDebet.get(i).getText().toString()).equals("") || jenisDebetAl.size() != dataBtDebet.size()){
                        Toast.makeText(EditDataJurnalActivity.this,"Lengkapi data debet",Toast.LENGTH_SHORT).show();
                        break debetloop;
                    }else if (i == (dataEtDebet.size() - 1)){
                        Log.i("nilaiLoopDb", "nilai loop:" + i);
                        kreditloop:
                        for (int j = 0; j < dataEtKredit.size(); j++){
                            if (NumberTextWatcherForThousand.trimCommaOfString(dataEtKredit.get(j).getText().toString()).equals("") || jenisKreditAl.size() != dataBtKredit.size()){
                                Toast.makeText(EditDataJurnalActivity.this,"Lengkapi data kredit",Toast.LENGTH_SHORT).show();
                                break debetloop;
                            } else if (j == ((dataBtKredit.size()) - 1)){
                                Log.i("nilaiLoopKr", "nilai loop:" + j);
                                int nominalDebet = 0;
                                int nominalKredit = 0;
                                for (int k = 0; k < dataEtDebet.size(); k++){
                                    nominalDebet += Integer.parseInt(NumberTextWatcherForThousand.trimCommaOfString(dataEtDebet.get(k).getText().toString()));
                                }
                                for (int k = 0; k< dataEtKredit.size(); k++){
                                    nominalKredit += Integer.parseInt(NumberTextWatcherForThousand.trimCommaOfString(dataEtKredit.get(k).getText().toString()));
                                }
                                if (nominalDebet == nominalKredit){
                                    updateData();
                                } else {
                                    Toast.makeText(EditDataJurnalActivity.this, "Nominal debet dan kredit tidak seimbang", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
            }
        });

    }

    private void updateData() {
        int jenisAkunDebet, jenisAkunKredit;
        int nominalAkunDebet, nominalAkunKredit;
        String pid = editPid;
        String keterangan = etKeterangan.getText().toString();

        DataJurnalMar dataJurnalMar = new DataJurnalMar();
        dataJurnalMar.setPid(pid);
        dataJurnalMar.setTgl(tglStor);
        dataJurnalMar.setKet(keterangan);
        dataJurnalMar.setKode_trans(pilihanTransaksi);

        new DBAdapterMix(EditDataJurnalActivity.this).updateJurnalMar(dataJurnalMar);
        Log.i("queryUpdate","update into jurnal(pid,tgl,keterangan,trans) values (" + pid + "," + tglStor + ", " + keterangan +", " + pilihanTransaksi + ");" );

//                testing input data transaksi bagian debet
        for (int i = 0; i < dataEtDebet.size() ; i++){
//                    kalo nggak null brarti update
            if (idDebet.get(i) != 999){
                jenisAkunDebet = jenisDebetAl.get(i);
                nominalAkunDebet = Integer.parseInt(NumberTextWatcherForThousand.trimCommaOfString(dataEtDebet.get(i).getText().toString()));
                if (jenisAkunDebet == 2 || jenisAkunDebet == 3 || jenisAkunDebet == 4 || jenisAkunDebet == 5 || jenisAkunDebet == 6 ){
                    nominalAkunDebet *= -1;
                }

                DataTransMar dataTransMar = new DataTransMar();
                dataTransMar.setPid(pid);
                dataTransMar.setKode_akun(kodeDebetAl.get(i).toString());
                dataTransMar.setNominal(nominalAkunDebet);
                dataTransMar.setPos(0);

                new DBAdapterMix(EditDataJurnalActivity.this).updateTrans(dataTransMar,idDebet.get(i));
                Log.i("queryUpdate", "update into trans(pid,kode_akun,nominal,pos) values (" + pid + "," + kodeDebetAl.get(i).toString() + "," + nominalAkunDebet + ",0" + ");");
            }else {
//                        kalo null, brarti nambah baru lagi
                jenisAkunDebet = jenisDebetAl.get(i);
                nominalAkunDebet = Integer.parseInt(NumberTextWatcherForThousand.trimCommaOfString(dataEtDebet.get(i).getText().toString()));
                if (jenisAkunDebet == 2 || jenisAkunDebet == 3 || jenisAkunDebet == 4 || jenisAkunDebet == 5 || jenisAkunDebet == 6 ){
                    nominalAkunDebet *= -1;
                }

                DataTransMar dataTransMar = new DataTransMar();
                dataTransMar.setPid(pid);
                dataTransMar.setKode_akun(kodeDebetAl.get(i).toString());
                dataTransMar.setNominal(nominalAkunDebet);
                dataTransMar.setPos(0);

                new DBAdapterMix(EditDataJurnalActivity.this).insertTrans(dataTransMar);
                Log.i("queryTrans", "insert into trans(pid,kode_akun,nominal,pos) values (" + pid + "," + kodeDebetAl.get(i).toString() + "," + nominalAkunDebet + ",0" + ");");
            }

        }

//                testing input data transaksi bagian kredit
        for (int i = 0 ; i < dataEtKredit.size() ; i++){
//                    kalo nggak null brarti update
            if (idKredit.get(i) != 999){
                jenisAkunKredit = jenisKreditAl.get(i);
                nominalAkunKredit = Integer.parseInt(NumberTextWatcherForThousand.trimCommaOfString(dataEtKredit.get(i).getText().toString()));
                Log.i("jenisAkun","" + jenisAkunKredit);
                if (jenisAkunKredit == 0 || jenisAkunKredit == 1 || jenisAkunKredit == 7 || jenisAkunKredit == 8 || jenisAkunKredit == 9){
                    nominalAkunKredit *= -1;
                }
                DataTransMar dataTransMar = new DataTransMar();
                dataTransMar.setPid(pid);
                dataTransMar.setKode_akun(kodeKreditAl.get(i).toString());
                dataTransMar.setNominal(nominalAkunKredit);
                dataTransMar.setPos(1);

                new DBAdapterMix(EditDataJurnalActivity.this).updateTrans(dataTransMar,idKredit.get(i));
                Log.i("queryUpdate", "update into trans(pid,kode_akun,nominal,pos) values (" + pid + "," + kodeKreditAl.get(i).toString() + "," + nominalAkunKredit + ",1" + ");");
            }else {
                jenisAkunKredit = jenisKreditAl.get(i);
                nominalAkunKredit = Integer.parseInt(NumberTextWatcherForThousand.trimCommaOfString(dataEtKredit.get(i).getText().toString()));
                Log.i("jenisAkun","" + jenisAkunKredit);
                if (jenisAkunKredit == 0 || jenisAkunKredit == 1 || jenisAkunKredit == 7 || jenisAkunKredit == 8 || jenisAkunKredit == 9){
                    nominalAkunKredit *= -1;
                }
                DataTransMar dataTransMar = new DataTransMar();
                dataTransMar.setPid(pid);
                dataTransMar.setKode_akun(kodeKreditAl.get(i).toString());
                dataTransMar.setNominal(nominalAkunKredit);
                dataTransMar.setPos(1);

                new DBAdapterMix(EditDataJurnalActivity.this).insertTrans(dataTransMar);
                Log.i("queryTrans", "insert into trans(pid,kode_akun,nominal,pos) values (" + pid + "," + kodeKreditAl.get(i).toString() + "," + nominalAkunKredit + ",1" + ");");
            }

        }

        finish();
    }

    private void loadKredit(String idTrans, String pidTrans, String kodeAkunTrans, String namaAkunTrans, int nominalTrans, int jenisTrans, int jumKredit, int etJumKredit) {
        pilihKredit = new Button(this);
        pilihKredit.setId(jumKredit);
        pilihKredit.setText(namaAkunTrans);
        pilihKredit.setOnClickListener(viewOnclick);

        etNomKredit = new EditText(this);
        etNomKredit.setId(etJumKredit);
        etNomKredit.addTextChangedListener(new NumberTextWatcherForThousand(etNomKredit));
        etNomKredit.setInputType(InputType.TYPE_CLASS_NUMBER);
        etNomKredit.setHint("Masukkan Nominal Debet");
        etNomKredit.setText(String.valueOf(Math.abs(nominalTrans)));

        llKredit.addView(pilihKredit);
        llKredit.addView(etNomKredit);

        dataBtKredit.add(pilihKredit);
        dataEtKredit.add(etNomKredit);

        kodeKreditAl.add(Integer.valueOf(kodeAkunTrans));
        jenisKreditAl.add(jenisTrans);
        idKredit.add(Integer.valueOf(idTrans));

    }

    private void loadDebet(String idTrans, String pidTrans, String kodeAkunTrans, String namaAkunTrans, int nominalTrans, int jenisTrans, int jumDebet, int etJumDebet) {
        pilihDebet = new Button(this);
        pilihDebet.setId(jumDebet);
        pilihDebet.setText(namaAkunTrans);
        pilihDebet.setOnClickListener(viewOnclick);

        etNomDebet = new EditText(this);
        etNomDebet.setId(etJumDebet);
        etNomDebet.addTextChangedListener(new NumberTextWatcherForThousand(etNomDebet));
        etNomDebet.setInputType(InputType.TYPE_CLASS_NUMBER);
        etNomDebet.setHint("Masukkan Nominal Debet");
        etNomDebet.setText(String.valueOf(Math.abs(nominalTrans)));

        llDebet.addView(pilihDebet);
        llDebet.addView(etNomDebet);

        dataBtDebet.add(pilihDebet);
        dataEtDebet.add(etNomDebet);

        kodeDebetAl.add(Integer.valueOf(kodeAkunTrans));
        jenisDebetAl.add(jenisTrans);
        idDebet.add(Integer.valueOf(idTrans));

    }

    private void tambahDebet(int jumDebet, int etJumDebet) {
        pilihDebet = new Button(this);
        pilihDebet.setId(jumDebet);
        pilihDebet.setText("Pilih Akun Debet");
        pilihDebet.setOnClickListener(viewOnclick);

        etNomDebet = new EditText(this);
        etNomDebet.setId(etJumDebet);
        etNomDebet.addTextChangedListener(new NumberTextWatcherForThousand(etNomDebet));
        etNomDebet.setInputType(InputType.TYPE_CLASS_NUMBER);
        etNomDebet.setHint("Masukkan Nominal Debet");

        llDebet.addView(pilihDebet);
        dataBtDebet.add(pilihDebet);

        llDebet.addView(etNomDebet);
        dataEtDebet.add(etNomDebet);
        idDebet.add(999);
    }

    private void tambahKredit(int jumKredit, int etJumKredit) {
        pilihKredit = new Button(this);
        pilihKredit.setId(jumKredit);
        pilihKredit.setText("Pilih Akun Kredit");
        pilihKredit.setOnClickListener(viewOnclick);

        etNomKredit = new EditText(this);
        etNomKredit.setId(etJumKredit);
        etNomKredit.addTextChangedListener(new NumberTextWatcherForThousand(etNomKredit));
        etNomKredit.setInputType(InputType.TYPE_CLASS_NUMBER);
        etNomKredit.setHint("Masukkan Nominal Kredit");

        llKredit.addView(pilihKredit);
        dataBtKredit.add(pilihKredit);

        llKredit.addView(etNomKredit);
        dataEtKredit.add(etNomKredit);
        idKredit.add(999);
    }

    View.OnClickListener viewOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int idClick = view.getId();
            if (idClick < 200){
//                Toast.makeText(TambahDataJurnalActivity.this, "id : " + idClick, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditDataJurnalActivity.this, PilihDebetActivity.class);
                intent.putExtra("pilihan", pilihanTransaksi);
                intent.putExtra("sumber", idClick);
                Log.i("noid","" + idClick);
                startActivityForResult(intent,idClick);
            }else {
//                Toast.makeText(TambahDataJurnalActivity.this, "id : " + idClick, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditDataJurnalActivity.this, PilihKreditActivity.class);
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
            kodeKreditAl.set(index,kodeKredit);
        }catch (Exception e){
            kodeKreditAl.add(kodeKredit);
        }
        try{
            jenisKreditAl.set(index,jenisKredit);
        }catch (Exception e){
            jenisKreditAl.add(jenisKredit);
        }
        dataBtKredit.get(index).setText(namaKredit);
//        Toast.makeText(EditDataJurnalActivity.this, kodeKredit + namaKredit + jenisKredit,Toast.LENGTH_SHORT).show();
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
//        Toast.makeText(EditDataJurnalActivity.this, kodeDebet + namaDebet + jenisDebet,Toast.LENGTH_SHORT).show();
    }
}
