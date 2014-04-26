package com.sharedpaint.drawables;

/**
 * Interface represent multi position object(example: Pencil)
 */
public interface MultiPositionsDrawable extends Drawable {
	
	/**
	 * Add new position to the drawable
	 * @param x
	 * @param y
	 */
	public void addPosition(float x, float y);
}
