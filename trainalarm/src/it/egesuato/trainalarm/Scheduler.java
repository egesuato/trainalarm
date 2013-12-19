package it.egesuato.trainalarm;

import it.egesuato.trainalarm.database.TrainAlarmDataSource;
import it.egesuato.trainalarm.model.TimeStartAlarm;
import it.egesuato.trainalarm.model.TrainAlarm;

import java.util.ArrayList;
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
 * Its purpose is to schedule every alarms installed at proper time.
 */
public class Scheduler extends IntentService {

	public static final String TAG = Scheduler.class.getSimpleName();
	
	public Scheduler() {
		super("starting alarms");
	}


	// Will be called asynchronously by Android
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "Starting alarms");
		
		TrainAlarmDataSource ds = new TrainAlarmDataSource(
				getApplicationContext());

		ds.open();
		List<TrainAlarm> allAlarms = new ArrayList<TrainAlarm>();
		try {
			allAlarms = ds.getAllAlarms();
			Log.d(TAG, String.format("Found %d trains to check", allAlarms.size()));
		} finally {
			ds.close();
		}
		
		
		
		for (TrainAlarm alarm : allAlarms){
			TimeStartAlarm.startAlarm(alarm, getApplicationContext());
		}
		
		

	}


	
}
