package com.sharedpaint.drawables;

import android.graphics.Canvas;


public class Rectangle extends AbsrtractStartEndPositionDrawable{

	private static final long serialVersionUID = 1L;

	@Override
	public void draw(Canvas canvas) {
		canvas.drawRect(startX, startY, endX, endY, paint);
	}

}
