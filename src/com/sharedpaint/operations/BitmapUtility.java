package com.sharedpaint.operations;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

/**
 * Bitmap utility
 */
public class BitmapUtility {
	public static Bitmap loadBitmapFromView(View v) {
		v.clearFocus();
		v.setPressed(false);

		boolean willNotCache = v.willNotCacheDrawing();
		v.setWillNotCacheDrawing(false);

		// Reset the drawing cache background color to fully transparent
		// for the duration of this operation
		int color = v.getDrawingCacheBackgroundColor();
		v.setDrawingCacheBackgroundColor(0);

		if (color != 0) {
			v.destroyDrawingCache();
		}
		v.buildDrawingCache();
		Bitmap cacheBitmap = v.getDrawingCache();
		if (cacheBitmap == null) {
			return null;
		}

		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

		// Restore the view
		v.destroyDrawingCache();
		v.setWillNotCacheDrawing(willNotCache);
		v.setDrawingCacheBackgroundColor(color);

		return bitmap;
	}

	public static boolean saveBitmapToExternalStorage(Bitmap bitmap,
			String albumName, String fileName, Activity activity) {
		if (!isExternalStorageWritable()) {
			return false;
		}

		try {
			File file = new File(getAlbumStorageDir(albumName).getAbsolutePath() + "/"
					+ fileName + ".jpg");
			FileOutputStream fOut = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
			fOut.flush();
			fOut.close();
			
			MediaStore.Images.Media.insertImage(activity.getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
			activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse
					("file://"
	                        + Environment.getExternalStorageDirectory())));
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(null, "Save file error!");
			return false;
		}

		return true;
	}

	/**
	 * Checks if external storage is available for read and write
	 **/
	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	public static File getAlbumStorageDir(String albumName) {
		// Get the directory for the user's public pictures directory.
		File file = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				albumName);
		if (!file.mkdirs()) {
			Log.e("Error", "Directory not created");
		}
		return file;
	}
}
