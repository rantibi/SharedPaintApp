package com.sharedpaint;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class DrawView extends View  {
	private DrawManager drawManager;
    
	public void setDrawManager(DrawManager drawManager) {
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

    
     @Override
     public boolean onTouchEvent(MotionEvent event) {
    	 
    	 if (drawManager.getDrowingOption().getDrawingOperation().onTouchEvent(this, drawManager, event)){
    		 invalidate();
    	 }
         return true;
     }
}