package helper;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHandler extends SQLiteOpenHelper{
	
	private static String TAG = SQLiteHandler.class.getSimpleName();
	
	private static final int DATABASE_VERSION = 6;
	
	private static final String DATABASE_NAME = "ping_schemas";
	
	private static final String TABLE_USER = "user";
	private static final String TABLE_ACCOUNT = "account";
	
	// Login Columns
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_UID = "uid";
	public static final String KEY_CREATED_AT = "created_at";
	public static final String KEY_PHONE = "phone";
	public static final String KEY_TYPE = "type";
	public static final String KEY_LONGITUDE = "longitude";
	public static final String KEY_LATITUDE = "latitude";
	
	
	public SQLiteHandler(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public void onCreate(SQLiteDatabase db){
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
				+ KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, "
				+ KEY_EMAIL + " TEXT UNIQUE, " + KEY_UID + " TEXT,"
				+ KEY_CREATED_AT + " TEXT," + KEY_PHONE + " TEXT," 
				+ KEY_TYPE +" TEXT" +")";
		
		String CREATE_ACCOUNT_TABLE = "CREATE TABLE " + TABLE_ACCOUNT + "("
				+ KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, "
				+ KEY_EMAIL + " TEXT, " + KEY_UID + " TEXT, " 
				+ KEY_CREATED_AT + " TEXT," + KEY_PHONE + " TEXT," 
				+ KEY_LONGITUDE + " TEXT, " + KEY_LATITUDE + " TEXT" + ")";
		
		db.execSQL(CREATE_LOGIN_TABLE);
		db.execSQL(CREATE_ACCOUNT_TABLE);
		
		Log.d(TAG, "Database tables created");
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
		
		onCreate(db);
	}
	
	public void addUser(String name, String email, String uid, String created_at,String phone, String type){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name);
		values.put(KEY_EMAIL, email);
		values.put(KEY_UID, uid);
		values.put(KEY_CREATED_AT, created_at);
		values.put(KEY_PHONE, phone);
		values.put(KEY_TYPE, type);
		
		long id = db.insert(TABLE_USER, null, values);
		db.close();
		Log.d(TAG, "New User inserted into sqlite :: " + id);
	}
	
	public void addAccount(String name, String email, String uid, String created_at, String phone){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name);
		values.put(KEY_EMAIL, email);
		values.put(KEY_UID, uid);
		values.put(KEY_CREATED_AT, created_at);
		values.put(KEY_PHONE, phone);
		
		long id = db.insert(TABLE_ACCOUNT, null, values);
		db.close();
	}
	
	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT * FROM " + TABLE_USER;
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		cursor.moveToFirst();
		if(cursor.getCount() > 0){
			user.put("name", cursor.getString(1));
			user.put("email", cursor.getString(2));
			user.put("uid", cursor.getString(3));
			user.put("created_at", cursor.getString(4));
			user.put("phone", cursor.getString(5));
			user.put("type", cursor.getString(6));
		}
		cursor.close();
		db.close();
		
		return user;
		
	}
	
	public Cursor getAllUser(){
		String where = null;
		SQLiteDatabase db = this.getReadableDatabase();
		
		String query = "SELECT * FROM " + TABLE_ACCOUNT;
		Cursor c = db.rawQuery(query,null);
		
		if( c != null){
			c.moveToFirst();
		}
		
		return c;
	}
	
	public Cursor getSelectedAccountInfo(long rowId){
		String query = "SELECT * FROM " + TABLE_ACCOUNT + " WHERE " 
				+ KEY_ID + " = " + rowId;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		
		if(c != null){
			c.moveToFirst();
		}
		
		return c;
		
	}
	
	public void updateAccount(String uid, String longitude, String latitude){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(KEY_LONGITUDE, longitude);
		cv.put(KEY_LATITUDE, latitude);
		
		db.update(TABLE_ACCOUNT, cv, KEY_UID + " = \"" + uid + "\"", null);
		db.close();
	}
	
	public double[] getUserLocation(String uid){
		double[] location = {};
		
		SQLiteDatabase db= this.getWritableDatabase();
		String query = "SELECT * FROM " + TABLE_ACCOUNT + " WHERE "
				+ KEY_UID + " = \""+ uid+"\"";
		
		Cursor c= db.rawQuery(query, null);
		if(c!=null){
			c.moveToFirst();
				location[0] = Double.parseDouble(c.getString(c.getColumnIndex(KEY_LONGITUDE)));
				location[1] = Double.parseDouble(c.getString(c.getColumnIndex(KEY_LATITUDE)));
			
		}
		
		c.close();
		db.close();
		
		return location;
	}
	
	
	public void deleteUsers(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USER, null, null);
		db.close();
	}
	
	public void deleteAccounts(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ACCOUNT, null, null);
		db.close();
	}

}
