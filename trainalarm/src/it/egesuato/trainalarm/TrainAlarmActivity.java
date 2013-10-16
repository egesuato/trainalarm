package it.egesuato.trainalarm;

import it.egesuato.trainalarm.database.TrainAlarmDataSource;
import it.egesuato.trainalarm.model.TrainAlarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

public class TrainAlarmActivity extends Activity {

	private static final String TAG = TrainAlarmActivity.class.getName();
	public static String EDIT_MODE = "EDIT_MODE";
	public static String NEW_MODE = "NEW_MODE";
	
	public static String MODE = "MODE";
	
	
	private String mode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_train_alarm);
		
		mode = getIntent().getStringExtra(TrainAlarmActivity.MODE);
		Button btnDelete = (Button) findViewById(R.id.delete);
		
		TimePicker timeStartAlarm = (TimePicker) findViewById(R.id.startAlarmAt);
		timeStartAlarm.setIs24HourView(true);

		if (mode.equals(EDIT_MODE)){
	    	EditText txtTrainNumber = (EditText) findViewById(R.id.trainNumber);
	    	EditText txtTrainDescription = (EditText) findViewById(R.id.trainDescription);
			btnDelete.setVisibility(View.VISIBLE);
			
	    	long id = getIntent().getLongExtra("id", -1);
	    	TrainAlarmDataSource ds = new TrainAlarmDataSource(getApplicationContext());
	    	ds.open();
	    	TrainAlarm alarmById = null;
	    	try{
	    		alarmById = ds.getAlarmById(id);
	    	}finally{
	    		ds.close();
	    	}
	    	if (alarmById != null){
		    	txtTrainNumber.setText(String.valueOf(alarmById.getTrainNumber()));
		    	txtTrainDescription.setText(alarmById.getDescription());
		    	
		    	long startTime = alarmById.getStartTime();
		    	Calendar cal = new GregorianCalendar();
		    	cal.setTimeInMillis(startTime);
		    	
		    	int hh = cal.get(Calendar.HOUR_OF_DAY);
		    	int mm = cal.get(Calendar.MINUTE);
		    	
		    	timeStartAlarm.setCurrentHour(hh);
		    	timeStartAlarm.setCurrentMinute(mm);
		    	
		    	
	    	} else{
	    		Log.e(TAG, "Error, no alarm found for id " + id);
	    	}
		} else {
			btnDelete.setVisibility(View.INVISIBLE);
			
		}
		
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.train_alarm, menu);
		return true;
	}
    
    public void saveAlarm(View view){
    	EditText txtTrainNumber = (EditText) findViewById(R.id.trainNumber);
    	EditText txtTrainDescription = (EditText) findViewById(R.id.trainDescription);
    	TimePicker timeStartAlarm = (TimePicker) findViewById(R.id.startAlarmAt);

    	TrainAlarm trainAlarm = new TrainAlarm();
    	trainAlarm.setDescription(txtTrainDescription.getText().toString());
    	trainAlarm.setTrainNumber(Integer.parseInt(txtTrainNumber.getText().toString()));
    	
    	Calendar now = new GregorianCalendar();
    	Calendar time = new GregorianCalendar(
    			now.get(Calendar.YEAR), 
    			now.get(Calendar.MONTH), 
    			now.get(Calendar.DAY_OF_MONTH), 
    			timeStartAlarm.getCurrentHour(),
    			timeStartAlarm.getCurrentMinute()
    			);
    	
    	trainAlarm.setStartTime(time.getTimeInMillis());
    	trainAlarm.setId(-1);
    	
    	if (mode.equals(EDIT_MODE)){
    		long id = getIntent().getLongExtra("id", -1);
    		trainAlarm.setId(id);
	    }
    	
    	TrainAlarmDataSource ds = new TrainAlarmDataSource(getApplicationContext());
    	ds.open();
    	try{
    		ds.createOrUpdateAlarm(trainAlarm);
    	}finally{
    		ds.close();
    	}
    	
    	finish();
    }
    
    public void deleteAlarm(View view){
    	long id = getIntent().getLongExtra("id", -1);
    	TrainAlarmDataSource dataSource = new TrainAlarmDataSource(getApplicationContext());
    	dataSource.open();
    	try{
   
    		dataSource.deleteById(id);
    	}finally{
    		dataSource.close();
    	}
    	finish();
    	
    }
}
