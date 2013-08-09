package it.egesuato.trainalarm.model;

public class TrainAlarm {
	private long id;
	private int trainNumber;
	private String description;
	// in HH:MM:SS format
	private String startAlarmAt;

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

	public String getStartAlarmAt() {
		return startAlarmAt;
	}

	public void setStartAlarmAt(String startAlarmAt) {
		this.startAlarmAt = startAlarmAt;
	}

}