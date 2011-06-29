package com.passwordmanager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InsertData extends Activity{
	
	EditText mPassKey, mUsername, mPassword;
	Button mInsert, mBack;
	Context context;
	DbHelper mydb;
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Put your code here
		setContentView(R.layout.insert);
		context = getApplicationContext();
		
		mPassKey = (EditText)findViewById(R.id.insert_key);
		mUsername = (EditText)findViewById(R.id.insert_username);
		mPassword = (EditText)findViewById(R.id.insert_password);
		
		mInsert = (Button)findViewById(R.id.insert_btn);
		System.out.println(this);
		mInsert.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean uniquePassKey = checkUniquePassKey(mPassKey.getText().toString());
				if(uniquePassKey){
					
					String passKey = mPassKey.getText().toString();
					String username = mUsername.getText().toString();
					String password = mPassword.getText().toString();
					insertData(passKey, username, password);
					onBackPressed();
				}
				else{
					Toast.makeText(context, "Duplicate Passkey Found", Toast.LENGTH_LONG).show();
				}	
			}
		});
		
		mBack = (Button)findViewById(R.id.insert_btn_Back);
		mBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				onBackPressed();
			}
		});
	}
	
	private boolean checkUniquePassKey(String passKey){
		mydb = new DbHelper(context);
		SQLiteDatabase db = mydb.getReadableDatabase();
		String table = DbHelper.PM_STORED_TABLE_NAME;
		String[] columns = {"passkey"};
		String selection = "passKey=?";
		String[] selectionArgs = {passKey};
		
		boolean STATUS = false;
		db.beginTransaction();
		try{
			Cursor c = db.query(table, columns, selection, selectionArgs, null, null, null);
			startManagingCursor(c);
			if(c.moveToFirst()){
				STATUS = false;
			}else{
				STATUS = true;
			}
		}catch(Exception e){
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}finally{
			db.endTransaction();
			db.close();
		}
		return STATUS;
		
	}
	
	private boolean insertData(String passKey, String username, String password){
		
		boolean status = false;
		mydb = new DbHelper(context);
		ContentValues values = new ContentValues();
		values.put("passKey", passKey);
		values.put("username", username);
		values.put("password", password);
		
		SQLiteDatabase db = mydb.getWritableDatabase();
		
		String table = DbHelper.PM_STORED_TABLE_NAME;
		try{
			db.beginTransaction();
			db.insert(table, null, values);
			db.setTransactionSuccessful();
			Toast.makeText(context, "Password Stored Successfully", Toast.LENGTH_LONG).show();
			status = true;
			
		}catch(Exception e){
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}finally{
			db.endTransaction();
			db.close();
		}
		return status;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent i = new Intent(InsertData.this, FirstScreen.class);
		startActivity(i);
		finish();
	}
}
