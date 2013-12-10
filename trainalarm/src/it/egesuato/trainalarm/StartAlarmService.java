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
 */
public class StartAlarmService extends IntentService {

	private static final int ID_NOTIFICATION = 234240;
	public static final String TAG = StartAlarmService.class.getSimpleName();
	
	public StartAlarmService() {
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
		/*List<String> results = new ArrayList<String>();
		
		for (TrainAlarm alarm : allAlarms) {
			String check = check(alarm);
			results.add(check);
		}
		
		if (!results.isEmpty()){
			notifyUser(results);
		}
	//	if (!results.isEmpty()){
		Calendar cal = Calendar.getInstance();

		// restarting service in 2 minutes
		alarm.set(AlarmManager.RTC, cal.getTimeInMillis()+ 2*60*1000,  pintent); 
		*/
	//	}
		

	}


//	public void startAlarm( TrainAlarm alarm) {
//		AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//		
//		Intent newIntent = new Intent(getApplicationContext(), AlarmService.class);
//		
//		int[] millisToTime = TimeStartAlarm.millisToTime(alarm.getStartTime());
//		int id = millisToTime[0] * 100 + millisToTime[1];
//		
//		PendingIntent pintent = PendingIntent.getService(getApplicationContext(), id, newIntent, 0);
//		
//		if (alarm.getStartTime() > TimeStartAlarm.nowFromMidnight()){
//			long realStartingTime = alarm.getStartTime()-(5*60*1000);
//			newIntent.putExtra("requestCode", id);
//			newIntent.putExtra("id", alarm.getId());
//			
//			Log.d(TAG, "Set alarm to start at " + TimeStartAlarm.timeToHumanReadable(realStartingTime));
//			manager.set(AlarmManager.RTC, realStartingTime,  pintent); 
//		}
//	}
//
//	private void notifyUser(List<String> results) {
//		for (String result : results){
//			NotificationMessage message = new NotificationMessage(result);
//			
//			//Define sound URI
//			Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//	
//
//			
//			Notification.Builder mBuilder = new Notification.Builder(this)
//					.setSmallIcon(R.drawable.ic_launcher)
//					.setContentTitle(message.getTitle())
//					.setContentText(message.getDetail())
//					.setSound(soundUri);
//			
//			
//			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//			
//			mNotificationManager.notify(ID_NOTIFICATION, mBuilder.getNotification());
//		}
//	    
//	}
//
//	private String check(TrainAlarm alarm) {
//		return checker.checkTrainStatus(alarm.getTrainNumber());
//	}

	
}
