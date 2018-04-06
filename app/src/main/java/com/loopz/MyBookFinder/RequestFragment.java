package com.loopz.MyBookFinder;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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


public class RequestFragment extends Fragment {

    EditText auth,tit,vol;
    Button req;
    String usrid;
    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_request, container, false);
        auth=(EditText)v.findViewById(R.id.author) ;
        tit=(EditText)v.findViewById(R.id.title);
        vol=(EditText)v.findViewById(R.id.vol);
        Bundle ext=getArguments();
        usrid= ext != null ? ext.getString("userid") : null;
        req=(Button) v.findViewById(R.id.req);
        req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title= tit.getText().toString();
                String author= auth.getText().toString();
                String volume= vol.getText().toString();

                ReqProcess reqProcess=new ReqProcess(getActivity().getApplicationContext());
                reqProcess.execute(title,author,volume,usrid);
                tit.setText("");
                auth.setText("");
                vol.setText("");
            }
        });
        return v;
    }

    public class ReqProcess extends AsyncTask<String,Void,String> {
        Context context;
        ReqProcess (Context ctx) {

            context = ctx;

        }
        @Override
        protected String doInBackground(String... params) {
            String type = "req";
            String login_url = Server_Constants.API+"reqbook.php";
            if(type.equals("req")) {
                try {
                    String usrid=params[3];
                    String title = params[0];
                    String author = params[1];
                    String volume = params[2];
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("userid","UTF-8")+"="+URLEncoder.encode(usrid,"UTF-8")+"&"
                            +URLEncoder.encode("title","UTF-8")+"="+URLEncoder.encode(title,"UTF-8")+"&"
                            +URLEncoder.encode("author","UTF-8")+"="+URLEncoder.encode(author,"UTF-8")+"&"
                            +URLEncoder.encode("volume","UTF-8")+"="+URLEncoder.encode(volume,"UTF-8");
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
