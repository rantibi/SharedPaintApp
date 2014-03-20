package com.sharedpaint;

import java.io.IOException;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.util.List;
import java.util.SortedSet;
import java.util.Stack;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingDeque;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;

import com.sharedpaint.drawables.AbstractDrawable;
import com.sharedpaint.drawables.Drawable;
import com.sharedpaint.serializables.SerializablePaint;
import com.sharedpaint.transfer.BoardUpdate;
import com.sharedpaint.transfer.DrawableHolder;

public class DrawManager implements Serializable {
	private static final long serialVersionUID = 1L;
	private SortedSet<Drawable> drawables;
	private Drawable currentDrawable;
	private transient Paint paint;
	private transient DrawingOption drawingOption;
	private int width;
	private int height;
	private DrawManagerListener drawManagerListener;
	private DrawableFactory drawablesFactory;
	private long lastUpdate;
	
	private DrawManager() {
		drawables = new ConcurrentSkipListSet<Drawable>();
		//redoDrawables = new Stack<Drawable>();
		createPaint();
	}
	
	public DrawManager(DrawableFactory drawablesFactory){
		this();
		this.drawablesFactory = drawablesFactory;
	}
	
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		
		/*if (backgroundBitmap==null){
			backgroundBitmap = new BitmapDrawable();  
			backgroundBitmap.setBitmap(Bitmap.createBitmap(width,height,Config.ARGB_8888));	
		}*/	
	}

	private void createPaint() {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(false);
		paint.setStrokeCap(Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);
	}

	public void draw(Canvas canvas) {
		canvas.drawColor(0xFFFFFFFF);
		//backgroundBitmap.draw(canvas);
		
		for (Drawable drawable : drawables) {
			drawable.draw(canvas);
		}

		if (currentDrawable != null) {
			currentDrawable.draw(canvas);
		}
	}

	public void acceptCurrentDrawable() {
		currentDrawable.setTime(drawables.size() > 0 ? drawables.last().getTime() + 1 : 0);
		addDrawable(currentDrawable);
		drawManagerListener.drawableAccept(currentDrawable);
		currentDrawable = null;
		//redoDrawables.clear();
	}

	public void addDrawable(Drawable drawable) {
		drawables.add(drawable);
		
		/*if (drawables.size() > CommonConfig.getMaxDrawableUndo()){
			Canvas canvas = new Canvas(backgroundBitmap.getBitmap());
			drawables.poll().draw(canvas);
		}*/
	}
	
	public Bitmap getCurrentViewAsBitmap() {
		//Bitmap bitmap = Bitmap.createBitmap(backgroundBitmap.getBitmap().getWidth(),
		//		backgroundBitmap.getBitmap().getHeight(),Config.ARGB_8888);
		Bitmap bitmap = Bitmap.createBitmap(width, height,Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		draw(canvas);
		return bitmap;
	}
/*
	public void undolLastDrawable() {
		if (!drawables.isEmpty()) {
			Drawable last = drawables.last();
			drawables.remove(last);
			redoDrawables.push(last);
		}
	}*/

	/*public void redolLastDrawable() {
		if (!redoDrawables.isEmpty()) {
			drawables.add(redoDrawables.pop());
		}
	}*/

	public void setCurrentDrawable(Drawable drawable) {
		currentDrawable = drawable;
	}

	public Paint getPaint() {
		return paint;
	}

	public void setCurrentDrawingOption(DrawingOption drawingOption) {
		this.drawingOption = drawingOption;
	}

	public DrawingOption getDrowingOption() {
		return drawingOption;
	}

	public DrawManagerListener getDrawManagerListener() {
		return drawManagerListener;
	}

	public void setDrawManagerListener(DrawManagerListener drawManagerListener) {
		this.drawManagerListener = drawManagerListener;
	}

	public DrawableFactory getDrawableFactory() {
		return drawablesFactory;
	}


	public void setDrawableFactory(DrawableFactory drawableFactory) {
		this.drawablesFactory = drawableFactory;
	}


	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeObject(new SerializablePaint(paint));
		out.writeObject(drawingOption.name());
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		paint = ((SerializablePaint)in.readObject()).getPaint();
		drawingOption = DrawingOption.valueOf((String) in.readObject());
	}

	public void updateBoard(BoardUpdate boardUpdate) {
		setLastUpdate(boardUpdate.getTo().getTime());
		
		Drawable drawable= new AbstractDrawable() {
			@Override
			public void draw(Canvas canvas) {
			}
		};
		
		for (Long id : boardUpdate.getRemovedDrawables().keySet()) {
			drawable.setId(id);
			drawables.remove(drawable);
		}
		
		for (DrawableHolder holder : boardUpdate.getNewDrawables()) {
			try {
				drawable = IOUtils.byteArrayToObject(holder.getDrawable());
				drawable.setTime(holder.getDateTime().getTime());
				drawables.add(drawable);
			} catch (Exception e) {
				// TODO handle this exception!!!
				e.printStackTrace();
			}
		}
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
