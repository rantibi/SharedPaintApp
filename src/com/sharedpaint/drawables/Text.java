package com.sharedpaint.drawables;

import java.io.IOException;

import com.sharedpaint.serializables.SerializablePaint;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Text implements Drawable{
	private static final long serialVersionUID = 1L;
	private String text;
	private float x;
	private float y;
	private transient Paint paint;

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

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeObject(new SerializablePaint(paint));
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		paint = ((SerializablePaint)in.readObject()).getPaint();
	}
}
