package it.egesuato.trainalarm;

import it.egesuato.trainalarm.database.TrainAlarmDataSource;
import it.egesuato.trainalarm.model.TrainAlarm;
import it.egesuato.trainalarm.model.TrainAlarmListAdapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
/**
 * Main activity. Used to display all alarms saved.
 * 
 * @author emanuele
 *
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    	Intent intent = new Intent(this, AlarmService.class);
    	
    	startService(intent);
    	
        refresh();
        
    }
    
    

    public void refresh(){
    	TrainAlarmDataSource ds = new TrainAlarmDataSource(getApplicationContext());
    	
    	ds.open();
    	List<TrainAlarm> allAlarms = new ArrayList<TrainAlarm>();
    	try{
    		allAlarms = ds.getAllAlarms();
    	}finally{
    		ds.close();
    	}
    	TrainAlarmListAdapter adapter = new TrainAlarmListAdapter(getApplicationContext(), R.layout.listview_item_row, allAlarms);
    	
    	final ListView listView = (ListView) findViewById(R.id.listAlarms);
    	listView.setAdapter(adapter);
    	listView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				
				TrainAlarm alarm = (TrainAlarm) listView.getItemAtPosition(position);
				
		    	Intent intent = new Intent(MainActivity.this, TrainAlarmActivity.class);
		    	intent.putExtra(TrainAlarmActivity.MODE, TrainAlarmActivity.EDIT_MODE);
		    	intent.putExtra("id", alarm.getId());
		    	startActivity(intent);
		    	
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
    	
    	
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
    	startActivity(intent);

    }
}
