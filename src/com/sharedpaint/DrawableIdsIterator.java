package com.sharedpaint;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import android.app.Activity;
import android.os.AsyncTask;

import com.sharedpaint.connection.ServerProxy;
import com.sharedpaint.transfer.BoardDetails;

public class DrawableIdsIterator implements Iterator<Long>, Serializable {

	private static final long serialVersionUID = 1L;
	private static final int MAX_CACHE_IDS = 10;
	private static final int MIN_CACHE_IDS = 5;

	private transient Activity activity;
	private BoardDetails boardDetails;
	private Queue<Long> nextIds;

	protected DrawableIdsIterator(){
	}
	
	public DrawableIdsIterator(Activity activity, BoardDetails boardDetails) {
		this.boardDetails = boardDetails;
		this.setActivity(activity);
		nextIds = new LinkedList<Long>();
		new GetNewIdsTask().execute((Void)null);
	}

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public Long next() {
		if (nextIds.size() < MIN_CACHE_IDS){
			new GetNewIdsTask().execute((Void)null);
		}
		//DOTO: fix it!!!
		return nextIds.poll();
	}

	@Override
	public void remove() {
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	class GetNewIdsTask extends AsyncTask<Void, Void, Boolean> {
		
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				nextIds.addAll(ServerProxy.getInstance(getActivity()).getNewDrawaleIds(boardDetails.getId(),MAX_CACHE_IDS));
			} catch (SharedPaintException e) {
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			//TODO: do somthing if not
		}
	}
}
