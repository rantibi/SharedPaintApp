package com.sharedpaint.drawables;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;


public class Rect extends AbsrtractDrawable{

	@Override
	public void draw(View view, Canvas canvas) {
		canvas.drawRect(startX, startY, EndX, EndY, paint);
	}

}
