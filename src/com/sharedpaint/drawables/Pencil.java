package com.sharedpaint.drawables;

import java.io.IOException;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.sharedpaint.serializables.SerializablePaint;
import com.sharedpaint.serializables.SerializablePath;

public class Pencil implements MultiPositionsDrawable {

	private static final long serialVersionUID = 1L;
	private SerializablePath path;
	private transient Paint paint;
	private long id;
	
	public Pencil() {
		path = new SerializablePath();
	}

	

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public long getId() {
		return id;
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
		if (path.isEmpty()) {
			path.moveTo(x, y);
		}

		path.lineTo(x, y);
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeObject(new SerializablePaint(paint));
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		paint = ((SerializablePaint) in.readObject()).getPaint();
	}

}
