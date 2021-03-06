package com.example.android.cz3002project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.drm.ProcessedData;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SignUpActivity extends ActionBarActivity {
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    String inputName;
    String inputEmail;
    String inputPhoneNumber;
    String inputPassword;
    String inputRepeatPassword;
    private static String url_create_user = "http://10.27.44.239/create_user.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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

    public void UserSignUp(View view)
    {
        inputName = ((EditText) findViewById(R.id.signUpEditTextName)).getText().toString();
        inputEmail = ((EditText) findViewById(R.id.signUpEditTextEmail)).getText().toString();
        inputPhoneNumber = ((EditText) findViewById(R.id.signUpEditTextPhoneNumber)).getText().toString();
        inputPassword = ((EditText) findViewById(R.id.signUpEditTextPassword)).getText().toString();
        inputRepeatPassword = ((EditText) (findViewById(R.id.signUpEditTextRepeatPassword))).getText().toString();

        Log.e("INSIDE", inputPassword);
        if(inputPassword.compareTo(inputRepeatPassword) == 0) {
            new CreateNewUser().execute();

        }
//        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
//        startActivity(intent);
    }

    class CreateNewUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignUpActivity.this);
            pDialog.setMessage("Creating User Account..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", inputName));
            params.add(new BasicNameValuePair("email", inputEmail));
            params.add(new BasicNameValuePair("phone_number", inputPhoneNumber));
            params.add(new BasicNameValuePair("password", inputPassword));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_user,
                    "GET", params);

            // check log cat fro response
            //Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt("success");

                if (success == 1) {
                    // successfully created product
                    //Intent i = new Intent(getApplicationContext(), AllProductsActivity.class);
                    //startActivity(i);
                    Log.e("SIGN UP PROCESS", "SUCCESS");
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
