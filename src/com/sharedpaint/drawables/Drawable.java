package com.sharedpaint.drawables;

import android.graphics.Canvas;
import android.graphics.Paint;

public interface Drawable {

	public void draw(Canvas canvas);

	public void setPaint(Paint paint);
}
