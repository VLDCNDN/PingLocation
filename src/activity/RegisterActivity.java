package activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import utils.CheckConnection;

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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import app.AppConfig;
import app.AppController;

public class RegisterActivity extends Activity{
	
	EditText inputName;
	EditText inputEmail;
	EditText inputPassword;
	EditText inputPhone;
	
	ProgressDialog pDialog;
	SessionManager session;
	SQLiteHandler db;

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_register);
	        
	        inputName  = (EditText) findViewById(R.id.reg_name);
	        inputEmail = (EditText) findViewById(R.id.reg_email);
	        inputPassword = (EditText) findViewById(R.id.reg_password);
	        inputPhone = (EditText) findViewById(R.id.reg_phone);
	        
	        pDialog = new ProgressDialog(this);
	        pDialog.setCancelable(false);
	        
	        session = new SessionManager(getApplicationContext());
	        
	        db = new SQLiteHandler(getApplicationContext());
	        
	        if(session.isLoggedIn()){
	        	Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
	        	startActivity(intent);
	        	finish();
	        }
	        
	 }
	 
	 public void ocRegisterListener(View view){
		 switch(view.getId()){
		 case R.id.btnRegister:
			 
			 String name = inputName.getText().toString().trim();
			 	String email = inputEmail.getText().toString().trim();
			 	String password = inputPassword.getText().toString().trim();
			 	String phone = inputPhone.getText().toString().trim();
			 	String type = LoginActivity.USER_REGULAR;
			 	
			 	if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty()){
			 	//	if(CheckConnection.isOnline(RegisterActivity.this)){

				 		registerUser(name,email,password,phone,type);
			 //		}else{
			 	//		Toast.makeText(this, "Please connect to the Internet", Toast.LENGTH_SHORT).show();
			 //	}
			 	}else{
			 		Toast.makeText(getApplicationContext(), "Please enter value", Toast.LENGTH_SHORT).show();
			 	}
			 	
			 break;
		 case R.id.btnLinkToLogin:
			 
			 Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
			 startActivity(intent);
			 finish();
		 
		 }
	 }
	 
	 private void registerUser(final String name, final String email, final String password,final String phone,final String type){
		 String tag_string_req = "req_register";
		 
		 pDialog.setMessage("Registering ... ");
		 showDialog();
		 
		 StringRequest strReq = new StringRequest(Method.POST, AppConfig.URL_REGISTER, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				hideDialog();
				try{
					JSONObject jObj = new JSONObject(response);
					boolean error = jObj.getBoolean("error");
					if(!error){
						String uid = jObj.getString("uid");
						
						JSONObject user = jObj.getJSONObject("user");
						String name = user.getString("name");
						String email = user.getString("email");
						String created_at = user.getString("created_at");
						String phone = user.getString("phone");
						String type = user.getString("type");
						
					//	db.addUser(name, email, uid, created_at,phone,type);
						Toast.makeText(getApplicationContext(), "User created", Toast.LENGTH_SHORT).show();
						
						Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
						startActivity(intent);
						finish();
					}else{
						String errorMsg = jObj.getString("error_msg");
						Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
					}
				}catch(JSONException e){
					e.printStackTrace();
				}
			}
		
		 
		 }, new Response.ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				Log.e("Registration", "Registration Error: " + arg0.getMessage());
				Toast.makeText(getApplicationContext(), "Registration error: " + arg0.getMessage(), Toast.LENGTH_SHORT).show();
				hideDialog();
			}
			
		}){
			 protected Map<String, String> getParams(){
				 Map<String,String> params = new HashMap<String, String>();
				 params.put("name", name);
				 params.put("email", email);
				 params.put("password", password);
				 params.put("phone", phone);
				 params.put("type", type);
				 
				 
				 return params;
			 }
		 };
		 
		 AppController.getInstance().addToRequestQueue(strReq,tag_string_req);
	 }
	 
	 private void showDialog() {
	        if (!pDialog.isShowing())
	            pDialog.show();
	    }
	 
	    private void hideDialog() {
	        if (pDialog.isShowing())
	            pDialog.dismiss();
	    }

	 
}
