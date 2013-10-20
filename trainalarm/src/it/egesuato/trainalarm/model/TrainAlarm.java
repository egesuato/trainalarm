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
		int[] hhmm = TimeStartAlarm.millisToTime(startTime);
		
		StringBuffer buff = new StringBuffer();
		buff.append("Alarm at ");
		if (hhmm[0]<=9)
			buff.append("0");
		buff.append(hhmm[0]);
		buff.append(":");
		if (hhmm[1] <= 9)
			buff.append(0);
		buff.append(hhmm[1]);
		
		buff.append(" for train ");
		buff.append(getTrainNumber());
		return buff.toString();
		
	}

}