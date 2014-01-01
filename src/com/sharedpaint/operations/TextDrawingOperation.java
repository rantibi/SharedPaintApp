package com.sharedpaint.operations;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.example.myfirstapp.R;
import com.sharedpaint.AddTextDialogFragment;
import com.sharedpaint.AddTextDialogFragment.OnClickSerializableListener;
import com.sharedpaint.DrawManager;
import com.sharedpaint.drawables.Text;

public class TextDrawingOperation extends AbsrtcatDrawaingOptionOperation {

	State state = State.NEW_TEXT;

	public TextDrawingOperation() {
		super(Text.class);
		state = State.NEW_TEXT;
	}

	public void setState(State state) {
		this.state = state;
	}

	@Override
	public void onSelect(View view, DrawManager drawManager) {
		this.state = State.NEW_TEXT;
		currentInstance = null;
	}

	@Override
	public boolean onTouchEvent(View view, DrawManager drawManager,
			MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			state = state.touchDown(this, view, drawManager, event);
			return false;
		case MotionEvent.ACTION_MOVE:
			if (state == State.TEXT_ADDED && acceptTouchMove(drawManager, event)){
				((Text) currentInstance).setPosition(event.getX(), event.getY());
				return true;
			}
			return false;
		case MotionEvent.ACTION_UP:
			if (state == State.TEXT_ADDED){
				drawManager.acceptCurrentDrawable();
				state = State.NEW_TEXT;
			}
			return true;
		}
		return false;
	}

	@Override
	public void onDeselect(View view, DrawManager drawManager) {
		state = State.NEW_TEXT;

		drawManager.acceptCurrentDrawable();
	}

	private enum State {
		NEW_TEXT {
			@Override
			State touchDown(final TextDrawingOperation textOperation,
					final View view, final DrawManager drawManager,
					final MotionEvent event) {
				final float x = event.getX();
				final float y = event.getY();
				
				final AddTextDialogFragment textDialog = AddTextDialogFragment
						.newInstance(new OnClickSerializableListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								EditText editText = (EditText) ((Dialog) dialog)
										.findViewById(R.id.add_text_input);
								if (editText.getText().toString().equals("")) {
									textOperation.setState(NEW_TEXT);
								} else {
									textOperation.setState(State.TEXT_ADDED);
									textOperation.creatTextInstance(drawManager,
											x,y, editText.getText()
													.toString());
									view.invalidate();
								}
							}
						}, new OnClickSerializableListener() {

							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								textOperation.setState(NEW_TEXT);
							}
						});
				textDialog.show(((FragmentActivity) view.getContext())
						.getSupportFragmentManager(), "text_dialog");
				return TEXT_DIALOG_SHOW;
			}
		},
		TEXT_DIALOG_SHOW {
			@Override
			State touchDown(TextDrawingOperation textOperation, View view,
					DrawManager drawManager, MotionEvent event) {
				// TODO Auto-generated method stub
				return NEW_TEXT;
			}
		},
		TEXT_ADDED {
			@Override
			State touchDown(TextDrawingOperation textOperation, View view,
					DrawManager drawManager, MotionEvent event) {
				return TEXT_ADDED;
			}
		};

		State touchDown(TextDrawingOperation textOperation, View view,
				DrawManager drawManager, MotionEvent event) {
			return textOperation.state;
		}
	}

	protected void creatTextInstance(DrawManager drawManager, float x, float y,
			String text) {
		currentInstance = newDrawableClassInstance();
		currentX = x;
		currentY = y;
		((Text) currentInstance).setText(text);
		Paint paint = new Paint(drawManager.getPaint());
		paint.setTextSize(paint.getStrokeWidth() * 3);
		paint.setStrokeWidth(3);
		currentInstance.setPaint(paint);
		((Text)currentInstance).setPosition(currentX, currentY);
		drawManager.setCurrentDrawable(currentInstance);
	}
}
