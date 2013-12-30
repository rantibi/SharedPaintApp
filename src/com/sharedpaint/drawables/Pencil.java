package com.sharedpaint.drawables;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.view.View;


public class Pencil extends AbsrtractDrawable{
	Path path;
	
	public Pencil() {
		path = new Path();
	}
	
	@Override
	public void setPaint(Paint paint) {
		super.setPaint(paint);
		paint.setStyle(Style.STROKE);
	}
	
	@Override
	public void draw(View view, Canvas canvas) {
		canvas.drawPath(path, paint);
	}

	@Override
	public void setStartPosition(float x, float y) {
		path.moveTo(x, y);
		path.lineTo(x, y);
	}

	@Override
	public void updateEndPosition(float x, float y) {
		path.lineTo(x, y);
	}

}
