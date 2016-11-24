package com.example.pinglocation;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class UserLocationActivity extends Activity{
	
	private TextView tvName;
	private TextView tvEmail;
	private TextView tvPhone;
	
	private TextView tvLong;
	private TextView tvLat;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_location);
	
		tvName = (TextView) findViewById(R.id.user_name);
		tvEmail = (TextView) findViewById(R.id.user_email);
		tvPhone = (TextView) findViewById(R.id.user_phone);
		tvLong = (TextView) findViewById(R.id.user_long);
		tvLat = (TextView) findViewById(R.id.user_lat);
		
	}
	

}
