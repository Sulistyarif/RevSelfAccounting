package com.zakiadev.sulistyarif.revselfaccounting;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.wajahatkarim3.easymoneywidgets.EasyMoneyEditText;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataJurnalMar;
import com.zakiadev.sulistyarif.revselfaccounting.data.DataTransMar;
import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapter;
import com.zakiadev.sulistyarif.revselfaccounting.db.DBAdapterMix;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
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
    int pilihanTransaksi, index;
    String namaDebet;
    int kodeDebet, jenisDebet;
    String namaKredit;
    int kodeKredit, jenisKredit;
    int kodeId;
    Calendar calendar;
    String tglStor;
    Spinner spinner;
    boolean isPembalikUtang1,isPembalikUtang2, isPiutangPendapatan1, isPiutangPendapatan2, isPendapatanTerimaMuka1, isPendapatanTerimaMuka2, isBiayaDimuka1, isBiayaDimuka2;
    int indexPembalikUtang1,indexPembalikUtang2;
    int yangDibalik;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_data_activity);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        llDebet = (LinearLayout)findViewById(R.id.llDebet);
        llKredit = (LinearLayout)findViewById(R.id.llKredit);

        btAddDebet = (Button)findViewById(R.id.btAddDebet);
        btAddKredit = (Button)findViewById(R.id.btAddKredit);
        btAddJurnal = (Button)findViewById(R.id.btAddData);
        btTgl = (Button)findViewById(R.id.btTgl2);

//        membaerikan value pertama
        isPembalikUtang1 = isPembalikUtang2 = isPiutangPendapatan1 = isPiutangPendapatan2 = isPendapatanTerimaMuka1 = isPendapatanTerimaMuka2 = isBiayaDimuka1 = isBiayaDimuka2 = false;

//        set tanggal sekarang pada button pemilihan tanggal
        Date tgl = new Date();
        tgl.setTime(System.currentTimeMillis());
        SimpleDateFormat formatTgl = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatTglStor = new SimpleDateFormat("yyyy-MM-dd");
        tglStor = formatTglStor.format(tgl);
        btTgl.setText(formatTgl.format(tgl));
        calendar = Calendar.getInstance(TimeZone.getDefault());

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
        listSpinner.add("Penjualan Aset - Pendapatan Jasa");
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

                debetloop:
                for (int i = 0; i < dataEtDebet.size(); i++){

//                    ngecek pembalik
                    if (jenisDebetAl.get(i).toString().equals("7") || jenisDebetAl.get(i).toString().equals("8")){
                        isPembalikUtang1 = true;
                        indexPembalikUtang1 = i;
                    }else if (kodeDebetAl.get(i).toString().equals("1110")){
                        isPiutangPendapatan1 = true;
                        indexPembalikUtang1 = i;
                    }else if (kodeDebetAl.get(i).toString().equals("4102")){
                        isPendapatanTerimaMuka1 = true;
                        indexPembalikUtang1 = i;
                    }else if (jenisDebetAl.get(i).toString().equals("0")){
                        isBiayaDimuka1 = true;
                        indexPembalikUtang1 = i;
                    }

                    if (NumberTextWatcherForThousand.trimCommaOfString(dataEtDebet.get(i).getText().toString()).equals("") || jenisDebetAl.size() != dataBtDebet.size()){
                        Toast.makeText(TambahDataJurnalActivity.this,"Lengkapi data debet",Toast.LENGTH_SHORT).show();
                        break debetloop;
                    }else if (i == (dataEtDebet.size() - 1)){
                        Log.i("nilaiLoopDb", "nilai loop:" + i);
                        kreditloop:
                        for (int j = 0; j < dataEtKredit.size(); j++){

//                            ngecek pembalik
                            if (jenisKreditAl.get(j).toString().equals("2")){
                                isPembalikUtang2 = true;
                                indexPembalikUtang2 = j;
                            }else if (kodeKreditAl.get(j).toString().equals("4102")){
                                isPiutangPendapatan2 = true;
                                indexPembalikUtang2 = j;
                            }else if (kodeKreditAl.get(j).toString().equals("2108")){
                                isPendapatanTerimaMuka2 = true;
                                indexPembalikUtang2 = j;
                            }else if (jenisKreditAl.get(j).toString().equals("7")){
                                isBiayaDimuka2 = true;
                                indexPembalikUtang2 = j;
                            }

                            if (NumberTextWatcherForThousand.trimCommaOfString(dataEtKredit.get(j).getText().toString()).equals("") || jenisKreditAl.size() != dataBtKredit.size()){
                                Toast.makeText(TambahDataJurnalActivity.this,"Lengkapi data kredit",Toast.LENGTH_SHORT).show();
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
                                    tambahJurnal();
                                } else {
                                    Toast.makeText(TambahDataJurnalActivity.this, "Nominal debet dan kredit tidak seimbang", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        return true;
    }

    private void tambahJurnal() {
        kodeId = new DBAdapterMix(TambahDataJurnalActivity.this).selectLastId();
        int jenisAkunDebet, jenisAkunKredit;
        int nominalAkunDebet, nominalAkunKredit;
        String pid = "p" + kodeId;
        String keterangan = etKeterangan.getText().toString();

        DataJurnalMar dataJurnalMar = new DataJurnalMar();
        dataJurnalMar.setPid(pid);
        dataJurnalMar.setTgl(tglStor);
        dataJurnalMar.setKet(keterangan);
        dataJurnalMar.setKode_trans(pilihanTransaksi);

        new DBAdapterMix(TambahDataJurnalActivity.this).insertJurnalMar(dataJurnalMar);
        Log.i("queryJurnal","insert into jurnal(pid,tgl,keterangan,trans) values (" + pid + "," + tglStor + ", " + keterangan +", " + pilihanTransaksi + ");" );

//                testing input data transaksi bagian debet
        for (int i = 0; i < dataEtDebet.size() ; i++){
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

//                    tidak menggunakan insert namun menggunakan insert or update
            new DBAdapterMix(TambahDataJurnalActivity.this).insertTrans(dataTransMar);
            Log.i("queryTrans", "insert into trans(pid,kode_akun,nominal,pos) values (" + pid + "," + kodeDebetAl.get(i).toString() + "," + nominalAkunDebet + ",0" + ");");
        }

//                testing input data transaksi bagian kredit
        for (int i = 0 ; i < dataEtKredit.size() ; i++){
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

            new DBAdapterMix(TambahDataJurnalActivity.this).insertTrans(dataTransMar);
            Log.i("queryTrans", "insert into trans(pid,kode_akun,nominal,pos) values (" + pid + "," + kodeKreditAl.get(i).toString() + "," + nominalAkunKredit + ",1" + ");");
        }

        if (isPembalikUtang1 && isPembalikUtang2){
            tambahJurnalPembalik(indexPembalikUtang1,indexPembalikUtang2,0);
        }else if (isPiutangPendapatan1 && isPiutangPendapatan2){
            tambahJurnalPembalik(indexPembalikUtang1, indexPembalikUtang2, 1);
        }else if (isPendapatanTerimaMuka1 && isPendapatanTerimaMuka2){
            tambahJurnalPembalik(indexPembalikUtang1, indexPembalikUtang2, 2);
        }else if (isBiayaDimuka1 && isBiayaDimuka2){
            tambahJurnalPembalik(indexPembalikUtang1,indexPembalikUtang2, 3);
        }

        finish();
    }

    private void tambahJurnalPembalik(int indexDebetAsal, int indexKreditAsal, int kodePembalik) {

        kodeId = new DBAdapterMix(TambahDataJurnalActivity.this).selectLastId();
        String pid = "p" + kodeId;
        String keterangan = "";
        switch (kodePembalik){
            case 0:{
                keterangan = "Pembalikan Utang Gaji";
                break;
            }
            case 1:{
                keterangan = "Pembalikan Piutang Pendapatan Sewa";
                break;
            }
            case 2:{
                keterangan = "Pembalikan Utang Pendapatan";
                break;
            }
            case 3:{
                keterangan = "Pembalikan Sewa Dibayar Dimuka";
                break;
            }
        }


//        tanggal setornya dibuat tanggal 1 bulan depan
        calendar.set(Calendar.DAY_OF_MONTH, 01);
        calendar.add(Calendar.MONTH, 1);
        String formatDateStor = "yyyy-MM-dd";

        SimpleDateFormat sdfStor = new SimpleDateFormat(formatDateStor, Locale.US);
        String tglStorPembalik = sdfStor.format(calendar.getTime());

        DataJurnalMar dataJurnalMar = new DataJurnalMar();
        dataJurnalMar.setPid(pid);
        dataJurnalMar.setTgl(tglStorPembalik);
        dataJurnalMar.setKet(keterangan);
        dataJurnalMar.setKode_trans(pilihanTransaksi);

        new DBAdapterMix(TambahDataJurnalActivity.this).insertJurnalMar(dataJurnalMar);

//        masukan yang harusnya debet dijadikan kredit, dan kredit dijadikan debet

//        debet
        DataTransMar dataTransMar = new DataTransMar();
        dataTransMar.setPid(pid);
        dataTransMar.setKode_akun(kodeKreditAl.get(indexKreditAsal).toString());
        dataTransMar.setNominal(Integer.parseInt(NumberTextWatcherForThousand.trimCommaOfString(dataEtKredit.get(indexKreditAsal).getText().toString())));
        dataTransMar.setPos(0);

        new DBAdapterMix(TambahDataJurnalActivity.this).insertTrans(dataTransMar);

        DataTransMar dataTransMar1 = new DataTransMar();
        dataTransMar1.setPid(pid);
        dataTransMar1.setKode_akun(kodeDebetAl.get(indexDebetAsal).toString());
        dataTransMar1.setNominal(Integer.parseInt(NumberTextWatcherForThousand.trimCommaOfString(dataEtDebet.get(indexDebetAsal).getText().toString())));
        dataTransMar1.setPos(1);

        new DBAdapterMix(TambahDataJurnalActivity.this).insertTrans(dataTransMar1);

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
//        Toast.makeText(TambahDataJurnalActivity.this, kodeKredit + namaKredit + jenisKredit,Toast.LENGTH_SHORT).show();
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
//        Toast.makeText(TambahDataJurnalActivity.this, kodeDebet + namaDebet + jenisDebet,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_restart:{
                startActivity(getIntent());
                finish();
                return true;
            }
            default:{
                return super.onOptionsItemSelected(item);
            }
        }
    }
}
