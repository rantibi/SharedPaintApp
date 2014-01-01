package com.sharedpaint;

import com.example.myfirstapp.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class ShowStrokeWidthDialogFragment extends DialogFragment {

	private static final String STROKE_COLOR = "STROKE_COLOR";
	private static final String STROKE_WIDTH = "STROKE_WIDTH";
	private StrokeWidthView strokeWidthView;

	public static ShowStrokeWidthDialogFragment newInstance(float strokeWidth,
			Paint paint) {
		ShowStrokeWidthDialogFragment dialog = new ShowStrokeWidthDialogFragment();
		Bundle bundle = new Bundle(2);
		bundle.putFloat(STROKE_WIDTH, strokeWidth);
		bundle.putInt(STROKE_COLOR, paint.getColor());
		dialog.setArguments(bundle);
		return dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View inflate = inflater
				.inflate(R.layout.show_stroke_width_dialog, null);
		strokeWidthView = (StrokeWidthView) inflate
				.findViewById(R.id.stroke_width_view);
		strokeWidthView.setStrokeWidth(getArguments().getFloat(STROKE_WIDTH));
		Paint paint = new Paint();
		paint.setAntiAlias(false);
		paint.setDither(false);
		paint.setStrokeCap(Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setColor(getArguments().getInt(STROKE_COLOR));
		strokeWidthView.setPaint(paint);
		builder.setView(inflate);

		// Create the AlertDialog object and return it
		AlertDialog alertDialog = builder.create();
		return alertDialog;
	}

	@Override
	public void onStart() {
		super.onStart();

		  // safety check
		  if (getDialog() == null)
		    return;

		  int dialogWidth = 210;// specify a value here
		  int dialogHeight = LayoutParams.WRAP_CONTENT; // specify a value here

		  getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LayoutParams params = getDialog().getWindow().getAttributes();
		params.width = 70;
		getDialog().getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public StrokeWidthView getStrokeWidthView() {
		return strokeWidthView;
	}

}
