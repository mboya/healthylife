package blackwidow.healthylife;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import blackwidow.library.JSONParser;

public class HealthyLifeCreateAccount extends ActionBarActivity {
	private static String loginURL = "http://192.168.0.18/healthylife/api.php";
	
	ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	JSONArray user = new JSONArray();
	
	TextView error;
	String fn, pn, un, pw;
	Button createAccount;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_healthy_life_create_account);
		
		error = (TextView)findViewById(R.id.error1_txt_al);
		error.setText("");
		
		createAccount = (Button)findViewById(R.id.create1_btn);
		createAccount.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				checkFields();
			}
		});
	}
	
	public void checkFields(){
		String fn = ((EditText)findViewById(R.id.fullname_et_al)).getText().toString();
		String pn = ((EditText)findViewById(R.id.phonenumber_et_al)).getText().toString();
		String un = ((EditText)findViewById(R.id.username1_et_al)).getText().toString();
		String pw = ((EditText)findViewById(R.id.password1_et_al)).getText().toString();
		
		if (fn.equals("") || pn.equals("") || un.equals("") || pw.equals("")){
			error.setTextColor(Color.RED);
			error.setText("Please Provide all the details");
		}else if (fn.equals("") && pn.equals("") && un.equals("") && pw.equals("")){
			error.setTextColor(Color.RED);
			error.setText("Please Provide all the details");
		}else{
			new userReg().execute();
		}
	}
	
	class userReg extends AsyncTask<String, String, String>{
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(HealthyLifeCreateAccount.this);
			pDialog.setMessage("Please Wait ... ");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@SuppressLint("InlinedApi")
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String fn = ((EditText)findViewById(R.id.fullname_et_al)).getText().toString();
			String pn = ((EditText)findViewById(R.id.phonenumber_et_al)).getText().toString();
			String un = ((EditText)findViewById(R.id.username1_et_al)).getText().toString();
			String pw = ((EditText)findViewById(R.id.password1_et_al)).getText().toString();
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("tag", "register"));
			params.add(new BasicNameValuePair("full_name", fn));
			params.add(new BasicNameValuePair("phone_number", pn));
			params.add(new BasicNameValuePair("username", un));
			params.add(new BasicNameValuePair("password", pw));
			
			try{
				JSONObject obj = jsonParser.makeHttpRequest(loginURL, "POST", params);
				Log.d("Details", obj.toString());
				try{
					int success = obj.getInt("success");
					if (success == 1) {
						intent = new Intent(HealthyLifeCreateAccount.this, HealthyLifeLogin.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); 
						startActivity(intent);
						
					}else{
						error.setTextColor(Color.RED);
						error.setText("Registration Failed");
					}
					
				}catch(JSONException e){
					e.printStackTrace();
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return null;
		}
		
		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
		}
		
	}
	
	public void onStop(){
		super.onStop();
		finish();
	}

}
