package com.loopz.MyBookFinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class Login extends Activity {

    EditText user, passwrd;
    ProgressBar progressBar;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = (EditText) findViewById(R.id.username);
        passwrd = (EditText) findViewById(R.id.pass);

        session = new Session(this);

        passwrd.setImeOptions(EditorInfo.IME_ACTION_DONE);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        passwrd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:

                        String pass = passwrd.getText().toString();
                        String userid = user.getText().toString();
                        String type = "login";
                        if (Objects.equals(pass, "") || Objects.equals(userid, "")) {
                            Toast.makeText(getApplicationContext(), "Please fill the fields", Toast.LENGTH_LONG).show();
                        } else {
                            LoginProcess loginProcess = new LoginProcess(getApplicationContext());
                            loginProcess.execute(type, userid, pass);
                            break;
                        }
                }
                return false;
            }
        });
        if (session.loggedin()) {
            Bundle extras = new Bundle();
            Intent i = new Intent(Login.this, MainActivity.class);
            String userid = session.getuserid();
            String uname = session.getusername();
            extras.putString("userid", userid);
            extras.putString("username", uname);
            i.putExtras(extras);
            startActivity(i);
            finish();
        }
    }


    public void newpage(View v) {
        String pass = passwrd.getText().toString();
        String userid = user.getText().toString();
        if (Objects.equals(pass, "") || Objects.equals(userid, "")) {
            Toast.makeText(this, "Please fill the fields", Toast.LENGTH_LONG).show();
        } else {

            String type = "login";
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            } catch (Exception e) {
                // TODO: handle exception
                Log.d(TAG, "newpage: Exception");
            }
            LoginProcess loginProcess = new LoginProcess(this);
            loginProcess.execute(type, userid, pass);
        }
    }

    public class LoginProcess extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog alertDialog;

        LoginProcess(Context ctx) {
            context = ctx;

        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String login_url = Server_Constants.API + "login.php";

            Log.d(TAG, "doInBackground: " + login_url);
            Log.d(TAG, "doInBackground: " + type);
            if (type.equals("login")) {

                String user_name = params[1];
                String password = params[2];

                try {

                    DefaultHttpClient http_client;
                    HttpPost http_post;
                    ArrayList<NameValuePair> name_pair_value_array;
                    String network_action_response;

                    http_client = new DefaultHttpClient();
                    http_post = new HttpPost(login_url);
                    name_pair_value_array = new ArrayList<>(2);
                    name_pair_value_array.add(new BasicNameValuePair("user_name", user_name));
                    name_pair_value_array.add(new BasicNameValuePair("password", password));

                    http_post.setEntity(new UrlEncodedFormEntity(name_pair_value_array));

                    ResponseHandler<String> response_handler = new BasicResponseHandler();
                    network_action_response = http_client.execute(http_post, response_handler);
                    return network_action_response;

                } catch (IOException e) {
                    Log.d(TAG, "doInBackground: " + e.getLocalizedMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Login Status");
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "onPostExecute: " + result);
            if (Objects.equals(result, "Success")) {
                callgetname();
            } else {
                alertDialog.setMessage(result);
                alertDialog.show();
                progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    public void callgetname() {
        GetName getName = new GetName(this);
        String userid = user.getText().toString();
        getName.execute(userid);
    }

    public class GetName extends AsyncTask<String, Void, String> {
        Context context;

        GetName(Context ctx) {
            context = ctx;

        }

        @Override
        protected String doInBackground(String... params) {
            String type = "login";
            String name_url = Server_Constants.API + "fetch.php";
            Log.d(TAG, "doInBackground: " + name_url);
            if (type.equals("login")) {
                try {
                    String user_name = params[0];
                    URL url = new URL(name_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    StringBuilder result = new StringBuilder();
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result.append(line);
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {

            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String result) {
            String usr = user.getText().toString();
            Bundle extras = new Bundle();
            session.addlogin(true, usr, result);
            Toast.makeText(context, "Welcome " + result, Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            extras.putString("userid", usr);
            extras.putString("username", result);
            i.putExtras(extras);
            startActivity(i);
            finish();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
