package com.sharedpaint.drawables;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.sharedpaint.serializables.SerializableBitmap;
import com.sharedpaint.serializables.SerializablePaint;

public class BitmapDrawable implements Drawable {

	/** 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient Bitmap bitmap;
	private transient Paint paint;
	private long id;

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, 0, 0, paint);
	}

	@Override
	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeObject(new SerializableBitmap(bitmap));
		out.writeObject(new SerializablePaint(paint));
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		bitmap = ((SerializableBitmap) in.readObject()).getBitmap();
		paint = ((SerializablePaint) in.readObject()).getPaint();
	}
}
