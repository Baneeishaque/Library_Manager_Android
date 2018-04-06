package com.loopz.MyBookFinder;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    Button pass;
    EditText oldp,newp;
    String oldpa,newpa;
    String usrid;
    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_settings, container, false);
        pass=(Button)v.findViewById(R.id.passchange);
        oldp=(EditText)v.findViewById(R.id.oldpass);
        newp=(EditText)v.findViewById(R.id.newpass);
        Bundle ext=getArguments();
        usrid= ext != null ? ext.getString("userid") : null;
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldpa=oldp.getText().toString();
                newpa=newp.getText().toString();
                ChangePass changePass=new ChangePass(getActivity().getApplicationContext());
                changePass.execute(oldpa,newpa,usrid);
                oldp.setText("");
                newp.setText("");
            }
        });

        return v;
    }
    public class ChangePass extends AsyncTask<String,Void,String> {
        Context context;
        ChangePass (Context ctx) {
            context = ctx;

        }
        @Override
        protected String doInBackground(String... params) {
            String type = "pass";
            String login_url = Server_Constants.API+"updatepass.php";
            if(type.equals("pass")) {
                try {
                    String userid = params[2];
                    String newpass = params[1];
                    String oldpass= params[0];
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("userid","UTF-8")+"="+URLEncoder.encode(userid,"UTF-8")+"&"
                            +URLEncoder.encode("oldpass","UTF-8")+"="+URLEncoder.encode(oldpass,"UTF-8")+"&"
                            +URLEncoder.encode("newpass","UTF-8")+"="+URLEncoder.encode(newpass,"UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    StringBuilder result= new StringBuilder();
                    String line="";
                    while((line = bufferedReader.readLine())!= null) {
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

        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
