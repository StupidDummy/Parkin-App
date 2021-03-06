package com.example.android.cz3002project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LogIn extends ActionBarActivity {
    EditText inputEmail;
    EditText inputPassword;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static String url_read_user = "http://10.27.44.239/read_user.php";
    private static final String TAG_SUCCESS = "success";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void UserLogIn(View view)
    {
        inputEmail = (EditText) findViewById(R.id.logInEditTextEmail);
        inputPassword = (EditText) findViewById(R.id.logInEditTextPassword);
        new UserLogInProcess().execute();
        //Intent intent = new Intent(LogIn.this, Game3.class);
        //startActivity(intent);
    }

    class UserLogInProcess extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LogIn.this);
            pDialog.setMessage("Log In...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));


            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_read_user,
                    "GET", params);

            // check log cat fro response
            //Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    //Intent i = new Intent(getApplicationContext(), AllProductsActivity.class);
                    //startActivity(i);
                    Log.e("LOGIN PROCESS", ((Integer)success).toString());
                    JSONArray user = json.getJSONArray("user");
                    JSONObject jo = user.getJSONObject(0);
                    String retrievedPassword = jo.getString("password");
                    String retrievedName = jo.getString("name");
                    String retrievedEmail = jo.getString("email");
                    editor.putString("Name", retrievedName);
                    editor.putString("Email", retrievedEmail);
                    editor.apply();
                    if(retrievedPassword.compareTo(password) == 0)
                        Log.e("LOGIN PROCESS", "Success");
                    else
                        Log.e("LOGIN PROCESS", "Failed");
                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}
