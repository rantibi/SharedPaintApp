package com.sharedpaint.activitys;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myfirstapp.R;

public class DesktopActivity extends Activity {

	private ListView mainListView;
	private ArrayAdapter<MyDrawingItem> listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_desktop);
	
		// Find the ListView resource.   
	    mainListView = (ListView) findViewById( R.id.my_drawings_list_view );  
	  
	     
	    List<MyDrawingItem> planetList = new ArrayList<MyDrawingItem>();  
	    
	    planetList.add(new MyDrawingItem(BitmapFactory.decodeResource(getResources(),
                R.drawable.rectangle)
	    , "Drawing name 1"));
	    planetList.add(new MyDrawingItem(BitmapFactory.decodeResource(getResources(),
                R.drawable.triangle)
	    , "Drawing name 2"));
	    planetList.add(new MyDrawingItem(BitmapFactory.decodeResource(getResources(),
                R.drawable.circle), "Drawing name 3"));
	    planetList.add(new MyDrawingItem(BitmapFactory.decodeResource(getResources(),
                R.drawable.rectangle), "Drawing name 4"));
	    
	    
	    // Create ArrayAdapter using the planet list.  
	    listAdapter = new MyDrawingsAdpter(this, R.layout.my_drawing_list_item, planetList);  
	      
	    // Set the ArrayAdapter as the ListView's adapter.  
	    mainListView.setAdapter( listAdapter );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.desktop, menu);
		return true;
	}

	class MyDrawingsAdpter extends ArrayAdapter<MyDrawingItem> {

		private int resource;
		private Object context;
		private List<MyDrawingItem> objects;

		public MyDrawingsAdpter(Context context, int resource,
				List<MyDrawingItem> objects) {
			super(context, resource, objects);
			this.resource = resource;
			this.context = context;
			this.objects = objects;
		
		}
		
		@Override
	    public View getView(int position, View convertView, ViewGroup parent) {

	        /*
	         * The convertView argument is essentially a "ScrapView" as described is Lucas post 
	         * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
	         * It will have a non-null value when ListView is asking you recycle the row layout. 
	         * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
	         */
	        if(convertView==null){
	            // inflate the layout
	            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
	            convertView = inflater.inflate(R.layout.my_drawing_list_item, parent, false);
	        }

	        // object item based on the position
	        MyDrawingItem item = objects.get(position);

	        ImageView imageView = (ImageView) convertView.findViewById(R.id.row_my_drawing_image);
	        imageView.setImageBitmap(item.getBitmap());
	        
	        // get the TextView and then set the text (item name) and tag (item ID) values
	        TextView textViewItem = (TextView) convertView.findViewById(R.id.row_my_drawing_text_view);
	        textViewItem.setText(item.getName());

	        return convertView;

	    }
		
	}

	class MyDrawingItem {
		Bitmap bitmap;
		String name;

		public Bitmap getBitmap() {
			return bitmap;
		}

		public String getName() {
			return name;
		}

		private MyDrawingItem(Bitmap bitmap, String name) {
			super();
			this.bitmap = bitmap;
			this.name = name;
		}
	}

}
