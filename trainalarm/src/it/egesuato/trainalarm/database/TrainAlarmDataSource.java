package it.egesuato.trainalarm.database;

import it.egesuato.trainalarm.model.TrainAlarm;

import java.util.ArrayList;
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
			MySQLiteHelper.COLUMN_TRAIN_START_ALARM_AT 
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
		values.put(MySQLiteHelper.COLUMN_TRAIN_START_ALARM_AT,
				trainAlarm.getStartAlarmAt());
		
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

	private TrainAlarm cursorToTrainAlarm(Cursor cursor) {
		TrainAlarm trainAlarm = new TrainAlarm();
		trainAlarm.setId(cursor.getLong(0));
		trainAlarm.setTrainNumber(cursor.getInt(1));
		trainAlarm.setDescription(cursor.getString(2));
		trainAlarm.setStartAlarmAt(cursor.getString(3));

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