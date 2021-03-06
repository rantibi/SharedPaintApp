package com.sharedpaint.operations;

import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import com.sharedpaint.DrawManager;
import com.sharedpaint.drawables.StartEndPositionDrawable;

public class StartEndPositionOperation extends AbsrtcatDrawaingOptionOperation {
	
	public StartEndPositionOperation(
			Class<? extends StartEndPositionDrawable> drawableClass) {
		super(drawableClass);
	}

	@Override
	public void onSelect(View view, DrawManager drawManager) {
		// Do nothing
	}

	@Override
	public boolean onTouchEvent(View view, DrawManager drawManager,
			MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touchDown(drawManager, event);
			return false;
		case MotionEvent.ACTION_MOVE:
			if (acceptTouchMove(drawManager, event)){
				((StartEndPositionDrawable)currentInstance).setEndPosition(currentX, currentY);
			}
			return true;
		case MotionEvent.ACTION_UP:
			drawManager.acceptCurrentDrawable();
			return true;
		}

		return false;
	}

	@Override
	public void onDeselect(View view, DrawManager drawManager) {
		// Do nothing
	}

	private void touchDown(DrawManager drawManager, MotionEvent event) {
		currentInstance = (StartEndPositionDrawable) newDrawableClassInstance(drawManager.getDrawableFactory());		
		currentX = event.getX();
		currentY = event.getY();
		((StartEndPositionDrawable)currentInstance).setStartPosition(currentX, currentY);
		currentInstance.setPaint(new Paint(drawManager.getPaint()));
		drawManager.setCurrentDrawable(currentInstance);
	}
}
