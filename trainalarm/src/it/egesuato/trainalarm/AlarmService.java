package it.egesuato.trainalarm;

import it.egesuato.trainalarm.database.TrainAlarmDataSource;
import it.egesuato.trainalarm.model.TrainAlarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
/**
 * This is a single-worker-thread used to check trains in background mode.
 */
public class AlarmService extends IntentService {

	private static final int ID_NOTIFICATION = 234240;
	private final static long TO_STOP_AFTER_MS = 5 * 60 * 1000;
	public static final String TAG = TrainChecker.class.getSimpleName();
	private TrainChecker checker = new TrainChecker();
	
	public AlarmService() {
		super("train alarm service");
	}


	// Will be called asynchronously by Android
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "Starting alarm service");
		
		TrainAlarmDataSource ds = new TrainAlarmDataSource(
				getApplicationContext());

		ds.open();
		List<TrainAlarm> allAlarms = new ArrayList<TrainAlarm>();
		try {
			allAlarms = ds.getAllAlarmsToBeChecked(10);
			Log.d(TAG, String.format("Found %d trains to check", allAlarms.size()));
		} finally {
			ds.close();
		}
		List<String> results = new ArrayList<String>();
		
		for (TrainAlarm alarm : allAlarms) {
			String check = check(alarm);
			results.add(check);
		}
		
		if (!results.isEmpty()){
			notifyUser(results);
		}
	//	if (!results.isEmpty()){
		Calendar cal = Calendar.getInstance();

		Intent newIntent = new Intent(this, AlarmService.class);
		PendingIntent pintent = PendingIntent.getService(this, 0, newIntent, 0);

		AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		// restarting service in 2 minutes
		alarm.set(AlarmManager.RTC, cal.getTimeInMillis()+ 2*60*1000,  pintent); 

	//	}
		

	}

	private void notifyUser(List<String> results) {
		for (String result : results){
			NotificationMessage message = new NotificationMessage(result);
			
			//Define sound URI
			Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	
			
			Notification.Builder mBuilder = new Notification.Builder(this)
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle(message.getTitle())
					.setContentText(message.getDetail())
					.setSound(soundUri);
			
			
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			
			mNotificationManager.notify(ID_NOTIFICATION, mBuilder.getNotification());
		}
	    
	}

	private String check(TrainAlarm alarm) {
		return checker.checkTrainStatus(alarm.getTrainNumber());
	}

	
}
