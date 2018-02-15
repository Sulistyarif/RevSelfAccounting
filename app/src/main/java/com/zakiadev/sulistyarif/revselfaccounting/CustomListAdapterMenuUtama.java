package com.zakiadev.sulistyarif.revselfaccounting;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.PublicKey;

/**
 * Created by Sulistyarif on 31/01/2018.
 */

public class CustomListAdapterMenuUtama extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] nama;
    private final int[] gambar;


    public CustomListAdapterMenuUtama(Activity context, String[] nama, int[] gambar) {
        super(context, R.layout.child_menu_utama, nama);
        this.context = context;
        this.nama = nama;
        this.gambar = gambar;
    }

    public View getView(int pos, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.child_menu_utama, null, true);

        TextView tvMenu = (TextView) rowView.findViewById(R.id.tvMenuUtama);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.ivMenuUtama);

        tvMenu.setText(nama[pos]);
        imageView.setImageResource(gambar[pos]);

        return rowView;

    }

}
