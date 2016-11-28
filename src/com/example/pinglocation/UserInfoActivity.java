package com.example.pinglocation;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import helper.JSONParser;
import helper.SQLiteHandler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import app.AppConfig;

public class UserInfoActivity extends Activity{
	private TextView txtName;
	private TextView txtPhone;
	private TextView txtEmail;
	private TextView txtLongitude;
	private TextView txtLatitude;
	
	private String uid;
	private String name;
	private String phone;
	private String email;
	private double longitude;
	private double latitude;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_location);
		
		txtName = (TextView) findViewById(R.id.user_name);
		txtPhone = (TextView) findViewById(R.id.user_phone);
		txtEmail = (TextView) findViewById(R.id.user_email);
		txtLongitude = (TextView) findViewById(R.id.user_long);
		txtLatitude = (TextView) findViewById(R.id.user_lat);
		
		Bundle extras = getIntent().getExtras();
		
		if(extras != null){
			uid = extras.getString(SQLiteHandler.KEY_UID);
			phone = extras.getString(SQLiteHandler.KEY_PHONE);
			name = extras.getString(SQLiteHandler.KEY_NAME);
			email = extras.getString(SQLiteHandler.KEY_EMAIL);
			
			txtName.setText(name);
			txtPhone.setText(phone);
			txtEmail.setText(email);
			
			new LoadUserLocation(this).execute();
			
			
		}
		
	}
	
	public void ocRefreshLocate(View view){
		new LoadUserLocation(this).execute();
	}
	
	class LoadUserLocation extends AsyncTask<String, String, String>{
		
		ProgressDialog pDialog;
		Context context;

		JSONParser jsonParser = new JSONParser();
		double[] location;
		SQLiteHandler db;
		
		LoadUserLocation(Context context){
			this.context = context;
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Getting " + name + " Location");
			
			db = new SQLiteHandler(context);
			
			
		}
		
		protected void onPreExecute() {
			super.onPreExecute();
			if (!pDialog.isShowing())
				pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> params2 = new ArrayList<NameValuePair>();
			params2.add(new BasicNameValuePair("uid",uid));
			
			JSONObject json = jsonParser.makeHttpRequest(AppConfig.URL_GET_LOCATION, "POST", params2);
			
			try{
				boolean error = json.getBoolean("error");
				if(!error){
					longitude = json.getDouble("longitude");
					latitude = json.getDouble("latitude");
					
					//location = db.getUserLocation(uid);
					
					
					return "success";
				}else{
					return "failed";
				}
			}catch(JSONException e){
				e.printStackTrace();
				return "failed";
			}
		}
		
		/*
		 * After completing background task Dismiss the progress dialog *
		 */
		protected void onPostExecute(String result) {
			// dismiss the dialog once done
			if(result.equals("success")){
				if(longitude <= 0 && latitude <= 0){
					Toast.makeText(context, "can't locate " + name, Toast.LENGTH_SHORT).show();
					
				}else{
					db.updateAccount(uid, String.valueOf(longitude), String.valueOf(latitude));
					
					txtLongitude.setText(""+longitude);
					txtLatitude.setText(""+latitude);
					Toast.makeText(context, name + " located!", Toast.LENGTH_SHORT).show();
							
				}
				
			}else{
				Toast.makeText(context, "cant locate the user!", Toast.LENGTH_SHORT).show();
				
			}
			if (pDialog.isShowing()){
					pDialog.dismiss();
				}	
		}
		
		
	}
}
