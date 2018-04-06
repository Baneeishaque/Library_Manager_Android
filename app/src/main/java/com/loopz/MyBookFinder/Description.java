package com.loopz.MyBookFinder;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Description extends AppCompatActivity {

    private static final String TAG = "Library";
    String id,shelf,row,pos;
    ListView SubjectListView;
    ProgressBar progressBarSubject;
    String ServerURL;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description);
        id=getIntent().getStringExtra("id");
        ServerURL = Server_Constants.API+"bookdetails.php?ID="+id+"";
        SubjectListView = (ListView)findViewById(R.id.listview);
        progressBarSubject = (ProgressBar)findViewById(R.id.progressBar1);
        new GetHttpResponse(getApplicationContext()).execute();
    }

    public void GetMap(View v){
        tv=(TextView)findViewById(R.id.stat);
        String s=tv.getText().toString().substring(tv.getText().toString().indexOf(":")+2);
        if(Objects.equals(s, "Available")) {
            Intent in = new Intent(getApplicationContext(), Map.class);
            String usrid = getIntent().getStringExtra("userid");
            Bundle extras = new Bundle();
            extras.putString("userid", usrid);
            Log.d("Library","Shelf "+shelf.substring(shelf.indexOf(" ")+1));
            extras.putString("a", shelf.substring(shelf.indexOf(" ")+1));
            Log.d("Library","Row "+row.substring(row.indexOf(" ")+1));
            extras.putString("b", row.substring(row.indexOf(" ")+1));
            Log.d("Library","Column "+pos.substring(pos.indexOf(" ")+1));
            extras.putString("c", pos.substring(pos.indexOf(" ")+1));
            in.putExtras(extras);
            startActivity(in);
        }
        else{
            Toast.makeText(this, "Book Not Available", Toast.LENGTH_SHORT).show();
        }

    }

    private class GetHttpResponse extends AsyncTask<Void, Void, Void> {
        public Context context;
        String ResultHolder;
        List<Books> BooksList;
        public GetHttpResponse(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpServicesClass httpServiceObject = new HttpServicesClass(ServerURL);
            try {
                httpServiceObject.ExecutePostRequest();
                if (httpServiceObject.getResponseCode() == 200) {
                    ResultHolder = httpServiceObject.getResponse();
                    Log.d(TAG, "doInBackground: "+ResultHolder);
                    if (ResultHolder != null) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(ResultHolder);
                            JSONObject jsonObject;
                            Books Books;
                            BooksList = new ArrayList<Books>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Books = new Books();
                                jsonObject = jsonArray.getJSONObject(i);
                                Books.Title = jsonObject.getString("title");
                                Books.bkid = jsonObject.getString("bkid");
                                Books.Author = jsonObject.getString("author");
                                Books.department = jsonObject.getString("department");
                                Books.desc = jsonObject.getString("description");
                                Books.num = jsonObject.getString("number");
                                Books.volume = jsonObject.getString("volume");
                                Books.status = jsonObject.getString("status");
                                Books.shelf = jsonObject.getString("shelf");
                                Books.row = jsonObject.getString("row");
                                Books.pos= jsonObject.getString("position");
                                BooksList.add(Books);
                                shelf=Books.shelf.toString();
                                row=Books.row.toString();
                                pos=Books.pos.toString();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(context, httpServiceObject.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result)
        {
            progressBarSubject.setVisibility(View.GONE);
            SubjectListView.setVisibility(View.VISIBLE);
            if (BooksList != null) {
                BookAdapter adapter = new BookAdapter(BooksList, context);

                SubjectListView.setAdapter(adapter);
            }
        }
    }

}
