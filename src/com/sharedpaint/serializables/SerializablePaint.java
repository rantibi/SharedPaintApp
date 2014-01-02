package com.sharedpaint.serializables;

import java.io.IOException;
import java.io.Serializable;

import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;

/**
 * Implement Serialize for paint
 * 
 * @author ran
 * 
 */
public class SerializablePaint implements Serializable {
	private static final long serialVersionUID = 1L;
	private transient Paint paint;

	public SerializablePaint(Paint paint) {
		this.paint = paint;
	}

	public Paint getPaint() {
		return paint;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();

		if (paint == null) {
			out.writeBoolean(false);
		} else {
			out.writeBoolean(true);
			out.writeInt(paint.getColor());
			out.writeObject(paint.getStyle().name());
			out.writeFloat(paint.getStrokeWidth());
			out.writeFloat(paint.getTextSize());
			out.writeInt(paint.getFlags());
			out.writeObject(paint.getStrokeCap().name());
			out.writeObject(paint.getStrokeJoin().name());
		}
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		
		if (in.readBoolean()) {
			paint = new Paint();
			paint.setColor(in.readInt());
			paint.setStyle(Style.valueOf((String) in.readObject()));
			paint.setStrokeWidth(in.readFloat());
			paint.setTextSize(in.readFloat());
			paint.setFlags(in.readInt());
			paint.setStrokeCap(Cap.valueOf((String) in.readObject()));
			paint.setStrokeJoin(Join.valueOf((String) in.readObject()));
		}
	}
}
