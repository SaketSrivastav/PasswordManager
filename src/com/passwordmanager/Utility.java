/**
 * 
 */
package com.passwordmanager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

/**
 * @author saksriva
 *
 */
public class Utility extends Activity{
	
	DbHelper mydb = null;
	SQLiteDatabase db = null;
	private final String table = DbHelper.PM_STORED_TABLE_NAME;
	
	public ArrayList<String> getDataKeys(Context context){
		
		ArrayList<String> update_keys = new ArrayList<String>();
		mydb = new DbHelper(context);
		db = mydb.getReadableDatabase();

		db.beginTransaction();
		try {
			String table = DbHelper.PM_STORED_TABLE_NAME;
			String[] columns = {"passkey"};
			Cursor c = db.query(table, columns, null, null, null, null, null);
			startManagingCursor(c);

			if(c.moveToFirst()){
				int rowCount = c.getCount();
				Log.v(getClass().getSimpleName(), "Database Not Empty");
				for(int i = 0;c.moveToNext() && i < rowCount ;i++){
					//delete_keys[i] = new String(); 
					update_keys.add(c.getString(0));
					Log.v("DELETE_KEYS",c.getString(0));
				}
			}else{
				Toast.makeText(context, "Not Keys Available", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}finally{
			db.endTransaction();
			db.close();
		}

		return update_keys;
	}

}
