package com.graduan.graduanapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.graduan.graduanapp.R;

public class LoadingDialog {
    private Dialog loadingDialog;
    private ColorDrawable dialogBackground;
    private Context context;
    private static LoadingDialog _instance = null;

    public static LoadingDialog getInstance() {
        return _instance = _instance == null ? new LoadingDialog() : _instance;
    }

    public LoadingDialog with(Context context) {
        this.context = context;
        return _instance;
    }

    public LoadingDialog isLoading() {
        if (loadingDialog != null) {
            if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
        }
        if (dialogBackground == null)
            dialogBackground = new ColorDrawable(Color.TRANSPARENT);
        loadingDialog = new Dialog(context);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.getWindow().setBackgroundDrawable(dialogBackground);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        return _instance;
    }

    public void dismissLoading() {
        if (loadingDialog != null && loadingDialog.isShowing())
            loadingDialog.dismiss();
    }
}
