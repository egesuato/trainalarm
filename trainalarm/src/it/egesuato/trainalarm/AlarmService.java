package it.egesuato.trainalarm;

import it.egesuato.trainalarm.database.TrainAlarmDataSource;
import it.egesuato.trainalarm.model.TrainAlarm;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class AlarmService extends Service implements Runnable {

	private static final int ID_NOTIFICATION = 234240;
	private static Object sLock = new Object();
	private static boolean sThreadRunning = false;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		// Only start processing thread if not already running
		synchronized (sLock) {
			if (!sThreadRunning) {
				sThreadRunning = true;
				new Thread(this).start();
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void run() {
		TrainAlarmDataSource ds = new TrainAlarmDataSource(
				getApplicationContext());

		ds.open();
		List<TrainAlarm> allAlarms = new ArrayList<TrainAlarm>();
		try {
			allAlarms = ds.getAllAlarms();
		} finally {
			ds.close();
		}

		for (TrainAlarm alarm : allAlarms) {
			String hhmmss = alarm.getStartAlarmAt();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			try {
				Date date = sdf.parse(hhmmss);
				Time time = new Time(date.getTime());

				if (System.currentTimeMillis() > time.getTime()) {
					String check = check(alarm);

				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		Intent updateIntent = new Intent();
		updateIntent.setClass(this, AlarmService.class);

		PendingIntent pendingIntent = PendingIntent.getService(this, 0,
				updateIntent, 0);

		// Schedule alarm, and force the device awake for this update
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, 1000, pendingIntent);

		notifyUser();

	}

	private void notifyUser() {
		Notification.Builder mBuilder = new Notification.Builder(this)
				//.setSmallIcon(R.drawable.app_icon)
				.setContentTitle("My notification")
				.setContentText("Hello World!");

		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, TrainAlarmActivity.class);

		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(TrainAlarmActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(ID_NOTIFICATION, mBuilder.build());
	}

	private String check(TrainAlarm alarm) {
		return "";
	}

}
