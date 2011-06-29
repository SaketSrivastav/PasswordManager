package com.passwordmanager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

public class DeleteData extends Activity {

	Context context;
	AutoCompleteTextView mDeleteKey;
	Button mDelete, mBack;
	DbHelper mydb;
	SQLiteDatabase db = null;

	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.delete);
		context = getApplicationContext();

		mDeleteKey = (AutoCompleteTextView)findViewById(R.id.delete_key);
		ArrayList<String> delete_keys = getDeleteKeys();
		ArrayAdapter adapter = new ArrayAdapter(context,
				android.R.layout.simple_dropdown_item_1line, delete_keys);
		mDeleteKey.setAdapter(adapter);

		mDelete = (Button)findViewById(R.id.delete_btn);
		mDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try{
					boolean deleted = deletePassword(mDeleteKey.getText().toString());
					if(deleted){
						Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_LONG).show();
						onBackPressed();
					}else{
						Toast.makeText(context, "Deletion Failed", Toast.LENGTH_LONG).show();
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});

		mBack = (Button)findViewById(R.id.delete_btn_Back);
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	private ArrayList<String> getDeleteKeys() {
		// TODO Auto-generated method stub
		ArrayList<String> delete_keys = new ArrayList<String>();
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
					delete_keys.add(c.getString(0));
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

		return delete_keys;
	}

	private boolean deletePassword(String deleteKey) {

		boolean deleted = false;

		mydb = new DbHelper(context);
		db = mydb.getWritableDatabase();

		db.beginTransaction();
		try {
			String table = DbHelper.PM_STORED_TABLE_NAME;
			String whereClause = "passkey=?";
			String[] whereArgs = {deleteKey};
			int rowsDeleted = db.delete(table, whereClause, whereArgs);

			if(rowsDeleted == 1){
				deleted = true;
				Log.v(getClass().getSimpleName(),"Delete Successfull");
			}else{
				Log.v(getClass().getSimpleName(),"Delete Failed");
			}
		} catch (Exception e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}finally{
			db.endTransaction();
			db.close();
		}
		return deleted;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent i = new Intent(DeleteData.this, FirstScreen.class);
		startActivity(i);
		finish();
	}
}
