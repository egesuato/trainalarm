package it.egesuato.trainalarm;

import it.egesuato.trainalarm.model.TrainAlarmListAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
/**
 * This activity represents a single row used by {@link TrainAlarmListAdapter}.
 * 
 * @author emanuele
 *
 */
public class ListviewItemRowActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_item_row);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.listview_item_row, menu);
		return true;
	}

}
