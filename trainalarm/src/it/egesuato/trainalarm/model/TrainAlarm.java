package it.egesuato.trainalarm.model;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class TrainAlarm {
	private long id;
	private int trainNumber;
	private String description;
	private long startTime;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getTrainNumber() {
		return trainNumber;
	}

	public void setTrainNumber(int trainNumber) {
		this.trainNumber = trainNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public String toString(){
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(startTime);
		
		int hh = cal.get(Calendar.HOUR_OF_DAY);
		int mm = cal.get(Calendar.MINUTE);
		
		StringBuffer buff = new StringBuffer();
		buff.append("Alarm at ");
		if (hh<=9)
			buff.append("0");
		buff.append(hh);
		buff.append(":");
		if (mm <= 9)
			buff.append(0);
		buff.append(mm);
		
		buff.append(" for train ");
		buff.append(getTrainNumber());
		return buff.toString();
		
	}

}