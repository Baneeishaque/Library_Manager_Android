package com.loopz.MyBookFinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.zxing.Result;

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

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.content.ContentValues.TAG;

public class Shelf extends Activity implements ZXingScannerView.ResultHandler {
private ZXingScannerView zXingScannerView;
String qrresult,usrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shelf);
        Bundle extras=getIntent().getExtras();
        String s2= null;
        if (extras != null) {
            s2 = extras.getString("b");
        }
        String s3= null;
        if (extras != null) {
            s3 = extras.getString("c");
        }
        if (extras != null) {
            usrid=extras.getString("userid");
        }
        String btn="sr"+s2+s3;
        int resid=getResources().getIdentifier(btn,"id",getPackageName());
        Button f = (Button) findViewById(resid);
        f.setBackgroundResource(R.drawable.booksel);
        f.setClickable(true);
    }
    public void read (View view){
        zXingScannerView =new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        qrresult=result.getText();
        IssueProcess issueProcess=new IssueProcess(this);
        issueProcess.execute("issue",qrresult,usrid);

    }
    public class IssueProcess extends AsyncTask<String,Void,String> {
        Context context;
        AlertDialog alertDialog;
        IssueProcess (Context ctx) {

            context = ctx;

        }
        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String bookid = params[1];
            String userid = params[2];
            String login_url = Server_Constants.API+"issuebook.php?userid="+userid+"&bookid="+bookid;
            Log.d(TAG, "doInBackground: "+login_url);
            if(type.equals("issue")) {
                try {
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("userid","UTF-8")+"="+URLEncoder.encode(userid,"UTF-8")+"&"
                            +URLEncoder.encode("bookid","UTF-8")+"="+URLEncoder.encode(bookid,"UTF-8");
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
            Log.d(TAG, "onPostExecute: "+result);
                Intent in = new Intent(getApplicationContext(), FinalBook.class);
                in.putExtra("res", result);
                startActivity(in);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}