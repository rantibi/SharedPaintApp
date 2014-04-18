package com.sharedpaint.activitys;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sharedpaint.R;
import com.sharedpaint.SharedPaintException;
import com.sharedpaint.connection.ServerProxy;
import com.sharedpaint.transfer.BoardDetails;

/**
 * Activity which displays create new board
 */
public class CreateNewBoardActivity extends Activity {
	/**
	 * A dummy authentication store containing known user names and passwords.
	 */

	public static final String BOARD_NAME = "BOARD_NAME";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private CreateNewBoardTask authTask = null;

	// Value for board name at the time of the login attempt.
	private String boardName;

	// UI references.
	private EditText boardNameView;
	private View createNewBoardFormView;
	private View createNewBoardStatusView;
	private TextView createNewBoardStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_create_new_board);

		boardName = getIntent().getStringExtra(BOARD_NAME);
		boardNameView = (EditText) findViewById(R.id.new_borad_name_edit);
		boardNameView.setText(boardName);

		createNewBoardFormView = findViewById(R.id.create_new_board_form);
		createNewBoardStatusView = findViewById(R.id.create_new_board_status);
		createNewBoardStatusMessageView = (TextView) findViewById(R.id.create_new_board_status_message);

		findViewById(R.id.create_new_board_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.create_new_board, menu);
		return true;
	}

	/**
	 * Attempts to create new board the account specified by the login form.
	 * If there are form errors, the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (authTask != null) {
			return;
		}

		// Reset errors.
		boardNameView.setError(null);

		// Store values at the time of the login attempt.
		boardName = boardNameView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid email address.
		if (TextUtils.isEmpty(boardName)) {
			boardNameView
					.setError(getString(R.string.create_new_board_name_length_error));
			focusView = boardNameView;
			cancel = true;
		} else if (boardName.length() < 5 || boardName.length() > 15) {
			boardNameView
					.setError(getString(R.string.create_new_board_name_length_error));
			focusView = boardNameView;
			cancel = true;
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the create new board.
			createNewBoardStatusMessageView
					.setText(R.string.create_new_board_progress_creating);
			showProgress(true);
			authTask = new CreateNewBoardTask();
			authTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			createNewBoardStatusView.setVisibility(View.VISIBLE);
			createNewBoardStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							createNewBoardStatusView
									.setVisibility(show ? View.VISIBLE
											: View.GONE);
						}
					});

			createNewBoardFormView.setVisibility(View.VISIBLE);
			createNewBoardFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							createNewBoardFormView
									.setVisibility(show ? View.GONE
											: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			createNewBoardStatusView.setVisibility(show ? View.VISIBLE
					: View.GONE);
			createNewBoardFormView.setVisibility(show ? View.GONE
					: View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous create new board task used to authenticate
	 * the user.
	 */
	public class CreateNewBoardTask extends AsyncTask<Void, Void, Boolean> {
		Exception exception;
		private BoardDetails board;

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				board = ServerProxy.getInstance()
						.createNewBoard(boardName);
			} catch (SharedPaintException e) {
				exception = e;
				return false;
			}

			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			authTask = null;
			showProgress(false);

			if (success) {
				Intent intent = new Intent(CreateNewBoardActivity.this,
						DrawPaintActivity.class);
				
				intent.putExtra(DrawPaintActivity.BOARD, board);
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(CreateNewBoardActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			authTask = null;
			showProgress(false);
		}
	}
}
