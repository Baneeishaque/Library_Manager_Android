package com.loopz.MyBookFinder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Objects;

public class FinalBook extends AppCompatActivity {

    TextView is,date,msg;
    String stdate,year,mnth,day;
    private Session session;
    public void home(View vi)
    {
        Bundle extras=new Bundle();
        Intent intent =new Intent(getApplicationContext(), MainActivity.class);
        String userid=session.getuserid();
        String uname=session.getusername();
        extras.putString("userid",userid);
        extras.putString("username",uname);
        intent.putExtras(extras);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Bundle extras=new Bundle();
        Intent intent =new Intent(getApplicationContext(), MainActivity.class);
        String userid=session.getuserid();
        String uname=session.getusername();
        extras.putString("userid",userid);
        extras.putString("username",uname);
        intent.putExtras(extras);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_book);
        session=new Session(this);
        String s=getIntent().getStringExtra("res");
        is= findViewById(R.id.issuestat);
        date= findViewById(R.id.date);
        msg= findViewById(R.id.retmsg);
        if(Objects.equals(s, "Issue Unsuccessful"))
        {
            msg.setVisibility(View.GONE);
            is.setText(s);
            date.setText("Oops.. Issue Unsuccessful");
        }
        else{
            is.setText("Book Issued Successfully");
            msg.setText("Return on or before");
            stdate=s;
            year=stdate.substring(0,4);
            mnth=stdate.substring(5,7);
            day=stdate.substring(8,10);
            s=day+"/"+mnth+"/"+year;
            date.setText(s);
            int yr=Integer.parseInt(year);
            int mn=Integer.parseInt(mnth);
            int dy=Integer.parseInt(day);
            //Reminder
            Calendar cal = Calendar.getInstance();
            cal.set(yr,
                    mn,
                    dy,
                    7,
                    30,
                    0);
            setAlarm(cal);
        }
    }
    private void setAlarm(Calendar targetCal){

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
        }

    }

}
