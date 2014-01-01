package com.sharedpaint.drawables;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;


public class Line extends AbsrtractStartEndPositionDrawable{

	@Override
	public void draw(Canvas canvas) {
		canvas.drawLine(startX, startY, endX, endY, paint);
	}

}
