package com.sharedpaint.drawables;

/**
 * Represent a drawable object with start and end position
 */
public interface StartEndPositionDrawable  extends Drawable{

	public abstract void setStartPosition(float x, float y);

	public abstract void setEndPosition(float x, float y);

}