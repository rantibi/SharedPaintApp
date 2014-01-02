package com.sharedpaint;

import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.example.myfirstapp.R;
import com.sharedpaint.views.ColorPickerView;
import com.sharedpaint.views.ColorPickerView.OnColorChangedListener;

public class DrawPaintActivity extends FragmentActivity {
	private static final String DRAW_MANAGER = "DRAW_MANAGER";
	private DrawView drawView;
	private DrawManager drawManager;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(DRAW_MANAGER, drawManager);
	
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.draw_paint_activitiy);
		
		if (savedInstanceState == null){
			drawManager = new DrawManager();
			setDefaultValues();
		}else{
			drawManager = (DrawManager) savedInstanceState.getSerializable(DRAW_MANAGER);
			setSelectedDrawingOption(drawManager.getDrowingOption());
			setPaintColor(drawManager.getPaint().getColor(), true);
			setPaintStrokeWidth(drawManager.getPaint().getStrokeWidth());
			setPaintStyle(drawManager.getPaint().getStyle());
		}
		
		drawView = (DrawView) findViewById(R.id.draw_view);
		drawView.setDrawManager(drawManager);
		addColorPickerView();
		addStokeWidthListener();
	}


	private void addColorPickerView() {
		ColorPickerView colorPickerView = (ColorPickerView) findViewById(R.id.paint_color_picker_view);
		colorPickerView.setOnColorChangedListener(new OnColorChangedListener() {

			@Override
			public void colorSelecting(int color) {
				setPaintColor(color, false);
			}

			@Override
			public void colorSelected(int color) {
				colorSelecting(color);
				hideAllExtraMenusBar();
			}

		});
	}


	private void addStokeWidthListener() {
		SeekBar strokeWidthseekBar = (SeekBar) findViewById(R.id.stroke_width_seek_bar);
		strokeWidthseekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					private ShowStrokeWidthDialogFragment strokeWidthDialog;

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						drawManager.getPaint().setStrokeWidth(
								seekBar.getProgress() + 1);
						if (strokeWidthDialog != null) {
							strokeWidthDialog.dismiss();
						}
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						if (strokeWidthDialog == null) {
							strokeWidthDialog = ShowStrokeWidthDialogFragment.newInstance(
									drawManager.getPaint().getColor(),
									drawManager.getPaint());
						}
						strokeWidthDialog.show(getSupportFragmentManager(),
								"show_stroke_width");

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						if (strokeWidthDialog != null) {
							StrokeWidthView strokeWidthView = strokeWidthDialog
									.getStrokeWidthView();
						
							if (strokeWidthView != null) {
								strokeWidthView.setStrokeWidth(seekBar
										.getProgress() + 1);
								strokeWidthView.invalidate();
							}
						}
					}
				});
	}

	private void setDefaultValues() {
		setSelectedDrawingOption(DrawingOption.PENCIL);
		setPaintColor(Color.BLUE, true);
		setPaintStrokeWidth(10);
		setPaintStyle(Style.STROKE);
	}

	private void setPaintStrokeWidth(float strokeWidth) {
		SeekBar strokeWidthSeekBar = (SeekBar) findViewById(R.id.stroke_width_seek_bar);
		strokeWidthSeekBar.setProgress((int) strokeWidth);
		drawManager.getPaint().setStrokeWidth(strokeWidth);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.darw_paint_activitiy_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_undo_selected:
			drawManager.undolLastDrawable();
			drawView.invalidate();
			return true;
		case R.id.action_redo_selected:
			drawManager.redolLastDrawable();
			drawView.invalidate();

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setPaintColor(int color, boolean updatePiker) {
		if (updatePiker) {
			ColorPickerView colorPickerView = (ColorPickerView) findViewById(R.id.paint_color_picker_view);
			colorPickerView.setColor(color);
		}

		drawManager.getPaint().setColor(color);
		Button colorButton = (Button) findViewById(R.id.selected_color_button);
		((GradientDrawable) ((StateListDrawable) colorButton.getBackground())
				.getCurrent()).setColor(color);
	}

	public void onTextDrawingOptionSelected(View view) {
		onDrawingOptionSelected(view);

	}

	public void onDrawingOptionSelected(View view) {
		DrawingOption.valueOf((String) view.getTag()).getDrawingOperation()
				.onSelect(drawView, drawManager);
		setSelectedDrawingOption(DrawingOption.valueOf((String) view.getTag()));
		hideAllExtraMenusBar();
	}

	private void setSelectedDrawingOption(DrawingOption drawableOption) {
		DrawingOption lastDrowingOption = drawManager.getDrowingOption();

		if (lastDrowingOption != null) {
			lastDrowingOption.getDrawingOperation().onDeselect(drawView,
					drawManager);
		}

		drawManager.setCurrentDrawingOption(drawableOption);
		drawableOption.getDrawingOperation().onSelect(drawView, drawManager);
		ImageButton selectedDrawingOption = (ImageButton) findViewById(R.id.selected_draw_option_button);
		selectedDrawingOption
				.setImageResource(drawableOption.getIconResource());
	}

	public void onSelectColorClick(View view) {
		hideAllExtraMenusBar();
		showMenuBar(R.id.paint_color_picker_view);

	}

	public void onSelecteDrawOptionClick(View view) {
		hideAllExtraMenusBar();
		showMenuBar(R.id.draw_option_scrool_view);
	}

	public void onSelecetStyleButtonClick(View view) {
		hideAllExtraMenusBar();
		showMenuBar(R.id.style_scrool_view);
	}

	public void onStyleSelectedClick(View view) {
		setPaintStyle(Style.valueOf((String) view.getTag()));
		hideAllExtraMenusBar();
	}

	public void showMenuBar(int id) {
		View view = findViewById(id);
		view.setVisibility(View.VISIBLE);

	}

	public void hideAllExtraMenusBar() {
		int[] menuBarsId = { R.id.draw_option_scrool_view,
				R.id.paint_color_picker_view, R.id.style_scrool_view };

		for (int id : menuBarsId) {
			View view = findViewById(id);
			if (view.getVisibility() != View.GONE) {
				view.setVisibility(View.GONE);
			}
		}
	}

	private void setPaintStyle(Style style) {
		drawManager.getPaint().setStyle(style);
		ImageButton selectedShape = (ImageButton) findViewById(R.id.selected_style_button);
		int resId = 0;
		switch (style) {
		case FILL:
			resId = R.drawable.fill_style;
			break;
		case STROKE:
			resId = R.drawable.stroke_style;
			break;
		case FILL_AND_STROKE:
			resId = R.drawable.stroke_and_fill_style;
			break;
		default:
			break;
		}

		selectedShape.setImageResource(resId);
	}
}
