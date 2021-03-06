package it.egesuato.trainalarm.database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_NAME = "train_alarm";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_TRAIN_NUMBER = "number";
  public static final String COLUMN_TRAIN_DESCRIPTION = "description";
  public static final String COLUMN_START_ALARM_AT = "start_alarm_at";

  private static final String DATABASE_NAME = "train_alarm.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table " +
       TABLE_NAME + "(" + COLUMN_ID +
       " integer primary key autoincrement, " + 
      	COLUMN_TRAIN_NUMBER + " integer not null, " +
      	COLUMN_TRAIN_DESCRIPTION + " text, " +
      	COLUMN_START_ALARM_AT +" long); ";

  public SQLiteHelper(Context context) {
	  super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
	  database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(SQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    onCreate(db);
  }

} 