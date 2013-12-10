package it.egesuato.trainalarm.model;

import it.egesuato.trainalarm.AlarmService;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class TimeStartAlarm {
	
	private static final String TAG = TimeStartAlarm.class.getName();

	public static long timeToMillis(int hh, int mm){
		return ((hh * 60) + mm) * 60 * 1000;
	}
	
	/**
	 * @return milliseconds from midnight to now.
	 */
	public static long nowFromMidnight(){
		Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		return timeToMillis(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
	}

	/**
	 * 
	 * @param millis needs to be millis from midnight
	 * @return an array with in the first position the hour, the second the minutes. 
	 */
	public static int[] millisToTime(long millis){
		long minutes = (long) (millis / 60000);
		
		int hh = (int) minutes / 60;
		int mm = (int) minutes % 60;
		
		return new int[]{hh, mm};
	}

	public static long midnightTime(){
		Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal.getTimeInMillis();
	}

	
	public static String timeToHumanReadable(long time){
		int[] hhmm = TimeStartAlarm.millisToTime(time);		
		return timeToHumanReadable(hhmm);
	}
	
	public static String timeToHumanReadable( int[] hhmm){
		StringBuffer buff = new StringBuffer();
		if (hhmm[0]<=9)
			buff.append("0");
		buff.append(hhmm[0]);
		buff.append(":");
		if (hhmm[1] <= 9)
			buff.append(0);
		buff.append(hhmm[1]);

		return buff.toString();
	}
	
	public static void startAlarm( TrainAlarm alarm, Context ctx) {
		AlarmManager manager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
		int[] millisToTime = TimeStartAlarm.millisToTime(alarm.getStartTime());
		int requestCode = millisToTime[0] * 100 + millisToTime[1];
		
		Intent newIntent = new Intent(ctx.getApplicationContext(), AlarmService.class);
		newIntent.putExtra("requestCode", requestCode);
		newIntent.putExtra("id", alarm.getId());
		
		PendingIntent pintent = PendingIntent.getService(ctx.getApplicationContext(), requestCode, newIntent, 0);
		
		long nowFromMidnight = TimeStartAlarm.nowFromMidnight();
		long realStartingTime = alarm.getStartTime()-(2*60*1000);
		
		Log.d(TAG, "Set alarm to start at " + TimeStartAlarm.timeToHumanReadable(realStartingTime));
		
		manager.set(AlarmManager.RTC, TimeStartAlarm.midnightTime() + realStartingTime,  pintent); 
	}
	
}
