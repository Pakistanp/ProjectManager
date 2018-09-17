package com.example.piotr.projectmanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.piotr.projectmanager.Activities.ProjectsActivity;
import com.example.piotr.projectmanager.Database.UsersDatabaseHelper;

public class AlarmReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        int i = intent.getIntExtra("ID", 0);
        UsersDatabaseHelper db = new UsersDatabaseHelper(context);
        if(db.projectExist(i)) {
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("default",
                        "YOUR_CHANNEL_NAME",
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
                mNotificationManager.createNotificationChannel(channel);
            }
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "default")
                    .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                    .setContentTitle(intent.getStringExtra("TITLE")) // title for notification
                    .setContentText("The time is up to the end of the project!")// message for notification
                    .setOnlyAlertOnce(true)
                    .setAutoCancel(true); // clear notification after click
            Intent intentA = new Intent(context, ProjectsActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, intentA, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pi);
            mNotificationManager.notify(100 + i, mBuilder.build());
        }
}
}
