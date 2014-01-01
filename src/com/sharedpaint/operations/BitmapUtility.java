package com.sharedpaint.operations;

import android.graphics.Bitmap;
import android.view.View;

public class BitmapUtility {
	public static Bitmap loadBitmapFromView(View v) {
		 v.clearFocus();
       v.setPressed(false);

       boolean willNotCache = v.willNotCacheDrawing();
       v.setWillNotCacheDrawing(false);

       // Reset the drawing cache background color to fully transparent
       // for the duration of this operation
       int color = v.getDrawingCacheBackgroundColor();
       v.setDrawingCacheBackgroundColor(0);

       if (color != 0) {
           v.destroyDrawingCache();
       }
       v.buildDrawingCache();
       Bitmap cacheBitmap = v.getDrawingCache();
       if (cacheBitmap == null) {
           return null;
       }

       Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

       // Restore the view
       v.destroyDrawingCache();
       v.setWillNotCacheDrawing(willNotCache);
       v.setDrawingCacheBackgroundColor(color);

       return bitmap;
	}
}
