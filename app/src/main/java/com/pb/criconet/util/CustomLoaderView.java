package com.pb.criconet.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.pb.criconet.R;

import java.util.Objects;

public class CustomLoaderView {
    private Dialog pDialog;

    private CustomLoaderView(Context context){
        pDialog = new Dialog(context);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.custom_dialog_progress);
        Objects.requireNonNull(pDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
    }

    private CustomLoaderView(Context context, String msg){
        pDialog = new Dialog(context);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.custom_dialog_progress_video);
        Objects.requireNonNull(pDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
    }


    public static CustomLoaderView initialize(Context context){
        return new CustomLoaderView(context);
    }
    public static CustomLoaderView initialize(Context context,String msg){
        return new CustomLoaderView(context,msg);
    }

    public void showLoader(){
        if(pDialog != null)
            pDialog.show();
    }

    public void hideLoader(){
        if(pDialog != null) {
            pDialog.dismiss();
            pDialog.hide();
        }
    }
}
