package com.sharedpaint.activitys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class MessageBox {

	public static void show(Context context,String message){
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setMessage(message);

		alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		});

		alert.show();
	}
}
