package helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
	// Logcat tag
	private static String TAG = SessionManager.class.getSimpleName();
	
	SharedPreferences pref;
	
	Editor editor;
	Context _context;
	
	// Shared pref mode
	int PRIVATE_MODE = 0;
	
	// Shared pref file name
	private static final String PREF_NAME = "pingLocation";
	
	private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
	
	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	public void setLogin(boolean isLoggedIn){
		editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
		editor.commit();
		
		Log.d(TAG, "User login session modified!");
	}
	
	public boolean isLoggedIn(){
		return pref.getBoolean(KEY_IS_LOGGEDIN, false);
	}

}
