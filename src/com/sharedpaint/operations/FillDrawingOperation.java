package com.sharedpaint.operations;

import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;

import com.sharedpaint.DrawManager;
import com.sharedpaint.drawables.BitmapDrawable;
import com.sharedpaint.drawables.QueueLinearFloodFiller;

/**
 * Fill drawing operation - flood filler
 */
public class FillDrawingOperation extends AbsrtcatDrawaingOptionOperation{

	public FillDrawingOperation() {
		super(BitmapDrawable.class);
	}

	@Override
	public void onSelect(View view, DrawManager drawManager) {
		
	}

	@Override
	public boolean onTouchEvent(View view, DrawManager drawManager,
			MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			createFillDrawable(drawManager, view, (int)event.getX(), (int)event.getY());
			return true;
		}
		
		return false;
	}

	@Override
	public void onDeselect(View view, DrawManager drawManager) {
		
	}

	public void createFillDrawable(DrawManager drawManager, View view,int x, int y){
		Bitmap bitmap = BitmapUtility.loadBitmapFromView(view);
		QueueLinearFloodFiller floodFiller = new QueueLinearFloodFiller(
				bitmap,bitmap.getPixel(x, y), drawManager.getPaint().getColor());
		floodFiller.setTolerance(5);
		bitmap = floodFiller.floodFill(x, y);
		
		currentInstance = newDrawableClassInstance(drawManager.getDrawableFactory());
		((BitmapDrawable)currentInstance).setBitmap(bitmap);
		drawManager.setCurrentDrawable(currentInstance);
		drawManager.acceptCurrentDrawable();
	}

	
}

