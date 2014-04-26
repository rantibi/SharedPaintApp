package com.sharedpaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.util.AttributeSet;
import android.view.View;

/*
 * Stroked view
 */
public class StrokeWidthView extends View{
	float strokeWidth;
	private Paint paint;
	
	public StrokeWidthView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setStrokeWidth(strokeWidth);
		paint.setAntiAlias(false);
		paint.setDither(false);
		paint.setStrokeCap(Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);
		canvas.drawLine(this.getWidth() / 2, this.getHeight() /2,
				this.getWidth() / 2 + 1, this.getHeight() /2,
				paint);
	}

	public void setStrokeWidth(float strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	public void setPaint(Paint paint) {
		this.paint = new Paint(paint);
	}
	
	
}
