package it.egesuato.trainalarm.database;

import it.egesuato.trainalarm.model.TrainAlarm;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TrainAlarmDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_TRAIN_NUMBER,
			MySQLiteHelper.COLUMN_TRAIN_DESCRIPTION,
			MySQLiteHelper.COLUMN_TRAIN_START_ALARM_AT };

	public TrainAlarmDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public TrainAlarm createAlarm(TrainAlarm trainAlarm) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_TRAIN_NUMBER,
				trainAlarm.getTrainNumber());
		values.put(MySQLiteHelper.COLUMN_TRAIN_DESCRIPTION,
				trainAlarm.getDescription());
		values.put(MySQLiteHelper.COLUMN_TRAIN_START_ALARM_AT,
				trainAlarm.getStartAlarmAt());

		long insertId = database
				.insert(MySQLiteHelper.TABLE_NAME, null, values);

		Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME, allColumns,
				MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();
		TrainAlarm newTrainAlarm = cursorToTrainAlarm(cursor);
		cursor.close();
		return newTrainAlarm;
	}

	public void deleteComment(TrainAlarm trainAlarm) {
		long id = trainAlarm.getId();
		System.out.println("Train alarm deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_NAME, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<TrainAlarm> getAllAlarms() {
		List<TrainAlarm> alarms = new ArrayList<TrainAlarm>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME, allColumns,
				null, null, null, null, null);

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
}