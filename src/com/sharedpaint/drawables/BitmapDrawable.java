package com.sharedpaint.drawables;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

public class BitmapDrawable extends AbsrtractStartEndPositionDrawable{

	private Bitmap bitmap;
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, 0, 0, paint);
	}

	

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	
	

}
