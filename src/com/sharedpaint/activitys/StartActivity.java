package com.sharedpaint.activitys;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.sharedpaint.R;
import com.sharedpaint.activitys.util.SystemUiHider;
import com.sharedpaint.connection.AuthenticationException;
import com.sharedpaint.connection.ConnectionException;
import com.sharedpaint.connection.ServerProxy;

/**
 * First loading activity
 */
public class StartActivity extends Activity {
	

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		CacheLoginTask authTask = new CacheLoginTask();
		authTask.execute((Void) null);

		setContentView(R.layout.activity_start);

		final View contentView = findViewById(R.id.fullscreen_content);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}



	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class CacheLoginTask extends AsyncTask<Void, Void, Boolean> {
		private Throwable exception;

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				return ServerProxy.getInstance().loginFromCache();
			} catch (ConnectionException e) {				
			} catch (AuthenticationException e) {				
			}
			catch (Throwable e) {
				exception = e;				
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {

			if (success) {
				startActivity(DesktopActivity.class);
			} else {
				if (exception != null) {
					Toast.makeText(StartActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
					finish();
					return;
				} else {
					startActivity(LoginActivity.class);
				}
			}
		}

		@Override
		protected void onCancelled() {
		}
	}

	private void startActivity(Class<? extends Activity> activityClass) {
		Intent intent = new Intent(this, activityClass);
		startActivity(intent);
		finish();
	}

}
