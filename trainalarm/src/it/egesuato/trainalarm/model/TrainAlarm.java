package it.egesuato.trainalarm.model;

import java.util.Date;

public class TrainAlarm {
	private long id;
	private int trainNumber;
	private String description;
	
	// in HH:MM:SS format
	private int hoursStartAlarmAt;
	private int minutesStartAlarmAt;

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

	public int getHoursStartAlarmAt() {
		return hoursStartAlarmAt;
	}

	public void setHoursStartAlarmAt(int hoursStartAlarmAt) {
		this.hoursStartAlarmAt = hoursStartAlarmAt;
	}

	public int getMinutesStartAlarmAt() {
		return minutesStartAlarmAt;
	}

	public void setMinutesStartAlarmAt(int minutesStartAlarmAt) {
		this.minutesStartAlarmAt = minutesStartAlarmAt;
	}
	
	public String getDisplayableHoursAndMinutes(){
		return getHoursStartAlarmAt() + ":" + getMinutesStartAlarmAt();
	}

	

}