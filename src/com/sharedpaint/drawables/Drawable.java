package com.sharedpaint.drawables;

import java.io.Serializable;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Drawable object interface - all object implement this interface
 */
public interface Drawable extends Serializable, Comparable<Drawable>{

	public void draw(Canvas canvas); 

	public void setPaint(Paint paint); 
	
	public long getId();
	
	public void setId(long id);
	
	public void setTime(long time);
	
	public long getTime();
}
