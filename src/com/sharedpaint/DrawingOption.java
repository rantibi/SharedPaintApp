package com.sharedpaint;

import com.sharedpaint.drawables.Circle;
import com.sharedpaint.drawables.Line;
import com.sharedpaint.drawables.Pencil;
import com.sharedpaint.drawables.Rectangle;
import com.sharedpaint.drawables.Text;
import com.sharedpaint.drawables.Triangle;
import com.sharedpaint.operations.DrawingOptionOperation;
import com.sharedpaint.operations.FillDrawingOperation;
import com.sharedpaint.operations.MultiPositionsOperation;
import com.sharedpaint.operations.StartEndPositionOperation;
import com.sharedpaint.operations.TextDrawingOperation;

public enum DrawingOption {

	PENCIL(new MultiPositionsOperation(Pencil.class)),
	LINE (new StartEndPositionOperation(Line.class)),
	CIRCLE(new StartEndPositionOperation(Circle.class)),
	RECTANGLE(new StartEndPositionOperation(Rectangle.class)),
	TRIANGLE(new StartEndPositionOperation(Triangle.class)),
	TEXT(new TextDrawingOperation()),
	FILL(new FillDrawingOperation());

	private DrawingOptionOperation drawingOperation;
	
	private DrawingOption(DrawingOptionOperation drawingOperation){
		this.drawingOperation = drawingOperation;
	}
	
	public DrawingOptionOperation getDrawingOperation(){
		return drawingOperation;
	}
}
