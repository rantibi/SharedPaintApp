package com.sharedpaint.views;

import com.sharedpaint.drawables.Triangle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ColorPickerView extends View {
	private Paint paint;
	private int currentColor = 0;
	private final int[] rgbBarColors = new int[258 + 43 + 43];
	private OnColorChangedListener listener;

	public void setOnColorChangedListener(OnColorChangedListener listener) {
		this.listener = listener;
	}

	private float strokeWidth;
	private Triangle triangle;

	public ColorPickerView(Context c, AttributeSet attr) {
		this(c, null, 0, attr);
	}

	public ColorPickerView(Context c, OnColorChangedListener l, int color,
			AttributeSet attr) {
		super(c, attr);
		listener = l;

		currentColor = 0;
		triangle = new Triangle();
		triangle.setPaint(new Paint());
		// Initialize the colors of the hue slider bar
		int index = 0;

		// black (#000) to red (#f00)
		for (float i = 0; i < 256; i += 256 / 42, index++) {
			rgbBarColors[index] = Color.rgb((int) i, 0, 0);
		}

		// Red (#f00) to pink (#f0f)
		for (float i = 0; i < 256; i += 256 / 42, index++) {
			rgbBarColors[index] = Color.rgb(255, 0, (int) i);
		}

		// Pink (#f0f) to blue (#00f)
		for (float i = 0; i < 256; i += 256 / 42, index++) {
			rgbBarColors[index] = Color.rgb(255 - (int) i, 0, 255);
		}

		// Blue (#00f) to light blue (#0ff)
		for (float i = 0; i < 256; i += 256 / 42, index++) {
			rgbBarColors[index] = Color.rgb(0, (int) i, 255);
		}

		// Light blue (#0ff) to green (#0f0)
		for (float i = 0; i < 256; i += 256 / 42, index++) {
			rgbBarColors[index] = Color.rgb(0, 255, 255 - (int) i);
		}

		// Green (#0f0) to yellow (#ff0)
		for (float i = 0; i < 256; i += 256 / 42, index++) {
			rgbBarColors[index] = Color.rgb((int) i, 255, 0);
		}

		// Yellow (#ff0) to red (#f00)
		for (float i = 0; i < 256; i += 256 / 42, index++) {
			rgbBarColors[index] = Color.rgb(255, 255 - (int) i, 0);
		}

		// Yellow (#ff0) to white(#f00)
		for (float i = 0; i < 256; i += 256 / 42, index++) {
			rgbBarColors[index] = Color.rgb(255, (int) i, (int) i);
		}

		// Initializes the Paint that will draw the View
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setTextSize(12);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		strokeWidth = (float) canvas.getWidth() / rgbBarColors.length;

		float xPoint = strokeWidth / 2;

		// Display all the colors of the hue bar with lines
		for (int x = 0; x < rgbBarColors.length; x++) {
			paint.setColor(rgbBarColors[x]);
			paint.setStrokeWidth(strokeWidth);
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawLine(xPoint, 0, xPoint, canvas.getHeight(), paint);
			xPoint += strokeWidth;
		}

		float xCurrentColor = strokeWidth / 2 + currentColor * strokeWidth;
	
		drawColorPointer(canvas, xCurrentColor);
	}

	private void drawColorPointer(Canvas canvas, float colorXPosition) {
		Path pointerPath = new Path();
		pointerPath.moveTo(colorXPosition, (float) canvas.getHeight() / 5 + 3);
		pointerPath.lineTo(colorXPosition - 23, 0);
		pointerPath.lineTo(colorXPosition + 23, 0);
		pointerPath.lineTo(colorXPosition, (float) canvas.getHeight() / 5 + 3);
		
		Paint pointerPaint = new Paint();
		pointerPaint.setStrokeWidth(2);
		pointerPaint.setColor(Color.BLACK);
		pointerPaint.setStyle(Style.FILL);
		canvas.drawPath(pointerPath, pointerPaint);
		pointerPaint.setColor(Color.WHITE);
		pointerPaint.setStyle(Style.STROKE);
		canvas.drawPath(pointerPath, pointerPaint);
	}

	/*
	 * @Override protected void onMeasure(int widthMeasureSpec, int
	 * heightMeasureSpec) {
	 * setMeasuredDimension(MeasureSpec.getSize(heightMeasureSpec), 40);
	 * 
	 * }
	 */

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		currentColor = (int) (event.getX() / strokeWidth);

		if (listener != null) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				listener.colorSelecting(getColor());
				break;
			case MotionEvent.ACTION_MOVE:
				listener.colorSelecting(getColor());
				break;
			case MotionEvent.ACTION_UP:
				listener.colorSelected(getColor());
				break;
			default:
				break;
			}
		}

		// Force the redraw of the dialog
		invalidate();
		return true;
	}

	public void setColor(int color) {
		for (int i = 0; i < rgbBarColors.length; i++) {
			if (rgbBarColors[i] == color) {
				currentColor = i;
				return;
			}
		}
	}

	public int getColor() {
		return rgbBarColors[currentColor];
	}

	public interface OnColorChangedListener {
		public void colorSelecting(int color);

		public void colorSelected(int color);
	}
}