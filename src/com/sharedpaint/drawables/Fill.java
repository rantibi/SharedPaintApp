package com.sharedpaint.drawables;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.View;

public class Fill extends AbsrtractDrawable {

	private LinkedList<Float> points;

	@Override
	public void draw(View view, Canvas canvas) {
		if (points == null) {
			points = new LinkedList<Float>();
			Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
			fill(bitmap, (int) EndX, (int) EndY,
					bitmap.getPixel((int) EndX, (int) EndY), paint.getColor());
		} else {
			float[] pts = new float[points.size()];

			for (int i = 0; i < pts.length; i++) {
				pts[i] = points.get(i);
			}

			canvas.drawPoints(pts, paint);
		}
	}

	
	
	private void fill(Bitmap bitmap, int x, int y, int startPointColor,
			int fillColor) {
		float[][] mat = new float[bitmap.getWidth()][bitmap.getHeight()];
		
		for (int i = 0; i < bitmap.getWidth(); i++) {
			for (int j = 0; j < bitmap.getHeight(); j++) {
				mat[i][j] = bitmap.getPixel(i, j); 
			}
		}
		
		fill(mat, x, y, startPointColor, fillColor);

		
	}



	private void fill(float[][] mat, int x, int y, int startPointColor,
			int fillColor) {
		mat[x][y] = fillColor;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (x + i >= 0 && x + i <= mat.length && y + j >= 0
						&& y + j <= mat[0].length
						&& mat[x + i][y + j] == startPointColor) {
					fill(mat, x + i, y + j, startPointColor, fillColor);
				}
			}
		}
	}

	/*
	 * private void fill(Bitmap bitmap, int x, int y, int startPointColor, int
	 * fillColor) { Queue<Point> pointToCheck = new LinkedList<Point>();
	 * pointToCheck.add(new Point(x, y));
	 * 
	 * while (!pointToCheck.isEmpty()) { Point currPoint = pointToCheck.poll();
	 * points.add((float) currPoint.x); points.add((float) currPoint.y);
	 * bitmap.setPixel(currPoint.x, currPoint.y, fillColor);
	 * 
	 * for (int i = -1; i <= 1; i++) { for (int j = -1; j <= 1; j++) { if
	 * (currPoint.x + i >= 0 && currPoint.x + i < bitmap.getWidth() &&
	 * currPoint.y + j >= 0 && currPoint.y + j < bitmap.getHeight() &&
	 * bitmap.getPixel(currPoint.x + i, currPoint.y + j) == startPointColor) {
	 * pointToCheck.add(new Point(currPoint.x + i, currPoint.y + j)); } } } } }
	 */
}
