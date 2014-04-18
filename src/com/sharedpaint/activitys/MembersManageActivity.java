package com.sharedpaint.activitys;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharedpaint.R;
import com.sharedpaint.SharedPaintException;
import com.sharedpaint.connection.ServerProxy;
import com.sharedpaint.transfer.BoardDetails;

public class MembersManageActivity extends Activity {
	private BoardDetails boardDetails;
	private ListView mainListView;
	private List<String> membersList;
	private MembersAdpter listAdapter;
	private boolean isManager;
	private TextView newUserEmail;
	private AddMemberTask addTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_members_manage);
		boardDetails = (BoardDetails) getIntent().getSerializableExtra(
				DrawPaintActivity.BOARD);
		createBoardsListView();

		new LoadMembersTask().execute((Void) null);

		isManager = ServerProxy.getInstance()
				.getUserEmail().equals(boardDetails.getManagerEmail());

		if (!isManager) {
			View view = (View) findViewById(R.id.add_members_view);
			view.setVisibility(View.GONE);
		}

		newUserEmail = (TextView) findViewById(R.id.add_member_email);

		Button addButton = (Button) findViewById(R.id.add_member_button);
		addButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				addMember();
			}

		});

	}

	private void addMember() {
		if (addTask != null) {
			return;
		}

		// Reset errors.
		newUserEmail.setError(null);

		// Store values at the time of the login attempt.
		String email = newUserEmail.getText().toString();

		boolean cancel = false;
		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			newUserEmail.setError(getString(R.string.error_field_required));
			cancel = true;
		} else if (!email.contains("@")) {
			newUserEmail.setError(getString(R.string.error_invalid_email));
			cancel = true;
		}

		if (!cancel) {
			addTask = new AddMemberTask();
			addTask.execute(newUserEmail.getText().toString());
		}
	}

	private void createBoardsListView() {
		// Find the ListView resource.
		mainListView = (ListView) findViewById(R.id.members_list_view);
		membersList = new ArrayList<String>();
		listAdapter = new MembersAdpter(this, R.layout.board_members_list_item,
				membersList);
		mainListView.setAdapter(listAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.members_mange, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class LoadMembersTask extends AsyncTask<Void, Void, Boolean> {
		private SharedPaintException excepetion;

		@Override
		protected Boolean doInBackground(Void... params) {
			List<String> boards;

			try {
				boards = ServerProxy.getInstance()
						.getBoardMembers(boardDetails.getId());
			} catch (SharedPaintException e) {
				excepetion = e;
				return false;
			}

			membersList.clear();
			membersList.addAll(boards);
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (success) {
				listAdapter.notifyDataSetChanged();
			} else {
				MessageBox
						.show(MembersManageActivity.this,
								excepetion.getMessage(),
								getString(android.R.string.ok));
			}
		}

		@Override
		protected void onCancelled() {
			// mAuthTask = null;
			// showProgress(false);
		}
	}

	public class RemoveMemberTask extends AsyncTask<String, Void, Boolean> {
		private SharedPaintException excepetion;

		@Override
		protected Boolean doInBackground(String... params) {

			try {
				ServerProxy.getInstance()
						.removeMemberFromBoard(boardDetails.getId(), params[0]);
				membersList.remove(params[0]);
			} catch (SharedPaintException e) {
				excepetion = e;
				return false;
			}

			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (success) {
				listAdapter.notifyDataSetChanged();
			} else {
				Toast.makeText(MembersManageActivity.this, excepetion.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
	}

	public class AddMemberTask extends AsyncTask<String, Void, Boolean> {
		private SharedPaintException excepetion;

		@Override
		protected Boolean doInBackground(String... params) {

			try {
				ServerProxy.getInstance()
						.addMemberToBoard(boardDetails.getId(), params[0]);
				membersList.add(params[0]);
			} catch (SharedPaintException e) {
				excepetion = e;
				return false;
			}

			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (success) {
				listAdapter.notifyDataSetChanged();
				newUserEmail.setText("");
			} else {
				newUserEmail.setError(excepetion.getMessage());
				}

			addTask = null;
		}

		@Override
		protected void onCancelled() {
			addTask = null;
			// showProgress(false);
		}
	}

	class MembersAdpter extends ArrayAdapter<String> {

		private int resource;
		private Object context;
		private List<String> objects;

		public MembersAdpter(Context context, int resource, List<String> objects) {
			super(context, resource, objects);
			this.resource = resource;
			this.context = context;
			this.objects = objects;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			/*
			 * The convertView argument is essentially a "ScrapView" as
			 * described is Lucas post
			 * http://lucasr.org/2012/04/05/performance-tips
			 * -for-androids-listview/ It will have a non-null value when
			 * ListView is asking you recycle the row layout. So, when
			 * convertView is not null, you should simply update its contents
			 * instead of inflating a new row layout.
			 */
			if (convertView == null) {
				// inflate the layout
				LayoutInflater inflater = ((Activity) context)
						.getLayoutInflater();
				convertView = inflater.inflate(
						R.layout.board_members_list_item, parent, false);
			}

			// object item based on the position
			final String item = objects.get(position);

			ImageButton imageButton = (ImageButton) convertView
					.findViewById(R.id.row_remove_member_image);

			if (!isManager) {
				imageButton.setVisibility(View.GONE);
			} else {
				imageButton.setTag(item);
				imageButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						new RemoveMemberTask().execute(item);
					}
				});
			}

			// get the TextView and then set the text (item name) and tag (item
			// ID) values
			TextView textViewItem = (TextView) convertView
					.findViewById(R.id.row_member_text_view);
			textViewItem.setText(item);

			return convertView;

		}
	}

}
