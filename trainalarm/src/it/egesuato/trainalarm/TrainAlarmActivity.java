package it.egesuato.trainalarm;

import it.egesuato.trainalarm.database.TrainAlarmDataSource;
import it.egesuato.trainalarm.model.TrainAlarm;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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
		
		if (mode.equals(EDIT_MODE)){
	    	EditText txtTrainNumber = (EditText) findViewById(R.id.trainNumber);
	    	EditText txtTrainDescription = (EditText) findViewById(R.id.trainDescription);
	    	TimePicker timeStartAlarm = (TimePicker) findViewById(R.id.startAlarmAt);
	    	
	    	long id = getIntent().getLongExtra("id", -1);
	    	TrainAlarmDataSource dataSource = new TrainAlarmDataSource(getApplicationContext());
	    	TrainAlarm alarmById = dataSource.getAlarmById(id);
	    	
	    	if (alarmById != null){
		    	txtTrainNumber.setText(alarmById.getTrainNumber());
		    	txtTrainDescription.setText(alarmById.getDescription());
		    	
		    	stringAsTime(alarmById, timeStartAlarm);
		    	
		    	
	    	} else{
	    		Log.e(TAG, "Error, no alarm found for id " + id);
	    	}
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
    	trainAlarm.setStartAlarmAt(timeAsString(timeStartAlarm));
    	trainAlarm.setId(-1);
    	
    	if (mode.equals(EDIT_MODE)){
    		long id = getIntent().getLongExtra("id", -1);
    		trainAlarm.setId(id);
	    }
    	
    	TrainAlarmDataSource dataSource = new TrainAlarmDataSource(getApplicationContext());
    	dataSource.open();
    	try{
    		dataSource.createOrUpdateAlarm(trainAlarm);
    	}finally{
    		dataSource.close();
    	}
    	
    	finish();
    }
    
    
	private void stringAsTime(TrainAlarm alarmById, TimePicker timeStartAlarm) {
		String startAlarmAt = alarmById.getStartAlarmAt();
		
		String[] split = startAlarmAt.split(":");
		String hh = split[0];
		if (hh.length() == 2 && hh.startsWith("0")){
			hh = hh.substring(1);
		}
		String mm = split[1];
		if (mm.length() == 2 && mm.startsWith("0")){
			mm = mm.substring(1);
		}
		
		timeStartAlarm.setCurrentHour(Integer.parseInt(hh));
		timeStartAlarm.setCurrentMinute(Integer.parseInt(mm));
		
	}
	
    private String timeAsString(TimePicker timePicker){
    	String currentHour = String.valueOf(timePicker.getCurrentHour());
    	String currentMinute = String.valueOf(timePicker.getCurrentMinute());
    	
    	if (currentHour.length() == 1){
    		currentHour = "0" + currentHour;
    	}
    	
    	if (currentMinute.length() == 1){
    		currentMinute = "0" + currentMinute;
    	}
    	
    	return currentHour + ":" + currentMinute;
    	
    }
}
