package com.loopz.MyBookFinder;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static String ip, usrid;
    private Session session;
    public boolean ishome = true;
    static ListAdapterClass adapter;
    static ListView SubjectListView;
    static List<subjects> subjectsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new Session(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Bundle extras = getIntent().getExtras();
        usrid = extras != null ? extras.getString("userid") : null;
        String usrname = extras.getString("username");
        View h = navigationView.getHeaderView(0);
        TextView v = (TextView) h.findViewById(R.id.usrname);
        v.setText(usrname);
        TextView v2 = (TextView) h.findViewById(R.id.usrid);
        v2.setText(usrid);
        ip = "abc.php";
        FragmentManager fm = getSupportFragmentManager();
        HomeFragment hf = new HomeFragment();
        fm.beginTransaction().replace(R.id.frag, hf).commit();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (ishome) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }

                doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            } else {

                FragmentManager fm = getSupportFragmentManager();
                HomeFragment hf = new HomeFragment();
                ishome = true;
                fm.beginTransaction().replace(R.id.frag, hf).commit();
            }


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ishome = true;
                int a = s.length();
                List<subjects> filterList = new ArrayList<subjects>();
                if (a < 1) {
//                    ip = "abc.php";
//                    HomeFragment hf = new HomeFragment();
//                    FragmentManager fm = getSupportFragmentManager();
//                    fm.beginTransaction().replace(R.id.frag, hf, hf.getTag()).commit();
                    adapter = new ListAdapterClass(subjectsList, MainActivity.this);
                    SubjectListView.setAdapter(adapter);

                } else {
//                    ip = "search.php?ID=" + s;
//                    HomeFragment hf = new HomeFragment();
//                    FragmentManager fm = getSupportFragmentManager();
//                    fm.beginTransaction().replace(R.id.frag, hf, hf.getTag()).commit();
                    int flag = 0;
                    for (int i = 0; i < subjectsList.size(); i++) {

                        if (subjectsList.get(i).getSubjectName().toLowerCase().contains(s.toLowerCase())) {
                            filterList.add(subjectsList.get(i));
                            flag = 1;
                        }
                        if (flag == 1) {
                            adapter = new ListAdapterClass(filterList, MainActivity.this);
                            SubjectListView.setAdapter(adapter);
                        }
                    }
                }
                return false;
            }
        });

        getMenuInflater().inflate(R.menu.noti, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_notifications) {
            Toast.makeText(this, "No new notifications", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.hom) {
            ip = "abc.php";
            HomeFragment hf = new HomeFragment();
            FragmentManager fm = getSupportFragmentManager();
            ishome = true;
            fm.beginTransaction().replace(R.id.frag, hf, hf.getTag()).commit();


        } else if (id == R.id.his) {
            Bundle args = new Bundle();
            args.putString("userid", usrid);
            HistoryFragment hf = new HistoryFragment();
            hf.setArguments(args);
            FragmentManager fm = getSupportFragmentManager();
            ishome = false;
            fm.beginTransaction().replace(R.id.frag, hf, hf.getTag()).commit();
        } else if (id == R.id.re) {
            Bundle args = new Bundle();
            args.putString("userid", usrid);
            RequestFragment rf = new RequestFragment();
            rf.setArguments(args);
            FragmentManager fm = getSupportFragmentManager();
            ishome = false;
            fm.beginTransaction().replace(R.id.frag, rf, rf.getTag()).commit();

        } else if (id == R.id.rem) {
            ReminderFragment ref = new ReminderFragment();
            FragmentManager fm = getSupportFragmentManager();
            ishome = false;
            fm.beginTransaction().replace(R.id.frag, ref, ref.getTag()).commit();


        } else if (id == R.id.set) {
            Bundle args = new Bundle();
            args.putString("userid", usrid);
            SettingsFragment sf = new SettingsFragment();
            sf.setArguments(args);
            FragmentManager fm = getSupportFragmentManager();
            ishome = false;
            fm.beginTransaction().replace(R.id.frag, sf, sf.getTag()).commit();
        } else if (id == R.id.log) {

            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            session.addlogin(false, null, null);
                            Intent i = new Intent(getApplicationContext(), Login.class);
                            startActivity(i);
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class HomeFragment extends Fragment {


        ProgressBar progressBarSubject;
        String ServerURL = Server_Constants.API + ip;
        TextView tx;
        Button Bn;
        private Shelf shelf;
        private ZXingScannerView zXingScannerView;

        public HomeFragment() {
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View v = inflater.inflate(R.layout.fragment_home, container, false);
            SubjectListView = (ListView) v.findViewById(R.id.listview1);
            progressBarSubject = (ProgressBar) v.findViewById(R.id.progressBar);
            shelf = new Shelf();
            Bn = (Button) v.findViewById(R.id.button3);
            setHasOptionsMenu(true);
            new GetHttpResponse(getActivity().getApplicationContext()).execute();
            SubjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    tx = (TextView) view.findViewById(R.id.bookid);
                    String st = tx.getText().toString().substring(tx.getText().toString().indexOf(":") + 2);
                    Intent in = new Intent(getActivity().getApplicationContext(), Description.class);
                    in.putExtra("userid", usrid);
                    in.putExtra("id", st);
                    startActivity(in);
                }
            });
            Bn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity().getApplicationContext(), "Work on Progress", Toast.LENGTH_SHORT).show();
                }
            });

            return v;
        }

        @Override
        public void onPrepareOptionsMenu(Menu menu) {
            MenuItem menuItem = menu.findItem(R.id.action_search);
            menuItem.setVisible(true);
        }

        private class GetHttpResponse extends AsyncTask<Void, Void, Void> {
            public Context context;
            String ResultHolder;

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
                                subjects subjects;
                                subjectsList = new ArrayList<subjects>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    subjects = new subjects();
                                    jsonObject = jsonArray.getJSONObject(i);
                                    subjects.SubjectName = jsonObject.getString("title");
                                    subjects.SubId = jsonObject.getString("bkid");
                                    subjects.Author = jsonObject.getString("author");
                                    subjectsList.add(subjects);
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
            protected void onPostExecute(Void result) {
                progressBarSubject.setVisibility(View.GONE);
                SubjectListView.setVisibility(View.VISIBLE);
                if (subjectsList != null) {
                    adapter = new ListAdapterClass(subjectsList, context);

                    SubjectListView.setAdapter(adapter);
                }
            }
        }


    }

}
