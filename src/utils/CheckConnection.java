package utils;


import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckConnection {
	public static boolean isOnline(Context context) { 
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);    
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();    
	    return netInfo != null && netInfo.isConnectedOrConnecting();
	}
}
