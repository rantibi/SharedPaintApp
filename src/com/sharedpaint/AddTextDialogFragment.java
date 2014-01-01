package com.sharedpaint;

import java.io.Serializable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import com.example.myfirstapp.R;

public class AddTextDialogFragment extends DialogFragment {
	public static String OK_LISTENER = "OK_LISTENER";
	public static String CANCEL_LISTENER = "CANCEL_LISTENER";

	public static AddTextDialogFragment newInstance(
			OnClickSerializableListener okListener,
			OnClickSerializableListener cancelListener) {
		AddTextDialogFragment textDialog = new AddTextDialogFragment();
		Bundle bundle = new Bundle(2);
		bundle.putSerializable(AddTextDialogFragment.OK_LISTENER, okListener);
		bundle.putSerializable(AddTextDialogFragment.CANCEL_LISTENER,
				cancelListener);
		textDialog.setArguments(bundle);
		return textDialog;
	}
     
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		builder.setView(inflater.inflate(R.layout.add_text_dialog, null));
		builder.setMessage(R.string.type_your_text)
				.setPositiveButton(android.R.string.ok,
						(OnClickListener) getArguments().get(OK_LISTENER))
				.setNegativeButton(
						android.R.string.cancel,
						(OnClickListener) getArguments()
								.get(CANCEL_LISTENER));
		// Create the AlertDialog object and return it
		return builder.create();
	}

	public interface OnClickSerializableListener extends OnClickListener, Serializable {

	}
}
