package it.egesuato.trainalarm;

import it.egesuato.trainalarm.database.TrainAlarmDataSource;
import it.egesuato.trainalarm.model.TrainAlarm;
import it.egesuato.trainalarm.model.TrainAlarmListAdapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
/**
 * Main activity. Used to display all alarms saved.
 * 
 * @author emanuele
 *
 */
public class MainActivity extends Activity {
	/*
	 * constant used to notify refresh requests
	 */
    private static final int ACTIVITY_RESULT_FOR_REFRESH = 1;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    	final ListView listView = (ListView) findViewById(R.id.listAlarms);
    	listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				TrainAlarm alarm = (TrainAlarm) listView.getItemAtPosition(position);
				
		    	Intent intent = new Intent(MainActivity.this, TrainAlarmActivity.class);
		    	intent.putExtra(TrainAlarmActivity.MODE, TrainAlarmActivity.EDIT_MODE);
		    	intent.putExtra("id", alarm.getId());
		    	startActivityForResult(intent, ACTIVITY_RESULT_FOR_REFRESH);
			}
		});
    	
    	
    	Intent intent = new Intent(this, StartAlarmService.class);
    	
    	startService(intent);
    	
        refresh();
        
    }
    
    

    public void refresh(){
    	// delegate refresh task to an AsyncTask
    	new RefreshPageTask().execute();
    }
    
    
    
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		refresh();
	}



	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    
    public void goToInsertActivity(View view){
    	Intent intent = new Intent(this, TrainAlarmActivity.class);
    	intent.putExtra(TrainAlarmActivity.MODE, TrainAlarmActivity.NEW_MODE);
    	startActivityForResult(intent, 1);

    }
    
    /**
     * Async task used to refresh the list of alarms installed.
     * 
     * @author emanuele
     *
     */
    private class RefreshPageTask extends AsyncTask<Void, Void, List<TrainAlarm>> {

		@Override
		protected List<TrainAlarm> doInBackground(Void... params) {

	    	TrainAlarmDataSource ds = new TrainAlarmDataSource(getApplicationContext());
	    	
	    	ds.open();
	    	List<TrainAlarm> allAlarms = new ArrayList<TrainAlarm>();
	    	try{
	    		allAlarms = ds.getAllAlarms();
	    	}finally{
	    		ds.close();
	    	}
	    	
			return allAlarms;
		}

		@Override
		protected void onPostExecute(List<TrainAlarm> allAlarms) {
	    	TrainAlarmListAdapter adapter = new TrainAlarmListAdapter(getApplicationContext(), R.layout.listview_item_row, allAlarms);
	    	
	    	final ListView listView = (ListView) findViewById(R.id.listAlarms);
	    	listView.setAdapter(adapter);
		}
		
		
    	
    }
}
