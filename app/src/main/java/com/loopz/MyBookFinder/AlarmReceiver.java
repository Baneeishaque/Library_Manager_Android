package com.loopz.MyBookFinder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        Intent intent = new Intent();
        PendingIntent pIntent = PendingIntent.getActivity(arg0, 0, intent, 0);
        Notification noti = new Notification.Builder(arg0)
                .setTicker("Ticker Title")
                .setContentTitle("Return Book")
                .setContentText("Return book before deadline")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent).getNotification();
        noti.flags=Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) arg0.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, noti);

    }

}
