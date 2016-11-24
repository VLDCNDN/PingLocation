package com.example.pinglocation;

import helper.GPSTrack;
import helper.JSONParser;
import helper.SQLiteHandler;
import helper.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import utils.InsertAccountTask;
import utils.MyPreference;


import activity.LoginActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import app.AppConfig;

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
	
	private boolean isFirstTime;
	
	GPSTrack gps;
	
	JSONParser jsonParser = new JSONParser();
	
	String uid;
	
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
			
			new UpdateLocationTask(this,uid,latitude,longitude).execute();
		}else{
			Toast.makeText(this, "Enable your GPS", Toast.LENGTH_SHORT).show();
		}
		
		HashMap<String, String> hash = db.getUserDetails();
		
		accountType = hash.get("type");
		textName.setText(textName.getText().toString() +" "+hash.get("name")+"!");
		txtEmail.setText(txtEmail.getText().toString() + " " +hash.get("email"));
		txtPhone.setText(txtPhone.getText().toString() + " " +hash.get("phone"));
		
		uid = hash.get("uid");
		
		if(accountType.equals("ADMIN")){
			btnViewAll.setVisibility(View.VISIBLE);
			// will enter all accounts
			isFirstTime = MyPreference.isFirst(this);
			if(isFirstTime){
				new InsertAccountTask(this).execute();
			}
			
			
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
				MyPreference.resetSharedPreff(this);
			}
			new UpdateLocationTask(this,uid,0.0,0.0).execute();
			
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
				new UpdateLocationTask(this,uid,latitude,longitude).execute();
				
				Toast.makeText(this, "Location set", Toast.LENGTH_SHORT).show();
			}else{
				gps.showAlert();
			}
		 
		 
	 }
	 
	 class UpdateLocationTask extends AsyncTask<String, String, String>{
		 
		 String uid;
		 double latitude;
		 double longitude;
		 Context context;
		 
		 UpdateLocationTask(Context context, String uid, double latitude, double longitude){
			 this.context = context;
			 this.latitude = latitude;
			 this.longitude = longitude;
			 this.uid = uid;
		 }
		 
		 @Override
			protected void onPreExecute() {
				super.onPreExecute();
			//	showDialog();
			}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			List<NameValuePair> params2 = new ArrayList<NameValuePair>();
			params2.add(new BasicNameValuePair("uid",uid));
			params2.add(new BasicNameValuePair("longitude",Double.toString(longitude)));
			params2.add(new BasicNameValuePair("latitude", Double.toString(latitude)));
			
			JSONObject json = jsonParser.makeHttpRequest(AppConfig.URL_UPDATE_LOCATION, "POST", params2);
			
			try{
				boolean error = json.getBoolean("error");
				
				if(!error){
					Log.e("TESTING","location updated");
					return "success";
				}else{
					return "error";
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			return null;
		}
		
		/*
		 * After completing background task Dismiss the progress dialog *
		 */
		protected void onPostExecute(String result) {
			// dismiss the dialog once done
			if(result.equals("success")){
				//hideDialog();	
			}
			
		}
		 
	 }
	 
	 
}


