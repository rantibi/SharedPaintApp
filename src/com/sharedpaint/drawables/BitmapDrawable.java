package com.sharedpaint.drawables;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.sharedpaint.serializables.SerializableBitmap;

/**
 * Draw bitmap object
 */
public class BitmapDrawable extends AbstractDrawable {

	/** 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient Bitmap bitmap;


	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, 0, 0, paint);
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
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		bitmap = ((SerializableBitmap) in.readObject()).getBitmap();
	}
}
