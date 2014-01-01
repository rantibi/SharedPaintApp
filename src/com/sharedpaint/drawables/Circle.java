package com.sharedpaint.drawables;


import android.graphics.Canvas;
import android.view.View;


public class Circle extends AbsrtractStartEndPositionDrawable{

	@Override
	public void draw(Canvas canvas) {

		float radius = (float)Math.sqrt(Math.pow(startX-endX, 2) + Math.pow(startY-endY, 2));
		canvas.drawCircle(startX, startY, radius, paint);
	}

}
