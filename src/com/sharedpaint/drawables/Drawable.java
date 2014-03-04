package com.sharedpaint.drawables;

import java.io.Serializable;

import android.graphics.Canvas;
import android.graphics.Paint;

public interface Drawable extends Serializable{

	public void draw(Canvas canvas); 

	public void setPaint(Paint paint); 
	
	public long getId();
	
	public void setId(long id);
}
