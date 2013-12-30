package com.sharedpaint.drawables;

import android.graphics.Paint;
import android.graphics.Paint.Style;


public abstract class AbsrtractDrawable implements Drawable{

	protected float startX;
	protected float startY;
	protected float EndY;
	protected float EndX;
	protected Paint paint;

	public AbsrtractDrawable() {
	}
	
	@Override
	public void setPaint(Paint paint) {
		this.paint = paint;
	}
	
	@Override
	public void setStartPosition(float x, float y) {
		startX = x;
		startY = y;
		EndX = x;
		EndY = y;
	}

	@Override
	public void updateEndPosition(float x, float y) {
		EndX = x;
		EndY = y;
	}

	@Override
	public void setColor(float color) {
		paint.setColor((int) color);
	}
	
	@Override
	public void setStrokeWidth(int strokeWidth) {
		paint.setStrokeWidth(strokeWidth);
	}

	@Override
	public void setStyle(Style style) {
		paint.setStyle(style);
	}
}
