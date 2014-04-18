package com.sharedpaint.activitys;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.os.AsyncTask;

import com.sharedpaint.SharedPaintException;
import com.sharedpaint.connection.ServerProxy;
import com.sharedpaint.drawables.Drawable;

public class DrawablesAdder {
	private ConcurrentLinkedQueue<Drawable> queue;
	private AsyncTask<Void, Void, Boolean> asyncTask;
	private AtomicBoolean isRunning;
	private Lock runningLock;
	private DrawPaintActivity drawPaintActivity;

	public DrawablesAdder(DrawPaintActivity drawPaintActivity) {
		this.drawPaintActivity = drawPaintActivity;
		queue = new ConcurrentLinkedQueue<Drawable>();
		runningLock = new ReentrantLock();
		isRunning = new AtomicBoolean(false);
	}

	private void createAndExecuteTask() {
		asyncTask = new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				runningLock.lock();
				try {
					isRunning.set(true);
				} finally {
					runningLock.unlock();
				}
			}

			@Override
			protected Boolean doInBackground(Void... paramArrayOfParams) {

				try {
					while (isRunning.get()) {
						runningLock.lock();
			
						try {
							if (queue.isEmpty()) {
								isRunning.set(false);
								return true;
							}
						} finally {
							runningLock.unlock();
						}

						ServerProxy.getInstance()
								.addNewDrawable(
										drawPaintActivity.getBoardDetails()
												.getId(), queue.poll());
					}
				} catch (SharedPaintException e) {
					return false;
				}
				
				return true;
			}

			protected void onPostExecute(Boolean result) {
				// TODO: do something with the exception
			};
		};
	
		asyncTask.execute((Void)null);
	}

	public void addDrawable(Drawable drawable) {
		runningLock.lock();
		try {
			queue.add(drawable);

			if (!isRunning.get()) {
				createAndExecuteTask();
			}
		} finally {
			runningLock.unlock();
		}
	}
}
