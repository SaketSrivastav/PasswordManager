package com.passwordmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FirstScreen extends Activity implements OnClickListener{
    
	private static final String TAG = "PasswordManager";
	Button mSetMasterPassword;
	Button mInsert;
	Button mUpdate;
	Button mDelete;
	Button mDisplayAll;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.v(TAG,getClass().getSimpleName());
        
        mSetMasterPassword = (Button)findViewById(R.id.btn_set_master_pass);
        mSetMasterPassword.setOnClickListener(this);
        
        mInsert = (Button)findViewById(R.id.btn_insert);
        mInsert.setOnClickListener(this);
        
        mUpdate = (Button)findViewById(R.id.btn_update);
        mUpdate.setOnClickListener(this);
        
        mDelete = (Button)findViewById(R.id.btn_delete);
        mDelete.setOnClickListener(this);
        
        mDisplayAll = (Button)findViewById(R.id.btn_fetchAll);
        mDisplayAll.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_set_master_pass:
			Intent changeMasterPassword = new Intent(FirstScreen.this, ChangeMasterPassword.class);
			startActivity(changeMasterPassword);
			break;
		case R.id.btn_insert:
			Intent insertData = new Intent(FirstScreen.this, Controller.class);
			insertData.putExtra("data", "insert");
			startActivity(insertData);
			break;
		case R.id.btn_update:
			Intent updateData = new Intent(FirstScreen.this, Controller.class);
			updateData.putExtra("data", "update");
			startActivity(updateData);
			break;
		case R.id.btn_delete:
			Intent deleteData = new Intent(FirstScreen.this, Controller.class);
			deleteData.putExtra("data", "delete");
			startActivity(deleteData);
			break;
		case R.id.btn_fetchAll:
			Intent display = new Intent(FirstScreen.this, Controller.class);
			display.putExtra("data", "display");
			startActivity(display);
			break;
		}
		finish();
	}
}