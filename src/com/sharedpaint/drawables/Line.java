package com.sharedpaint.drawables;

import android.graphics.Canvas;


public class Line extends AbsrtractStartEndPositionDrawable{

	
	private static final long serialVersionUID = 1L;

	@Override
	public void draw(Canvas canvas) {
		canvas.drawLine(startX, startY, endX, endY, paint);
	}

}
