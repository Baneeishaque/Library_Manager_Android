package com.loopz.MyBookFinder;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookRqstFragment extends Fragment {


    ListView Rqstlistvw;
    ProgressBar pbrqst;
    String ServerURL;
    String usrid;
    public BookRqstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_book_rqst, container, false);
        Bundle ext=getArguments();
        usrid= ext != null ? ext.getString("userid") : null;
        ServerURL = Server_Constants.API+"bookrqsthstry.php?ID="+usrid;
        Rqstlistvw = (ListView)v.findViewById(R.id.bkrqstlst);
        pbrqst = (ProgressBar)v.findViewById(R.id.progressBar3);
        new GetHttpResponse(getActivity().getApplicationContext()).execute();
        return  v;
    }

    private class GetHttpResponse extends AsyncTask<Void, Void, Void> {
        public Context context;
        String ResultHolder;
        List<Books> BooksList;
        GetHttpResponse(Context context) {
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
                                Books.Title = jsonObject.getString("Author");
                                Books.bkid = jsonObject.getString("bookname");
                                Books.Author = jsonObject.getString("volume");
                                Books.num = jsonObject.getString("requestdate");
                                BooksList.add(Books);
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
            pbrqst.setVisibility(View.GONE);
            Rqstlistvw.setVisibility(View.VISIBLE);
            if (BooksList != null) {
                RqstAdapter adapter = new RqstAdapter(BooksList, context);

                Rqstlistvw.setAdapter(adapter);
            }
        }
    }

}
