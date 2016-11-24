package utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.pinglocation.MainActivity;

import helper.JSONParser;
import helper.SQLiteHandler;
import helper.SessionManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import app.AppConfig;

public class InsertAccountTask extends AsyncTask<String, String, String>{
		Context context;
		String name;
		String email;
		String phone;
		String uid;
		String created_at;
		
		private ProgressDialog pDialog;
		
		private SQLiteHandler db;
		
		JSONParser jsonParser = new JSONParser();
		
		public InsertAccountTask(Context context){
			this.context = context;
			db = new SQLiteHandler(context);
			
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Fetching users");
			pDialog.setCancelable(false);
			
		}
		protected void onPreExecute() {
			super.onPreExecute();
			if (!pDialog.isShowing())
				pDialog.show();
				db.deleteAccounts();
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			List<NameValuePair> params2 = new ArrayList<NameValuePair>();
			params2.add(new BasicNameValuePair("type","ADMIN"));
			
			JSONArray json = jsonParser.makeHttpRequestArray(AppConfig.URL_ALLACCOUNT, "POST",params2);
			
			try{
				
				for(int i = 0; i < json.length();i++){
					JSONObject users = json.getJSONObject(i);
					for(int j = 0; j < users.length();j++){
						JSONObject user = users.getJSONObject("user");
						String name = user.getString("name");
						String uid = user.getString("uid");
						String email = user.getString("email");
						String phone = user.getString("phone");
						String created_at = user.getString("created_at");
						
						db.addAccount(name, email, uid, created_at, phone);
					}
				}
			
				
			}catch(JSONException e){
				e.printStackTrace();
				Log.e("JSON ERROR", e.toString());
			}

			return null;
		}
		
		/*
		 * After completing background task Dismiss the progress dialog *
		 */
		protected void onPostExecute(String result) {
			// dismiss the dialog once done

				if (pDialog.isShowing())
					pDialog.dismiss();
			
			
		
			
		}
		
		
	}
	

