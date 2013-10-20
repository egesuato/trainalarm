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
	private SQLiteHelper dbHelper;
	private String[] allColumns = { 
			SQLiteHelper.COLUMN_ID,
			SQLiteHelper.COLUMN_TRAIN_NUMBER,
			SQLiteHelper.COLUMN_TRAIN_DESCRIPTION,
			SQLiteHelper.COLUMN_START_ALARM_AT,
			
	};

	public TrainAlarmDataSource(Context context) {
		dbHelper = new SQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	
	/**
	 * Creates or updates a trainAlarm
	 * @param trainAlarm
	 * @return
	 */
	public TrainAlarm createOrUpdateAlarm(TrainAlarm trainAlarm) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_TRAIN_NUMBER,
				trainAlarm.getTrainNumber());
		values.put(SQLiteHelper.COLUMN_TRAIN_DESCRIPTION,
				trainAlarm.getDescription());
		values.put(SQLiteHelper.COLUMN_START_ALARM_AT,
				trainAlarm.getStartTime());
		
		long newOrUpdatedId = trainAlarm.getId();
		
		if (trainAlarm.getId() == -1){
			newOrUpdatedId = database
				.insert(SQLiteHelper.TABLE_NAME, null, values);
			
		} else {
						
			database.update(SQLiteHelper.TABLE_NAME, values,  SQLiteHelper.COLUMN_ID + " = " +trainAlarm.getId(), null);
		}
		Cursor cursor = database.query(SQLiteHelper.TABLE_NAME, allColumns,
				SQLiteHelper.COLUMN_ID + " = " + newOrUpdatedId, null, null, null,
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
				SQLiteHelper.TABLE_NAME, 
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
		long nowInMilliseconds = cal.getTimeInMillis();
		
		cal.add(Calendar.MINUTE, 10);
		long endTimeWindow = cal.getTimeInMillis();
		
		StringBuffer where = new StringBuffer();
		addWhere(where, SQLiteHelper.COLUMN_START_ALARM_AT, ">=", nowInMilliseconds, true);
		addWhere(where, SQLiteHelper.COLUMN_START_ALARM_AT, "<=", endTimeWindow, false);

		Cursor cursor = database.query(
				SQLiteHelper.TABLE_NAME, 
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
			String operator, long value, boolean addAnd) {

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
		trainAlarm.setStartTime(cursor.getLong(3));

		return trainAlarm;
	}
	
	/**
	 * Returns a {@link TrainAlarm} with the given id, null if not found. 
	 * 
	 * @param id to look for.
	 * @return a {@link TrainAlarm} given a specified id or null
	 * if no alarm found.
	 */
	public TrainAlarm getAlarmById(long id) {
		Log.d(TAG_LOG, "Get row by id " + id);
		
		TrainAlarm alarm = null;
		
		Cursor cursor = database.query(
				SQLiteHelper.TABLE_NAME, 
				allColumns,
				SQLiteHelper.COLUMN_ID + " = " + id, 
				null, 
				null, 
				null, 
				null);

		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			alarm = cursorToTrainAlarm(cursor);
		} else{
			Log.e(TAG_LOG, "Row by id " + id + " not found");
		}
		cursor.close();
		return alarm;
		
	}

	public void deleteById(long id) {
		Log.d(TAG_LOG, "Delete row by id " + id);
		
		database.delete(SQLiteHelper.TABLE_NAME, SQLiteHelper.COLUMN_ID
				+ " = " + id, null);
		
	}
}