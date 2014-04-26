package com.sharedpaint.operations;

import android.view.MotionEvent;

import com.sharedpaint.DrawManager;
import com.sharedpaint.DrawableFactory;
import com.sharedpaint.drawables.Drawable;

/**
 * Basic implementation for drawing operation
 */
public abstract class AbsrtcatDrawaingOptionOperation implements DrawingOptionOperation{

	protected static final float TOUCH_TOLERANCE = 4;
	protected Drawable currentInstance;
	protected float currentX;
	protected float currentY;
	protected Class<? extends Drawable> drawableClass;
	
	public AbsrtcatDrawaingOptionOperation(
			Class<? extends Drawable> drawableClass ) {
		this.drawableClass = drawableClass;
	}
	
	protected Drawable newDrawableClassInstance(DrawableFactory drawableFactory){
		return drawableFactory.newDrawable(drawableClass);
	}
	
	/**
	 * This method set currentX and currentY to event x,y if the move is larger then the tolerance
	 * @param drawManager
	 * @param event
	 * @return
	 */
	protected boolean acceptTouchMove(DrawManager drawManager, MotionEvent event) {
		float dx = Math.abs(event.getX() - currentX);
		float dy = Math.abs(event.getY() - currentY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			currentX = event.getX();
			currentY = event.getY();
			return true;
		}
		
		return false;
	}
}
