package com.sharedpaint;

import java.io.Serializable;

import com.sharedpaint.drawables.Drawable;

/*
 * 
 */
public interface DrawManagerListener extends Serializable {
	public void drawableAccept(Drawable drawable);
}
