package com.sharedpaint.drawables;

import android.graphics.Paint;


public abstract class AbsrtractStartEndPositionDrawable implements StartEndPositionDrawable{

	protected float startX;
	protected float startY;
	protected float endY;
	protected float endX;
	protected Paint paint;

	
	@Override
	public void setPaint(Paint paint) {
		this.paint = paint;
	}
	
	
	@Override
	public void setStartPosition(float x, float y) {
		startX = x;
		startY = y;
		endX = x;
		endY = y;
	}

	
	
	@Override
	public void setEndPosition(float x, float y) {
		endX = x;
		endY = y;
	}
}
