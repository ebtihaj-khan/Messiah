package com.CFP.messiah;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MessiahRegistertion extends Activity {
	EditText FullName, PhoneNumber;
	Button Register;
	String fullname, phonenumber;
	int status;
	SharedPreferences users;
	Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messiah_registertion);
		FullName = (EditText) findViewById(R.id.etFullName);
		PhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
		Register = (Button) findViewById(R.id.btnRegister);
		Register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				fullname = FullName.getText().toString();
				phonenumber = PhoneNumber.getText().toString();
				if (fullname.length() < 3)
					FullName.setError("atleast 3 character long");
				else if (phonenumber.length() < 10)
					PhoneNumber.setError("Must Required");
				else

				if (CheckNetwork.isInternetAvailable(MessiahRegistertion.this)) {
					new SetConnection().execute();
				} else {

					Toast.makeText(getApplicationContext(),
							"No Internet Connection", Toast.LENGTH_SHORT)
							.show();

				}
			}

		});
	}

	private void doregister(String FullName, String PhoneNumber) {

		UserFunctions userFunction = new UserFunctions();
		JSONObject json = userFunction.Registertion(FullName, PhoneNumber);
		Log.v("Error", json.toString());
		// WebApiCaller caller = new WebApiCaller();
		// JSONObject object = caller.SignIn(Username, Password);

		try {
			status = json.getInt("Status");// object.getBoolean("Status");

			if (status == 1) {
				Log.v("Login", "Username and Password");

			} else {
				Log.v("Login", "Invalid Username and Password");

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void startnextactivity() {
		if (status == 1) {
			users = getSharedPreferences("Login Credentials", MODE_PRIVATE);
			editor = users.edit();
			editor.putString("Fullname", fullname);
			editor.putString("Phonenumber", phonenumber);
			editor.commit();
			startActivity(new Intent(MessiahRegistertion.this, Verification.class));
			Log.v("Login", "Username and Password");

		} else {
			Toast.makeText(getApplicationContext(), "ERROR", 5000).show();
			Log.v("Login", "Invalid Username and Password");

		}
		

	}

	private class SetConnection extends AsyncTask<Void, Void, Void> {

		private ProgressDialog dialog;
		 
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(MessiahRegistertion.this);
			dialog.setMessage("Please Wait...");
	        dialog.show();
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... params) {
			doregister(fullname, phonenumber);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if (dialog.isShowing()) {
	            dialog.dismiss();
	        }
			startnextactivity();
		}
	}
}