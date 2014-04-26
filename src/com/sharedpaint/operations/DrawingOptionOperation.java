package com.sharedpaint.operations;

import com.sharedpaint.DrawManager;

import android.view.MotionEvent;
import android.view.View;

/**
 * Draw operation
 */
public interface DrawingOptionOperation {
	
	/**
	 * Call when the operation select as current operation
	 * @param drawManager
	 */
	public void onSelect(View view, DrawManager drawManager);
	
	/**
	 * Call on touch event with action ACTION_UP
	 * @param view
	 * @param drawManager
	 * @param event - The event details
	 * @return does view invalidate needed
	 */
	public boolean onTouchEvent(View view, DrawManager drawManager, MotionEvent event);
	
	/**
	 * Call when the operation deselect, and it not current operation any more
	 * @param drawManager
	 */
	public void onDeselect(View view, DrawManager drawManager);
	
}
