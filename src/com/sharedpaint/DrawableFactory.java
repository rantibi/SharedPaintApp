package com.sharedpaint;

import java.io.Serializable;
import java.util.Iterator;

import com.sharedpaint.drawables.Drawable;

public class DrawableFactory implements Serializable {

	private static final long serialVersionUID = 1L;
	private Iterator<Long> drawableIdsIterator;

	public DrawableFactory(Iterator<Long> drawableIdsIterator) {
		this.drawableIdsIterator = drawableIdsIterator;
	}

	public Drawable newDrawable(Class<? extends Drawable> drawableClass) {
		try {
			Drawable newInstance = drawableClass.newInstance();
			newInstance.setId(drawableIdsIterator.next());
			return newInstance;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
