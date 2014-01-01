package com.sharedpaint.drawables;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import android.app.ActionBar.LayoutParams;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

public class Fill extends AbsrtractStartEndPositionDrawable {

	// private LinkedList<Float> points;
	private Bitmap bitmap;


	
	@Override
	public void draw(Canvas canvas) {
		if (bitmap == null) {
			
			QueueLinearFloodFiller floodFiller = new QueueLinearFloodFiller(
					bitmap,bitmap.getPixel(
							(int) endX, (int) endY), paint.getColor());
			floodFiller.setTolerance(10);
			bitmap = floodFiller.floodFill((int)endX, (int)endY);
			
			
			// Bitmap bitmapCopy = Bitmap.createBitmap(view.getDrawingCache());
			// bitmap = fill(bitmapCopy, (int) EndX, (int) EndY,
			// bitmapCopy.getPixel((int) EndX, (int) EndY), paint.getColor());
		} else {
			/*
			 * float[] pts = new float[points.size()];
			 * 
			 * for (int i = 0; i < pts.length; i++) { pts[i] = points.get(i); }
			 * 
			 * canvas.drawPoints(pts, paint);
			 */
			canvas.drawBitmap(bitmap, 0, 0, paint);
		}
	}

	/*
	 * private void fill(Bitmap bitmap, int x, int y, int startPointColor, int
	 * fillColor) { if (bitmap.getPixel(x, y) != startPointColor) return;
	 * 
	 * bitmap.setPixel(x, y, fillColor); points.add((float) x);
	 * points.add((float) y);
	 * 
	 * if (x - 1 > 0 && y - 1 > 0) fill(bitmap, x - 1, y - 1, startPointColor,
	 * fillColor); if (x - 1 > 0 && y + 1 < bitmap.getHeight()) fill(bitmap, x -
	 * 1, y + 1, startPointColor, fillColor); if (x + 1 < bitmap.getWidth() && y
	 * - 1 > 0) fill(bitmap, x + 1, y - 1, startPointColor, fillColor); if (x +
	 * 1 < bitmap.getWidth() && y + 1 < bitmap.getHeight()) fill(bitmap, x + 1,
	 * y + 1, startPointColor, fillColor); }
	 */

	private Bitmap fill(Bitmap bitmap, int x, int y, int startPointColor,
			int fillColor) {
		Queue<Point> queue = new LinkedList<Point>();

		if (bitmap.getPixel(x, y) != startPointColor)
			return null;

		Bitmap returnBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), bitmap.getConfig());

		queue.add(new MyPoint(x, y));

		while (!queue.isEmpty()) {
			Point n = queue.poll();

			if (bitmap.getPixel(n.x, n.y) == startPointColor) {
				int east, west;

				for (east = n.x; east >= 0
						&& bitmap.getPixel(east, n.y) == startPointColor; east--)
					;
				for (west = n.x; west < bitmap.getWidth()
						&& bitmap.getPixel(west, n.y) == startPointColor; west++)
					;

				east++;
				west--;
				for (int i = east; i <= west; i++) {
					bitmap.setPixel(i, n.y, fillColor);
					returnBitmap.setPixel(i, n.y, fillColor);
					// points.add((float) i);
					// points.add((float) n.y);

					if (y - 1 >= 0
							&& bitmap.getPixel(i, n.y - 1) == startPointColor) {
						MyPoint myPoint = new MyPoint(i, n.y - 1);

						if (!queue.contains(myPoint)) {
							queue.add(myPoint);
						}
					}

					if (y + 1 < bitmap.getHeight()
							&& bitmap.getPixel(i, n.y + 1) == startPointColor) {

						MyPoint myPoint = new MyPoint(i, n.y + 1);

						if (!queue.contains(myPoint)) {
							queue.add(myPoint);
						}
					}
				}
			}
		}

		return returnBitmap;
	}

	/*
	 * private void fill(Bitmap bitmap, int x, int y, int startPointColor, int
	 * fillColor){
	 * 
	 * 
	 * boolean[][] painted = new boolean[bitmap.getHeight()][bitmap.getWidth()];
	 * 
	 * for (int i = 0; i < bitmap.getHeight(); i++) { for (int j = 0; j <
	 * bitmap.getWidth(); j++) {
	 * 
	 * if (bitmap.getPixel(i, j) == startPointColor && !painted[i][j]) {
	 * 
	 * Queue<Point> queue = new LinkedList<Point>(); queue.add(new Point(j, i));
	 * 
	 * int pixelCount = 0; while (!queue.isEmpty()) { Point p = queue.remove();
	 * 
	 * if ((p.x >= 0) && (p.x < bitmap.getWidth() && (p.y >= 0) && (p.y < bitmap
	 * .getHeight()))) { if (!painted[p.y][p.x] && bitmap.getPixel(p.x, p.y) ==
	 * startPointColor) { painted[p.y][p.x] = true; points.add((float) p.x);
	 * points.add((float) p.y);
	 * 
	 * pixelCount++;
	 * 
	 * queue.add(new Point(p.x + 1, p.y)); queue.add(new Point(p.x - 1, p.y));
	 * queue.add(new Point(p.x, p.y + 1)); queue.add(new Point(p.x, p.y - 1)); }
	 * } } System.out.println("Blob detected : " + pixelCount + " pixels"); }
	 * 
	 * } } } /* private void fill(Bitmap bitmap, int x, int y, int
	 * startPointColor, int fillColor) { Queue<Point> pointToCheck = new
	 * LinkedList<Point>(); pointToCheck.add(new Point(x, y));
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

	@Override
	public void setPaint(Paint paint) {
		super.setPaint(paint);
		paint.setStrokeWidth(1);
	}

	class MyPoint extends Point {

		public MyPoint(int x, int y) {
			super(x, y);
		}

		@Override
		public boolean equals(Object o) {
			return ((MyPoint) o).x == x && ((MyPoint) o).y == y;
		}
	}
	
	
}
