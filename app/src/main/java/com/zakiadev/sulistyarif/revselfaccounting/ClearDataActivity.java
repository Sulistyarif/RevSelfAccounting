package com.zakiadev.sulistyarif.revselfaccounting;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

/**
 * Created by sulistyarif on 03/03/18.
 */

public class ClearDataActivity extends AppCompatActivity {

    private static ClearDataActivity instance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

//        clear data
        ClearDataActivity.getInstance().clearAppData();

//        ngulang activity ke awal
        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

//        digunakan untuk ngulang shared preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firstTime", false);
        editor.putBoolean("firstTime1", false);
        editor.commit();

        startActivity(i);
    }

    public static ClearDataActivity getInstance(){
        return instance;
    }

    public void clearAppData(){
        File cacheDirectory = getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()){
            String[] fileNames = applicationDirectory.list();
            for (String filename : fileNames){
                if (!filename.equals("lib")){
                    hapus(new File(applicationDirectory, filename));
                }
            }
        }
    }

    public static boolean hapus(File file){
        boolean deletedAll = true;
        if (file != null){
            if (file.isDirectory()){
                String[] children = file.list();
                for (int i = 0; i < children.length; i++){
                    deletedAll = hapus(new File(file, children[i])) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }
        return deletedAll;
    }
}
