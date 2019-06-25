package com.el_asdka2.hatly;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.RemoteMessage;

public class AcceptFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    NotificationManagerCompat notificationManager;
    public int NotificationID;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if(remoteMessage.getData().size() > 0 ){
            String message = remoteMessage.getData().get("message");
            String from = remoteMessage.getData().get("from_user_id");

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                String CHANNEL_ID = "my_channel_02";
                CharSequence name = "my_channel";
                String Description = "This is my channel";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mChannel.setDescription(Description);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setShowBadge(false);

                notificationManager.createNotificationChannel(mChannel);

            }else {
                Intent intent = new Intent(this,recycler_delivery.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                        0, intent, 0);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "01")
                        .setSmallIcon(R.drawable.ic_icon)
                        .setVibrate(new long[]{1000, 1000})
                        .setContentTitle("Accepted")
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setContentText(message)
                        .setContentIntent(pendingIntent)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setAutoCancel(true)
                        .setWhen(0);


                NotificationID = (int) System.currentTimeMillis();
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(NotificationID, mBuilder.build());

            }
        }else Toast.makeText(getApplicationContext(),"Empty Data",Toast.LENGTH_LONG).show();



    }

}
