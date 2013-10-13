package it.egesuato.trainalarm.database;

import it.egesuato.trainalarm.model.TrainAlarm;
import android.text.format.Time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TrainAlarmDataSource {

	// Database fields
	private static final String TAG_LOG = TrainAlarmDataSource.class.getSimpleName();
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { 
			MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_TRAIN_NUMBER,
			MySQLiteHelper.COLUMN_TRAIN_DESCRIPTION,
			MySQLiteHelper.COLUMN_TRAIN_HH_START_ALARM_AT,
			MySQLiteHelper.COLUMN_TRAIN_MM_START_ALARM_AT
			
	};

	public TrainAlarmDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public TrainAlarm createOrUpdateAlarm(TrainAlarm trainAlarm) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_TRAIN_NUMBER,
				trainAlarm.getTrainNumber());
		values.put(MySQLiteHelper.COLUMN_TRAIN_DESCRIPTION,
				trainAlarm.getDescription());
		values.put(MySQLiteHelper.COLUMN_TRAIN_HH_START_ALARM_AT,
				trainAlarm.getHoursStartAlarmAt());
		values.put(MySQLiteHelper.COLUMN_TRAIN_MM_START_ALARM_AT,
				trainAlarm.getMinutesStartAlarmAt());
		
		long newOrUpdatedId;
		
		if (trainAlarm.getId() == -1){
			newOrUpdatedId = database
				.insert(MySQLiteHelper.TABLE_NAME, null, values);
			
		} else {
			values.put(MySQLiteHelper.COLUMN_ID, trainAlarm.getId());
			
			newOrUpdatedId = database
					.update(MySQLiteHelper.TABLE_NAME, values,  MySQLiteHelper.COLUMN_ID + " = " +trainAlarm.getId(), null);
		}
		Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME, allColumns,
				MySQLiteHelper.COLUMN_ID + " = " + newOrUpdatedId, null, null, null,
				null);
		cursor.moveToFirst();
		TrainAlarm newTrainAlarm = cursorToTrainAlarm(cursor);
		cursor.close();
		return newTrainAlarm;
	}
	
	/**
	 * Return all alarms saved in database.
	 * 
	 * @return all alarms currently present in database.
	 */
	public List<TrainAlarm> getAllAlarms() {
		Log.d(TAG_LOG, "Retrieving all alarms");
		
		List<TrainAlarm> alarms = new ArrayList<TrainAlarm>();

		Cursor cursor = database.query(
				MySQLiteHelper.TABLE_NAME, 
				allColumns,
				null, 
				null, 
				null,
				null, 
				null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			TrainAlarm trainAlarm = cursorToTrainAlarm(cursor);
			alarms.add(trainAlarm);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return alarms;
	}

	/**
	 * Returning all alarms with alarm >= currentTime and alarm>=currentTime+gaptime
	 * 
	 * @param minutes for the "window" time for which the alarm is still valid. 
	 * For example: if an alarm is set to 15.30 but minutes is 10 and now it is 15.35 the alarm is 
	 * retrieved and the train checked. 
	 * @return all alarms that needs to be checked NOW.
	 */
	public List<TrainAlarm> getAllAlarmsToBeChecked(int minutes) {
		Log.d(TAG_LOG, "Retrieving all alarms to be checked");
		
		// looking for all alarms with alarm >= currentTime and alarm <= currentTime + 10 minutes
		List<TrainAlarm> alarms = new ArrayList<TrainAlarm>();
		Calendar cal = new GregorianCalendar();
		int startHH = cal.get(Calendar.HOUR_OF_DAY);
		int startMM = cal.get(Calendar.MINUTE);
		
		cal.add(Calendar.MINUTE, 10);
		int finishHH = cal.get(Calendar.HOUR_OF_DAY);
		int finishMM = cal.get(Calendar.MINUTE);
		
		StringBuffer where = new StringBuffer();
		addWhere(where, MySQLiteHelper.COLUMN_TRAIN_HH_START_ALARM_AT, ">=", startHH, true);
		addWhere(where, MySQLiteHelper.COLUMN_TRAIN_HH_START_ALARM_AT, "<=", finishHH, true);
		addWhere(where, MySQLiteHelper.COLUMN_TRAIN_MM_START_ALARM_AT, ">=", startMM, true);
		addWhere(where, MySQLiteHelper.COLUMN_TRAIN_MM_START_ALARM_AT, "<=", finishMM, false);

		Cursor cursor = database.query(
				MySQLiteHelper.TABLE_NAME, 
				allColumns,
				where.toString() , 
				null, 
				null,
				null, 
				null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			TrainAlarm trainAlarm = cursorToTrainAlarm(cursor);
			alarms.add(trainAlarm);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return alarms;
	}
	
	private void addWhere(StringBuffer where, String columnName,
			String operator, int value, boolean addAnd) {

		where.append(columnName);
		where.append(operator);
		where.append(value);
		if (addAnd){
			where.append(" and ");
		}
	}

	private TrainAlarm cursorToTrainAlarm(Cursor cursor) {
		TrainAlarm trainAlarm = new TrainAlarm();
		trainAlarm.setId(cursor.getLong(0));
		trainAlarm.setTrainNumber(cursor.getInt(1));
		trainAlarm.setDescription(cursor.getString(2));
		trainAlarm.setHoursStartAlarmAt(cursor.getInt(3));
		trainAlarm.setMinutesStartAlarmAt(cursor.getInt(4));

		return trainAlarm;
	}

	public TrainAlarm getAlarmById(long id) {
		Log.d(TAG_LOG, "Get row by id " + id);
		
		TrainAlarm alarm = null;
		
		Cursor cursor = database.query(
				MySQLiteHelper.TABLE_NAME, 
				allColumns,
				MySQLiteHelper.COLUMN_ID + " = " + id, 
				null, 
				null, 
				null, 
				null);

		cursor.moveToFirst();
		alarm = cursorToTrainAlarm(cursor);
		cursor.close();
		return alarm;
		
	}

	public void deleteById(long id) {
		Log.d(TAG_LOG, "Delete row by id " + id);
		
		database.delete(MySQLiteHelper.TABLE_NAME, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
		
	}
}