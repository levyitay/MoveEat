package com.Rest2Go;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.widget.Toast;

public class ActivityWrapper extends Activity {
	private AsyncTask<?, ?, ?> mLoadingTask;

	public void showLoading(final AsyncTask<?, ?, ?> task) {
		mLoadingTask = task;
		showDialog(ProgressDialog.STYLE_HORIZONTAL);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		ProgressDialog loadingDialog = new ProgressDialog(this);
		loadingDialog.setIndeterminate(true);
		loadingDialog.setMessage("Loading");
		loadingDialog.setCancelable(true);
		loadingDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG);
				if (mLoadingTask != null) {
					mLoadingTask.cancel(true);
				}
			}

		});
		return loadingDialog;
	}

}
