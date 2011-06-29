package com.passwordmanager;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DisplayAll extends Activity {
	
	DbHelper mydb;
	Context context;
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_all);
		context = getApplicationContext();
		
		SQLiteDatabase db = null;
		TableLayout mRootTable = (TableLayout)findViewById(R.id.display_rootLayout);
		try{
			mydb = new DbHelper(context);
			db = mydb.getReadableDatabase();
			db.beginTransaction();
			String table = DbHelper.PM_STORED_TABLE_NAME;
			String[] columns = {"passkey","username","password"};
			
			Cursor cursor = db.query(table, columns, null, null, null, null, null);
			startManagingCursor(cursor);
			if(cursor.moveToFirst()){
				int rowCount = cursor.getCount();
				LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				for (int i = 0; i < rowCount; i++) {
					TableRow row = new TableRow(context);
					TextView[] tv = new TextView[3];
					for (int j = 0; j < tv.length; j++) {
						tv[j] = new TextView(context);
						tv[j].setLayoutParams(params);
						tv[j].setText(cursor.getString(j));
						System.out.println("Row text: "+tv[j].getText());
						row.addView(tv[j]);
					}
					cursor.moveToNext();
					mRootTable.addView(row);
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			db.endTransaction();
			db.close();
		}
		
	}
}
