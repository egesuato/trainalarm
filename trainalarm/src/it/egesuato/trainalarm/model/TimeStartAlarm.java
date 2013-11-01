package it.egesuato.trainalarm.model;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


public class TimeStartAlarm {
	
	public static long timeToMillis(int hh, int mm){
		return ((hh * 60) + mm) * 60 * 1000;
	}
	
	public static long nowFromMidnight(){
		Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		return timeToMillis(cal.get(Calendar.HOUR_OF_DAY), Calendar.MINUTE);
	}

	
	public static int[] millisToTime(long millis){
		long minutes = (long) (millis / 60000);
		
		int hh = (int) minutes / 60;
		int mm = (int) minutes % 60;
		
		return new int[]{hh, mm};
	}
}
