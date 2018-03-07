package com.zakiadev.sulistyarif.revselfaccounting.tasting;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by sulistyarif on 14/02/18.
 */

public class JavaScriptInterface {
    Context context;
    LaporanJurnalUmum laporanJurnalUmum;

    public JavaScriptInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void showToast(String toast){
        Toast.makeText(context,toast,Toast.LENGTH_LONG).show();
    }

}
