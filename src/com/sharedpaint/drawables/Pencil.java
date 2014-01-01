package com.sharedpaint.drawables;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.view.View;


public class Pencil implements MultiPositionsDrawable{
	Path path;
	private Paint paint;
	
	public Pencil() {
		path = new Path();
	}
	
	@Override
	public void setPaint(Paint paint) {
		this.paint = paint;
		this.paint.setStyle(Style.STROKE);
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawPath(path, paint);
	}

	public void addPosition(float x, float y) {
		if (path.isEmpty()){
			path.moveTo(x, y);
		}
		
		path.lineTo(x, y);
	}


}
