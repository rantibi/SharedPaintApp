package com.sharedpaint;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;

import com.sharedpaint.drawables.Circle;
import com.sharedpaint.drawables.Drawable;
import com.sharedpaint.drawables.Pencil;


public class DrawManger {
	List<Drawable> drawables;
	private Class<? extends Drawable> currentDrawableClass;
	private Drawable currentDrawable;
	private Paint paint;
	
	public DrawManger() {
		drawables = new LinkedList<Drawable>();
		currentDrawableClass = Pencil.class;
		paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeCap(Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
	}
	
	public void draw(DrawView drawView, Canvas canvas){
		canvas.drawColor(0xFFFCCCCF);
   	 
		for (Drawable drawable : drawables) {
			drawable.draw(drawView, canvas);
		}
		
		if (currentDrawable != null){
			currentDrawable.draw(drawView, canvas);
		}
	}
	
	public void addDraw(Drawable drawable) {
		drawables.add(drawable);
	}

	public void setDrawbleClass(Class<? extends Drawable> drawableClass) {
		currentDrawableClass = drawableClass;
	}
	
	public void createNewDrawable(float startX, float startY){
		try {
			currentDrawable = currentDrawableClass.newInstance();
			currentDrawable.setPaint(new Paint(paint));
			currentDrawable.setStartPosition(startX, startY);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateCurrentDrawable(float endX, float endY){
		currentDrawable.updateEndPosition(endX, endY);
	}
	
	public void acceptCurrentDrawable(){
		drawables.add(currentDrawable);
		currentDrawable = null;
	}
	
	public void cancelLastDrawable() {
		if (!drawables.isEmpty()){
			drawables.remove(drawables.size() - 1);
		}
	}

	public void setColor(int color) {
		paint.setColor(color);
	}

	public void setStrokeWidth(int strokeWidth) {
		paint.setStrokeWidth(strokeWidth);
	}
	
	public void setStyle(Style style) {
		paint.setStyle(style);
	}
}
