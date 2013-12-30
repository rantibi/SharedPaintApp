package com.sharedpaint.drawables;


import android.graphics.Canvas;
import android.view.View;


public class Circle extends AbsrtractDrawable{

	@Override
	public void draw(View view, Canvas canvas) {

		float radius = (float)Math.sqrt(Math.pow(startX-EndX, 2) + Math.pow(startY-EndY, 2));
		canvas.drawCircle(startX, startY, radius, paint);
	}

}
