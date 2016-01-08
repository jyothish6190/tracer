package com.exalture.tracer.Utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Jyothish Jose on 06-01-2016.
 */
public class AlertUtility {
    private static final String TAG = "AlertUtility";

    private static AlertUtility instance = new AlertUtility();

    public void showMessage(Context context, String message, String title) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setTitle(title);
        dialog.setMessage(message);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    public static AlertUtility getInstance() {
        return instance;
    }
}
