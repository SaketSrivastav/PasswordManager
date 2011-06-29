package com.passwordmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeMasterPassword extends Activity {

	private static final String TAG = "PasswordManager";
	EditText mEditCurrPass,mEditNewMasterPass;
	Button mVerifyCurrPass, mBack;
	String mStringEditCurrPass;
	Context context;
	DbHelper mydb;
	SQLiteDatabase db = null;
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG,"In SetMasterPassword.onCreate");
		context = getApplicationContext();
		setContentView(R.layout.change_master);

		mEditCurrPass = (EditText)findViewById(R.id.current_master_password);
		mEditNewMasterPass = (EditText)findViewById(R.id.new_master_password);
		
		mBack = (Button)findViewById(R.id.controller_back);
		mBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		
		mVerifyCurrPass = (Button)findViewById(R.id.change_master_password);
		mVerifyCurrPass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean verified;
				try {
					verified = verifyMasterPassword(mEditCurrPass.getText().toString(), context);
					if(verified){
						System.out.println("Verified");
						changeMasterPassword(mEditCurrPass.getText().toString(), mEditNewMasterPass.getText().toString());
					}else{
						Toast.makeText(getApplicationContext(), "Master Password Verification Failed", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public boolean changeMasterPassword(String currentMasterPass, String newMasterPass) {
		boolean STATUS = false;
		mydb = new DbHelper(context);
		SQLiteDatabase db = mydb.getWritableDatabase();
		String updateQuery = "UPDATE PM_MASTER_PASSWORD SET status = 'N' WHERE password = '"+currentMasterPass+"';";
		String insertQuery = "INSERT INTO PM_MASTER_PASSWORD VALUES("+"'"+newMasterPass+"','Y');";
		
		db.beginTransaction();
		try{	
			db.execSQL(updateQuery);
			db.execSQL(insertQuery);
			db.setTransactionSuccessful();
			STATUS = true;
			Toast.makeText(getApplicationContext(), "Master Password Successfully Updated", Toast.LENGTH_SHORT).show();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			db.endTransaction();
			db.close();
		}
		return STATUS;
	}

	public boolean verifyMasterPassword(String currentMasterPassword, Context context) throws Exception{

		String passStatus = null;
		boolean STATUS = false;
		Log.v(TAG,"In verifyMasterPassword");
		try{
			mydb = new DbHelper(context);
			db = mydb.getReadableDatabase();
			
			Log.v(TAG,"Before cursor verifyMasterPassword");
			System.out.println("MYDB: "+mydb);
			
			//SELECT
			String[] columns = {"password","status"};

			//WHERE clause
			String selection = "password=?";

			//WHERE clause arguments
			String[] selectionArgs = {currentMasterPassword};

			Cursor cursor = db.query(DbHelper.MASTER_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
			startManagingCursor(cursor);
	
			Log.v(TAG,"After cursor verifyMasterPassword");
			if(cursor.moveToFirst()){
				System.out.println("In ChangeMasterPassword");
				int numberOfRows = cursor.getCount();
				System.out.println("Number of rows :"+numberOfRows);
				System.out.println("Cursor.getString(1): "+cursor.getString(1));
				passStatus = cursor.getString(1);
				
				if(numberOfRows == 0){
					Toast.makeText(getApplicationContext(), "Error: Password Not Found", Toast.LENGTH_SHORT).show();
					STATUS = false;
				}else if(numberOfRows >= 1){
					if(passStatus.equals("Y")){
						Log.v(TAG, "Password Found");
						STATUS =  true;
					}else{
						Toast.makeText(getApplicationContext(),"Password Already Used", Toast.LENGTH_SHORT).show();
						STATUS =  false;
					}
				}
				else{
					STATUS =  false;
				}
			}
			else{
				Toast.makeText(context, "No Data Found", Toast.LENGTH_LONG).show();
				STATUS = false;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			//Utility.alertMessage(e.getMessage(), context);
		}finally{
			if(db != null){
				db.close();
			}
			System.out.println("Outside ChangeMasterPassword");
			finish();
			return STATUS;
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent onBack = new Intent(ChangeMasterPassword.this, FirstScreen.class);
		startActivity(onBack);
		finish();
	}
}
