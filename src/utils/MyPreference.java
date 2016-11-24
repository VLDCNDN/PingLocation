package utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreference {
	private static final String MY_PREFF = "my_preference";
	
	
	public static boolean isFirst(Context context){
		final SharedPreferences reader = context.getSharedPreferences(MY_PREFF, Context.MODE_PRIVATE);
		final boolean first = reader.getBoolean("is_first", true);
		
		if(first){
			final SharedPreferences.Editor editor = reader.edit();
			editor.putBoolean("is_first", false);
			editor.commit();
		}
		return first;
	}
	
	public static void resetSharedPreff(Context context){
		final SharedPreferences reader = context.getSharedPreferences(MY_PREFF, Context.MODE_PRIVATE);
		reader.edit().remove("is_first").commit();
	}
}
