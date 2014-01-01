package com.sharedpaint.drawables;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;


public class Rectangle extends AbsrtractStartEndPositionDrawable{

	@Override
	public void draw(Canvas canvas) {
		canvas.drawRect(startX, startY, endX, endY, paint);
	}

}
