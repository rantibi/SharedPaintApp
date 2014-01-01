package com.sharedpaint.operations;

import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import com.sharedpaint.DrawManager;
import com.sharedpaint.drawables.MultiPositionsDrawable;


public class MultiPositionsOperation extends AbsrtcatDrawaingOptionOperation{
	public MultiPositionsOperation(
			Class<? extends MultiPositionsDrawable> drawableClass) {
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
					((MultiPositionsDrawable) currentInstance).addPosition(currentX, currentY);
					return true;
				}
				return false;
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

	public void touchDown(DrawManager drawManager, MotionEvent event) {
		currentInstance = (MultiPositionsDrawable) newDrawableClassInstance();		
		currentX = event.getX();
		currentY = event.getY();
		((MultiPositionsDrawable)currentInstance).addPosition(currentX, currentY);
		currentInstance.setPaint(new Paint(drawManager.getPaint()));
		drawManager.setCurrentDrawable(currentInstance);
	}

}
