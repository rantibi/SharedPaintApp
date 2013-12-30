package com.sharedpaint;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.example.myfirstapp.R;
import com.sharedpaint.drawables.Drawable;
import com.sharedpaint.drawables.Pencil;
import com.sharedpaint.views.ColorPickerView;
import com.sharedpaint.views.ColorPickerView.OnColorChangedListener;

public class DrawPaintActivity extends Activity {
	private DrawView drawView;
	private DrawManger drawManager;
	private Menu menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create a GLSurfaceView instance and set it
		// as the ContentView for this Activity.
		drawManager = new DrawManger();
		/*
		 * drawView = new DrawView(this); drawView.setDrawManager(drawManager);
		 * /*SeekBar seekBar = new SeekBar(this); LinearLayout layout = new
		 * LinearLayout(this); layout.addView(seekBar);
		 * layout.addView(drawView);
		 */
		setContentView(R.layout.draw_paint_activitiy);
		drawView = (DrawView) findViewById(R.id.draw_view);
		drawView.setDrawManager(drawManager);

		ColorPickerView colorPickerView = (ColorPickerView) findViewById(R.id.paint_color_picker_view);
		colorPickerView.setOnColorChangedListener(new OnColorChangedListener() {

			@Override
			public void colorSelecting(int color) {
				setPaintColor(color);
			}

			@Override
			public void colorSelected(int color) {
				colorSelecting(color);
				View shapeScroolView = findViewById(R.id.paint_color_picker_view);
				shapeScroolView.setVisibility(View.GONE);
			}

		});

		SeekBar strokeWidthseekBar = (SeekBar) findViewById(R.id.stroke_width_seek_bar);
		strokeWidthseekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						drawManager.setStrokeWidth(seekBar.getProgress() + 1);
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub

					}
				});

		setDefaultValues();
	}

	private void setDefaultValues() {
		setSelectedShape(Pencil.class, getResources().getText(R.string.pencil));
		setPaintColor(Color.BLUE);
		setPaintStrokeWidth(10);
		setPaintStyle(Style.STROKE, getResources().getText(R.string.stroke));
	}

	private void setPaintStrokeWidth(int strokeWidth) {
		SeekBar strokeWidthSeekBar = (SeekBar) findViewById(R.id.stroke_width_seek_bar);
		strokeWidthSeekBar.setProgress(strokeWidth);
		drawManager.setStrokeWidth(strokeWidth);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.open_gles20, menu);
		this.menu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_cancel_selected:
			drawManager.cancelLastDrawable();
			drawView.invalidate();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setPaintColor(int color) {
		drawManager.setColor(color);
		Button colorButton = (Button) findViewById(R.id.selected_color_button);
		((GradientDrawable) ((StateListDrawable) colorButton.getBackground())
				.getCurrent()).setColor(color);
	}
	
	public void onFillShapeSelectedClick(View view) {
		
	}

	public void onShapeChooseClick(View view) {
		try {
			setSelectedShape(
					(Class<? extends Drawable>) Class.forName((String) ((Button) view)
							.getTag()), ((Button) view).getText());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		View shapeScroolView = findViewById(R.id.shape_scrool_view);
		shapeScroolView.setVisibility(View.GONE);
	}

	private void setSelectedShape(Class<? extends Drawable> drawable,
			CharSequence text) {
		drawManager.setDrawbleClass(drawable);
		Button selectedShape = (Button) findViewById(R.id.selected_shape_button);
		selectedShape.setText(text);
	}

	public void onSelecedColorClick(View view) {
		View shapeScroolView = findViewById(R.id.paint_color_picker_view);
		shapeScroolView.setVisibility(View.VISIBLE);

	}

	public void onSelectedShapeClick(View view) {
		View shapeScroolView = findViewById(R.id.shape_scrool_view);
		shapeScroolView.setVisibility(View.VISIBLE);
	}

	public void onSelecedStyleButtonClick(View view) {
		View shapeScroolView = findViewById(R.id.style_scrool_view);
		shapeScroolView.setVisibility(View.VISIBLE);
	}
	
	
	public void onStyleSelectedClick(View view) {
		setPaintStyle(Style.valueOf((String) view.getTag()), ((Button)view).getText());
		View shapeScroolView = findViewById(R.id.style_scrool_view);
		shapeScroolView.setVisibility(View.GONE);

	}

	private void setPaintStyle(Style style, CharSequence buttonText) {
		drawManager.setStyle(style);
		Button selectedShape = (Button) findViewById(R.id.selected_style_button);
		selectedShape.setText(buttonText);
	}
}
