package com.sharedpaint;

import java.io.IOException;
import java.io.Serializable;
import java.util.Stack;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;

import com.sharedpaint.drawables.Drawable;
import com.sharedpaint.serializables.SerializableBitmap;
import com.sharedpaint.serializables.SerializablePaint;

public class DrawManager implements Serializable {
	private static final long serialVersionUID = 1L;
	BlockingDeque<Drawable> drawables;
	Stack<Drawable> redoDrawables;
	private Drawable currentDrawable;
	private transient Paint paint;
	private transient DrawingOption drowingOption;
	private DrawView drawView;
	private transient Bitmap backgroundBitmap;
	
	public DrawManager() {
		drawables = new LinkedBlockingDeque<Drawable>();
		redoDrawables = new Stack<Drawable>();
		createPaint();
	}
	
	public void createBackgroundBitmapIfNotExist(int width, int height) {
		if (backgroundBitmap==null){
			backgroundBitmap = Bitmap.createBitmap(width,height,Config.ARGB_8888);	
		}	
	}

	private void createPaint() {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(false);
		paint.setStrokeCap(Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);
	}

	public void draw(Canvas canvas) {
		canvas.drawColor(0xFFFCCCCF);
		canvas.drawBitmap(backgroundBitmap,0,0 , paint);
		
		for (Drawable drawable : drawables) {
			drawable.draw(canvas);
		}

		if (currentDrawable != null) {
			currentDrawable.draw(canvas);
		}
	}

	public void acceptCurrentDrawable() {
		addDrawable(currentDrawable);
		currentDrawable = null;
		redoDrawables.clear();
	}

	private void addDrawable(Drawable drawable) {
		drawables.add(drawable);
		
		if (drawables.size() > CommonConfig.getMaxDrawableUndo()){
			Canvas canvas = new Canvas(backgroundBitmap);
			drawables.poll().draw(canvas);
		}
	}
	
	public Bitmap getCurrentViewAsBitmap() {
		Bitmap bitmap = Bitmap.createBitmap(backgroundBitmap.getWidth(),backgroundBitmap.getHeight(),Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		draw(canvas);
		return bitmap;
	}

	public void undolLastDrawable() {
		if (!drawables.isEmpty()) {
			redoDrawables.push(drawables.removeLast());
		}
	}

	public void redolLastDrawable() {
		if (!redoDrawables.isEmpty()) {
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

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeObject(new SerializablePaint(paint));
		out.writeObject(drowingOption.name());
		out.writeObject(new SerializableBitmap(backgroundBitmap));
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		paint = ((SerializablePaint)in.readObject()).getPaint();
		drowingOption = DrawingOption.valueOf((String) in.readObject());
		backgroundBitmap = ((SerializableBitmap)in.readObject()).getBitmap();
	}

	
}
