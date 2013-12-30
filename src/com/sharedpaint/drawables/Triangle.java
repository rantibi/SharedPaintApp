package com.sharedpaint.drawables;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;


public class Triangle extends AbsrtractDrawable{

	@Override
	public void draw(View view, Canvas canvas) {
		/*float headX = startX + (EndX - startX)/2;
		float headY = startY;
		float leftX = startX;
		float leftY = EndY;
		float rightX = EndX;
		float rigthY = EndY;
		*/
		
		float headX = startX;
		float headY = startY;
		float leftX = startX - (EndX - startX) /2;
		float leftY = EndY;
		float rightX = startX + (EndX - startX) /2;
		float rigthY = EndY;
		
		Path path = new Path();
		path.moveTo(headX, headY);
		path.lineTo(leftX, leftY);
		path.lineTo(rightX, rigthY);
		path.lineTo(headX, headY);
		
		canvas.drawPath(path, paint);
	}

}
