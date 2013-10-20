package it.egesuato.trainalarm.model;


public class TimeStartAlarm {
	
	public static long timeToMillis(int hh, int mm){
		return ((hh * 60) + mm) * 60 * 1000;
	}


	
	public static int[] millisToTime(long millis){
		long minutes = (long) (millis / 60000);
		
		int hh = (int) minutes / 60;
		int mm = (int) minutes % 60;
		
		return new int[]{hh, mm};
	}
}
