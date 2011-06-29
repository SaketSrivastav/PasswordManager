package com.passwordmanager;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentValues;
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
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.Toast;

public class UpdateData extends Activity {

	Context context;

	AutoCompleteTextView mUpdateKey;
	Button mGetData ,mUpdate, mBack;
	TableRow row_username, row_password;

	String username, password;

	DbHelper mydb;
	SQLiteDatabase db = null;

	boolean GET_OLD_DATA = false; 

	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.update);
		context = getApplicationContext();

		mUpdateKey = (AutoCompleteTextView)findViewById(R.id.update_key);
		ArrayList<String> update_keys = getUpdateKeys();
		ArrayAdapter adapter = new ArrayAdapter(context,
				android.R.layout.simple_dropdown_item_1line, update_keys);
		mUpdateKey.setAdapter(adapter);

		mUpdate = (Button)findViewById(R.id.update_btn);
		row_username = (TableRow)findViewById(R.id.row_username);
		row_password = (TableRow)findViewById(R.id.row_password);

		mGetData = (Button)findViewById(R.id.update_get_data);
		mGetData.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try{
					HashMap<String, String> oldData = getOldData(mUpdateKey.getText().toString());
					username = oldData.get("username");
					password = oldData.get("password");
					Log.v("UpdateData","get Data over");
					GET_OLD_DATA = true;
					if(GET_OLD_DATA){

						Log.v("UpdateData","Inside if block");
						mUpdate.setVisibility(Button.VISIBLE);

						row_username.setVisibility(TableRow.VISIBLE);			
						row_password.setVisibility(TableRow.VISIBLE);

						EditText mUsername = (EditText)findViewById(R.id.update_username);
						mUsername.setText(username);

						EditText mPassword = (EditText)findViewById(R.id.update_password);
						mPassword.setText(password);

						mUpdate.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								try{

									boolean updated = updatePassword(mUpdateKey.getText().toString(), username, password);
									if(updated){
										Toast.makeText(context, "Update Successfully", Toast.LENGTH_LONG).show();
										onBackPressed();
									}else{
										Toast.makeText(context, "Update Failed", Toast.LENGTH_LONG).show();
									}

								}catch(Exception e){
									e.printStackTrace();
								}
							}
						});
					}else{
						Log.v("UpdateData","Else Block");
						GET_OLD_DATA = false;

						mUpdate.setVisibility(Button.INVISIBLE);

						row_username = (TableRow)findViewById(R.id.row_username);
						row_username.setVisibility(TableRow.INVISIBLE);

						row_password = (TableRow)findViewById(R.id.row_password);
						row_password.setVisibility(TableRow.INVISIBLE);
					}

				}catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});

		mBack = (Button)findViewById(R.id.update_btn_Back);
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});

	}

	protected HashMap<String, String> getOldData(String updateKey) {

		HashMap<String, String> oldData = new HashMap<String, String>();

		mydb = new DbHelper(context);
		db = mydb.getReadableDatabase();

		db.beginTransaction();
		try {
			String table = DbHelper.PM_STORED_TABLE_NAME;
			String[] columns = {"username","password"};
			String selection = "passkey=?";
			String [] selectionArgs = {updateKey};
			Cursor c = db.query(table, columns, selection, selectionArgs, null, null, null);
			startManagingCursor(c);

			if(c.moveToFirst()){
				oldData.put("username", c.getString(c.getColumnIndex("username")));
				oldData.put("password", c.getString(c.getColumnIndex("password")));

				Log.v("GET_OLD_USERNAME",c.getString(c.getColumnIndex("username")));
				Log.v("GET_OLD_PASSWORD",c.getString(c.getColumnIndex("password")));
			}else{
				Toast.makeText(context, "No such Key Available", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}finally{
			db.endTransaction();
			db.close();
			Log.v("UpdateData","closing DB");
		}

		return oldData;
	}

	private ArrayList<String> getUpdateKeys() {
		// TODO Auto-generated method stub
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
					update_keys.add(c.getString(0));
					Log.v("UPDATE_KEYS",c.getString(0));
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

	private boolean updatePassword(String updateKey, String username, String password) {

		boolean updated = false;

		Log.v("UpdateData","In update password");
		mydb = new DbHelper(context);
		db = mydb.getWritableDatabase();

		db.beginTransaction();
		try {
			String table = DbHelper.PM_STORED_TABLE_NAME;
			String whereClause = "passkey=?";
			String[] whereArgs = {updateKey};

			ContentValues values = new ContentValues();
			values.put("password", password);
			values.put("username", username);
			
			int rowsUpdated = db.update(table, values, whereClause, whereArgs);
			//Required otherwise update will rolledback
			System.out.println("Rows Updated: "+rowsUpdated);
			if(rowsUpdated == 1){
				updated = true;
				
				Log.v(getClass().getSimpleName(),"Update Successfull");
			}else{
				Log.v(getClass().getSimpleName(),"Update Failed");
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}finally{
			db.endTransaction();
			db.close();
		}

		return updated;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent back = new Intent(UpdateData.this, FirstScreen.class);
		startActivity(back);
		finish();
	}
}
