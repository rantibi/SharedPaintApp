package com.sharedpaint;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class DrawView extends View  {
	private DrawManger drawManager;
    
	public void setDrawManager(DrawManger drawManager) {
		this.drawManager = drawManager;
	}

	public DrawView(Context context){
        super(context);
        setDrawingCacheEnabled(true);
    }
    
	public DrawView(Context context, AttributeSet attr) {
		super(context, attr);
		setDrawingCacheEnabled(true);
	}
	
	
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
         super.onSizeChanged(w, h, oldw, oldh);
         
     }

     @Override
     protected void onDraw(Canvas canvas) {
    	 super.onDraw(canvas);   
    	 drawManager.draw(this, canvas);
     }

     private float mX, mY;
     private static final float TOUCH_TOLERANCE = 4;

     private void touch_start(float x, float y) {
    	 drawManager.createNewDrawable(x, y);
     }
     private void touch_move(float x, float y) {
         float dx = Math.abs(x - mX);
         float dy = Math.abs(y - mY);
         if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
             drawManager.updateCurrentDrawable(x, y);
             mX = x;
             mY = y;
         }
         
     }
     private void touch_up() {
         drawManager.acceptCurrentDrawable();
     }

     @Override
     public boolean onTouchEvent(MotionEvent event) {
         float x = event.getX();
         float y = event.getY();

         switch (event.getAction()) {
             case MotionEvent.ACTION_DOWN:
                 touch_start(x, y);
                 invalidate();
                 break;
             case MotionEvent.ACTION_MOVE:
                 touch_move(x, y);
                 invalidate();
                 break;
             case MotionEvent.ACTION_UP:
                 touch_up();
                 invalidate();
                 break;
         }
         return true;
     }
}