  package blackwidow.healthylife;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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

public class HealthyLifeLogin extends ActionBarActivity {
	private static String apiURL = "http://192.168.0.18/healthylife/api.php";
	
	ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	
	TextView error_txt;
	String un, pw;
	Button login, create;
	Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthy_life_login);
        
        error_txt = (TextView)findViewById(R.id.error_txt_al);
        error_txt.setText("Or");
        
        login = (Button)findViewById(R.id.login_btn);
        login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				error_txt.setText("");
				checkFields();
			}
		});
        create = (Button)findViewById(R.id.create_btn);
        create.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				intent = new Intent(HealthyLifeLogin.this, HealthyLifeCreateAccount.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				
			}
		});
    }
    
    public void checkFields(){
    	un = ((EditText)findViewById(R.id.username_et_al)).getText().toString();
    	pw = ((EditText)findViewById(R.id.password_et_al)).getText().toString();
    	
    	if (un.equals("") || pw.equals("")) {
    		error_txt.setTextColor(Color.RED);
    		error_txt.setText("please provide all the details");
    	}else if (un.equals("") && pw.equals("")) 	{
    		error_txt.setTextColor(Color.RED);
    		error_txt.setText("please provide all the details");
    	}else 	{
    		new userLogin().execute();
    	}
    }
    
    class  userLogin extends AsyncTask<String, String, String>{
    	/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(HealthyLifeLogin.this);
			pDialog.setMessage("Please Wait ... ");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@SuppressLint("InlinedApi")
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			error_txt = (TextView)findViewById(R.id.error_txt_al);
			
			un = ((EditText)findViewById(R.id.username_et_al)).getText().toString();
	    	pw = ((EditText)findViewById(R.id.password_et_al)).getText().toString();
	    	
	    	List<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("tag", "login"));
	    	params.add(new BasicNameValuePair("nickname", un));
			params.add(new BasicNameValuePair("password", pw));
			try{
				JSONObject obj = jsonParser.makeHttpRequest(apiURL, "POST", params);
				Log.d("Details", obj.toString());
				try{
					int success = obj.getInt("success");
					if (success == 1) {
						intent = new Intent(HealthyLifeLogin.this, HealthyLifeDashboard.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); 
						startActivity(intent);
						
					}else{
						error_txt.setTextColor(Color.RED);
			    		error_txt.setText("Failed to Log In");
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
    

}
