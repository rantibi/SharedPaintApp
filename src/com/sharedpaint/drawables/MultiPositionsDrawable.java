package com.sharedpaint.drawables;

public interface MultiPositionsDrawable extends Drawable {
	
	/**
	 * Add new position to the drawable
	 * @param x
	 * @param y
	 */
	public void addPosition(float x, float y);
}
