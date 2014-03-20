package com.sharedpaint.drawables;

import java.io.IOException;

public abstract class AbsrtractStartEndPositionDrawable extends
		AbstractDrawable implements StartEndPositionDrawable {

	private static final long serialVersionUID = 1L;
	protected float startX;
	protected float startY;
	protected float endY;
	protected float endX;

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
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
	}
}
