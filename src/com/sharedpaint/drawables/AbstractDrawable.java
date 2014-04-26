package com.sharedpaint.drawables;

import java.io.IOException;

import android.graphics.Paint;

import com.sharedpaint.serializables.SerializablePaint;

/**
 * Abstract implementation for drawable object - getters/setters implementation
 */
public abstract class AbstractDrawable implements Drawable {
	private static final long serialVersionUID = 1L;
	protected transient Paint paint;
	private long id;
	private long time;

	@Override
	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public long getTime() {
		return time;
	}

	@Override
	public int compareTo(Drawable another) {
		if(id == another.getId()){
			return 0;
		}
		
		return (int) (time - another.getTime());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Drawable)){
		return false;
		}
		Drawable other = (Drawable) obj;
		return id == other.getId();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeObject(new SerializablePaint(paint));
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		paint = ((SerializablePaint) in.readObject()).getPaint();
	}

}
