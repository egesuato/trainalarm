package it.egesuato.trainalarm;

import it.egesuato.trainalarm.database.TrainAlarmDataSource;
import it.egesuato.trainalarm.model.TimeStartAlarm;
import it.egesuato.trainalarm.model.TrainAlarm;

import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

public class AlarmService extends IntentService {
	public static final String TAG = AlarmService.class.getSimpleName();
	private TrainChecker checker = new TrainChecker();
	
	public AlarmService() {
		super("starting service");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "Starting alarms");
		
		Long id = (Long) intent.getExtras().get("id");
		if (id == null){
			return;
		}
		TrainAlarmDataSource ds = new TrainAlarmDataSource(
				getApplicationContext());

		ds.open();
		
		try {
			TrainAlarm alarm = ds.getAlarmById(id);
			
			List<String> results = checkAlarm(alarm);
			if (!results.isEmpty()){
				notifyUser(alarm, results);
			}
			
		} finally {
			ds.close();
		}
	}

	private List<String> checkAlarm(TrainAlarm alarm) {
		
		List<String> results = new ArrayList<String>();
		
		String check = checker.checkTrainStatus(alarm.getTrainNumber());
		Log.d(TAG, "Checked train " + alarm.getTrainNumber() + " with result: " + alarm.getDescription());
		results.add(check);
		
		return results;
	
	}
	
	
	private void notifyUser(TrainAlarm alarm, List<String> results) {
		int[] millisToTime = TimeStartAlarm.millisToTime(alarm.getStartTime());
		int id = millisToTime[0] * 100 + millisToTime[1];
		
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
			
			mNotificationManager.notify(id, mBuilder.getNotification());
		}
	    
	}

}
