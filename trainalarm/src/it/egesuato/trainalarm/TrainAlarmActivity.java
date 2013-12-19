package it.egesuato.trainalarm;

import it.egesuato.trainalarm.database.TrainAlarmDataSource;
import it.egesuato.trainalarm.model.TimeStartAlarm;
import it.egesuato.trainalarm.model.TrainAlarm;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
		
		EditText txtTrainNumber = (EditText) findViewById(R.id.trainNumber);
		txtTrainNumber.requestFocus();
		
		TimePicker timeStartAlarm = (TimePicker) findViewById(R.id.startAlarmAt);
		timeStartAlarm.setIs24HourView(true);

		if (mode.equals(EDIT_MODE)){
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
		    	int[] hhmm = TimeStartAlarm.millisToTime(startTime);
		    	
		    	timeStartAlarm.setCurrentHour(hhmm[0]);
		    	timeStartAlarm.setCurrentMinute(hhmm[1]);
		    	
		    	
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
    	Editable text = txtTrainNumber.getText();
    	if (text == null || "".equals(text.toString())){
    		showDialog("Error", "Train number is mandatory", txtTrainNumber);
    		return;
    	} 
		trainAlarm.setTrainNumber(Integer.parseInt(text.toString()));
    	
    	long millis = TimeStartAlarm.timeToMillis(timeStartAlarm.getCurrentHour(), timeStartAlarm.getCurrentMinute());
		trainAlarm.setStartTime(millis);
    	trainAlarm.setId(-1);
    	
    	if (mode.equals(EDIT_MODE)){
    		long id = getIntent().getLongExtra("id", -1);
    		trainAlarm.setId(id);
	    }
    	
    	TrainAlarmDataSource ds = new TrainAlarmDataSource(getApplicationContext());
    	ds.open();
    	try{
    		trainAlarm = ds.createOrUpdateAlarm(trainAlarm);
    	}finally{
    		ds.close();
    	}
    	finish();
    	
		TimeStartAlarm.startAlarm(trainAlarm, getApplicationContext());
    	
    }
    
    private void showDialog(String title, String message, View txtTrainNumber) {
    	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		// Setting Dialog Title
		alertDialog.setTitle(title);
		
		// Setting Dialog Message
		alertDialog.setMessage(message);
		
		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		        // Write your code here to execute after dialog closed
		        }
		});
		
		// Showing Alert Message
		alertDialog.show();
		
		txtTrainNumber.requestFocus();
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
