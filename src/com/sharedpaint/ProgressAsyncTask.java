package com.sharedpaint;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/*
 * Async task with progress dialog
 */
public abstract class ProgressAsyncTask extends AsyncTask<Void, Void, Boolean> {
	private ProgressDialog dialog;
	private Context context;
	private CharSequence title;
	private CharSequence message;
	private Exception exception;

	public ProgressAsyncTask(Context context, CharSequence title,
			CharSequence message) {
		super();
		this.context = context;
		this.title = title;
		this.message = message;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog = ProgressDialog.show(context, title, message);
		pre();
	}

	@Override
	protected Boolean doInBackground(Void... paramArrayOfParams) {

		try {
			background();
		} catch (Exception e) {
			exception = e;
			return false;
		}

		return true;
	}

	protected void onPostExecute(Boolean result) {
		if (result) {
			post();
		}else{
			Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
		}

		dialog.dismiss();
		dialog = null;
	}

	public void pre() {
	}

	public void background() throws Exception {
	};

	public void post() {
	};
}
