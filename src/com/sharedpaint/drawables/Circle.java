package com.sharedpaint.drawables;


import android.graphics.Canvas;


public class Circle extends AbsrtractStartEndPositionDrawable{

	
	private static final long serialVersionUID = 1L;

	@Override
	public void draw(Canvas canvas) {

		float radius = (float)Math.sqrt(Math.pow(startX-endX, 2) + Math.pow(startY-endY, 2));
		canvas.drawCircle(startX, startY, radius, paint);
	}

}
