package com.sharedpaint.drawables;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public interface Drawable {

	public void draw(View view, Canvas canvas);
	
	public void setStartPosition(float x, float y);
	
	public void updateEndPosition(float x, float y);

	public void setColor(float color);
	
	public void setStyle(Paint.Style style);

	public void setStrokeWidth(int strokeWidth);
	
	public void setPaint(Paint paint);
}
