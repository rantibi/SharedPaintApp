package com.sharedpaint.activitys;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sharedpaint.R;
import com.sharedpaint.SharedPaintException;
import com.sharedpaint.connection.ServerProxy;
import com.sharedpaint.transfer.BoardDetails;

public class DesktopActivity extends Activity {

	private ListView mainListView;
	private ArrayAdapter<BoardDetails> listAdapter;
	private ArrayList<BoardDetails> drawingList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_desktop);

		createBoardsListView();
	}

	private void createBoardsListView() {
		// Find the ListView resource.
		mainListView = (ListView) findViewById(R.id.my_drawings_list_view);
		drawingList = new ArrayList<BoardDetails>();
		listAdapter = new DrawingsAdpter(this, R.layout.my_drawing_list_item,
				drawingList);
		mainListView.setAdapter(listAdapter);
		mainListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(DesktopActivity.this,
						DrawPaintActivity.class);
				intent.putExtra(DrawPaintActivity.BOARD,
						drawingList.get(position));
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.desktop, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_create_new_board:
			Intent intent = new Intent(this, CreateNewBoardActivity.class);
			startActivity(intent);
			break;
		case R.id.action_logout:
			logout();
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void logout() {
		ServerProxy.getInstance(this).logout();
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		new LoadBoardsTask().execute((Void) null);
	}

	public class LoadBoardsTask extends AsyncTask<Void, Void, Boolean> {
		private SharedPaintException excepetion;

		@Override
		protected Boolean doInBackground(Void... params) {
			List<BoardDetails> boards;

			try {
				boards = ServerProxy.getInstance(DesktopActivity.this)
						.getBoards();
			} catch (SharedPaintException e) {
				excepetion = e;
				return false;
			}

			drawingList.clear();
			drawingList.addAll(boards);
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (success) {
				listAdapter.notifyDataSetChanged();
			} else {
				MessageBox.show(DesktopActivity.this, excepetion.getMessage(),
						getString(android.R.string.ok));
			}
		}

		@Override
		protected void onCancelled() {
			// mAuthTask = null;
			// showProgress(false);
		}
	}

	class DrawingsAdpter extends ArrayAdapter<BoardDetails> {

		private int resource;
		private Object context;
		private List<BoardDetails> objects;

		public DrawingsAdpter(Context context, int resource,
				List<BoardDetails> objects) {
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
				convertView = inflater.inflate(R.layout.my_drawing_list_item,
						parent, false);
			}

			// object item based on the position
			BoardDetails item = objects.get(position);

			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.row_my_drawing_image);
			imageView.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.ic_launcher));

			// get the TextView and then set the text (item name) and tag (item
			// ID) values
			TextView textViewItem = (TextView) convertView
					.findViewById(R.id.row_my_drawing_text_board_name);
			textViewItem.setText(item.getName());
/*
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(item.getLastUpdate());
			
			TextView lastUpdateViewItem = (TextView) convertView
					.findViewById(R.id.row_my_drawing_text_last_update);
			lastUpdateViewItem.setText(calendar.toString());
*/
			return convertView;

		}

	}
}
