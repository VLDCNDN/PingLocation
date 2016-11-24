package activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import utils.CheckConnection;

import helper.JSONParser;
import helper.SQLiteHandler;
import helper.SessionManager;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pinglocation.MainActivity;
import com.example.pinglocation.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import app.AppConfig;
import app.AppController;

public class LoginActivity extends Activity {

	private static final String TAG = "";
	private EditText inputEmail;
	private EditText inputPassword;
	private ProgressDialog pDialog;
	private SessionManager session;
	private SQLiteHandler db;
	
	public static final String USER_ADMIN = "ADMIN";
	public static final String USER_REGULAR = "REGULAR";

	JSONParser jsonParser = new JSONParser();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		inputEmail = (EditText) findViewById(R.id.email);
		inputPassword = (EditText) findViewById(R.id.password);

		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Sign in...");
		pDialog.setCancelable(false);
		// SQLite
		db = new SQLiteHandler(getApplicationContext());
		// Session manager
		session = new SessionManager(getApplicationContext());

		if (session.isLoggedIn()) {
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}

	}

	public void ocLoginListener(View view) {
		switch (view.getId()) {
		case R.id.btnLogin:
			String email = inputEmail.getText().toString().trim();
			String password = inputPassword.getText().toString().trim();

			if (!email.isEmpty() && !password.isEmpty()) {
				// checkLogin(email,password);
//				if(CheckConnection.isOnline(LoginActivity.this)){
					new LoginTask(LoginActivity.this,email, password).execute();	
//				}else{
//					Toast.makeText(this, "Connect to the Internet", Toast.LENGTH_SHORT).show();
//				}
				
			} else {
				Toast.makeText(getApplicationContext(), "Please enter value",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btnLinkToRegister:
			Intent intent = new Intent(getApplicationContext(),
					RegisterActivity.class);
			startActivity(intent);
			finish();
			break;
		}
	}


	private void showDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hideDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}


	class LoginTask extends AsyncTask<String, String, String> {
		String email;
		String password;
		Context context;

		LoginTask(Context context, String email, String password) {
			this.email = email;
			this.password = password;
			this.context = context;
		}

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			// Building Parameters
			List<NameValuePair> params2 = new ArrayList<NameValuePair>();
			params2.add(new BasicNameValuePair("email", email));
			params2.add(new BasicNameValuePair("password", password));

			JSONObject json = jsonParser.makeHttpRequest(AppConfig.URL_LOGIN,
					"POST", params2);

			try {
				boolean error = json.getBoolean("error");
				
				//startMainActivity(error);
				
				if (!error) {
					
					session.setLogin(true);
					String uid = json.getString("uid");
					
					JSONObject user = json.getJSONObject("user");
					String name = user.getString("name");
					String email = user.getString("email");
					String created_at = user.getString("created_at");
					String phone = user.getString("phone");
					String type = user.getString("type");
					
					db.addUser(name, email, uid, created_at,phone,type);
					
					return "success";
				
				} else {
					return "error";

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/*
		 * After completing background task Dismiss the progress dialog *
		 */
		protected void onPostExecute(String result) {
			// dismiss the dialog once done
			if(result.equals("error")){
				Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show();
			}else{
				
				Intent intent = new Intent(context,
						MainActivity.class);
				
				startActivity(intent);
				finish();
			}
			hideDialog();
			
		}

	}
	
	
//	// Veryfying login details in mysql db
//	private void checkLogin(final String email, final String password) {
//		String tag_string_req = "req_login";
//		pDialog.setMessage("Logging in ...");
//		showDialog();
//
//		StringRequest strReq = new StringRequest(Method.POST,
//				AppConfig.URL_LOGIN, new Response.Listener<String>() {
//
//					public void onResponse(String response) {
//						Log.d(TAG, "Login Response: " + response.toString());
//						hideDialog();
//
//						try {
//							JSONObject jObj = new JSONObject(response);
//							boolean error = jObj.getBoolean("error");
//
//							if (!error) {
//								session.setLogin(true);
//
//								String uid = jObj.getString("uid");
//								JSONObject user = jObj.getJSONObject("user");
//								String name = user.getString("name");
//								String email = user.getString("email");
//								String created_at = user
//										.getString("created_at");
//
//								// Inserting row in users table
//								db.addUser(name, email, uid, created_at,phone,type);
//
//								Intent intent = new Intent(LoginActivity.this,
//										MainActivity.class);
//								startActivity(intent);
//								finish();
//							} else {
//								String errorMsg = jObj.getString("error_msg");
//								Toast.makeText(getApplicationContext(),
//										errorMsg, Toast.LENGTH_SHORT).show();
//
//							}
//
//						} catch (JSONException e) {
//							e.printStackTrace();
//							Toast.makeText(getApplicationContext(),
//									"JSON Error", Toast.LENGTH_SHORT).show();
//						}
//					}
//				}, new Response.ErrorListener() {
//
//					@Override
//					public void onErrorResponse(VolleyError error) {
//						// TODO Auto-generated method stub
//						Log.e(TAG, "Login Error: " + error.getMessage());
//						Toast.makeText(getApplicationContext(),
//								error.getMessage(), Toast.LENGTH_LONG).show();
//						hideDialog();
//					}
//				}) {
//			protected Map<String, String> getparams() {
//				Map<String, String> params = new HashMap<String, String>();
//				params.put("email", email);
//				params.put("password", password);
//
//				return params;
//			}
//		};
//
//		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//
//	}


}
