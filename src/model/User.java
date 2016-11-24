package model;

import java.util.ArrayList;

public class User {

	private String name, phone,uid;
	private double longitude, latitude;
	
	public User(){
		
	}
	
	public User(String name, String phone, String uid, double longitude, double latitude){
		this.name = name;
		this.phone = phone;
		this.uid = uid;
		
		this.longitude = longitude;
		this.latitude = latitude;
		
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setNam(String name){
		this.name = name;
	}
	
	public String getPhone(){
		return this.phone;
	}
	
	public void setPhone(String phone){
		this.phone = phone;
	}
	
	public String getUID(){
		return this.uid;
	}
	
	public void setUID(String uid){
		this.uid = uid;
		
	}
	
	public double getLongitude(){
		return this.longitude;
	}
	
	public void setLongitude(double longitude){
		this.longitude = longitude;
	}
	
	public double getLatitude(){
		return this.latitude;
	}
	
	public void setLatitude(double latitude){
		this.latitude = latitude;
	}
	
	
}
