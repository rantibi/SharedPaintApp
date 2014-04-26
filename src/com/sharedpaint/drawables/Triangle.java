package com.sharedpaint.drawables;

import android.graphics.Canvas;
import android.graphics.Path;

/**
 * Draw triangle object
 */
public class Triangle extends AbsrtractStartEndPositionDrawable{
	private static final long serialVersionUID = 1L;

	@Override
	public void draw(Canvas canvas) {
		float headX = startX + (endX - startX)/2;
		float headY = startY;
		float leftX = startX;
		float leftY = endY;
		float rightX = endX;
		float rigthY = endY;
		
		Path path = new Path();
		path.moveTo(headX, headY);
		path.lineTo(leftX, leftY);
		path.lineTo(rightX, rigthY);
		path.lineTo(headX, headY);
		
		canvas.drawPath(path, paint);
	}

}
