package com.sharedpaint.activitys;

import java.util.concurrent.atomic.AtomicBoolean;

import android.os.AsyncTask;

import com.sharedpaint.connection.ServerProxy;
import com.sharedpaint.transfer.BoardUpdate;

public class DrawablesUpdater {
	private boolean keepRunning;
	private DrawPaintActivity drawPaintActivity;

	public DrawablesUpdater(DrawPaintActivity drawPaintActivity) {
		this.drawPaintActivity = drawPaintActivity;
	}

	public void start() {
		keepRunning = true;
		new Thread(new Runnable() {

			private AtomicBoolean running = new AtomicBoolean(false);

			@Override
			public void run() {
				while (keepRunning) {
					if (!running.get())
						new AsyncTask<Void, Void, Boolean>() {
							@Override
							protected void onPreExecute() {
								running.set(true);
								super.onPreExecute();
							}

							@Override
							protected Boolean doInBackground(
									Void... paramArrayOfParams) {

								try {
									BoardUpdate boardUpdate = ServerProxy
											.getInstance(drawPaintActivity)
											.getBoardUpdate(
													drawPaintActivity
															.getBoardDetails()
															.getId(), drawPaintActivity.getDrawManager().getLastUpdate());

									drawPaintActivity.getDrawManager().updateBoard(boardUpdate);

								} catch (Exception e1) {
									// TODO Do somthine with that error
									return false;
								}

								return true;
							}

							protected void onPostExecute(Boolean result) {
								running.set(false);
								if (result) {
									drawPaintActivity.getDrawView()
											.invalidate();

								}

								// TODO: do something with the exception
							};
						}.execute((Void) null);

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}).start();
	}

	public void stop() {
		keepRunning = false;
	}
}
