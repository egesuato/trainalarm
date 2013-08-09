package it.egesuato.trainalarm;

import java.text.SimpleDateFormat;

import it.egesuato.trainalarm.database.TrainAlarmDataSource;
import it.egesuato.trainalarm.model.TrainAlarm;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

public class TrainAlarmActivity extends Activity {
	private String mode;
	
	public static String MODE = "MODE";
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	
	public static String EDIT_MODE = "EDIT_MODE";
	public static String NEW_MODE = "NEW_MODE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_train_alarm);
		
		Intent intent = getIntent();
		mode = intent.getStringExtra(TrainAlarmActivity.MODE);
		
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

    	if (mode.equals(NEW_MODE)){
	    	 
	    	TrainAlarm trainAlarm = new TrainAlarm();
	    	trainAlarm.setDescription(txtTrainDescription.getText().toString());
	    	trainAlarm.setTrainNumber(Integer.parseInt(txtTrainNumber.getText().toString()));
	    	
	    	trainAlarm.setStartAlarmAt(timeAsString(timeStartAlarm));
	    	
	    	TrainAlarmDataSource dataSource = new TrainAlarmDataSource(getApplicationContext());
	    	dataSource.open();
	    	try{
	    		dataSource.createAlarm(trainAlarm);
	    	}finally{
	    		dataSource.close();
	    	}
	    	finish();
    	}
    }
    
    private void startAlarm(TrainAlarm alarm){
    	
        Intent updateIntent = new Intent(String.valueOf(alarm.getTrainNumber()));/*
        updateIntent.setClass(this, UpdateService.class);

        PendingIntent pendingIntent = PendingIntent.getService(this, 0, updateIntent, 0);

        // Schedule alarm, and force the device awake for this update
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, nextUpdate, pendingIntent);*/
        
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
