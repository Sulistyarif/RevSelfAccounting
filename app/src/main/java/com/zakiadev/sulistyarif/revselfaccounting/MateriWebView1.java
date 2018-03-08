package com.zakiadev.sulistyarif.revselfaccounting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by sulistyarif on 08/03/18.
 */

public class MateriWebView1 extends AppCompatActivity {

    WebView webView;
    int pilihanMenu;
    String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.materi_webview_activity);

        webView = (WebView)findViewById(R.id.wvMateri);
        webView.getSettings().setJavaScriptEnabled(true);

        Intent intent = getIntent();
        pilihanMenu = intent.getIntExtra("pilihanMenu",99);

        switch (pilihanMenu){
            case 0:{
                webView.loadUrl("file:///android_asset/pengenalan akutansi.html");
                break;
            }
            case 1:{
                webView.loadUrl("file:///android_asset/macam macam dan manfaat laporan keuangan.html");
                break;
            }
            case 2:{
                webView.loadUrl("file:///android_asset/elemen_dan_akun_dalam_laporan.html");
                break;
            }
            case 3:{
                webView.loadUrl("file:///android_asset/Kode Akun.html");
                break;
            }
            case 4:{
                webView.loadUrl("file:///android_asset/pencatatan transaksi.html");
                break;
            }
            case 5:{
                webView.loadUrl("file:///android_asset/transaksi penyesuaian.html");
                break;
            }
            default:{
                Toast.makeText(this,"Menu belum siap",Toast.LENGTH_SHORT).show();
            }
        }

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.equals("hrupin://laporan_laba_rugi")){
                    url = "file:///android_asset/Laporan_Laba_Rugi.html";
                    Intent intent1 = new Intent(MateriWebView1.this, MateriWebView2.class);
                    intent1.putExtra("urlIntent", url);
                    startActivity(intent1);
                    return true;
                }else if (url.equals("hrupin://laporan_neraca")){
                    url = "file:///android_asset/Laporan_Neraca.html";
                    Intent intent1 = new Intent(MateriWebView1.this, MateriWebView2.class);
                    intent1.putExtra("urlIntent", url);
                    startActivity(intent1);
                    return true;
                }else if (url.equals("hrupin://laporan_perubahan_ekui")){
                    url = "file:///android_asset/Laporan_perubahan_ekuitas.html";
                    Intent intent1 = new Intent(MateriWebView1.this, MateriWebView2.class);
                    intent1.putExtra("urlIntent", url);
                    startActivity(intent1);
                    return true;
                }else if (url.equals("hrupin://laporan_arus_kas")){
                    url = "file:///android_asset/Laporan_Arus_Kas.html";
                    Intent intent1 = new Intent(MateriWebView1.this, MateriWebView2.class);
                    intent1.putExtra("urlIntent", url);
                    startActivity(intent1);
                    return true;
                }else if (url.equals("hrupin://biaya_harus_dibayar")){
                    url = "file:///android_asset/penyesuaian_biaya.html";
                    Intent intent1 = new Intent(MateriWebView1.this, MateriWebView2.class);
                    intent1.putExtra("urlIntent", url);
                    startActivity(intent1);
                    return true;
                }else if (url.equals("hrupin://pendapatan_yang_harus_dibayar")){
                    url = "file:///android_asset/penyesuaian_pendapatan.html";
                    Intent intent1 = new Intent(MateriWebView1.this, MateriWebView2.class);
                    intent1.putExtra("urlIntent", url);
                    startActivity(intent1);
                    return true;
                }else if (url.equals("hrupin://beban_dibayar_dimuka")){
                    url = "file:///android_asset/penyesuaian_bebandibayardimuka.html";
                    Intent intent1 = new Intent(MateriWebView1.this, MateriWebView2.class);
                    intent1.putExtra("urlIntent", url);
                    startActivity(intent1);
                    return true;
                }else if (url.equals("hrupin://pendapatan_dibayar_dimuka")){
                    url = "file:///android_asset/penyesuaian_pendapatandimuka.html";
                    Intent intent1 = new Intent(MateriWebView1.this, MateriWebView2.class);
                    intent1.putExtra("urlIntent", url);
                    startActivity(intent1);
                    return true;
                }else if (url.equals("hrupin://pemakaian_biaya_perlengkapan")){
                    url = "file:///android_asset/penyesuaian_pemakaian_biaya_perlengkapan.html";
                    Intent intent1 = new Intent(MateriWebView1.this, MateriWebView2.class);
                    intent1.putExtra("urlIntent", url);
                    startActivity(intent1);
                    return true;
                }else if (url.equals("hrupin://penyusutan_aset_tetap")){
                    url = "file:///android_asset/penyesuaian_penyusutanaset.html";
                    Intent intent1 = new Intent(MateriWebView1.this, MateriWebView2.class);
                    intent1.putExtra("urlIntent", url);
                    startActivity(intent1);
                    return true;
                }else if (url.equals("hrupin://setoran_modal")){
                    url = "file:///android_asset/setor_modal.html";
                    Intent intent1 = new Intent(MateriWebView1.this, MateriWebView2.class);
                    intent1.putExtra("urlIntent", url);
                    startActivity(intent1);
                    return true;
                }else if (url.equals("hrupin://pembelian_aset")){
                    url = "file:///android_asset/pembelian_aset.html";
                    Intent intent1 = new Intent(MateriWebView1.this, MateriWebView2.class);
                    intent1.putExtra("urlIntent", url);
                    startActivity(intent1);
                    return true;
                }else if (url.equals("hrupin://penjualan_pendapatan")){
                    url = "file:///android_asset/penjualan_aset_pendapatan_jasa.html";
                    Intent intent1 = new Intent(MateriWebView1.this, MateriWebView2.class);
                    intent1.putExtra("urlIntent", url);
                    startActivity(intent1);
                    return true;
                }else if (url.equals("hrupin://utang")){
                    url = "file:///android_asset/utang.html";
                    Intent intent1 = new Intent(MateriWebView1.this, MateriWebView2.class);
                    intent1.putExtra("urlIntent", url);
                    startActivity(intent1);
                    return true;
                }else if (url.equals("hrupin://bayar_biaya")){
                    url = "file:///android_asset/biaya.html";
                    Intent intent1 = new Intent(MateriWebView1.this, MateriWebView2.class);
                    intent1.putExtra("urlIntent", url);
                    startActivity(intent1);
                    return true;
                }else if (url.equals("hrupin://pengembalian_ekuitas")){
                    url = "file:///android_asset/pengembalian ekuitas.html";
                    Intent intent1 = new Intent(MateriWebView1.this, MateriWebView2.class);
                    intent1.putExtra("urlIntent", url);
                    startActivity(intent1);
                    return true;
                }else if (url.equals("hrupin://barter")){
                    url = "file:///android_asset/barter.html";
                    Intent intent1 = new Intent(MateriWebView1.this, MateriWebView2.class);
                    intent1.putExtra("urlIntent", url);
                    startActivity(intent1);
                    return true;
                }
//                Intent intent1 = new Intent(MateriWebView1.this, MateriWebView2.class);
//                intent1.putExtra("urlIntent", url);
//                startActivity(intent1);
//                return super.shouldOverrideUrlLoading(view, url);
                return false;
            }
        });

    }
}
