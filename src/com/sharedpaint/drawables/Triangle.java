package com.sharedpaint.drawables;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;


public class Triangle extends AbsrtractStartEndPositionDrawable{

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
