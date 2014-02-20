package com.sharedpaint.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.example.myfirstapp.R;

public class CreateNewBoardActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_board);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_new_board, menu);
		return true;
	}

}
