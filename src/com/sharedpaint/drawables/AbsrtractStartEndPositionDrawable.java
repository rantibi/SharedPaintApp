package com.sharedpaint.drawables;

import java.io.IOException;

import com.sharedpaint.serializables.SerializablePaint;

import android.graphics.Paint;


public abstract class AbsrtractStartEndPositionDrawable implements StartEndPositionDrawable{

	private static final long serialVersionUID = 1L;
	protected float startX;
	protected float startY;
	protected float endY;
	protected float endX;
	protected transient Paint paint;

	
	@Override
	public void setPaint(Paint paint) {
		this.paint = paint;
	}
	
	
	@Override
	public void setStartPosition(float x, float y) {
		startX = x;
		startY = y;
		endX = x;
		endY = y;
	}

	
	
	@Override
	public void setEndPosition(float x, float y) {
		endX = x;
		endY = y;
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
