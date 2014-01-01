package com.sharedpaint.drawables;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Text implements Drawable{

	private String text;
	private float x;
	private float y;
	private Paint paint;

	@Override
	public void draw(Canvas canvas) {
		canvas.drawText(text, x, y, paint);
	}

	@Override
	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	public void setPosition(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void setText(String text) {
		this.text = text;
	}

}
