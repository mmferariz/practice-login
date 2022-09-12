package com.mferariz.myapplication.ui.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.mferariz.myapplication.R;

public class CustomDialog {

    private Activity activity;
    private AlertDialog dialog;

    public CustomDialog(Activity activity) {
        this.activity = activity;
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog (){
        if(dialog != null){
            dialog.dismiss();
        }
    }

    public static void showSnackbar(String text, View view) {
        if (view != null) {
            Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
        }
    }

    public static void showSnackbar(String text, View view, SnackbarType snackbarType) {
        if (view != null) {
            Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint( ContextCompat.getColor(view.getContext() ,snackbarType == SnackbarType.ALERT ? R.color.alert : R.color.error) );
            snackbar.show();
        }
    }

}

