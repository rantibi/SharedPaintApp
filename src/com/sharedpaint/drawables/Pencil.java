package com.sharedpaint.drawables;

import java.io.IOException;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.sharedpaint.serializables.SerializablePath;

/**
 * Draw free pencil object
 */
public class Pencil extends AbstractDrawable implements MultiPositionsDrawable {

	private static final long serialVersionUID = 1L;
	private SerializablePath path;

	public Pencil() {
		path = new SerializablePath();
	}

	@Override
	public void setPaint(Paint paint) {
		super.setPaint(paint);
		paint.setStyle(Style.STROKE);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawPath(path, paint);
	}

	public void addPosition(float x, float y) {
		if (path.isEmpty()) {
			path.moveTo(x, y);
		}

		path.lineTo(x, y);
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
	}

}
