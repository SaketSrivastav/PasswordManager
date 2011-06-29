/**
 * 
 */
package com.passwordmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

/**
 * @author saket
 *
 */
public class DbHelper extends SQLiteOpenHelper {
	
	private static final String TAG = "DBHelper";
	private static final String DATABASE_NAME = "passwordmanager.db";
	private static final int DATABASE_VERSION = 1;
    public static final String MASTER_TABLE_NAME = "PM_MASTER_PASSWORD";
	private static final String PM_MASTER_TABLE_CREATE =
	                "CREATE TABLE PM_MASTER_PASSWORD(" +
	                				"password TEXT PRIMARY KEY," +
	                				"status TEXT NOT NULL);";
	public static final String PM_STORED_TABLE_NAME = "PM_STORED_PASSWORD";
	private static final String PM_STORED_PASSWORD_TABLE_CREATE = 
		"CREATE TABLE PM_STORED_PASSWORD(" +
        				"passkey TEXT PRIMARY KEY,"+
        				"password TEXT NOT NULL," +
        				"username TEXT NOT NULL);";
	
	//private static final String SAKET_DB_ADMIN = "INSERT INTO "+SAKET_TABLE_NAME+"values(1, admin, password, admin@gmail.com);";
	
	
	public DbHelper(Context context) {
		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.v(TAG, "In DBHelper constructor");
		
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.beginTransaction();
		try{
			//Create Master Table  
			Log.v(TAG,"Before db execution");
			db.execSQL(PM_MASTER_TABLE_CREATE);
			Log.v(TAG,"Master table created");
			db.execSQL("INSERT INTO "+MASTER_TABLE_NAME+" VALUES('master','Y');");
					
			//Create Stored Password Table
			db.execSQL(PM_STORED_PASSWORD_TABLE_CREATE);
			db.execSQL("INSERT INTO "+PM_STORED_TABLE_NAME+" VALUES('Root','master','master');");
			db.setTransactionSuccessful();
			Log.v(TAG, "In DBHelper OnCreate");
		}catch(Exception e){
			System.out.println("In DbHelper catch block onCreate");
			e.printStackTrace();
		}finally{
			db.endTransaction();
			db.close();
		}
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//db.execSQL("DROP TABLE PM_MASTER_PASSWORD;");
		//this.onCreate(db);
	}

}
