package com.sharedpaint.activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.sharedpaint.DrawManager;
import com.sharedpaint.DrawManagerListener;
import com.sharedpaint.DrawView;
import com.sharedpaint.DrawableFactory;
import com.sharedpaint.DrawableIdsIterator;
import com.sharedpaint.DrawingOption;
import com.sharedpaint.ProgressAsyncTask;
import com.sharedpaint.R;
import com.sharedpaint.ShowStrokeWidthDialogFragment;
import com.sharedpaint.StrokeWidthView;
import com.sharedpaint.connection.ServerProxy;
import com.sharedpaint.drawables.Drawable;
import com.sharedpaint.operations.BitmapUtility;
import com.sharedpaint.transfer.BoardDetails;
import com.sharedpaint.transfer.BoardUpdate;
import com.sharedpaint.views.ColorPickerView;
import com.sharedpaint.views.ColorPickerView.OnColorChangedListener;

public class DrawPaintActivity extends FragmentActivity {
	public static final String BOARD = "BOARD";
	private static final String DRAW_MANAGER = "DRAW_MANAGER";
	private static final String DRAWABLE_IDS_ITERATOR = "DRAWABLE_IDS_ITERATOR";
	private DrawView drawView;
	private DrawManager drawManager;


	private BoardDetails boardDetails;
	private DrawableIdsIterator drawableIdsIterator;
	private DrawablesUpdater updater;
	private DrawablesAdder adder;

	public DrawView getDrawView() {
		return drawView;
	}
	
	public DrawManager getDrawManager() {
		return drawManager;
	}
	
	public BoardDetails getBoardDetails() {
		return boardDetails;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//drawManager.setDrawManagerListener(null);
		outState.putSerializable(DRAW_MANAGER, drawManager);
		outState.putSerializable(DRAWABLE_IDS_ITERATOR, drawableIdsIterator);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.draw_paint_activitiy);

		boardDetails = (BoardDetails) getIntent().getSerializableExtra(BOARD);
		setTitle(boardDetails.getName());
		adder = new DrawablesAdder(this);

		if (savedInstanceState == null) {
			drawableIdsIterator = new DrawableIdsIterator(boardDetails);
			drawManager = new DrawManager(new DrawableFactory(
					drawableIdsIterator));
			setDefaultValues();
		} else {
			drawableIdsIterator = (DrawableIdsIterator) savedInstanceState
					.getSerializable(DRAWABLE_IDS_ITERATOR);
			drawManager = (DrawManager) savedInstanceState
					.getSerializable(DRAW_MANAGER);
			setSelectedDrawingOption(drawManager.getDrowingOption());
			setPaintColor(drawManager.getPaint().getColor(), true);
			setPaintStrokeWidth(drawManager.getPaint().getStrokeWidth());
			setPaintStyle(drawManager.getPaint().getStyle());
		}
		
		setDrawManagerListener();
		setDrawView();
		addColorPickerView();
		addStokeWidthListener();
		loadDrawables();
	}

	private void setDrawManagerListener() {
		drawManager.setDrawManagerListener(new DrawManagerListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void drawableAccept(final Drawable drawable) {
				adder.addDrawable(drawable);
			}
		});
	}

	private void loadDrawables() {
		new ProgressAsyncTask(this, getString(R.string.loading),
				getString(R.string.please_wait)) {
			@Override
			public void background() throws Exception {
				BoardUpdate boardUpdate = ServerProxy.getInstance().
						getDrawablesInBoard(boardDetails.getId());
				drawManager.updateBoard(boardUpdate);
			};

			@Override
			public void post() {
				drawView.invalidate();
			};
		}.execute((Void) null);

		updater = new DrawablesUpdater(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		updater.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		updater.stop();
	}

	private void setDrawView() {
		drawView = (DrawView) findViewById(R.id.draw_view);
		drawView.setDrawManager(drawManager);
		drawView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					hideAllExtraMenusBar();
				}

				v.onTouchEvent(event);
				return true;
			}
		});
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
		
		
		
		if(boardDetails.getManagerEmail().equals(ServerProxy.getInstance().getUserEmail())){
			menu.findItem(R.id.action_leave_board).setVisible(false);
		}else{
			menu.findItem(R.id.action_delete_board).setVisible(false);
		}
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_undo_selected:
			// drawManager.undolLastDrawable();
			actionUndoSelected();
			drawView.invalidate();
			break;
		case R.id.action_redo_selected:
			// drawManager.redolLastDrawable();
			actionRedoSelected();
			break;
		case R.id.action_add_member:
			Intent intent = new Intent(this, MembersManageActivity.class);
			intent.putExtra(BOARD, boardDetails);
			startActivity(intent);
			break;
		case R.id.action_save_as_picture:  
			saveAsPicture();
			break;
		case R.id.action_delete_board:
			deleteBoard();
			break;
		case R.id.action_leave_board:
			leaveBoard();
			break;
		default:

		}
		return super.onOptionsItemSelected(item);
	}

	private void deleteBoard() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		        	new ProgressAsyncTask(DrawPaintActivity.this, getString(R.string.deleting),
		    				getString(R.string.please_wait)) {
		    			@Override
		    			public void background() throws Exception {
		    				ServerProxy.getInstance().deleteBoard(
		    						boardDetails.getId());
		    			};

		    			@Override
		    			public void post() {
		    				Toast.makeText(DrawPaintActivity.this, getString(R.string.delete_board_success_message), Toast.LENGTH_LONG).show();
		    				finish();
		    			};
		    		}.execute((Void) null);		

		        	break;

		        case DialogInterface.BUTTON_NEGATIVE:
		        	// Do nothing
		        	break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.delete_board_sure_message)).setPositiveButton(android.R.string.yes, dialogClickListener)
		    .setNegativeButton(android.R.string.no, dialogClickListener).show();
			}

	private void leaveBoard() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		        	new ProgressAsyncTask(DrawPaintActivity.this, getString(R.string.leaving),
		    				getString(R.string.please_wait)) {
		    			@Override
		    			public void background() throws Exception {
		    				ServerProxy.getInstance().leaveBoard(
		    						boardDetails.getId());
		    			};

		    			@Override
		    			public void post() {
		    				Toast.makeText(DrawPaintActivity.this, getString(R.string.leave_board_success_message), Toast.LENGTH_LONG).show();
		    				finish();
		    			};
		    		}.execute((Void) null);		

		        	break;

		        case DialogInterface.BUTTON_NEGATIVE:
		        	// Do nothing
		        	break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.leave_board_sure_message)).setPositiveButton(android.R.string.yes, dialogClickListener)
		    .setNegativeButton(android.R.string.no, dialogClickListener).show();
			}

	
	private void actionUndoSelected() {
		new ProgressAsyncTask(this, getString(R.string.undo_drawable_title),
				getString(R.string.please_wait)) {
			@Override
			public void background() throws Exception {
				ServerProxy.getInstance().undoInBoard(
						boardDetails.getId());
			};

			@Override
			public void post() {
				drawView.invalidate();
			};
		}.execute((Void) null);
	}

	
	private void actionRedoSelected() {
		new ProgressAsyncTask(this, getString(R.string.redo_drawable_title),
				getString(R.string.please_wait)) {
			@Override
			public void background() throws Exception {
				ServerProxy.getInstance().redoInBoard(
						boardDetails.getId());	
			};

			@Override
			public void post() {
				drawView.invalidate();
			};
		}.execute((Void) null);
	}

	private void saveAsPicture() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Save as picture");
		alert.setMessage("Insert picture name");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		input.setText(Long.toString(System.currentTimeMillis() / 1000));
		alert.setView(input);

		alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String filename = input.getText().toString();
				Bitmap bitmap = drawManager.getCurrentViewAsBitmap();

				if (BitmapUtility.saveBitmapToExternalStorage(bitmap,
						"SharedPaint", filename, DrawPaintActivity.this)) {
					MessageBox.show(DrawPaintActivity.this, "Picture saved",
							getString(R.string.save));
				} else {
					MessageBox.show(DrawPaintActivity.this, "Saved failed",
							getString(R.string.save));
				}

				dialog.dismiss();
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Do nothing
					}
				});

		alert.show();
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
		menuBarClick(R.id.paint_color_picker_view);
	}

	public void onSelecteDrawOptionClick(View view) {
		menuBarClick(R.id.draw_option_scrool_view);
	}

	public void onSelecetStyleButtonClick(View view) {
		menuBarClick(R.id.style_scrool_view);
	}

	public void onStyleSelectedClick(View view) {
		setPaintStyle(Style.valueOf((String) view.getTag()));
		hideAllExtraMenusBar();
	}

	public void menuBarClick(int id) {
		View view = findViewById(id);
		int visibility = view.getVisibility();
		hideAllExtraMenusBar();

		if (visibility == View.GONE) {
			view.setVisibility(View.VISIBLE);
		}
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
