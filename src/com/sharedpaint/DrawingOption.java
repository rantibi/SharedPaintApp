package com.sharedpaint;

import com.sharedpaint.R;
import com.sharedpaint.drawables.Circle;
import com.sharedpaint.drawables.Line;
import com.sharedpaint.drawables.Pencil;
import com.sharedpaint.drawables.Rectangle;
import com.sharedpaint.drawables.Triangle;
import com.sharedpaint.operations.DrawingOptionOperation;
import com.sharedpaint.operations.FillDrawingOperation;
import com.sharedpaint.operations.MultiPositionsOperation;
import com.sharedpaint.operations.StartEndPositionOperation;
import com.sharedpaint.operations.TextDrawingOperation;
public enum DrawingOption {

	PENCIL(R.drawable.pencil, new MultiPositionsOperation(Pencil.class)),
	LINE (R.drawable.line, new StartEndPositionOperation(Line.class)),
	CIRCLE(R.drawable.circle, new StartEndPositionOperation(Circle.class)),
	RECTANGLE(R.drawable.rectangle, new StartEndPositionOperation(Rectangle.class)),
	TRIANGLE(R.drawable.triangle, new StartEndPositionOperation(Triangle.class)),
	TEXT(R.drawable.text, new TextDrawingOperation()),
	FILL(R.drawable.fill, new FillDrawingOperation());

	private DrawingOptionOperation drawingOperation;
	private int iconResource;
	
	private DrawingOption(int iconResource, DrawingOptionOperation drawingOperation){
		this.iconResource = iconResource;
		this.drawingOperation = drawingOperation;
	}
	
	public DrawingOptionOperation getDrawingOperation(){
		return drawingOperation;
	}

	public int getIconResource() {
		return iconResource;
	}
}
