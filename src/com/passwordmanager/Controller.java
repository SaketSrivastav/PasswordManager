package com.passwordmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class Controller extends Activity {

	private static final String TAG = "PasswordManager";
	EditText mMasterPass;
	Button mEnter, mLogin;
	Intent intent;
	DbHelper mydb;
	Context context;
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Log.v(TAG,"in controller");
		setContentView(R.layout.login);
		context = getApplicationContext();

		mLogin = (Button)findViewById(R.id.btn_login);
		mLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setContentView(R.layout.controller);
				validate();
			}
		});
	}

	private void validate(){
		
		mEnter = (Button)findViewById(R.id.controller_enter);
		mEnter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
					mMasterPass = (EditText)findViewById(R.id.controller_master_password);
					String masterPassword = mMasterPass.getText().toString();
					System.out.println(masterPassword);
					boolean controllerVerifyMasterPassword = verifyMasterPasswordController(masterPassword);
					if(controllerVerifyMasterPassword){
						Log.v(TAG,"intents");
						Intent receivingIntent = getIntent();
						String intentValue = receivingIntent.getStringExtra("data");
						System.out.println("Intent Value: "+intentValue);

						if(intentValue.equals("insert")){
							Log.v(TAG,"To Insert");
							intent = new Intent(Controller.this, InsertData.class);
							startActivity(intent);
							finish();
						}else if(intentValue.equals("update")){
							Log.v(TAG,"To Update");
							intent = new Intent(Controller.this, UpdateData.class);
							startActivity(intent);
							finish();
						}else if(intentValue.equals("delete")){
							Log.v(TAG,"To Delete");
							intent = new Intent(Controller.this, DeleteData.class);
							startActivity(intent);
							finish();
						}else if(intentValue.equals("display")){
							Log.v(TAG,"To Display");
							intent = new Intent(Controller.this, DisplayAll.class);
							startActivity(intent);
							finish();
						}
					}
				}catch(Exception e){
					e.printStackTrace();
					//Utility.alertMessage(e.getMessage(), context);
				}finally{
					//finish();
					Log.v(TAG,"Outside controller");
				}
			}
		});
	}
	
	private boolean verifyMasterPasswordController(String masterPassword)throws Exception {
		boolean status = false;
		SQLiteDatabase db = null;
		try{
			mydb = new DbHelper(context);
			db = mydb.getReadableDatabase();
			db.beginTransaction();

			String table = DbHelper.MASTER_TABLE_NAME;
			String[] columns = {"password"};
			String selection = "password=? AND status='Y'";
			String[] selectionArgs = {masterPassword};
			Cursor cursor = db.query(table, columns, selection, selectionArgs, null, null, null);
			startManagingCursor(cursor);
			int rowCount = cursor.getCount();
			if(rowCount == 1){		
				status = true;
				Log.v(TAG,"Master Password Match");
			}else{
				Log.v(TAG,"Master Password Not Match");
				Toast.makeText(context, "Invalid Master Password", Toast.LENGTH_LONG).show();
			}
		}catch (Exception e) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(e.getMessage())
			.setCancelable(false)
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			AlertDialog alertDialog = builder.create();
			alertDialog.show();
		}
		finally{
			db.endTransaction();
			db.close();
		}
		return status;
	}
}
