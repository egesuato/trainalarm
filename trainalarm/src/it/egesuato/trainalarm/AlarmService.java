package it.egesuato.trainalarm;

import it.egesuato.trainalarm.database.TrainAlarmDataSource;
import it.egesuato.trainalarm.model.TrainAlarm;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
/**
 * This is a single-worker-thread used to check trains in background mode.
 */
public class AlarmService extends IntentService {

	private static final int ID_NOTIFICATION = 234240;
	private final static long TO_STOP_AFTER_MS = 10000;
	public static final String TAG = TrainChecker.class.getSimpleName();
	private TrainChecker checker = new TrainChecker();
	
	public AlarmService() {
		super("train alarm service");
	}


	// Will be called asynchronously be Android
	protected void onHandleIntent(Intent intent) {
		TrainAlarmDataSource ds = new TrainAlarmDataSource(
				getApplicationContext());

		ds.open();
		List<TrainAlarm> allAlarms = new ArrayList<TrainAlarm>();
		try {
			allAlarms = ds.getAllAlarms();
		} finally {
			ds.close();
		}
		List<String> results = new ArrayList<String>();
		
		for (TrainAlarm alarm : allAlarms) {
			String startingAlarm = alarm.getStartAlarmAt();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			try {
				Date date = sdf.parse(startingAlarm);
				Time time = new Time(date.getTime());
				
				if (System.currentTimeMillis() >= time.getTime() && 
						time.getTime() + TO_STOP_AFTER_MS >= System.currentTimeMillis() ) {
					String check = check(alarm);
					results.add(check);
				}

			} catch (ParseException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		
		notifyUser(results);
		
		if (!results.isEmpty()){
			Calendar cal = Calendar.getInstance();

			Intent newIntent = new Intent(this, AlarmService.class);
			PendingIntent pintent = PendingIntent.getService(this, 0, newIntent, 0);

			AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			// restarting service in 30 seconds
			alarm.set(AlarmManager.RTC, cal.getTimeInMillis()+ 30*1000,  pintent); 

		}
		

	}

	private void notifyUser(List<String> results) {
		
		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(TrainAlarmActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, TrainAlarmActivity.class);
		stackBuilder.addNextIntent(resultIntent);

		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		
		Notification.Builder mBuilder = new Notification.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("My notification")
				.setContentText("Hello World!");

		mBuilder.setContentIntent(resultPendingIntent);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(ID_NOTIFICATION, mBuilder.build());
	}

	private String check(TrainAlarm alarm) {
		return checker.checkTrainStatus(alarm.getTrainNumber());
	}

}
