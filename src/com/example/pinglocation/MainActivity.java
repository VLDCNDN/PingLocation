package com.example.pinglocation;

import helper.GPSTrack;
import helper.SQLiteHandler;
import helper.SessionManager;

import java.util.HashMap;

import utils.InsertAccountTask;


import activity.LoginActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private TextView textName;
	private TextView txtEmail;
	private TextView txtPhone;
	private TextView txtLong;
	private TextView txtLat;
	private String accountType;
	
	private SQLiteHandler db;
	
	private Button btnLogout;
	private Button btnViewAll;
	
	private SessionManager session;
	
	GPSTrack gps;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		textName = (TextView) findViewById(R.id.main_name);
		txtEmail = (TextView) findViewById(R.id.main_email);
		txtPhone = (TextView) findViewById(R.id.main_phone);
		txtLat = (TextView) findViewById(R.id.main_lat);
		txtLong = (TextView) findViewById(R.id.main_long);
		
		btnLogout = (Button) findViewById(R.id.btnLogout);
		btnViewAll = (Button) findViewById(R.id.btnViewAllUsers);
		
		db = new SQLiteHandler(getApplicationContext());
		gps = new GPSTrack(this);
		
		session = new SessionManager(getApplicationContext());
		if(!session.isLoggedIn()){
			logoutUser();
		}
		
		if(gps.canGetLocation()){
			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			
			txtLat.setText(""+latitude);
			txtLong.setText(""+longitude);
			
			
		}else{
			Toast.makeText(this, "Enable your network", Toast.LENGTH_SHORT).show();
		}
		
		HashMap<String, String> hash = db.getUserDetails();
		
		accountType = hash.get("type");
		textName.setText(textName.getText().toString() +" "+hash.get("name")+"!");
		txtEmail.setText(txtEmail.getText().toString() + " " +hash.get("email"));
		txtPhone.setText(txtPhone.getText().toString() + " " +hash.get("phone"));
		
		if(accountType.equals("ADMIN")){
			btnViewAll.setVisibility(View.VISIBLE);
			new InsertAccountTask(this).execute();
			
		}else{
			btnViewAll.setVisibility(View.GONE);
		}
		
		btnLogout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				logoutUser();
				
			}
		});
		
		btnViewAll.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, UsersActivity.class);
				startActivity(intent);
			}
		});
		
		
		
	}

	
	 private void logoutUser() {
	        session.setLogin(false);
	 
	        db.deleteUsers();
			if(accountType.equals("ADMIN")){
				db.deleteAccounts();
			}
	        // Launching the login activity
	        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
	        startActivity(intent);
	        finish();
	    }
	 
	 public void ocRefresh(View view){
		 gps = new GPSTrack(this);
		 if(gps.canGetLocation()){
				double latitude = gps.getLatitude();
				double longitude = gps.getLongitude();
				
				txtLat.setText(""+latitude);
				txtLong.setText(""+longitude);
				
				Toast.makeText(this, "Location set", Toast.LENGTH_SHORT).show();
			}else{
				gps.showAlert();
			}
		 
		 
	 }
	 
	 
}


