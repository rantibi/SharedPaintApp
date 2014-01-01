package com.sharedpaint;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;

import com.sharedpaint.drawables.Drawable;


public class DrawManager {
	List<Drawable> drawables;
	Stack<Drawable> redoDrawables;
	private Drawable currentDrawable;
	private Paint paint;
	private DrawingOption drowingOption;
	
	public DrawManager() {
		drawables = new LinkedList<Drawable>();
		redoDrawables = new Stack<Drawable>();
		paint = new Paint();
        paint.setAntiAlias(false);
        paint.setDither(false);
        paint.setStrokeCap(Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
	}
	
	public void draw(DrawView drawView, Canvas canvas){
		canvas.drawColor(0xFFFCCCCF);
   	 
		for (Drawable drawable : drawables) {
			drawable.draw(canvas);
		}
		
		if (currentDrawable != null){
			currentDrawable.draw(canvas);
		}
	}
		
	public void acceptCurrentDrawable(){
		drawables.add(currentDrawable);
		currentDrawable = null;
		redoDrawables.clear();
	}
	
	public void undolLastDrawable() {
		if (!drawables.isEmpty()){
			redoDrawables.push(drawables.remove(drawables.size() - 1));
		}
	}
	
	public void redolLastDrawable() {
		if (!redoDrawables.isEmpty()){
			drawables.add(redoDrawables.pop());
		}
	}

	public void setCurrentDrawable(Drawable drawable) {
		currentDrawable = drawable;
	}

	public Paint getPaint() {
		return paint;
	}

	public void setCurrentDrawingOption(DrawingOption drawingOption) {
		this.drowingOption = drawingOption;
	}

	public DrawingOption getDrowingOption() {
		return drowingOption;
	}
	
	
}
